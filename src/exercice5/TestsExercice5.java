package exercice5;
 


/**
 * Tests fonctionnels pour l'Exercice 5.
 *
 * Teste la notation pointée (space.robi) pour accéder
 * aux éléments dans des conteneurs imbriqués.
 */
public class TestsExercice5 {
 
    // -------------------------------------------------------------------------
    // TEST 1 — Script de base du sujet avec notation pointée
    // Attendu : un rectangle blanc avec une image alien dedans
    // -------------------------------------------------------------------------
    public static void test1_NotationPointeeBasique() {
        System.out.println("=== TEST 1 Ex5 : Notation pointée basique ===");
        System.out.println("Attendu : rectangle blanc 100x100 contenant l'image alien");
 
        Exercice5 exo = new Exercice5();
        exo.oneShot(
            "(space setDim 200 150) " +
            "(space add robi (Rect new)) " +
            "(space.robi setColor white) " +
            "(space.robi setDim 100 100) " +
            "(space.robi translate 20 10) " +
            "(space.robi add im (Image new alien.gif)) " +
            "(space.robi.im translate 20 20)"
        );
    }
 
    // -------------------------------------------------------------------------
    // TEST 2 — Deux rectangles imbriqués (script du sujet)
    // Attendu : grand rectangle blanc contenant un petit rectangle rouge
    // -------------------------------------------------------------------------
    public static void test2_DeuxRectanglesImbriques() {
        System.out.println("=== TEST 2 Ex5 : Deux rectangles imbriqués ===");
        System.out.println("Attendu : rectangle blanc 50x50 avec rectangle rouge dedans");
 
        Exercice5 exo = new Exercice5();
        exo.oneShot(
            "(space add robi (Rect new)) " +
            "(space.robi setDim 80 80) " +
            "(space.robi add robi (Rect new)) " +
            "(space.robi.robi setColor red) " +
            "(space.robi setColor white)"
        );
    }
 
    // -------------------------------------------------------------------------
    // TEST 3 — Suppression d'un élément imbriqué
    // Attendu : rectangle apparaît, son contenu disparaît avec lui
    // -------------------------------------------------------------------------
    public static void test3_SuppressionImbriquee() {
        System.out.println("=== TEST 3 Ex5 : Suppression imbriquée ===");
        System.out.println("Attendu : rectangle blanc avec rouge dedans, puis tout disparaît");
 
        Exercice5 exo = new Exercice5();
        exo.oneShot(
            "(space add robi (Rect new)) " +
            "(space.robi setDim 80 80) " +
            "(space.robi setColor white) " +
            "(space.robi add enfant (Rect new)) " +
            "(space.robi.enfant setColor red) " +
            "(space sleep 2000) " +
            "(space del robi)"
        );
    }
 
    // -------------------------------------------------------------------------
    // TEST 4 — Manipulation via la console (mode interactif)
    // -------------------------------------------------------------------------
    public static void test4_ModeInteractif() {
        System.out.println("=== TEST 4 Ex5 : Mode interactif ===");
        System.out.println("Taper dans la console :");
        System.out.println("  > (space add robi (Rect new))");
        System.out.println("  > (space.robi setColor white)");
        System.out.println("  > (space.robi add balle (Oval new))");
        System.out.println("  > (space.robi.balle setColor green)");
        System.out.println("  > (space.robi.balle translate 5 5)");
 
        new Exercice5().mainLoop();
    }
 
    public static void main(String[] args) {
       // test2_DeuxRectanglesImbriques();
        //test1_NotationPointeeBasique();
        //test3_SuppressionImbriquee();
        test4_ModeInteractif();
    }
}
 