package exercice4;
import stree.parser.SNode;

/**
 * Cœur de l'évaluation récursive du projet.
 * Son rôle est d'interpréter la structure des S-expressions pour soit 
 * résoudre des variables, soit déclencher des actions sur des objets.
 */
public class Interpreter {

    /**
     * ALGORITHME D'ÉVALUATION :
     * 1. Si le nœud est une feuille : on retourne sa valeur depuis l'Environment.
     * 2. Si c'est une liste : on identifie le récepteur (premier élément) 
     * et on lui délègue l'exécution de la méthode.
     */
    public Reference compute(Environment env, SNode next) {
        // Résolution des identifiants (ex: "robi")
        if (next.isLeaf()) return env.getReferenceByName(next.contents());
        
        // Dispatching de la commande au récepteur concerné
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        return receiver.run(next);
    }
}