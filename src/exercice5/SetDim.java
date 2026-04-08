package exercice5;

import java.awt.Dimension;
import graphicLayer.GRect;
import graphicLayer.GOval;
import stree.parser.SNode;

/**
 * Commande de redimensionnement pour les composants géométriques.
 * Dans la hiérarchie de l'exercice 5, elle permet d'ajuster la taille
 * d'un objet spécifique après qu'il a été résolu par son chemin unique.
 */
public class SetDim implements Command {
    @Override
    public Reference run(Reference receiver, SNode method) {
        Object target = receiver.getReceiver();
        
        // Extraction des dimensions depuis les arguments du script
        int w = Integer.parseInt(method.get(2).contents());
        int h = Integer.parseInt(method.get(3).contents());
        Dimension dim = new Dimension(w, h);

        // Application par transtypage selon le type concret de l'élément
        if (target instanceof GRect) {
            ((GRect) target).setDimension(dim);
        } else if (target instanceof GOval) {
            ((GOval) target).setDimension(dim);
        }

        // Retourne le récepteur pour permettre la continuité des instructions
        return receiver;
    }
}