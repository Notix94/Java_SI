package exercice5;

import graphicLayer.GString;
import stree.parser.SNode;

/**
 * Commande spécialisée pour la création de labels textuels (GString).
 * Dans la hiérarchie de l'exercice 5, le label est considéré comme un 
 * élément terminal (une feuille) qui ne peut pas contenir d'autres objets.
 */
public class NewString implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // Extraction de la chaîne de caractères (ex: "Hello world")
        String text = method.get(2).contents();
        GString label = new GString(text);
        
        // INITIALISATION DE LA RÉFÉRENCE
        // On passe 'null' car l'identité complète (le chemin) sera injectée 
        // par le conteneur parent lors de l'appel à 'add'.
        Reference newRef = new Reference(label, null);
        
        // Attribution des capacités de base
        newRef.addCommand("setColor", new SetColor());
        newRef.addCommand("translate", new Translate());
        
        // NOTE ARCHITECTURALE : 
        // On n'ajoute pas la commande "add" car un GString n'est pas un conteneur.
        
        return newRef;
    }
}