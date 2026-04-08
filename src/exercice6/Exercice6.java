package exercice6;

import java.awt.Dimension;
import java.io.IOException;
import java.util.*;
import graphicLayer.*;
import stree.parser.*;
import tools.Tools;

/**
 * Point d'entrée de l'Exercice 6 
 * Cette classe configure l'environnement initial et lance la boucle 
 * d'interprétation capable de gérer des scripts dynamiques (méta-programmation).
 */
public class Exercice6 {
    Environment environment = new Environment();

    public Exercice6() {
        // Initialisation de la fenêtre de rendu graphique
        GSpace space = new GSpace("Exercice 6 - Scripts", new Dimension(400, 400));
        space.open();

        // --- CRÉATION DES RÉFÉRENCES RACINES ---
        Reference spaceRef = new Reference(space);
        Reference rectClassRef   = new Reference(GRect.class);
        Reference ovalClassRef   = new Reference(GOval.class);
        Reference imageClassRef  = new Reference(GImage.class);
        Reference stringClassRef = new Reference(GString.class);

        // --- INJECTION DES COMMANDES DANS 'SPACE' ---
        spaceRef.addCommand("setColor",   new SetColor());
        spaceRef.addCommand("sleep",      new Sleep());
        spaceRef.addCommand("add",        new AddElement(environment));
        spaceRef.addCommand("del",        new DelElement(environment));
        
        // NOUTÉAUTÉS EXERCICE 6 :
        // Permet de définir de nouveaux scripts et de nettoyer l'espace
        spaceRef.addCommand("addScript",  new AddScript(environment)); 
        spaceRef.addCommand("clear",      new ClearCommand()); 

        // --- INJECTION DES COMMANDES DANS LES CLASSES ---
        rectClassRef.addCommand("new",   new NewElement());
        ovalClassRef.addCommand("new",   new NewElement());
        imageClassRef.addCommand("new",  new NewImage());
        stringClassRef.addCommand("new", new NewString());

        // --- ENREGISTREMENT DANS L'ANNUAIRE GLOBAL ---
        environment.addReference("space", spaceRef);
        environment.addReference("Rect",  rectClassRef);
        environment.addReference("Oval",  ovalClassRef);
        environment.addReference("Image", imageClassRef);
        environment.addReference("Label", stringClassRef);

        // Lancement de l'écouteur de commandes
        this.mainLoop();
    }

    /**
     * Boucle de lecture interactive .
     * Lit les commandes au clavier, les parse en arbres S-Node, 
     * puis délègue l'exécution à l'interpréteur.
     */
    private void mainLoop() {
        System.out.println("Exercice 6 prêt !");
        while (true) {
            System.out.print("> ");
            String input = Tools.readKeyboard();
            
            if (input == null || input.trim().isEmpty()) continue;
            
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