package exercice5;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import graphicLayer.*;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice5 {
    public Environment environment = new Environment();

    public Exercice5() {
        GSpace space = new GSpace("Exercice 5 - Hierarchie", new Dimension(500, 500));
        space.open();

        // --- ETAPE 1 : Creer les references avec leurs noms ---
        Reference spaceRef = new Reference(space, "space");
        Reference rectClassRef = new Reference(GRect.class, null);
        Reference ovalClassRef = new Reference(GOval.class, null);
        Reference imageClassRef = new Reference(GImage.class, null);
        Reference stringClassRef = new Reference(GString.class, null);

        // --- ETAPE 2 : Ajouter les commandes ---
        spaceRef.addCommand("setColor", new SetColor());
        spaceRef.addCommand("add", new AddElement(environment));
        spaceRef.addCommand("del", new DelElement(environment));
        
        // On donne l'env a NewElement pour qu'il puisse creer des AddElement plus tard
        rectClassRef.addCommand("new", new NewElement(environment));
        ovalClassRef.addCommand("new", new NewElement(environment));
        imageClassRef.addCommand("new", new NewImage());
        stringClassRef.addCommand("new", new NewString());

        // --- ETAPE 3 : Enregistrer dans l'annuaire ---
        environment.addReference("space", spaceRef);
        environment.addReference("Rect", rectClassRef);
        environment.addReference("Oval", ovalClassRef);
        environment.addReference("Image", imageClassRef);
        environment.addReference("Label", stringClassRef);
    }

    // Cette methode permet a Example1 de fonctionner
    public void oneShot(String script) {
        SParser<SNode> parser = new SParser<>();
        try {
            List<SNode> compiled = parser.parse(script);
            if (compiled != null) {
                for (SNode node : compiled) {
                    new Interpreter().compute(environment, node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // La boucle pour taper des commandes au clavier
    public void mainLoop() {
        while (true) {
            System.out.print("> ");
            String input = Tools.readKeyboard();
            if (input != null && !input.trim().isEmpty()) {
                oneShot(input);
            }
        }
    }

    // Permet de lancer Exercice5 directement
    public static void main(String[] args) {
        new Exercice5().mainLoop();
    }
}