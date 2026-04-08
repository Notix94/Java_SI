package exercice5;

import java.awt.Point;
import graphicLayer.GElement;
import stree.parser.SNode;

/**
 * Commande de déplacement relatif universelle.
 * Dans la structure hiérarchique de l'exercice 5, elle exploite le polymorphisme
 * (GElement) pour s'appliquer indistinctement à tout objet de l'arbre.
 */
public class Translate implements Command {
    @Override
    public Reference run(Reference receiver, SNode method) {
        // Transtypage vers la classe mère commune : GElement.
        // Cela permet de déplacer un Rect, un Oval ou une Image avec la même logique.
        GElement elem = (GElement) receiver.getReceiver();
        
        // Extraction des deltas (dx, dy) depuis les arguments du script
        int dx = Integer.parseInt(method.get(2).contents());
        int dy = Integer.parseInt(method.get(3).contents());
        
        // Application du vecteur de translation
        elem.translate(new Point(dx, dy));
        
        // On retourne le récepteur pour permettre d'enchaîner d'autres 
        // commandes sur cet objet dans le même script.
        return receiver;
    }
}