package exercice7;

import stree.parser.SNode;

/**
 * Commande "strictement inférieur" (<). Retourne true si le premier opérande
 * est strictement inférieur au second. Exemple : (< 2 5) → true
 */
public class StricInfCommand implements Command {
	@Override
	public Reference run(Reference self, SNode method, Environment env) {
		Reference a = new Interpreter().compute(env, method.get(2));
		Reference b = new Interpreter().compute(env, method.get(3));
		boolean result = a.asInt() < b.asInt();
		return new Reference(result);
	}
}