package exercice4;
import stree.parser.SNode;

/**
 * Interface du Design Pattern Command évoluée pour l'exercice 4.
 * Contrairement à l'exercice 3, elle retourne une Reference pour permettre 
 * l'évaluation récursive et la création dynamique d'objets.
 */
public interface Command {
    /**
     * Exécute une action sur un récepteur à partir d'un nœud syntaxique.
     * @param receiver La référence sur laquelle s'applique la commande.
     * @param method Le nœud contenant le nom de la méthode et ses arguments.
     * @return La Reference résultant de l'action (le récepteur ou un nouvel objet).
     */
    abstract public Reference run(Reference receiver, SNode method);
}