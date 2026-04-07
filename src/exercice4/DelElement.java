package exercice4;
import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

public class DelElement implements Command {
    private Environment env;
    public DelElement(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference receiver, SNode method) {
        String name = method.get(2).contents();
        Reference refToDelete = env.getReferenceByName(name);
        
        if (refToDelete != null) {
            GContainer container = (GContainer) receiver.getReceiver();
            container.removeElement((GElement) refToDelete.getReceiver());
        }
        return receiver;
    }
}