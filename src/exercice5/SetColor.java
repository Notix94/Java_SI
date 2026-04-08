package exercice5;
import java.awt.Color;
import java.util.Map;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

/**
 * Commande de modification de couleur.
 * Dans l'exercice 5, elle reste universelle : elle peut être appelée sur 
 * n'importe quel élément de la hiérarchie (nœud ou feuille).
 */
public class SetColor implements Command {
    // Table de correspondance pour traduire les chaînes du script en constantes Java
    private final Map<String, Color> colors = Map.of(
        "black", Color.BLACK, "white", Color.WHITE, "red", Color.RED, 
        "yellow", Color.YELLOW, "blue", Color.BLUE, "green", Color.GREEN
    );

    @Override
    public Reference run(Reference receiver, SNode method) {
        Object target = receiver.getReceiver();
        
        // Résolution de la couleur (noir par défaut si le nom est inconnu)
        Color c = colors.getOrDefault(method.get(2).contents().toLowerCase(), Color.BLACK);
        
        // Application dynamique selon le type réel de l'objet graphique
        if (target instanceof GElement) {
            ((GElement) target).setColor(c);
        } else if (target instanceof GSpace) {
            ((GSpace) target).setColor(c);
        }
        
        // Retourne le récepteur pour permettre l'enchaînement d'instructions
        return receiver;
    }
}