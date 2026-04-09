package exercice6;

import java.awt.Dimension;
import java.io.IOException;
import java.util.*;
import graphicLayer.*;
import stree.parser.*;
import tools.Tools;

/**
 * Point d'entrée de l'Exercice 6 Cette classe configure l'environnement initial
 * et lance la boucle d'interprétation capable de gérer des scripts dynamiques
 * (méta-programmation).
 */
public class Exercice6 {
	Environment environment = new Environment();

	public Exercice6() {
		setup();
		this.mainLoop(); // mode interactif normal
	}

	// Nouveau constructeur pour les tests
	public Exercice6(String scriptPerso) {
		setup();
		oneShot(scriptPerso);
	}

	// Extraire le setup dans une méthode séparée
	private void setup() {
		GSpace space = new GSpace("Exercice 6 - Scripts", new Dimension(400, 400));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		spaceRef.addCommand("addScript", new AddScript(environment));
		spaceRef.addCommand("clear", new ClearCommand());

		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
	}

	// Méthode pour les tests fonctionnels
	public void oneShot(String script) {
		SParser<SNode> parser = new SParser<>();
		try {
			List<SNode> compiled = parser.parse(script);
			if (compiled != null)
				for (SNode node : compiled)
					new Interpreter().compute(environment, node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Boucle de lecture interactive . Lit les commandes au clavier, les parse en
	 * arbres S-Node, puis délègue l'exécution à l'interpréteur.
	 */
	private void mainLoop() {
		System.out.println("Exercice 6 prêt !");
		while (true) {
			System.out.print("> ");
			String input = Tools.readKeyboard();

			if (input == null || input.trim().isEmpty())
				continue;

			SParser<SNode> parser = new SParser<>();
			List<SNode> compiled = null;

			try {
				// Transformation de la chaîne de texte en structure de données (S-Expressions)
				compiled = parser.parse(input);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (compiled != null) {
				for (SNode node : compiled) {
					// Exécution récursive de chaque instruction via l'interpréteur
					new Interpreter().compute(environment, node);
				}
			}
		}
	}

	public static void main(String[] args) {
		new Exercice6();
	}
}