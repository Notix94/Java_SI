package exercice4;

import java.util.HashMap;
import java.util.Map;
import stree.parser.SNode;

/**
 * Wrapper universel pour les objets manipulés par le script (instances ou classes).
 * Gère le catalogue des commandes (primitives) spécifiques à chaque récepteur.
 */
public class Reference {
    private Object receiver;
    private Map<String, Command> primitives;

    public Reference(Object receiver) {
        this.receiver = receiver;
        this.primitives = new HashMap<String, Command>();
    }

    public Object getReceiver() {
        return receiver;
    }

    /**
     * Enregistre une nouvelle capacité (commande) dans le catalogue de l'objet.
     */
    public void addCommand(String selector, Command primitive) {
        primitives.put(selector, primitive);
    }

    public Command getCommandByName(String selector) {
        return primitives.get(selector);
    }

    /**
     * ALGORITHME DE DISPATCHING :
     * Identifie dynamiquement la commande demandée et lui délègue l'exécution.
     * Cette approche remplace les structures conditionnelles complexes (if/else).
     */
    public Reference run(SNode method) {
        String selector = method.get(1).contents(); 
        Command cmd = getCommandByName(selector);
        
        if (cmd == null) throw new Error("Commande inconnue : " + selector);
        
        // Délégation de l'exécution à la commande trouvée
        return cmd.run(this, method);
    }
}