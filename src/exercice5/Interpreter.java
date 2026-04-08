package exercice5;
import stree.parser.SNode;

/**
 * Moteur d'évaluation récursif du langage.
 * Dans l'exercice 5, son rôle devient central car il permet de résoudre 
 * des chemins complexes et d'évaluer des expressions imbriquées 
 * (ex: lors de l'ajout d'un élément).
 */
public class Interpreter {

    /**
     * ALGORITHME D'ÉVALUATION :
     * 1. Cas de base (Feuille) : Résolution d'identifiant dans l'annuaire global.
     * 2. Cas récursif (Liste) : Identification du récepteur et dispatching de la commande.
     */
    public Reference compute(Environment env, SNode next) {
        // Résolution des noms (ex: "space.robi"). 
        // L'Environment se charge de trouver l'objet associé à ce chemin.
        if (next.isLeaf()) return env.getReferenceByName(next.contents());
        
        // Résolution du récepteur pour une commande (ex: "space")
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        
        // Délégation de l'exécution au récepteur trouvé
        if (receiver != null) {
            return receiver.run(next);
        } else {
            throw new Error("Récepteur inconnu : " + next.get(0).contents());
        }
    }
}