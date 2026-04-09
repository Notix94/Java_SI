package exercice7;

import java.awt.Dimension;
import java.util.List;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/**
 * Point d'entrée de l'Exercice 7 - Extension logique et arithmétique. Dans
 * cette étape, le langage s'enrichit d'opérateurs mathématiques et de
 * comparateurs logiques, transformant l'interpréteur en moteur de calcul.
 */
public class Exercice7 {
	Environment environment = new Environment();

	public Exercice7() {
		setup();
		this.mainLoop();
	}

	// Constructeur pour les tests — ne lance PAS mainLoop
	public Exercice7(String scriptPerso) {
		setup();
		oneShot(scriptPerso);
	}

	private void setup() {
		// Fenêtre graphique
		GSpace space = new GSpace("Exercice 7", new Dimension(400, 400));
		space.open();

		// Références graphiques
		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		spaceRef.addCommand("setDim", new SetDim());
		spaceRef.addCommand("addScript", new AddScript(environment));

		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);

		// Opérateurs arithmétiques
		environment.addReference("+", new Reference(new AddCommand()));
		environment.addReference("-", new Reference(new SubCommand()));
		environment.addReference("*", new Reference(new MulCommand()));
		environment.addReference("/", new Reference(new DivCommand()));

		// Opérateurs de comparaison
		environment.addReference(">", new Reference(new StricSupCommand()));
		environment.addReference("<", new Reference(new StricInfCommand()));
		environment.addReference(">=", new Reference(new SupCommand()));
		environment.addReference("<=", new Reference(new InfCommand()));
		environment.addReference("=", new Reference(new EgalCommand()));
	}

	public void oneShot(String script) {
		SParser<SNode> parser = new SParser<>();
		try {
			List<SNode> compiled = parser.parse(script);
			if (compiled != null)
				for (SNode node : compiled)
					new Interpreter().compute(environment, node);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Boucle de lecture interactive.
	 */
	public void mainLoop() {
		System.out.println("Exercice 7 prêt !");
		while (true) {
			System.out.print("> ");
			String input = Tools.readKeyboard();
			if (input == null || input.trim().isEmpty())
				continue;
			oneShot(input);
		}
	}

	public static void main(String[] args) {
		new Exercice7();
	}
}