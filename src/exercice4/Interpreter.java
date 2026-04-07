package exercice4;
import stree.parser.SNode;

public class Interpreter {
    public Reference compute(Environment env, SNode next) {
<<<<<<< HEAD
=======
    	
>>>>>>> afe240f693f8d7b92c4d64ac2837860c93591550
        if (next.isLeaf()) return env.getReferenceByName(next.contents());
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        return receiver.run(next);
    }
}