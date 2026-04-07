package exercice5; // Assure-toi que le package est bien exercice5

import java.util.HashMap;
import java.util.Map;
import stree.parser.SNode;

public class Reference {
    private Object receiver;
    private Map<String, Command> primitives;
    private String name; // <--- AJOUT : Pour stocker le nom (ex: "space.robi")

    // MODIF : Le constructeur prend maintenant le nom en paramètre
    public Reference(Object receiver, String name) {
        this.receiver = receiver;
        this.name = name;
        this.primitives = new HashMap<String, Command>();
    }

    public Object getReceiver() {
        return receiver;
    }

    // AJOUT : Getter pour récupérer le nom composé
    public String getName() {
        return name;
    }

    // AJOUT : Setter pour mettre à jour le nom si besoin (utile dans AddElement)
    public void setName(String name) {
        this.name = name;
    }

    public void addCommand(String selector, Command primitive) {
        primitives.put(selector, primitive);
    }

    public Command getCommandByName(String selector) {
        return primitives.get(selector);
    }

    public Reference run(SNode method) {
        // method.get(0) est le nom de l'objet (ex: space.robi)
        // method.get(1) est le nom de la commande (ex: setColor)
        String selector = method.get(1).contents(); 
        Command cmd = getCommandByName(selector);
        
        if (cmd == null) {
            throw new Error("Objet '" + name + "' ne comprend pas la commande : " + selector);
        }
        return cmd.run(this, method);
    }
}