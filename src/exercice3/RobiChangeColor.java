package exercice3;

import java.awt.Color;
import graphicLayer.GRect;

/**
 * Commande concrète pour modifier la couleur de Robi (GRect).
 * Encapsule la cible (robi) et la nouvelle couleur (newColor).
 * Dépendance : Interface Command.
 */
public class RobiChangeColor implements Command {
    private GRect robi;
    private Color newColor;

    public RobiChangeColor(GRect robi, Color newColor) {
        this.robi = robi;
        this.newColor = newColor;
    }

    @Override
    public void run() {
        // Applique le changement d'état à l'objet graphique
        robi.setColor(newColor);
    }
}