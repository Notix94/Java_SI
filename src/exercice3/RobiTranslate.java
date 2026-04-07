package exercice3;

import java.awt.Point; // Nécessaire pour utiliser la méthode translate de GBounded
import graphicLayer.GRect;

public class RobiTranslate implements Command {
    private GRect robi;
    private int dx, dy;

    public RobiTranslate(GRect robi, int dx, int dy) {
        this.robi = robi;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void run() {
        // On crée un nouvel objet Point car la signature est translate(Point gap)
        robi.translate(new Point(dx, dy));
    }
}