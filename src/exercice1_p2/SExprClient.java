package exercice1_p2;
import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;

import java.awt.*;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.*;

public class SExprClient {
    private static final String HOST   = "localhost";
    private static final int    PORT   = 9876;
    private static final String SCRIPT =
        "(space setColor white) (robi setColor red) " +
        "(robi translate 180 0) (space sleep 500) " +
        "(robi translate 0 80)  (space sleep 500) " +
        "(robi translate -180 0)(space sleep 500) " +
        "(robi translate 0 -80)";

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(SCRIPT);
            String json = in.readLine();
            System.out.println("Reçu JSON : " + json);

            GSpace space = new GSpace("Rendu Client", new Dimension(200, 100));
            GRect  robi  = new GRect();
            space.addElement(robi);
            space.open();

            Environment env = buildEnv(space, robi);
            executeFromJson(json, env);
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

    private static void executeFromJson(String json, Environment env) {
        json = json.trim().substring(1, json.length() - 1);
        String[] nodeStrs = json.split("\\],\\[");
        for (String nodeStr : nodeStrs) {
            nodeStr = nodeStr.replaceAll("[\\[\\]]", "");
            String[] parts = nodeStr.split(",");
            List<String> tokens = new ArrayList<>();
            for (String p : parts) tokens.add(p.replaceAll("\"", "").trim());
            executeTokens(tokens, env);
        }
    }

    private static void executeTokens(List<String> tokens, Environment env) {
        String receiverName = tokens.get(0);
        String methodName   = tokens.get(1);

        Reference ref = env.getReferenceByName(receiverName);
        if (ref == null) { System.err.println("Inconnu : " + receiverName); return; }

        if (methodName.equals("sleep")) {
            try { Thread.sleep(Long.parseLong(tokens.get(2))); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return;
        }

        Command cmd = ref.getCommandByName(methodName);
        if (cmd == null) { System.err.println("Commande inconnue : " + methodName); return; }
        cmd.run(ref, buildSNode(tokens), env);
    }

    private static SNode buildSNode(List<String> tokens) {
        return new SNode() {
            @Override public Boolean isLeaf() { return false; }
            @Override public String contents() { return tokens.get(0); }
            @Override public java.util.List<SNode> children() {
                java.util.List<SNode> kids = new ArrayList<>();
                for (String t : tokens) {
                    final String val = t;
                    kids.add(new SNode() {
                        @Override public Boolean isLeaf() { return true; }
                        @Override public String contents() { return val; }
                        @Override public java.util.List<SNode> children() { return Collections.emptyList(); }
                        @Override public void setContents(String c) {}
                        @Override public void addToContents(Character c) {}
                        @Override public void setParent(SNode p) {}
                        @Override public void addChild(SNode c) {}
                        @Override public SNode parent() { return null; }
                        @Override public int quote() { return 0; }
                        @Override public void quote(int q) {}
                        @Override public Object alien() { return null; }
                        @Override public void setAlien(Object o) {}
                    });
                }
                return kids;
            }
            @Override public void setContents(String c) {}
            @Override public void addToContents(Character c) {}
            @Override public void setParent(SNode p) {}
            @Override public void addChild(SNode child) {}
            @Override public SNode parent() { return null; }
            @Override public int quote() { return 0; }
            @Override public void quote(int q) {}
            @Override public Object alien() { return null; }
            @Override public void setAlien(Object o) {}
        };
    }
}