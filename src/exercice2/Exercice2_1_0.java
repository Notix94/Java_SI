package exercice2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

/**
 * Interpréteur de scripts Exercice 2.
 */
public class Exercice2_1_0 {
    GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
    GRect robi = new GRect();
    String script;

    /**
     * CONSTRUCTEUR PAR DÉFAUT
     * Utilise le script de démonstration classique.
     */
    public Exercice2_1_0() {
        this("(space setColor white) (robi setColor red) " +
             "(robi translate 180 0) (space sleep 500) " +
             "(robi translate 0 80) (space sleep 500) " +
             "(robi translate -180 0) (space sleep 500) " +
             "(robi translate 0 -80)");
    }

    
    public Exercice2_1_0(String script) {
        this.script = script;
        space.addElement(robi);
        space.open();
    }

    /**
     * Lance l'exécution du script stocké.
     */
    public void run() {
        this.runScript();
    }

    /**
     * Parse et itère sur les instructions du script.
     */
    private void runScript() {
        SParser<SNode> parser = new SParser<>();
        List<SNode> rootNodes = null;
        try {
            rootNodes = parser.parse(script);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rootNodes != null) {
            Iterator<SNode> itor = rootNodes.iterator();
            while (itor.hasNext()) {
                this.executeInternal(itor.next());
            }
        }
    }

    /**
     * Cœur de l'exécution (anciennement run(SNode expr))
     */
    private void executeInternal(SNode expr) {
        List<SNode> children = expr.children();
        if (children.size() < 2) return;

        String receiverName = children.get(0).contents();
        String methodName   = children.get(1).contents();

        Object target;
        if (receiverName.equals("space")) {
            target = space;
        } else if (receiverName.equals("robi")) {
            target = robi;
        } else {
            System.err.println("Récepteur inconnu : " + receiverName);
            return;
        }

        List<String> args = new java.util.ArrayList<>();
        for (int i = 2; i < children.size(); i++) {
            args.add(children.get(i).contents());
        }

        // --- GESTION DES MÉTHODES SPÉCIFIQUES ---
        if (methodName.equals("sleep")) {
            try {
                Thread.sleep(Long.parseLong(args.get(0)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        if (methodName.equals("translate") && args.size() == 2) {
            int dx = Integer.parseInt(args.get(0));
            int dy = Integer.parseInt(args.get(1));
            robi.translate(new Point(dx, dy));
            return;
        }

        // --- RÉFLEXION JAVA ---
        if (args.size() == 1) {
            String arg = args.get(0);
            Color color = parseColor(arg);
            if (color != null) {
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Color.class);
                    m.invoke(target, color);
                    return;
                } catch (NoSuchMethodException ignored) {} 
                catch (Exception e) { e.printStackTrace(); return; }
            }

            try {
                int val = Integer.parseInt(arg);
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, int.class);
                    m.invoke(target, val);
                    return;
                } catch (NoSuchMethodException ignored) {
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            } catch (NumberFormatException ignored) {}
        }

        if (args.size() == 2) {
            try {
                int w = Integer.parseInt(args.get(0));
                int h = Integer.parseInt(args.get(1));
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Dimension.class);
                    m.invoke(target, new Dimension(w, h));
                    return;
                } catch (NoSuchMethodException ignored) {}
            } catch (Exception e) { e.printStackTrace(); }
        }
        System.err.println("Méthode non trouvée : " + methodName + " avec args " + args);
    }

    private Color parseColor(String name) {
        switch (name.toLowerCase()) {
            case "black":  return Color.BLACK;
            case "white":  return Color.WHITE;
            case "red":    return Color.RED;
            case "green":  return Color.GREEN;
            case "blue":   return Color.BLUE;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "gray":   return Color.GRAY;
            default:       return null;
        }
    }

    public static void main(String[] args) {
        // Standalone launch
        new Exercice2_1_0().run();
    }
}