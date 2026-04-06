package exercice3;

import java.awt.Color;
import graphicLayer.GSpace;

/**
 * Commande permettant de changer la couleur de fond du GSpace.
 * Cette classe met en œuvre l'interface Command.
 */
public class SpaceChangeColor implements Command {
    private GSpace space;
    private Color newColor; 

    /**
     * Le constructeur initialise la commande avec la cible et la valeur.
     * @param space L'instance de la fenêtre à modifier.
     * @param newColor La couleur extraite du script.
     */
    public SpaceChangeColor(GSpace space, Color newColor) {
        this.space = space;
        this.newColor = newColor;
    }

    /**
     * L'exécution de la commande de script revient à envoyer le message run.
     */
    @Override
    public void run() {
        // Applique la nouvelle couleur au conteneur principal.
        this.space.setColor(newColor);
    }
}