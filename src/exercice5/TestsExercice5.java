package exercice5;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestsExercice5 {
    private Exercice5 application;

    @BeforeEach
    void configuration() {
        // On initialise l'environnement complet (space, Rect, Oval, etc.)
        application = new Exercice5(); //
    }

    @Test
    void testNommageCompose() {
        // On simule le script : (space add robi (Rect new))
        application.oneShot("(space add robi (Rect new))"); //

        // 1. Vérifie que l'objet est bien enregistré avec son nom composé "space.robi"
        Reference refRobi = application.environment.getReferenceByName("space.robi"); //
        assertNotNull(refRobi, "L'objet devrait être enregistré sous le nom 'space.robi'");

        // 2. Vérifie que le nom interne de la référence a été mis à jour
        assertEquals("space.robi", refRobi.getName(), "Le nom interne de la référence doit être le chemin complet"); //
    }

    @Test
    void testHierarchieProfonde() {
        // Test sur 3 niveaux : space -> boite -> balle
        application.oneShot("(space add boite (Rect new))");
        application.oneShot("(space.boite add balle (Oval new))");

        // Vérifie que le chemin complet permet de retrouver la balle
        Reference refBalle = application.environment.getReferenceByName("space.boite.balle"); //
        assertNotNull(refBalle, "La balle devrait être accessible via 'space.boite.balle'");
    }

    @Test
    void testCapaciteParentalite() {
        // Vérifie qu'un objet créé via NewElement peut lui-même devenir un parent
        application.oneShot("(space add test (Rect new))");
        Reference refTest = application.environment.getReferenceByName("space.test");

        // On vérifie que la commande "add" a bien été ajoutée au nouvel objet
        assertNotNull(refTest.getCommandByName("add"), "Le nouvel objet doit posséder la commande 'add'"); //
    }
}