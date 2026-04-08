package exercice5;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import graphicLayer.*;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/**
 * Point d'entrée principal de l'Exercice 5.
 * Cette classe orchestre la création de l'environnement, l'enregistrement des 
 * primitives et la gestion des deux modes d'exécution : script unique ou interactif.
 */
public class Exercice5 {
    public Environment environment = new Environment();

    public Exercice5() {
        // Initialisation de l'espace graphique de rendu
        GSpace space = new GSpace("Exercice 5 - Hierarchie", new Dimension(500, 500));
        space.open();

        // CONFIGURATION DE LA RÉFÉRENCE RACINE
        // 'space' est le point d'entrée de la hiérarchie graphique.
        Reference spaceRef = new Reference(space, "space");
        
        // RÉFÉRENCES DE CLASSES 
        // Elles permettent d'instancier dynamiquement de nouveaux objets via 'new'.
        Reference rectClassRef = new Reference(GRect.class, null);
        Reference ovalClassRef = new Reference(GOval.class, null);
        Reference imageClassRef = new Reference(GImage.class, null);
        Reference stringClassRef = new Reference(GString.class, null);

        // INJECTION DES COMMANDES SYSTÈME
        spaceRef.addCommand("setColor", new SetColor());
        spaceRef.addCommand("add", new AddElement(environment));
        spaceRef.addCommand("del", new DelElement(environment));
        
        // On injecte l'environnement dans les usines pour supporter le nommage hiérarchique
        rectClassRef.addCommand("new", new NewElement(environment));
        ovalClassRef.addCommand("new", new NewElement(environment));
        imageClassRef.addCommand("new", new NewImage());
        stringClassRef.addCommand("new", new NewString());

        // ENREGISTREMENT DANS L'ANNUAIRE GLOBAL
        environment.addReference("space", spaceRef);
        environment.addReference("Rect", rectClassRef);
        environment.addReference("Oval", ovalClassRef);
        environment.addReference("Image", imageClassRef);
        environment.addReference("Label", stringClassRef);
    }

    /**
     * Mode d'exécution "One-Shot" : Parse et exécute un script complet.
     * Utilisé notamment par les classes d'exemples (Example1, Example2).
     */
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

    /**
     *Boucle interactive : Permet de piloter l'environnement 
     * en temps réel via la console.
     */
    public void mainLoop() {
        while (true) {
            System.out.print("> ");
            String input = Tools.readKeyboard();
            if (input != null && !input.trim().isEmpty()) {
                oneShot(input);
            }
        }
    }

    public static void main(String[] args) {
        new Exercice5().mainLoop();
    }
}