package exercice5;
import stree.parser.SNode;

/**
 * Interface pivot du Design Pattern Command.
 * Dans l'exercice 5, elle permet d'unifier toutes les actions (création, 
 * modification, ajout) tout en supportant la structure hiérarchique grâce 
 * au retour de type 'Reference'.
 */
public interface Command {
    /**
     * Exécute une action scriptée sur un récepteur donné.
     * @param receiver L'objet (ou la classe) sur lequel s'applique la commande.
     * @param method Le nœud contenant l'action et ses paramètres.
     * @return La Reference résultant de l'exécution (essentiel pour le chaînage).
     */
    abstract public Reference run(Reference receiver, SNode method);
}