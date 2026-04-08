package exercice4;
import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

/**
 * Commande permettant de supprimer un élément d'un conteneur graphique.
 * Fait le lien entre l'identifiant textuel et l'instance réelle à retirer.
 * Dépendances : Environment.
 */
public class DelElement implements Command {
    private Environment env;
    
    public DelElement(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference receiver, SNode method) {
        // Extraction du nom de l'objet à supprimer
        String name = method.get(2).contents();
        Reference refToDelete = env.getReferenceByName(name);
        
        if (refToDelete != null) {
            // Suppression physique dans le conteneur graphique (GSpace ou GRect)
            GContainer container = (GContainer) receiver.getReceiver();
            container.removeElement((GElement) refToDelete.getReceiver());
        }
        // Retourne le conteneur pour permettre l'enchaînement
        return receiver;
    }
}