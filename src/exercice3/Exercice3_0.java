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

/**
 * Son intention est d'implémenter le 
 * Design Pattern Command pour gagner en modularité. 
 * Contrairement à l'exercice précédent, la logique d'exécution n'est plus 
 * centralisée dans l'interpréteur mais distribuée dans des classes spécialisées.
 * * Variables d'instance :
 * - space/robi : les cibles graphiques manipulées par le script.
 * - script : séquence d'instructions textuelles testant le mouvement et la couleur.
 * * Dépendances : Interface Command et ses implémentations (RobiTranslate, SpaceSleep, etc.)
 */
public class Exercice3_0 {
	GSpace space = new GSpace("Exercice 3", new Dimension(200, 100));
	GRect robi = new GRect();
	
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
	
	public Exercice3_0(String scriptPerso) {
	    this.script = scriptPerso;
	    space.addElement(robi);
	    space.open();
	    this.runScript();
	}

	/**
	 * Analyse le script et itère sur les nœuds racines pour lancer l'exécution.
	 */
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

	/**
	 * Point d'entrée de l'exécution d'une commande.
	 * Applique le principe de polymorphisme : on appelle cmd.run() sans savoir 
	 * s'il s'agit d'un mouvement ou d'un changement de couleur.
	 */
	private void run(SNode expr) {
		
		Command cmd = getCommandFromExpr(expr);
		if (cmd == null)
			throw new Error("unable to get command for: " + expr);
		
		cmd.run();
	}

	/**
	 * ALGORITHME : FACTORY DE COMMANDES
	 * Cette méthode joue le rôle de "fabrique". Elle analyse la structure du nœud 
	 * (Cible + Action) pour instancier la classe concrète correspondante.
	 * * @param expr Le nœud syntaxique (S-expression)
	 * @return Une instance de Command prête à être exécutée, ou null si l'instruction est invalide.
	 */
	Command getCommandFromExpr(SNode expr) {
		String target = expr.get(0).contents(); // Identifie le récepteur ("space" ou "robi") 
		String action = expr.get(1).contents(); // Identifie l'action ("setColor", "sleep", "translate") 

		// Dispatching vers les commandes du Space
		if (target.equals("space")) {
			if (action.equals("setColor")) {
				Color c = getColorFromString(expr.get(2).contents());
				return new SpaceChangeColor(space, c); 
			} else if (action.equals("sleep")) {
				int delay = Integer.parseInt(expr.get(2).contents());
				return new SpaceSleep(delay);
			}
		} 
		// Dispatching vers les commandes de Robi
		else if (target.equals("robi")) {
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
	 * Mappe les chaînes de caractères du script vers des objets java.awt.Color.
	 * Utilisé pour assurer la transition entre le langage de script et l'API Java.
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