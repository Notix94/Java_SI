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
 * Cette classe implémente un interpréteur de scripts basique.
 * Son intention est de dissocier la logique de mouvement du code source Java
 * en utilisant des S-expressions pour piloter les objets graphiques.
 * * Variables d'instance :
 * - space/robi : les cibles graphiques du script.
 * - script : la suite d'instructions textuelles à exécuter.
 * * Dépendances : stree.parser pour l'analyse syntaxique du script.
 */
public class Exercice2_1_0 {
    GSpace space = new GSpace("Exercice 2_1", new Dimension(200, 100));
    GRect robi = new GRect();
    
    // Script simulant un parcours rectangulaire avec des pauses
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

    /**
     * Transforme la chaîne de caractères 'script' en structures de données (SNodes)
     * et lance l'exécution séquentielle de chaque instruction.
     */
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

    /**
     * ALGORITHME D'INTERPRÉTATION ET DE RÉFLEXION :
     * Pour chaque expression, la méthode identifie le récepteur (target) et tente 
     * d'appeler dynamiquement la méthode correspondante via l'API Reflection de Java.
     * * @param expr Un SNode représentant une commande (ex: (robi setColor red))
     */
    private void run(SNode expr) {
        List<SNode> children = expr.children();
        if (children.size() < 2) return;

        String receiverName = children.get(0).contents();
        String methodName   = children.get(1).contents();

        // Résolution dynamique du récepteur
        Object target;
        if (receiverName.equals("space")) {
            target = space;
        } else if (receiverName.equals("robi")) {
            target = robi;
        } else {
            System.err.println("Récepteur inconnu : " + receiverName);
            return;
        }

        // Extraction des arguments sous forme de chaînes
        List<String> args = new java.util.ArrayList<>();
        for (int i = 2; i < children.size(); i++) {
        	args.add(children.get(i).contents());
        }

        // Traitement des cas particuliers (méthodes n'existant pas dans l'API graphique)
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

        /**
         * DISPATCHING DYNAMIQUE :
         * L'algorithme tente de faire correspondre les arguments du script aux signatures
         * des méthodes Java (Color, Integer, Dimension/Point) en essayant plusieurs types.
         */
        if (args.size() == 1) {
            String arg = args.get(0);

            // Tentative d'appel avec un paramètre de type Color
            Color color = parseColor(arg);
            if (color != null) {
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, Color.class);
                    m.invoke(target, color);
                    return;
                } catch (NoSuchMethodException ignored) {} 
                catch (Exception e) { e.printStackTrace(); return; }
            }

            // Tentative d'appel avec un paramètre de type int ou Integer
            try {
                int val = Integer.parseInt(arg);
                try {
                    java.lang.reflect.Method m = target.getClass().getMethod(methodName, int.class);
                    m.invoke(target, val);
                    return;
                } catch (NoSuchMethodException ignored) {
                    // Cette exception est gérée, on passe à la suite si la méthode n'existe pas
                } catch (Exception e) {
                    // On attrape IllegalAccessException et InvocationTargetException
                    e.printStackTrace(); 
                }
            } catch (NumberFormatException ignored) {}
        }

        // Tentative d'appel avec deux paramètres (souvent Dimension ou Point)
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

    /**
     * Mappe les noms de couleurs textuels aux constantes de la classe java.awt.Color.
     */
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