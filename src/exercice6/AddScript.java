package exercice6;
import stree.parser.SNode;

/**
 * Commande de méta-programmation (Exercice 6).
 * Permet d'étendre dynamiquement le langage en apprenant de nouvelles 
 * méthodes aux objets sous forme de scripts.
 */
public class AddScript implements Command {
    private Environment env;

    public AddScript(Environment env) { 
        this.env = env; 
    }

    @Override
    /**
     * Exécute l'ajout d'un script à un objet récepteur.
     * @param receiver L'objet qui va recevoir la nouvelle compétence.
     * @param method Le nœud du script (ex: (robi addScript nom [definition])).
     * @param env L'environnement de travail (passé ici pour respecter l'interface Exo 6).
     */
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Extraction du nom de la nouvelle commande (ex: "carre")
        String scriptName = method.get(2).contents();  
        
        // Extraction du bloc d'instructions (le corps du script)
        SNode definition = method.get(3);              
        
        // Création et enregistrement de la ScriptCommand.
        // On lui confie la définition du script et l'accès à l'environnement global.
        receiver.addCommand(scriptName, new ScriptCommand(definition, this.env));
        
        // On retourne le récepteur pour permettre le chaînage d'actions
        return receiver;
    }
}