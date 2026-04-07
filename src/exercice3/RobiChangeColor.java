package exercice3;

import java.awt.Color;
import graphicLayer.GRect;

public class RobiChangeColor implements Command {
    private GRect robi;
    private Color newColor;

    public RobiChangeColor(GRect robi, Color newColor) {
        this.robi = robi;
        this.newColor = newColor;
    }

    @Override
    public void run() {
        robi.setColor(newColor);
    }
}