package exercice4;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import graphicLayer.*;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/**
 * Extension dynamique de l'interpréteur (Exercice 4.2).
 * Cette classe permet de manipuler non seulement des instances, mais aussi des 
 * classes comme objets (méta-programmation) pour créer de nouveaux éléments 
 * graphiques via le script.
 */
public class Exercice4_2_0 {
	private Environment environment = new Environment();

	public Exercice4_2_0() {
		GSpace space = new GSpace("Exercice 4.2 - Dynamique", new Dimension(400, 400));
		space.open();

		// Création des références pour les objets et pour les Classes (usines)
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		// Configuration des commandes système
		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		
		// Les commandes "new" sont attachées aux références de classes
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		// Enregistrement dans l'annuaire global
		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
		
		this.mainLoop();
	}
	
	/**
	 * Boucle : délègue maintenant l'évaluation au moteur 'Interpreter'.
	 * Permet l'interprétation récursive des S-expressions complexes.
	 */
	private void mainLoop() {
		System.out.println("Interpréteur prêt !");
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
					// Utilisation du moteur récursif pour traiter chaque instruction
					new Interpreter().compute(environment, itor.next());
				}
			}
		}
	}

	public static void main(String[] args) {
		new Exercice4_2_0();
	}
}