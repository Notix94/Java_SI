package exercice7;

import stree.parser.SNode;

/**
 * Commande "strictement supérieur" (>). Retourne true si le premier opérande
 * est strictement supérieur au second. Exemple : (> 5 3) → true
 */
public class StricSupCommand implements Command {
	@Override
	public Reference run(Reference self, SNode method, Environment env) {
		Reference a = new Interpreter().compute(env, method.get(2));
		Reference b = new Interpreter().compute(env, method.get(3));
		boolean result = a.asInt() > b.asInt();
		return new Reference(result);
	}
}