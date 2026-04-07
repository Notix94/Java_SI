package exercice6;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class ClearCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        ((GSpace) receiver.getReceiver()).clear();
        return receiver;
    }
}