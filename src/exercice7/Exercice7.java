package exercice7;

import java.util.List;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

/**
 * Point d'entrée de l'Exercice 7 - Extension logique et arithmétique.
 * Dans cette étape, le langage s'enrichit d'opérateurs mathématiques et 
 * de comparateurs logiques, transformant l'interpréteur en moteur de calcul.
 */
public class Exercice7 {
    Environment environment = new Environment();

    public Exercice7() {
        // --- BOOTSTRAP DES OPÉRATEURS ---
        
        // Opérations Arithmétiques de base
        environment.addReference("+", new Reference(new AddCommand()));
        environment.addReference("-", new Reference(new SubCommand()));
        environment.addReference("*", new Reference(new MulCommand()));
        environment.addReference("/", new Reference(new DivCommand()));
        
        // Opérateurs de comparaison (Logique)
        environment.addReference(">", new Reference(new StricSupCommand()));
        environment.addReference("<", new Reference(new StricInfCommand()));
        environment.addReference(">=", new Reference(new SupCommand()));
        environment.addReference("<=", new Reference(new InfCommand()));
        environment.addReference("=", new Reference(new EgalCommand()));

        // Lancement du REPL (Read-Eval-Print Loop)
        this.mainLoop();
    }

    /**
     * Boucle de lecture interactive.
     * Nouveauté de l'Exo 7 : L'interpréteur affiche désormais le RÉSULTAT 
     * de l'évaluation, ce qui permet de l'utiliser comme une console interactive.
     */
    private void mainLoop() {
        System.out.println("Exercice 7 prêt !");
        while (true) {
            System.out.print("> ");
            String input = Tools.readKeyboard();
            
            if (input == null || input.trim().isEmpty()) continue;
            
            SParser<SNode> parser = new SParser<>();
            List<SNode> compiled = null;
            
            try { 
                compiled = parser.parse(input); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
            
            if (compiled != null) {
                for (SNode node : compiled) {
                    // Évaluation de l'expression (ex: (+ 2 3))
                    Reference result = new Interpreter().compute(environment, node);
                    
                    // AFFICHAGE DU RÉSULTAT : 
                    // Si la commande retourne une valeur, on l'affiche directement.
                    if (result != null) {
                        System.out.println(result.getReceiver());
                    }
                }
            }
        }
    }

    public static void main(String[] args) { 
        new Exercice7(); 
    }
}