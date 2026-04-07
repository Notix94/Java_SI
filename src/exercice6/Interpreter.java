package exercice6;
import stree.parser.SNode;

public class Interpreter {
    public Reference compute(Environment env, SNode next) {
    	
        if (next.isLeaf()) {
        	Reference r = env.getReferenceByName(next.contents());
        	return r != null ? r : new Reference(next.contents());
        }
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        if (receiver == null) throw new Error("Objet inconnu : " + next.get(0).contents());
        return receiver.run(next, env);
    }
}