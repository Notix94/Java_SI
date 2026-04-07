package exercice4;
import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        String text = method.get(2).contents();
        GString label = new GString(text);
        
        Reference newRef = new Reference(label);
        newRef.addCommand("setColor", new SetColor());
        newRef.addCommand("translate", new Translate());
        return newRef;
    }
}