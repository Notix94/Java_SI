package exercice4;
import java.awt.Point;
import graphicLayer.GElement;
import stree.parser.SNode;

/**
 * Commande de déplacement relatif universelle.
 * Exploite l'héritage (GElement) pour s'appliquer à n'importe quel 
 * composant graphique sans distinction de type.
 */
public class Translate implements Command {
    @Override
    public Reference run(Reference receiver, SNode method) {
        // Transtypage vers la classe mère commune pour l'unification
        GElement elem = (GElement) receiver.getReceiver();
        
        int dx = Integer.parseInt(method.get(2).contents());
        int dy = Integer.parseInt(method.get(3).contents());
        
        // Application du vecteur de déplacement
        elem.translate(new Point(dx, dy));
        
        return receiver;
    }
}