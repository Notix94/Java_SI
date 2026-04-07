package exercice5;

import java.awt.Image;
import javax.swing.ImageIcon;
import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // (Image new "alien.gif")
        String path = method.get(2).contents();
        Image imgSource = new ImageIcon(path).getImage();
        GImage img = new GImage(imgSource);
        
        // MODIF : On ajoute "null" en deuxième argument
        Reference newRef = new Reference(img, null);
        
        newRef.addCommand("translate", new Translate());
        // Note : On ne lui ajoute pas "add" car une Image n'est pas un container
        
        return newRef;
    }
}