package exercice1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestsMouvementRobi {
    private Exercice1_0 jeu;

    @BeforeEach
    void initialisation() {
        // On crée une instance du jeu
        jeu = new Exercice1_0();
    }

    @Test
    void testInitialisation() {
        // 1. On vérifie que la fenêtre (space) existe
        assertNotNull(jeu.space, "La fenêtre space doit être créée");
        
        // 2. On vérifie que Robi est bien dedans 
        // On utilise .contents() comme écrit dans GSpace.java
        assertTrue(jeu.space.contents().contains(jeu.robi), "Robi doit être présent dans la fenêtre");
    }

    @Test
    void testPositionDepart() {
        // Robi doit commencer en haut à gauche (0,0)
        assertEquals(0, jeu.robi.getX(), "X de départ doit être 0");
        assertEquals(0, jeu.robi.getY(), "Y de départ doit être 0");
    }

    @Test
    void testDimensionsRobi() {
        // On vérifie que robi a bien la taille par défaut 
        assertEquals(150, jeu.robi.getWidth(), "La largeur doit être de 200");
        assertEquals(200, jeu.robi.getHeight(), "La hauteur doit être de 150");
    }
}