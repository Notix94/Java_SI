package exercice3_p2;

import exercice6.*; 
import exercice7.SetColor; 
import graphicLayer.*;
import stree.parser.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Serveur de S-Expressions - Exercice 3.
 * Gère l'interprétation réseau et la capture d'écran (Screenshot).
 */
public class SExprServer {
    private static final int PORT = 9876;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Serveur Exercice 3 démarré sur le port " + PORT);

            // Initialisation de l'espace graphique du serveur
            GSpace space = new GSpace("Rendu Serveur", new Dimension(200, 100));
            GRect  robi  = new GRect();
            space.addElement(robi);
            space.open();
            
            Environment env = buildEnv(space, robi);
            Interpreter interpreter = new Interpreter();

            while (true) {
                try (Socket client = serverSocket.accept();
                     BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)) {
                			
                    String sexpr = in.readLine();
                    if (sexpr == null) continue;
                    					
                    System.out.println("📩 Reçu : " + sexpr);

                    SParser<SNode> parser = new SParser<>();
                    List<SNode> nodes = parser.parse(sexpr);
                    
                    // Par défaut, on renverra l'écho de ce qu'on a reçu (S-Nodes)
                    Object responseToClient = nodes;

                    // Exécution des commandes sur le serveur
                    for (SNode node : nodes) {
                        Reference result = interpreter.compute(env, node);
                        
                        if (result != null) {
                            Object obj = result.getReceiver();
                            // CAS SCREENSHOT : La commande renvoie un String (Base64)
                            if (obj instanceof String && ((String) obj).length() > 100) {
                                responseToClient = obj;
                            }
                        }
                    }

                    // ENVOI DE LA RÉPONSE AU CLIENT
                    if (responseToClient instanceof String) {
                        // On envoie la chaîne brute (l'image en Base64)
                        out.println(responseToClient);
                    } else {
                        // On sérialise la liste de nœuds
                        out.println(serialize((List<SNode>) responseToClient));
                    }

                } catch (Exception e) {
                    System.err.println("❌ Erreur traitement : " + e.getMessage());
                }
            }
        }
    }

    private static Environment buildEnv(GSpace space, GRect robi) {
        Environment env = new Environment();

        // Configuration de l'espace (Space)
        Reference spaceRef = new Reference(space);
        spaceRef.addCommand("setColor",   new SetColor()); 
        spaceRef.addCommand("sleep",      new Sleep());
        // Commande spécifique à l'Exercice 3
        spaceRef.addCommand("screenshot", new ScreenshotCommand());
        
        env.addReference("space", spaceRef);

        // Configuration de Robi
        Reference robiRef = new Reference(robi);
        robiRef.addCommand("setColor",    new SetColor());
        robiRef.addCommand("translate",   new Translate());
        robiRef.addCommand("setPosition", new SetPosition());
        env.addReference("robi", robiRef);

        return env;
    }

    /**
     * Transforme une liste de SNodes en chaîne JSON pour le client.
     */
    private static String serialize(List<SNode> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append("[");
            List<SNode> children = nodes.get(i).children();
            for (int j = 0; j < children.size(); j++) {
                sb.append("\"").append(children.get(j).contents()).append("\"");
                if (j < children.size() - 1) sb.append(",");
            }
            sb.append("]");
            if (i < nodes.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }
}