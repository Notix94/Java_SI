package exercice7;
import stree.parser.SNode;

public class Sleep implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        try {
            Thread.sleep(Integer.parseInt(method.get(2).contents()));
        } catch (InterruptedException e) { e.printStackTrace(); }
        return receiver;
    }
}