package exercice7;

import java.util.List;
import stree.parser.SNode;
import stree.parser.SParser;
import tools.Tools;

public class Exercice7 {
    Environment environment = new Environment();

    public Exercice7() {
        // Ajouter des fonctions de base
        
    	environment.addReference("+", new Reference(new AddCommand()));
    	environment.addReference("-", new Reference(new SubCommand()));
        environment.addReference("*", new Reference(new MulCommand()));
        environment.addReference("/", new Reference(new DivCommand()));
        
        environment.addReference(">", new Reference(new SupCommand()));
        environment.addReference("<", new Reference(new InfCommand()));
        environment.addReference(">=", new Reference(new StricSupCommand()));
        environment.addReference("<=", new Reference(new StricInfCommand()));
        environment.addReference("=", new Reference(new EgalCommand()));

        this.mainLoop();
    }

    private void mainLoop() {
        System.out.println("Exercice 7 prêt !");
        while (true) {
            System.out.print("> ");
            String input = Tools.readKeyboard();
            if (input == null || input.trim().isEmpty()) continue;
            SParser<SNode> parser = new SParser<>();
            List<SNode> compiled = null;
            try { compiled = parser.parse(input); }
            catch (Exception e) { e.printStackTrace(); }
            if (compiled != null)
                for (SNode node : compiled) {
                    Reference result = new Interpreter().compute(environment, node);
                    if ( result != null) System.out.println(result.getReceiver());
                }
        }
    }

    public static void main(String[] args) { new Exercice7(); }
}