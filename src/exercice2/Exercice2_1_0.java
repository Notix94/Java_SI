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

public class Exercice2_1_0 {
    GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
    GRect robi = new GRect();
    String script = "(space setColor white) (robi setColor red) " +
            "(robi translate 180 0) (space sleep 500) " +
            "(robi translate 0 80) (space sleep 500) " +
            "(robi translate -180 0) (space sleep 500) " +
            "(robi translate 0 -80)";

    public Exercice2_1_0() {
        space.addElement(robi);
        space.open();
        this.runScript();
    }

    private void runScript() {
        SParser<SNode> parser = new SParser<>();
        List<SNode> rootNodes = null;
        try {
            rootNodes = parser.parse(script);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<SNode> itor = rootNodes.iterator();
        while (itor.hasNext()) {
            this.run(itor.next());
        }
    }

    private void run(SNode expr) {
        // Récupère les enfants : (receiver method arg1 arg2 ...)
        List<SNode> children = expr.children();

        String receiverName = children.get(0).contents();
        String methodName   = children.get(1).contents();

        // Résoudre le récepteur
        Object target;
        if (receiverName.equals("space")) {
            target = space;
        } else if (receiverName.equals("robi")) {
            target = robi;
        } else {
            System.err.println("Récepteur inconnu : " + receiverName);
            return;
        }

        // Récupérer les arguments
        List<String> args = new java.util.ArrayList<>();
        for (int i = 2; i < children.size(); i++) {
        	args.add(children.get(i).contents());
        }

        // Cas spécial : sleep
        if (methodName.equals("sleep")) {
            try {
                Thread.sleep(Long.parseLong(args.get(0)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        // Cas spécial : translate (2 args entiers → Point)
        if (methodName.equals("translate") && args.size() == 2) {
            int dx = Integer.parseInt(args.get(0));
            int dy = Integer.parseInt(args.get(1));
            robi.translate(new Point(dx, dy));
            return;
        }

        // Cas général : setColor, setX, setY, setWidth, setHeight...
        if (args.size() == 1) {
            String arg = args.get(0);

            // Essai Color
            Color color = parseColor(arg);
            if (color != null) {
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Color.class);
                    m.invoke(target, color);
                    return;
                } catch (NoSuchMethodException ignored) {
                } catch (Exception e) { e.printStackTrace(); return; }
            }

            // Essai Integer
         // Essai Integer
            try {
                int val = Integer.parseInt(arg);
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Integer.class);
                    m.invoke(target, val);
                    return;
                } catch (NoSuchMethodException ignored) {}
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, int.class);
                    m.invoke(target, val);
                    return;
                } catch (NoSuchMethodException ignored) {}
            } catch (NumberFormatException ignored) {
            } catch (Exception e) { e.printStackTrace(); }
        }

        // Essai Dimension (2 args)
        if (args.size() == 2) {
            try {
                int w = Integer.parseInt(args.get(0));
                int h = Integer.parseInt(args.get(1));
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Dimension.class);
                    m.invoke(target, new Dimension(w, h));
                    return;
                } catch (NoSuchMethodException ignored) {}
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Point.class);
                    m.invoke(target, new Point(w, h));
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
        new Exercice2_1_0();
    }
}