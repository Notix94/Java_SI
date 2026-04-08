package exercice4;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/**
 * Point d'entrée de l'exercice 4 proposant un interpréteur interactif.
 * Initialise l'environnement avec des objets et gère la boucle 
 * de lecture clavier pour l'exécution dynamique de scripts.
 */
public class Exercice4_1_0 {
	private Environment environment = new Environment();

	public Exercice4_1_0() {
		GSpace space = new GSpace("Exercice 4.1 - Test", new Dimension(200, 100));
		GRect robi = new GRect();
		space.addElement(robi);
		space.open();

		// Encapsulation des objets graphiques dans des Reference universelles
		Reference spaceRef = new Reference(space);
		Reference robiRef = new Reference(robi);

		// Configuration du catalogue de commandes par objet
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		robiRef.addCommand("setColor", new SetColor());
		robiRef.addCommand("translate", new Translate());

		environment.addReference("space", spaceRef);
		environment.addReference("robi", robiRef);

		this.mainLoop();
	}

	/**
	 * ALGORITHME : BOUCLE DE LECTURE 
	 * Lit les instructions au clavier, les transforme en arbres syntaxiques (SNodes)
	 * et les envoie à l'interpréteur.
	 */
	private void mainLoop() {
		while (true) {
			System.out.print("> ");
			String input = Tools.readKeyboard();
			
			if (input == null || input.trim().isEmpty()) continue;

			SParser<SNode> parser = new SParser<>();
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (compiled != null) {
				Iterator<SNode> itor = compiled.iterator();
				while (itor.hasNext()) {
					this.run((SNode) itor.next());
				}
			}
		}
	}

	/**
	 * Résout le récepteur via l'environnement et lui délègue l'exécution 
	 * de la S-expression.
	 */
	private void run(SNode expr) {
		String receiverName = expr.get(0).contents();
		Reference receiver = environment.getReferenceByName(receiverName);
		
		if (receiver != null) {
			receiver.run(expr);
		} else {
			System.err.println("Erreur : Objet '" + receiverName + "' inconnu.");
		}
	}

	public static void main(String[] args) {
		new Exercice4_1_0();
	}
}