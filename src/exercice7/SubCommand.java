package exercice7;

import stree.parser.SNode;


/**
 * Commande de soustraction.
 * Retourne la différence entre deux entiers.
 * Exemple : (- 5 3) → 2
 */
public class SubCommand implements Command {
    @Override
    public Reference run(Reference self, SNode method, Environment env) {
        Reference a = new Interpreter().compute(env, method.get(2));
        Reference b = new Interpreter().compute(env, method.get(3));
        int result = a.asInt() - b.asInt();
        return new Reference(result);
    }
}