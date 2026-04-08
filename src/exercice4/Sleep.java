package exercice4;
import stree.parser.SNode;

/**
 * Commande de temporisation universelle.
 * Suspend l'exécution du thread courant selon la durée extraite du script.
 * Dépendance : Interface Command.
 */
public class Sleep implements Command {
    @Override
    public Reference run(Reference receiver, SNode method) {
        try {
            // Extraction de la durée en ms et mise en pause
            Thread.sleep(Integer.parseInt(method.get(2).contents()));
        } catch (InterruptedException e) { 
            e.printStackTrace(); 
        }
        // Retourne le récepteur pour permettre la continuité de l'évaluation
        return receiver;
    }
}