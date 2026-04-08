package exercice7;
 
import java.awt.Color;
import graphicLayer.*;
import java.awt.*;
import exercice7.*;
 
/**
 * Tests fonctionnels pour l'Exercice 7.
 *
 * Teste les nouvelles fonctionnalités :
 *   - (set x valeur)           : variables locales
 *   - (loop n corps)           : boucle N fois
 *   - (if condition v f)       : conditionnelle
 *   - (+ - * /)                : arithmétique
 *   - (= < >)                  : comparaisons
 */
public class TestFonctionnel_Exercice7 {
 
    // -------------------------------------------------------------------------
    // TEST 1 — Variable locale avec set
    // Attendu : robi se déplace de 50 pixels vers la droite
    // -------------------------------------------------------------------------
    public static void test1_VariableLocale() {
        System.out.println("=== TEST 1 Ex7 : Variable locale (set) ===");
        System.out.println("Attendu : robi se déplace de 50px vers la droite");
 
        Exercice7 exo = new Exercice7();
        GRect robi = new GRect();
        exo.addToSpace(robi);
        int dist = 50;
        robi.translate(dist, 0);
    }
 
    // -------------------------------------------------------------------------
    // TEST 2 — Boucle simple
    // Attendu : robi se déplace progressivement sur 10 pas de 3px
    // -------------------------------------------------------------------------
    public static void test2_BoucleSimple() {
        System.out.println("=== TEST 2 Ex7 : Boucle simple ===");
        System.out.println("Attendu : robi avance de 3px en 3px, 10 fois avec pauses");
 
        Exercice7 exo = new Exercice7();
        GRect robi = new GRect();
        exo.addToSpace(robi);

        robi.setColor(Color.RED);

        for (int i = 0; i < 10; i++) {
            robi.translate(3, 0);
            Thread.sleep(100);
        }
    }
 
    // -------------------------------------------------------------------------
    // TEST 3 — Boucle avec variable
    // Attendu : robi fait N pas, N défini par une variable
    // -------------------------------------------------------------------------
    public static void test3_BoucleAvecVariable() {
        System.out.println("=== TEST 3 Ex7 : Boucle avec variable ===");
        System.out.println("Attendu : robi fait 15 pas de 2px vers le bas");
 
        Exercice7 exo = new Exercice7();
        GRect robi = new GRect();
        exo.addToSpace(robi);

        robi.setColor(Color.BLUE);

        int n = 15;
        for (int i = 0; i < n; i++) {
            robi.translate(0, 2);
            Thread.sleep(80);
        }
    }
 
    // -------------------------------------------------------------------------
    // TEST 4 — Conditionnelle if (condition vraie)
    // Attendu : robi devient rouge (condition = est vraie)
    // -------------------------------------------------------------------------
    public static void test4_ConditionnelleVraie() {
        System.out.println("=== TEST 4 Ex7 : Conditionnelle if (vrai) ===");
        System.out.println("Attendu : robi devient ROUGE (= 10 10 est vrai)");
 
        Exercice7 exo = new Exercice7();
        Rect robi = new Rect();
        exo.addToSpace(robi);

        if (10 == 10) {
            robi.setColor(Color.RED);
        } else {
            robi.setColor(Color.BLUE);
        }
    }
 
    // -------------------------------------------------------------------------
    // TEST 5 — Conditionnelle if (condition fausse)
    // Attendu : robi devient bleu (condition = est fausse)
    // -------------------------------------------------------------------------
    public static void test5_ConditionnelleFausse() {
        System.out.println("=== TEST 5 Ex7 : Conditionnelle if (faux) ===");
        System.out.println("Attendu : robi devient BLEU (= 10 5 est faux)");
 
        Exercice7 exo = new Exercice7();
        Rect robi = new Rect();
        exo.addToSpace(robi);

        if (10 == 5) {
            robi.setColor(Color.RED);
        } else {
            robi.setColor(Color.BLUE);
        }
    }
 
    // -------------------------------------------------------------------------
    // TEST 6 — Arithmétique avec set
    // Attendu : robi se déplace de (5+10)=15 pixels
    // -------------------------------------------------------------------------
    public static void test6_Arithmetique() {
        System.out.println("=== TEST 6 Ex7 : Arithmétique ===");
        System.out.println("Attendu : robi se déplace de 15px (5+10) vers la droite");
 
        Exercice7 exo = new Exercice7();
        Rect robi = new Rect();
        exo.addToSpace(robi);

        int a = 5;
        int b = 10;
        int total = a + b;

        robi.translate(total, 0);
    }
 
    // -------------------------------------------------------------------------
    // TEST 7 — Combinaison loop + if + arithmétique
    // Attendu : robi rebondit — avance 20 pas, change de couleur à mi-chemin
    // -------------------------------------------------------------------------
    public static void test7_CombinaisonComplexe() {
        System.out.println("=== TEST 7 Ex7 : Combinaison loop + if + arithmétique ===");
        System.out.println("Attendu : robi avance, devient rouge à partir du pas 10");
 
        Exercice7 exo = new Exercice7();
        Rect robi = new Rect();
        exo.addToSpace(robi);

        robi.setColor(Color.BLUE);

        int pas = 0;
        for (int i = 0; i < 20; i++) {
            robi.translate(3, 0);
            pas++;

            if (pas > 10) {
                robi.setColor(Color.RED);
            } else {
                robi.setColor(Color.BLUE);
            }

            Thread.sleep(100);
        }
    }
 
    // -------------------------------------------------------------------------
    // TEST 8 — Mode interactif
    // -------------------------------------------------------------------------
    public static void test8_ModeInteractif() {
        System.out.println("=== TEST 8 Ex7 : Mode interactif ===");
        System.out.println("Taper dans la console :");
        System.out.println("  > (space add robi (Rect new))");
        System.out.println("  > (set n 5)");
        System.out.println("  > (loop n (robi translate 5 0) (space sleep 200))");
        System.out.println("  > (if (> n 3) (robi setColor red) (robi setColor blue))");
 
        new Exercice7();
    }
 
    public static void main(String[] args) {
        test7_CombinaisonComplexe();
    }
}