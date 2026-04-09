package exercice2_p2;
import exercice6.*;
import graphicLayer.*;
import stree.parser.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class SExprServer {
    private static final int PORT = 9876;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);

            // ✅ Une seule fois
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
                    System.out.println("Reçu : " + sexpr);

                    SParser<SNode> parser = new SParser<>();
                    List<SNode> nodes = parser.parse(sexpr);

                    for (SNode node : nodes) {
                        interpreter.compute(env, node);
                    }

                    out.println(serialize(nodes));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Environment buildEnv(GSpace space, GRect robi) {
        Environment env = new Environment();

        Reference spaceRef = new Reference(space);
        spaceRef.addCommand("setColor", new SetColor());
        spaceRef.addCommand("sleep",    new Sleep());
        env.addReference("space", spaceRef);

        Reference robiRef = new Reference(robi);
        robiRef.addCommand("setColor",    new SetColor());
        robiRef.addCommand("translate",   new Translate());
        robiRef.addCommand("setPosition", new SetPosition());
        env.addReference("robi", robiRef);

        return env;
    }

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