package exercice7;

import stree.parser.SNode;

/**
 * Commande de multiplication. Multiplie deux entiers. Exemple : (* 4 3) → 12
 */
public class MulCommand implements Command {
	@Override
	public Reference run(Reference self, SNode method, Environment env) {
		Reference a = new Interpreter().compute(env, method.get(2));
		Reference b = new Interpreter().compute(env, method.get(3));
		int result = a.asInt() * b.asInt();
		return new Reference(result);
	}
}