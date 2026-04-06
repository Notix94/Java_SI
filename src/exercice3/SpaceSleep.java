package exercice3;

public class SpaceSleep implements Command {
    private int duration;

    public SpaceSleep(int duration) {
        this.duration = duration;
    }

    @Override
    public void run() {
        try {
            // Provoque une mise en sommeil pour le nombre de millisecondes passé en argument
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}