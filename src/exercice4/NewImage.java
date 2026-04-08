package exercice4;
import java.awt.Image;
import javax.swing.ImageIcon;
import graphicLayer.GImage;
import stree.parser.SNode;

/**
 * Commande spécialisée pour l'instanciation d'images (GImage).
 * Charge une ressource externe via un chemin local et configure les 
 * capacités de l'objet résultant.
 */
public class NewImage implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // Extraction du chemin du fichier image depuis le script
        String path = method.get(2).contents();
        Image imgSource = new ImageIcon(path).getImage();
        GImage img = new GImage(imgSource);
        
        Reference newRef = new Reference(img);
        
        // Initialisation du catalogue de commandes autorisé pour une image
        newRef.addCommand("translate", new Translate());
        
        return newRef;
    }
}