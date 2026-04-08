package exercice7;

import stree.parser.SNode;

/**
 * Commande d'addition.
 * Évalue les deux opérandes de l'expression S et retourne leur somme.
 * Exemple : (+ 2 3) → 5
 */
public class AddCommand implements Command {
    @Override
    public Reference run(Reference self, SNode method, Environment env) {
        Reference a = new Interpreter().compute(env, method.get(2));
        Reference b = new Interpreter().compute(env, method.get(3));
        int sum = a.asInt() + b.asInt();
        return new Reference(sum);
    }
}