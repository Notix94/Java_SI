package exercice4;
import stree.parser.SNode;

public class Interpreter {
    public Reference compute(Environment env, SNode next) {
        if (next.isLeaf()) return env.getReferenceByName(next.contents());
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        return receiver.run(next);
    }
}