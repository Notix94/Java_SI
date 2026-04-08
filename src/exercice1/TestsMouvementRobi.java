package exercice1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import graphicLayer.GRect;
import graphicLayer.GSpace;

/**
 * Tests fonctionnels pour l'Exercice 1.
 *
 */
public class TestsMouvementRobi {

    // -------------------------------------------------------------------------
    // TEST 1 — Fenêtre de base avec robi en position (0,0)
    // Attendu : une fenêtre 200x150 avec un rectangle bleu en haut à gauche
    // -------------------------------------------------------------------------
    public static void test1_FenetreDeBase() {
        System.out.println("=== TEST 1 : Fenêtre de base ===");
        System.out.println("Attendu : rectangle bleu en position (0,0)");

        GSpace space = new GSpace("Test1 - Fenêtre de base", new Dimension(200, 150));
        GRect robi = new GRect();
        space.addElement(robi);
        space.open();

        System.out.println("Position robi : (" + robi.getX() + ", " + robi.getY() + ")");
        System.out.println("Taille robi   : " + robi.getWidth() + "x" + robi.getHeight());
    }

    // -------------------------------------------------------------------------
    // TEST 2 — Déplacement vers le bord droit uniquement
    // Attendu : robi glisse de gauche à droite puis s'arrête au bord droit
    // -------------------------------------------------------------------------
    public static void test2_DeplacementDroite() throws InterruptedException {
        System.out.println("=== TEST 2 : Déplacement vers la droite ===");
        System.out.println("Attendu : robi glisse vers le bord droit");

        GSpace space = new GSpace("Test2 - Déplacement droite", new Dimension(200, 150));
        GRect robi = new GRect();
        space.addElement(robi);
        space.open();

        new Thread(() -> {
            try {
                while (robi.getX() + robi.getWidth() < space.getWidth()) {
                    robi.translate(new Point(1, 0));
                    Thread.sleep(10);
                }
                System.out.println("Arrivé bord droit. X final = " + robi.getX());
                System.out.println("SUCCÈS si robi est collé au bord droit");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // -------------------------------------------------------------------------
    // TEST 3 — Tour complet le long des 4 bords
    // Attendu : robi fait le tour complet de la fenêtre dans le sens horaire
    // -------------------------------------------------------------------------
    public static void test3_TourComplet() throws InterruptedException {
        System.out.println("=== TEST 3 : Tour complet des 4 bords ===");
        System.out.println("Attendu : robi fait le tour → droite → bas → gauche → haut");

        GSpace space = new GSpace("Test3 - Tour complet", new Dimension(200, 150));
        GRect robi = new GRect();
        space.addElement(robi);
        space.open();

        new Thread(() -> {
            try {
                // Droite
                while (robi.getX() + robi.getWidth() < space.getWidth()) {
                    robi.translate(new Point(1, 0));
                    Thread.sleep(5);
                }
                System.out.println("Bord droit atteint ✓");

                // Bas
                while (robi.getY() + robi.getHeight() < space.getHeight()) {
                    robi.translate(new Point(0, 1));
                    Thread.sleep(5);
                }
                System.out.println("Bord bas atteint ✓");

                // Gauche
                while (robi.getX() > 0) {
                    robi.translate(new Point(-1, 0));
                    Thread.sleep(5);
                }
                System.out.println("Bord gauche atteint ✓");

                // Haut
                while (robi.getY() > 0) {
                    robi.translate(new Point(0, -1));
                    Thread.sleep(5);
                }
                System.out.println("Bord haut atteint ✓");

                // Couleur aléatoire
                Random rand = new Random();
                robi.setColor(new Color(
                    rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                System.out.println("Couleur aléatoire appliquée ✓");
                System.out.println("SUCCÈS : tour complet effectué");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // -------------------------------------------------------------------------
    // TEST 4 — Boucle infinie avec changement de couleur à chaque tour
    // Attendu : robi tourne indéfiniment, change de couleur après chaque tour
    // -------------------------------------------------------------------------
    public static void test4_BoucleInfinie() {
        System.out.println("=== TEST 4 : Boucle infinie ===");
        System.out.println("Attendu : robi tourne et change de couleur à chaque tour");
        System.out.println("Fermer la fenêtre pour arrêter.");

        GSpace space = new GSpace("Test4 - Boucle infinie", new Dimension(200, 150));
        GRect robi = new GRect();
        space.addElement(robi);
        space.open();

        new Thread(() -> {
            Random rand = new Random();
            int tourCount = 0;
            while (true) {
                try {
                    int maxX = space.getWidth()  - robi.getWidth();
                    int maxY = space.getHeight() - robi.getHeight();

                    while (robi.getX() < maxX)  { robi.translate(new Point(1,  0)); Thread.sleep(5); }
                    while (robi.getY() < maxY)  { robi.translate(new Point(0,  1)); Thread.sleep(5); }
                    while (robi.getX() > 0)     { robi.translate(new Point(-1, 0)); Thread.sleep(5); }
                    while (robi.getY() > 0)     { robi.translate(new Point(0, -1)); Thread.sleep(5); }

                    robi.setColor(new Color(
                        rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                    tourCount++;
                    System.out.println("Tour " + tourCount + " terminé, nouvelle couleur appliquée");

                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    // -------------------------------------------------------------------------
    // TEST 5 — Adaptation au redimensionnement
    // Attendu : redimensionner la fenêtre pendant l'animation,
    //           robi doit s'adapter et longer les nouveaux bords
    // -------------------------------------------------------------------------
    public static void test5_RedimensionnementFenetre() {
        System.out.println("=== TEST 5 : Adaptation au redimensionnement ===");
        System.out.println("Attendu : redimensionnez la fenêtre pendant l'animation");
        System.out.println("robi doit longer les nouveaux bords automatiquement");

        GSpace space = new GSpace("Test5 - Redimensionnement", new Dimension(200, 150));
        GRect robi = new GRect();
        space.addElement(robi);
        space.open();

        // Même code que test4 — space.getWidth() et getHeight() sont dynamiques
        new Thread(() -> {
            Random rand = new Random();
            while (true) {
                try {
                    // Les bornes sont recalculées à chaque itération
                    int maxX = space.getWidth()  - robi.getWidth();
                    int maxY = space.getHeight() - robi.getHeight();

                    if (robi.getX() < maxX && robi.getY() == 0)
                        robi.translate(new Point(1, 0));
                    else if (robi.getX() >= maxX && robi.getY() < maxY)
                        robi.translate(new Point(0, 1));
                    else if (robi.getX() > 0 && robi.getY() >= maxY)
                        robi.translate(new Point(-1, 0));
                    else if (robi.getX() == 0 && robi.getY() > 0)
                        robi.translate(new Point(0, -1));
                    else if (robi.getX() == 0 && robi.getY() == 0)
                        robi.setColor(new Color(
                            rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));

                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        // Changer le numéro du test pour lancer un scénario différent
    	test1_FenetreDeBase();
        test2_DeplacementDroite();
        test3_TourComplet();
        test5_RedimensionnementFenetre();
        test4_BoucleInfinie();
        
        
    }
}