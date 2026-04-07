package exercice4;

import java.awt.Dimension;
import graphicLayer.GElement;
import graphicLayer.GRect;
import graphicLayer.GOval;
import stree.parser.SNode;

public class SetDim implements Command {
    @Override
    public Reference run(Reference receiver, SNode method) {
        Object target = receiver.getReceiver();
        
        // On récupère la largeur et la hauteur depuis le script
        int w = Integer.parseInt(method.get(2).contents());
        int h = Integer.parseInt(method.get(3).contents());
        Dimension dim = new Dimension(w, h);

        // On vérifie le type réel de l'objet pour appeler la bonne méthode
        if (target instanceof GRect) {
            ((GRect) target).setDimension(dim);
        } else if (target instanceof GOval) {
            ((GOval) target).setDimension(dim);
        }
        //ajouter d'autres types ici si nécessaire (ex: GImage)

        return receiver;
    }
}