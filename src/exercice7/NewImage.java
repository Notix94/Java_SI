package exercice7;
import java.awt.Image;
import javax.swing.ImageIcon;
import graphicLayer.GImage;
import stree.parser.SNode;

public class NewImage implements Command {
    @Override
    public Reference run(Reference reference, SNode method, Environment env) {
        String pathKey = method.get(2).contents();
        Reference pathRef = env.getReferenceByName(pathKey);
        String path = (pathRef != null && pathRef.getReceiver() instanceof String)
                      ? (String) pathRef.getReceiver()
                      : pathKey;
        System.out.println("DEBUG - fichier existe : " + 
        	    new java.io.File(path).exists());
        Image imgSource = new ImageIcon(path).getImage();
        GImage img = new GImage(imgSource);
        
        Reference newRef = new Reference(img);
        newRef.addCommand("translate", new Translate());
        newRef.addCommand("setColor",  new SetColor());
        return newRef;
    }
}