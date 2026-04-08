package exercice5;

import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

/**
 * Commande cœur de l'exercice 5 : gère la hiérarchie des objets.
 * Permet d'imbriquer des éléments (ex: robi dans space) et de construire 
 * des chemins d'accès uniques pour chaque composant.
 * Dépendances : Environment, Interpreter.
 */
public class AddElement implements Command {
    private Environment env;

    public AddElement(Environment env) {
        this.env = env;
    }

    @Override
    public Reference run(Reference reference, SNode method) {
        // Extraction du nom local défini dans le script (ex: "robi")
        String childName = method.get(2).contents();
        
        // Instanciation récursive de l'élément via le moteur de l'interpréteur
        Reference newElemRef = new Interpreter().compute(env, method.get(3));
        
        // ALGORITHME DE NOMMAGE COMPOSÉ :
        // On construit le chemin absolu (ex: "space.robi") pour garantir 
        // l'unicité de l'objet dans l'annuaire global.
        String compositeName = reference.getName() + "." + childName;
        newElemRef.setName(compositeName);

        // Intégration dans la structure graphique parente
        GContainer container = (GContainer) reference.getReceiver();
        container.addElement((GElement) newElemRef.getReceiver());
        
        // Enregistrement du chemin complet dans l'environnement
        env.addReference(compositeName, newElemRef);
        
        return newElemRef;
    }
}