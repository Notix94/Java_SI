package exercice7;

import java.awt.Color;
import java.util.Map;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;
import exercice6.Command;
import exercice6.Reference;
import exercice6.Environment;

public class SetColor implements Command {
    private final Map<String, Color> colors = Map.of(
        "black", Color.BLACK, "white", Color.WHITE, "red", Color.RED, 
        "yellow", Color.YELLOW, "blue", Color.BLUE, "green", Color.GREEN
    );

    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        Object target = receiver.getReceiver();
        Color c = Color.BLACK; // Couleur par défaut si on ne trouve rien

        // On vérifie le nombre d'enfants dans la S-Expression
        // method.children().size() == 3 => (robi setColor yellow)
        // method.children().size() == 5 => (robi setColor 255 255 0)
        int nbArgs = method.children().size();

        if (nbArgs == 3) {
            // CAS 1 : Recherche par nom (ex: "yellow")
            String colorName = method.get(2).contents().toLowerCase();
            c = colors.getOrDefault(colorName, Color.BLACK);
        } 
        else if (nbArgs == 5) {
            // CAS 2 : Lecture des 3 composants RGB
            try {
                int r = Integer.parseInt(method.get(2).contents());
                int g = Integer.parseInt(method.get(3).contents());
                int b = Integer.parseInt(method.get(4).contents());
                c = new Color(r, g, b);
            } catch (NumberFormatException e) {
                System.err.println("Erreur : Les composants RGB doivent être des entiers.");
            }
        }

        // Application de la couleur sur la cible
        if (target instanceof GElement) {
            ((GElement) target).setColor(c);
        } else if (target instanceof GSpace) {
            ((GSpace) target).setColor(c);
        }
        
        return receiver;
    }
}