package exercice3;

/**
 * Commande de temporisation suspendant l'exécution du script.
 * Utilise Thread.sleep pour créer une pause (en millisecondes).
 * Dépendance : Interface Command.
 */
public class SpaceSleep implements Command {
    private int duration;

    public SpaceSleep(int duration) {
        this.duration = duration;
    }

    @Override
    public void run() {
        try {
            // Suspend le thread actuel pour la durée spécifiée
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}