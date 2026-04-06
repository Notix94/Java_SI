package exercice4;
import java.awt.Image;
import javax.swing.ImageIcon;
import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        String path = method.get(2).contents();
        Image imgSource = new ImageIcon(path).getImage();
        GImage img = new GImage(imgSource);
        
        Reference newRef = new Reference(img);
        newRef.addCommand("translate", new Translate());
        return newRef;
    }
}