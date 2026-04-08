package exercice2;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TestsInterpreteur {

    @Test
    void testLogiqueDuScript() {
        Exercice2_1_0 moteur = new Exercice2_1_0();
        
        // Le script prend environ 2 secondes à cause des "sleep"
        // Une fois terminé, Robi doit être revenu à 0,0
        assertEquals(0, moteur.robi.getX(), "Après son tour, Robi doit être revenu à X=0");
        assertEquals(0, moteur.robi.getY(), "Après son tour, Robi doit être revenu à Y=0");
    }
}