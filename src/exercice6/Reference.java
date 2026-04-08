package exercice6;

import java.util.HashMap;
import java.util.Map;
import stree.parser.SNode;

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

    public void addCommand(String selector, Command primitive) {
        primitives.put(selector, primitive); 
    }

    public Command getCommandByName(String selector) {
        return primitives.get(selector); 
    }

    // Cette méthode remplace la logique de tri manuelle 
    public Reference run(SNode method, Environment env) {
        String selector = method.get(1).contents(); // Récupère le nom de la commande
        Command cmd = getCommandByName(selector);
        if (cmd == null) throw new Error("Commande inconnue : " + selector);
        return cmd.run(this, method, env);
    }
}