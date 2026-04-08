package exercice5;

import java.awt.Image;
import javax.swing.ImageIcon;
import graphicLayer.GImage;
import stree.parser.SNode;

/**
 * Commande spécialisée pour l'instanciation d'images (GImage).
 * Dans l'exercice 5, elle produit des feuilles de l'arbre hiérarchique :
 * contrairement aux formes, une image ne peut pas contenir d'autres éléments.
 */
public class NewImage implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // Extraction du chemin de la ressource (ex: "alien.gif")
        String path = method.get(2).contents();
        Image imgSource = new ImageIcon(path).getImage();
        GImage img = new GImage(imgSource);
        
        // CRÉATION DE LA RÉFÉRENCE
        // Le nom est null car il sera défini par le parent via son chemin complet.
        Reference newRef = new Reference(img, null);
        
        // Les images sont des objets terminaux : on ne leur ajoute que 
        // des commandes de manipulation basiques, pas de commande "add".
        newRef.addCommand("translate", new Translate());
        
        return newRef;
    }
}