package exercice3;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	
	// Le script qui sera interprété ligne par ligne
	String script = "" +
	"   (space setColor black) " +
	"   (robi setColor yellow)" +
	"   (space sleep 1000)" +
	"   (space setColor white)\n" + 
	"   (space sleep 1000)" +
	"	(robi setColor red) \n" + 
	"   (space sleep 1000)" +
	"	(robi translate 100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 50)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate -100 0)\n" + 
	"	(space sleep 1000)\n" + 
	"	(robi translate 0 -40)";

	public Exercice3_0() {
		space.addElement(robi);
		space.open();
		this.runScript();
	}

	private void runScript() {
		SParser<SNode> parser = new SParser<>();
		List<SNode> rootNodes = null;
		try {
			rootNodes = parser.parse(script);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator<SNode> itor = rootNodes.iterator();
		while (itor.hasNext()) {
			this.run(itor.next());
		}
	}

	private void run(SNode expr) {
		// On récupère l'instance de la commande correspondante 
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		
		// L'exécution revient à envoyer le message run à l'instance 
		cmd.run();
	}

	/**
	 * Analyse la S-expression pour retourner l'instance de Command appropriée.
	 * Utilise une double conditionnelle : la cible, puis l'action.
	 */
	Command getCommandFromExpr(SNode expr) {
		String target = expr.get(0).contents(); // "space" ou "robi" 
		String action = expr.get(1).contents(); // "setColor", "sleep", "translate" 

		if (target.equals("space")) {
			if (action.equals("setColor")) {
				Color c = getColorFromString(expr.get(2).contents());
				return new SpaceChangeColor(space, c); 
			} else if (action.equals("sleep")) {
				int delay = Integer.parseInt(expr.get(2).contents());
				return new SpaceSleep(delay);
			}
		} else if (target.equals("robi")) {
			if (action.equals("setColor")) {
				Color c = getColorFromString(expr.get(2).contents());
				return new RobiChangeColor(robi, c);
			} else if (action.equals("translate")) {
				int dx = Integer.parseInt(expr.get(2).contents());
				int dy = Integer.parseInt(expr.get(3).contents());
				return new RobiTranslate(robi, dx, dy);
			}
		}
		return null;
	}

	/**
	 * Méthode utilitaire pour convertir le texte du script en objet Color.
	 */
	private Color getColorFromString(String colorName) {
		switch (colorName.toLowerCase()) {
			case "black": return Color.BLACK;
			case "white": return Color.WHITE;
			case "red": return Color.RED;
			case "yellow": return Color.YELLOW;
			case "blue": return Color.BLUE;
			default: return Color.BLACK;
		}
	}

	public static void main(String[] args) {
		new Exercice3_0();
	}
}