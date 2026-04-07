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

public class Exercice4_1_0 {
	// Une seule variable d'instance
	Environment environment = new Environment();

	public Exercice4_1_0() {
		// space et robi sont temporaires ici
		GSpace space = new GSpace("Exercice 4.1 - Test", new Dimension(200, 100));
		GRect robi = new GRect();

		space.addElement(robi);
		space.open();

		Reference spaceRef = new Reference(space);
		Reference robiRef = new Reference(robi);

		// Initialisation des references avec les nouvelles classes universelles
		
		// Commandes pour le space
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());

		// Commandes pour robi 
		robiRef.addCommand("setColor", new SetColor());
		robiRef.addCommand("translate", new Translate());

		// Enregistrement des references dans l'environement par leur nom
		environment.addReference("space", spaceRef);
		environment.addReference("robi", robiRef);

		this.mainLoop();
	}

	private void mainLoop() {
		while (true) {
			// prompt
			System.out.print("> ");
			// lecture d'une serie de s-expressions au clavier (return = fin de la serie)
			String input = Tools.readKeyboard();
			
			// Si l'utilisateur appuie juste sur Entrée sans rien taper, on évite l'erreur
			if (input == null || input.trim().isEmpty()) continue;

			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			try {
				compiled = parser.parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (compiled != null) {
				// execution des s-expressions compilees
				Iterator<SNode> itor = compiled.iterator();
				while (itor.hasNext()) {
					this.run((SNode) itor.next());
				}
			}
		}
	}

	private void run(SNode expr) {
		// Quel est le nom du receiver
		String receiverName = expr.get(0).contents();
		
		// Quel est le receiver
		Reference receiver = environment.getReferenceByName(receiverName);
		
		if (receiver != null) {
			// Demande au receiver d'executer la s-expression compilee
			receiver.run(expr);
		} else {
			System.err.println("Erreur : Objet '" + receiverName + "' inconnu dans l'environnement.");
		}
	}

	public static void main(String[] args) {
		new Exercice4_1_0();
	}
}