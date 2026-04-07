package exercice7;
import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

public class AddElement implements Command {
    private Environment env;
    public AddElement(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference reference, SNode method, Environment env) {
        String name = method.get(2).contents();
        // On évalue la partie (rect.class new) via l'interpréteur
        Reference newElemRef = new Interpreter().compute(env, method.get(3));
        
        GContainer container = (GContainer) reference.getReceiver();
        container.addElement((GElement) newElemRef.getReceiver());
        
        // On l'enregistre dans l'annuaire
        env.addReference(name, newElemRef);
        return newElemRef;
    }
}