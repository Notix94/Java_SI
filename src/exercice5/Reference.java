package exercice5;

import java.util.HashMap;
import java.util.Map;
import stree.parser.SNode;

/**
 * Wrapper universel supportant la hiérarchie (Exercice 5).
 * Cette classe fait le pont entre le script et les objets Java en gérant 
 * à la fois l'identité (chemin absolu) et les capacités (primitives).
 */
public class Reference {
    private Object receiver;
    private Map<String, Command> primitives;
    private String name; 

    /**
     * @param receiver L'entité réelle (GElement ou Class).
     * @param name Le chemin unique dans l'environnement (ex: "space.robi.bras").
     */
    public Reference(Object receiver, String name) {
        this.receiver = receiver;
        this.name = name;
        this.primitives = new HashMap<String, Command>();
    }

    public Object getReceiver() {
        return receiver;
    }

    /**
     * Retourne le chemin complet de la référence.
     * Essentiel pour la construction récursive des noms dans AddElement.
     */
    public String getName() {
        return name;
    }

    /**
     * Met à jour l'identité de l'objet dans la hiérarchie.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Enregistre une nouvelle commande dans le catalogue de l'objet.
     */
    public void addCommand(String selector, Command primitive) {
        primitives.put(selector, primitive);
    }

    public Command getCommandByName(String selector) {
        return primitives.get(selector);
    }

    /**
     * Extrait le sélecteur de la S-Expression et délègue l'action à la commande 
     * correspondante. Une erreur explicite est levée si la capacité est absente.
     */
    public Reference run(SNode method) {
        // Dans une S-expression de type (objet commande args...), 
        // l'index 1 correspond toujours au sélecteur de la méthode.
        String selector = method.get(1).contents(); 
        Command cmd = getCommandByName(selector);
        
        if (cmd == null) {
            // Rapport d'erreur enrichi pour faciliter le débogage du script
            throw new Error("L'objet '" + name + "' ne comprend pas la commande : " + selector);
        }
        return cmd.run(this, method);
    }
}