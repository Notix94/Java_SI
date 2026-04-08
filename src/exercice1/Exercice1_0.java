package exercice1;

import java.awt.Dimension;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import java.awt.Point;

/**
 * Cette classe constitue l'introduction au projet. Elle a pour but de valider
 * la bonne intégration de la bibliothèque 'graphicLayer' et de mettre en œuvre
 * une première animation simple.
 * * Dépendances : GSpace (fenêtre), GRect (forme graphique).
 */
public class Exercice1_0 {
	// L'espace de dessin principal et l'élément mobile 'robi'
	GSpace space = new GSpace("Exercice 1", new Dimension(200, 150));
	GRect robi = new GRect();

	public Exercice1_0() {
		// Assemblage de la scène graphique
		space.addElement(robi);
		space.open();
		
		// Lancement d'un thread séparé pour gérer l'animation sans bloquer
		// l'interface graphique (Thread-Safe via la boucle de rendu)
		new Thread(() -> {
			while (true) {
				// Calcul dynamique des limites de déplacement pour rester dans le cadre
				int x = space.getWidth() - robi.getWidth();
				int y = space.getHeight() - robi.getHeight();
				
				/**
				 * ALGORITHME DE PATROUILLE :
				 * Robi suit les bords de la fenêtre dans le sens des aiguilles d'une montre.
				 * L'état du mouvement est déterminé par la position actuelle (X,Y)
				 * par rapport aux bornes calculées (x,y).
				 */
				
				// 1. Déplacement vers la droite sur le bord haut
				if(robi.getX() < x && robi.getY() == 0) {
					robi.translate(new Point(1,0));
				}
				// 2. Descente sur le bord droit
				else if (robi.getX() == x && robi.getY() < y) {
					robi.setPosition(new Point(x,robi.getY()));
					robi.translate(new Point(0,1));
				}
				// 3. Retour vers la gauche sur le bord bas
				else if (robi.getX() > 0 && robi.getY() == y) {
					robi.setPosition(new Point(robi.getX(),y));
					robi.translate(new Point(-1,0));
				}
				// 4. Remontée sur le bord gauche
				else if (robi.getX() == 0 && robi.getY() > 0) {
					robi.setPosition(new Point(0,robi.getY()));
					robi.translate(new Point(0,-1));
				}
				// Sécurité : recentrage si Robi sort des limites
				else if (robi.getX() > x && robi.getY() > y) {
					robi.setPosition(new Point(0,0));
				}
				
				// Temporisation pour fluidifier l'animation (environ 20 FPS)
				try { Thread.sleep(50); } catch (InterruptedException e) {}
			}
		}).start();
	}

	public static void main(String[] args) {
		new Exercice1_0();
	}
}