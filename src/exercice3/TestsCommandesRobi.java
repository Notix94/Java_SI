package exercice3;
 
import exercice3.Exercice3_0;
 
/**
 * Tests fonctionnels pour l'Exercice 3.
 *
 * Même fonctionnalité que Ex2 mais avec l'architecture Command.
 * Observer que le résultat visuel est IDENTIQUE à Ex2.
 */
public class TestsCommandesRobi {
 
    // -------------------------------------------------------------------------
    // TEST 1 — Script de configuration (même résultat visuel qu'Ex2)
    // Attendu : fond noir, robi jaune
    // -------------------------------------------------------------------------
    public static void test1_ConfigurationCouleurs() {
        System.out.println("=== TEST 1 Ex3 : Configuration couleurs ===");
        System.out.println("Attendu : fond NOIR, robi JAUNE (même résultat qu'Ex2)");
 
        new Exercice3_0(
            "(space setColor black) (robi setColor yellow)"
        );
        
    }
 
    // -------------------------------------------------------------------------
    // TEST 2 — Script d'animation complet du sujet
    // Attendu : robi rouge fait 4 déplacements avec pauses
    // -------------------------------------------------------------------------
    public static void test2_AnimationCompletesujet() {
        System.out.println("=== TEST 2 Ex3 : Script complet du sujet ===");
        System.out.println("Attendu : fond blanc → noir, robi jaune → rouge, 4 déplacements");
 
        new Exercice3_0(
            "(space setColor black) " +
            "(robi setColor yellow) " +
            "(space sleep 1000) " +
            "(space setColor white) " +
            "(space sleep 1000) " +
            "(robi setColor red) " +
            "(space sleep 1000) " +
            "(robi translate 100 0) " +
            "(space sleep 1000) " +
            "(robi translate 0 50) " +
            "(space sleep 1000) " +
            "(robi translate -100 0) " +
            "(space sleep 1000) " +
            "(robi translate 0 -40)"
        );
        
    }
 
    // -------------------------------------------------------------------------
    // TEST 3 — Vérification que chaque commande est bien une classe Command
    // Attendu : même résultat qu'Ex2, mais code refactorisé avec des classes
    // -------------------------------------------------------------------------
    public static void test3_ArchitectureCommand() {
        System.out.println("=== TEST 3 Ex3 : Architecture Command ===");
        System.out.println("Attendu : toutes les commandes via des classes dédiées");
        System.out.println("Résultat visuel : fond rouge, robi vert en (50, 30)");
 
        new Exercice3_0(
            "(space setColor red) " +
            "(robi setColor green) " +
            "(robi translate 50 30)"
        );
        
    }
 
    public static void main(String[] args) {
        test2_AnimationCompletesujet();
        test1_ConfigurationCouleurs();
        test3_ArchitectureCommand();
        
    }
}
 