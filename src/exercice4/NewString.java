package exercice4;
import graphicLayer.GString;
import stree.parser.SNode;

/**
 * Commande spécialisée pour la création de labels textuels (GString).
 * Extrait la chaîne de caractères du script pour instancier le composant
 * et configure son catalogue de commandes initial.
 */
public class NewString implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // Extraction du texte et instanciation graphique
        String text = method.get(2).contents();
        GString label = new GString(text);
        
        Reference newRef = new Reference(label);
        
        // Configuration des capacités (couleur et mouvement) pour le label
        newRef.addCommand("setColor", new SetColor());
        newRef.addCommand("translate", new Translate());
        
        return newRef;
    }
}