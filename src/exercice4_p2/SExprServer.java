package exercice4_p2;

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
 * Serveur de S-Expressions — Version Finale Corrigée.
 * Gère la communication réseau, l'interprétation des commandes,
 * la persistance (Save/Load) et les captures d'écran.
 */
public class SExprServer {
    private static final int PORT = 9876;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Serveur Exercice 4 démarré sur le port " + PORT);

            // Initialisation de l'espace graphique du serveur
            GSpace space = new GSpace("Rendu Serveur", new Dimension(200, 100));
            GRect  robi  = new GRect();
            space.addElement(robi);
            space.open();
            
            // Construction de l'environnement et de l'interpréteur
            Environment env = buildEnv(space, robi);
            Interpreter interpreter = new Interpreter();

            while (true) {
                try (Socket client = serverSocket.accept();
                     BufferedReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter    out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)) {

                    String sexpr = in.readLine();
                    if (sexpr == null) continue;
                    
                    // Nettoyage esthétique des logs pour la console (suppression des guillemets)
                    System.out.println("📩 Reçu : " + sexpr.replace("\"", ""));

                    // Parsing de la commande reçue
                    SParser<SNode> parser = new SParser<>();
                    List<SNode> nodes = parser.parse(sexpr);
                    
                    Object responseToClient = nodes;

                    // Exécution de chaque nœud
                    for (SNode node : nodes) {
                        Reference result = interpreter.compute(env, node);
                        
                        if (result != null) {
                            Object obj = result.getReceiver();
                            // Si le résultat est une liste (mouvements), on prépare le JSON
                            if (obj instanceof List) {
                                responseToClient = obj;
                            } 
                            // Si le résultat est une longue String (Screenshot), on l'envoie telle quelle
                            else if (obj instanceof String && ((String) obj).length() > 100) {
                                responseToClient = obj;
                            }
                        }
                    }

                    // Envoi de la réponse au client
                    if (responseToClient instanceof String) {
                        out.println(responseToClient); // Envoi direct (Base64)
                    } else {
                        out.println(serialize((List<SNode>) responseToClient)); // Envoi formaté (JSON)
                    }

                } catch (Exception e) {
                    System.err.println("❌ Erreur traitement : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Définit le dictionnaire des commandes disponibles pour le serveur.
     */
    private static Environment buildEnv(GSpace space, GRect robi) {
        Environment env = new Environment();

        // Référence pour l'objet "space"
        Reference spaceRef = new Reference(space);
        spaceRef.addCommand("setColor",   new SetColor()); // Version RGB de l'exo 7
        spaceRef.addCommand("sleep",      new Sleep());
        spaceRef.addCommand("save",       new SaveCommand());
        spaceRef.addCommand("load",       new LoadCommand());
        spaceRef.addCommand("screenshot", new ScreenshotCommand());
        env.addReference("space", spaceRef);

        // Référence pour l'objet "robi"
        Reference robiRef = new Reference(robi);
        robiRef.addCommand("setColor",    new SetColor());
        robiRef.addCommand("translate",   new Translate());
        robiRef.addCommand("setPosition", new SetPosition());
        env.addReference("robi", robiRef);

        return env;
    }

    /**
     * Sérialise une liste de nœuds en format JSON.
     * Les guillemets ici sont obligatoires pour respecter le formatage JSON standard.
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