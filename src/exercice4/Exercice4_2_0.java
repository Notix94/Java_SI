package exercice4;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GImage;
import graphicLayer.GOval;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import graphicLayer.GString;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice4_2_0 {
	// Une seule variable d'instance : notre annuaire
	Environment environment = new Environment();

	public Exercice4_2_0() {
		// On agrandit un peu la fenêtre pour voir tout le monde
		GSpace space = new GSpace("Exercice 4.2 - Dynamique", new Dimension(400, 400));
		space.open();

		Reference spaceRef = new Reference(space);
		Reference rectClassRef = new Reference(GRect.class);
		Reference ovalClassRef = new Reference(GOval.class);
		Reference imageClassRef = new Reference(GImage.class);
		Reference stringClassRef = new Reference(GString.class);

		spaceRef.addCommand("setColor", new SetColor());
		spaceRef.addCommand("sleep", new Sleep());

		// FIX : On passe l'environnement pour que Add et Del puissent modifier l'annuaire
		spaceRef.addCommand("add", new AddElement(environment));
		spaceRef.addCommand("del", new DelElement(environment));
		
		rectClassRef.addCommand("new", new NewElement());
		ovalClassRef.addCommand("new", new NewElement());
		imageClassRef.addCommand("new", new NewImage());
		stringClassRef.addCommand("new", new NewString());

		// On enregistre les références avec les noms attendus par ton script
		environment.addReference("space", spaceRef);
		environment.addReference("Rect", rectClassRef);
		environment.addReference("Oval", ovalClassRef);
		environment.addReference("Image", imageClassRef);
		environment.addReference("Label", stringClassRef);
		
		this.mainLoop();
	}
	
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
					// L'interpréteur exécute chaque ligne
					new Interpreter().compute(environment, itor.next());
				}
			}
		}
	}

	public static void main(String[] args) {
		new Exercice4_2_0();
	}
}