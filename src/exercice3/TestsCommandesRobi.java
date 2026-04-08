package exercice3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Point;

class TestsCommandesRobi {
    private Exercice3_0 moteur;

    @BeforeEach
    void configuration() {
        // On initialise l'exercice 3
        moteur = new Exercice3_0();
    }

    @Test
    void testCreationCommandeTranslation() {
        // On vérifie que RobiTranslate déplace bien l'objet
        int xInitial = moteur.robi.getX();
        int yInitial = moteur.robi.getY();
        
        // On crée manuellement une commande de translation de +10, +10
        RobiTranslate cmd = new RobiTranslate(moteur.robi, 10, 10);
        cmd.run();
        
        assertEquals(xInitial + 10, moteur.robi.getX(), "Robi aurait dû bouger de 10 en X");
        assertEquals(yInitial + 10, moteur.robi.getY(), "Robi aurait dû bouger de 10 en Y");
    }

    @Test
    void testLogiqueDuScriptFinal() {
        // Le script de l'exercice 3 finit par ces mouvements :
        // (100 0) -> (0 50) -> (-100 0) -> (0 -40)
        // Calcul : 
        // X = 0 + 100 - 100 = 0
        // Y = 0 + 50 - 40 = 10
        
        // Comme le script tourne dans le constructeur avec des "sleep", 
        // à la fin de l'initialisation du test, Robi doit être à sa position finale.
        
        assertEquals(0, moteur.robi.getX(), "X final devrait être 0");
        assertEquals(10, moteur.robi.getY(), "Y final devrait être 10");
    }

    @Test
    void testPresenceDesObjets() {
        // On s'assure que tout est bien branché
        assertNotNull(moteur.space, "L'espace doit être créé");
        assertNotNull(moteur.robi, "Robi doit être créé");
    }
}