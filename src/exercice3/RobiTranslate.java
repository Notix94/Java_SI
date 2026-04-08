package exercice3;

import java.awt.Point;
import graphicLayer.GRect;

/**
 * Commande gérant le déplacement relatif de Robi (GRect).
 * Adapte les coordonnées entières du script pour l'API graphique.
 * Dépendance : Interface Command.
 */
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
        // Applique le vecteur de déplacement via un objet Point
        robi.translate(new Point(dx, dy));
    }
}