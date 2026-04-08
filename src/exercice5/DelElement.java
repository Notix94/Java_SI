package exercice5;
import graphicLayer.GElement;
import graphicLayer.GContainer;
import stree.parser.SNode;

/**
 * Commande de suppression d'un élément au sein de la hiérarchie.
 * Identifie l'objet via son nom (ou chemin complet) dans l'environnement
 * pour le retirer physiquement de son conteneur parent.
 */
public class DelElement implements Command {
    private Environment env;
    public DelElement(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference receiver, SNode method) {
        // Résolution de l'élément à supprimer via l'annuaire global
        String name = method.get(2).contents();
        Reference refToDelete = env.getReferenceByName(name);
        
        if (refToDelete != null) {
            // Retrait de l'élément de la structure graphique du parent
            GContainer container = (GContainer) receiver.getReceiver();
            container.removeElement((GElement) refToDelete.getReceiver());
            
            // Note : L'élément reste dans l'Environment (env) mais est détaché du rendu
        }
        // Retourne le conteneur pour permettre la continuité de l'exécution
        return receiver;
    }
}