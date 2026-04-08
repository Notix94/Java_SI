package exercice4;
import java.awt.Color;
import java.util.Map;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

/**
 * Commande de modification de couleur pour les objets graphiques.
 * Gère la conversion des noms textuels en constantes Java Color.
 * Dépendances : GElement, GSpace.
 */
public class SetColor implements Command {
    // Table de correspondance entre les noms du script et l'API Java
    private final Map<String, Color> colors = Map.of(
        "black", Color.BLACK, "white", Color.WHITE, "red", Color.RED, 
        "yellow", Color.YELLOW, "blue", Color.BLUE, "green", Color.GREEN
    );

    @Override
    public Reference run(Reference receiver, SNode method) {
        Object target = receiver.getReceiver();
        Color c = colors.getOrDefault(method.get(2).contents().toLowerCase(), Color.BLACK);
        
        // Dispatching selon le type réel du composant graphique
        if (target instanceof GElement) ((GElement) target).setColor(c);
        else if (target instanceof GSpace) ((GSpace) target).setColor(c);
        
        return receiver;
    }
}