package exercice6;
import graphicLayer.GSpace;
import stree.parser.SNode;

/**
 * Commande de réinitialisation graphique.
 * Permet de vider l'espace de rendu (GSpace) de tous ses éléments
 * pour repartir d'une fenêtre blanche.
 */
public class ClearCommand implements Command {
    @Override
    /**
     * Exécute le nettoyage du récepteur.
     * Note : Cette commande suppose que le récepteur est une instance de GSpace.
     */
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Transtypage vers GSpace pour accéder à la méthode de nettoyage
        ((GSpace) receiver.getReceiver()).clear();
        
        // Retourne le récepteur (le space) pour permettre de réenchaîner 
        // des commandes immédiatement après le clear.
        return receiver;
    }
}