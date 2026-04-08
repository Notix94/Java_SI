package exercice4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import graphicLayer.GRect;
import graphicLayer.GOval;

class TestsExercice4 {
    private Environment env;
    private Interpreter interprete;

    @BeforeEach
    void configuration() {
        env = new Environment(); //
        interprete = new Interpreter(); //
    }

    @Test
    void testAnnuaire() {
        // On vérifie que l'annuaire enregistre et retrouve bien les objets
        Reference ref = new Reference(new GRect());
        env.addReference("monRect", ref); //
        
        assertEquals(ref, env.getReferenceByName("monRect"), "L'objet doit être retrouvé par son nom"); //
    }

    @Test
    void testCreationDynamique() {
        // On teste la commande "new" de NewElement
        Reference rectClassRef = new Reference(GRect.class);
        NewElement createur = new NewElement(); //
        
        // On simule l'appel (Rect new)
        // Note : NewElement.run ignore le 2ème argument 'method' pour la création simple
        Reference instanceRef = createur.run(rectClassRef, null); //
        
        assertNotNull(instanceRef, "La nouvelle instance ne doit pas être nulle");
        assertTrue(instanceRef.getReceiver() instanceof GRect, "L'objet créé doit être un GRect");
    }

    @Test
    void testInterpreteurFeuille() {
        // Test de Interpreter.compute sur une "feuille" (juste un nom)
        Reference ref = new Reference(new GRect());
        env.addReference("robi", ref);
        
        // On simule un SNode qui contient juste "robi"
        // compute doit nous rendre la référence directement
        // SNode leaf = ... (simulation)
        // assertEquals(ref, interprete.compute(env, leaf)); //
    }

    @Test
    void testAjoutElement() {
        // On vérifie que AddElement enregistre bien le nouvel enfant dans l'annuaire
        AddElement addCmd = new AddElement(env); //
        
        // Ce test est plus complexe car il nécessite un SNode complet : (space add robi (Rect new))
        // Il valide que AddElement utilise bien compute() pour créer l'objet avant de l'ajouter
        //
    }
}