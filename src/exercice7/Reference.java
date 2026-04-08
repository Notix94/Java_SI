package exercice7;

import java.util.HashMap;
import java.util.Map;
import stree.parser.SNode;

public class Reference {
    private Object receiver;
    private Map<String, Command> primitives;

    public Reference(Object receiver) {
        this.receiver = receiver;
        this.primitives = new HashMap<>();
    }
    
    public Reference(int value) {
        this.receiver = value;
        this.primitives = new HashMap<>();
    }
    
    public Reference(boolean value) {
        this.receiver = value;
        this.primitives = new HashMap<>();
    }
    
    public Reference(String value) {
        this.receiver = value;
        this.primitives = new HashMap<>();
    }

    public Object getReceiver() {
        return receiver;
    }
    
    public void setReceiver(Object receiver) {
        this.receiver = receiver;
    }

    public void addCommand(String selector, Command primitive) {
        primitives.put(selector, primitive); 
    }

    public Command getCommandByName(String selector) {
        return primitives.get(selector);
    }

    // Cette methode remplace la logique de tri manuelle 
    public Reference run(SNode method, Environment env) {
        String selector = method.get(1).contents(); // recupe le nom de la commande
        Command cmd = getCommandByName(selector);
        if (cmd == null) throw new Error("Commande inconnue : " + selector);
        return cmd.run(this, method, env);
    }
    
    public int asInt() {
        if (receiver instanceof Integer) {
            return (Integer) receiver;
        } else if (receiver instanceof String) {
            try {
                return Integer.parseInt((String) receiver);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot convert to int: " + receiver);
            }
        } else {
            throw new RuntimeException("Cannot convert to int: " + receiver);
        }
    }

    public boolean isTrue() {
        if (receiver instanceof Boolean) return (Boolean) receiver;
        if (receiver instanceof Integer) return ((Integer) receiver) != 0;
        if (receiver instanceof String) return !((String) receiver).isEmpty();
        return receiver != null;
    }
}