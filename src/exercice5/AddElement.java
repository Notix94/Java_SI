package exercice5;

import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

public class AddElement implements Command {
    private Environment env;

    public AddElement(Environment env) {
        this.env = env;
    }

    @Override
    public Reference run(Reference reference, SNode method) {
        // On récupère le nom local (ex: "robi")
        String childName = method.get(2).contents();
        
        // On fabrique l'objet (ex: le rectangle) via l'interpréteur
        Reference newElemRef = new Interpreter().compute(env, method.get(3));
        
        // --- L'EXERCICE 5 ---
        // On construit le nom complet : "nomDuParent.nomDuFils" (ex: "space.robi")
        String compositeName = reference.getName() + "." + childName;
        newElemRef.setName(compositeName);
        // ----------------------------------------------

        // On ajoute l'élément physiquement dans le conteneur (Space, Rect, etc.)
        GContainer container = (GContainer) reference.getReceiver();
        container.addElement((GElement) newElemRef.getReceiver());
        
        // On l'enregistre dans l'annuaire avec son nom composé ("space.robi")
        env.addReference(compositeName, newElemRef);
        
        return newElemRef;
    }
}