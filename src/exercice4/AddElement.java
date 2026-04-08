package exercice4;
import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

/**
 * Commande pour ajouter un élément enfant à un conteneur (GSpace ou GRect).
 * Gère la liaison entre la scène graphique et l'annuaire global.
 * Dépendances : Environment, Interpreter.
 */
public class AddElement implements Command {
    private Environment env;

    public AddElement(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference reference, SNode method) {
        String name = method.get(2).contents();
        
        // Évaluation récursive pour créer l'instance de l'élément enfant
        Reference newElemRef = new Interpreter().compute(env, method.get(3));
        
        // Ajout physique dans le conteneur graphique
        GContainer container = (GContainer) reference.getReceiver();
        container.addElement((GElement) newElemRef.getReceiver());
        
        // Enregistrement de la nouvelle référence dans l'annuaire
        env.addReference(name, newElemRef);
        return newElemRef;
    }
}