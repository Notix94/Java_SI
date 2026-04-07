package exercice7;
import stree.parser.SNode;

public class Interpreter {
    public Reference compute(Environment env, SNode next) {
    	
    	//  Si c’est une feuille (nombre, variable, symbole)
        if (next.isLeaf()) {
        	Reference r = env.getReferenceByName(next.contents());
        	if( r != null) return r;
        	try{
        		// si c’est un nombre, le convertir en Reference
        		int val = Integer.parseInt(next.contents());
        		return new Reference(val);
        	} catch (NumberFormatException e){
        		return new Reference(next.contents());
        	}
        }
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        if (receiver == null) throw new Error("Objet inconnu : " + next.get(0).contents());
        return receiver.run(next, env);
    }
}