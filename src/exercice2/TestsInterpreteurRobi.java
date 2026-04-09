package exercice2;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;
import exercice2.*;

/**
 * Tests fonctionnels pour l'Exercice 2.
 *
 * Comment utiliser :
 *   Lancer chaque test depuis main(). Observer la fenêtre et la console.
 *   Chaque test affiche ce qui est attendu avant de s'exécuter.
 */
public class TestsInterpreteurRobi {

    // -------------------------------------------------------------------------
    // TEST 1 — Script de configuration des couleurs (Ex 2.1)
    // Attendu : fond noir, robi jaune
    // -------------------------------------------------------------------------
    public static void test1_ConfigurationCouleurs() {
        System.out.println("=== TEST 1 : Configuration couleurs ===");
        System.out.println("Attendu : fond de fenêtre NOIR, robi JAUNE");

        Exercice2_1_0 exo = new Exercice2_1_0("(space setColor black) (robi setColor yellow)");
        exo.run();
    }

    // -------------------------------------------------------------------------
    // TEST 2 — Script de configuration inverse
    // Attendu : fond blanc, robi bleu
    // -------------------------------------------------------------------------
    public static void test2_ConfigurationInverse() {
        System.out.println("=== TEST 2 : Configuration inverse ===");
        System.out.println("Attendu : fond BLANC, robi BLEU (couleur par défaut)");

        Exercice2_1_0 exo = new Exercice2_1_0(
            "(space setColor white) (robi setColor blue)"
        );
        exo.run();
    }

    // -------------------------------------------------------------------------
    // TEST 3 — Script d'animation avec translate (Ex 2.2)
    // Attendu : robi rouge se déplace en carré avec pauses visibles
    // -------------------------------------------------------------------------
    public static void test3_AnimationTranslate() {
        System.out.println("=== TEST 3 : Animation translate ===");
        System.out.println("Attendu : robi rouge se déplace droite→bas→gauche→haut");

        Exercice2_1_0 exo = new Exercice2_1_0(
            "(space setColor white) " +
            "(robi setColor red) " +
            "(robi translate 10 0) (space sleep 300) " +
            "(robi translate 0 10) (space sleep 300) " +
            "(robi translate -10 0) (space sleep 300) " +
            "(robi translate 0 -10)"
        );
        exo.run();
    }

    // -------------------------------------------------------------------------
    // TEST 4 — Script d'animation complet du sujet
    // Attendu : exactement le script de l'énoncé exercice 2.2
    // -------------------------------------------------------------------------
    public static void test4_ScriptSujet() {
        System.out.println("=== TEST 4 : Script exact du sujet ===");
        System.out.println("Attendu : fond blanc, robi rouge, 4 déplacements avec pauses");

        Exercice2_1_0 exo = new Exercice2_1_0(
            "(space setColor white) " +
            "(robi setColor red) " +
            "(robi translate 10 0) (space sleep 100) " +
            "(robi translate 0 10) (space sleep 100) " +
            "(robi translate -10 0) (space sleep 100) " +
            "(robi translate 0 -10)"
        );
        exo.run();
    }

    // -------------------------------------------------------------------------
    // TEST 5 — Script avec grande distance de déplacement
    // Attendu : robi rouge part de (0,0) et finit en bas à droite
    // -------------------------------------------------------------------------
    public static void test5_GrandDeplacement() {
        System.out.println("=== TEST 5 : Grand déplacement ===");
        System.out.println("Attendu : robi se déplace loin vers (150, 60)");

        Exercice2_1_0 exo = new Exercice2_1_0(
            "(space setColor black) " +
            "(robi setColor yellow) " +
            "(robi translate 150 0) (space sleep 500) " +
            "(robi translate 0 60)"
        );
        exo.run();
    }

    // -------------------------------------------------------------------------
    // TEST 6 — Commande inconnue : doit afficher une erreur sans planter
    // Attendu : message d'erreur dans la console, la fenêtre reste ouverte
    // -------------------------------------------------------------------------
    public static void test6_CommandeInconnue() {
        System.out.println("=== TEST 6 : Commande inconnue ===");
        System.out.println("Attendu : erreur console, fenêtre reste ouverte");

        try {
            Exercice2_1_0 exo = new Exercice2_1_0(
                "(robi commandeInexistante 10)"
            );
            exo.run();
        } catch (Exception e) {
            System.out.println("Exception attrapée (normal) : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Changer le numéro pour tester un scénario différent
        test3_AnimationTranslate();
        //test1_ConfigurationCouleurs();
        //test2_ConfigurationInverse();
       // test4_ScriptSujet();
       //test5_GrandDeplacement();
       // test6_CommandeInconnue();
    }
}