package exercice7;
import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command {
    @Override
    public Reference run(Reference reference, SNode method, Environment env) {
        String textKey  = method.get(2).contents();
        Reference textRef = env.getReferenceByName(textKey);
        String text = (textRef != null && textRef.getReceiver() instanceof String)
                      ? (String) textRef.getReceiver()
                      : textKey;
        GString label = new GString(text);
        
        Reference newRef = new Reference(label);
        newRef.addCommand("setColor", new SetColor());
        newRef.addCommand("translate", new Translate());
        return newRef;
    }
}