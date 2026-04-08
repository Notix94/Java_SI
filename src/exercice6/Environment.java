package exercice6;

import java.util.HashMap;

public class Environment {
    private HashMap<String, Reference> variables;
    private Environment parent;

    public Environment() {
        variables = new HashMap<>(); 
        this.parent = null;
    }
    
 // Constructeur pour cr�er un environnement local
    public Environment(Environment parent) {
        variables = new HashMap<>();
        this.parent = parent;
    }

    public void addReference(String name, Reference ref) {
        variables.put(name, ref);
    }

    public Reference getReferenceByName(String name, Reference ref) {
        return variables.put(name, ref); 
    }
    
    public Reference getReferenceByName(String name) {
        Reference r = variables.get(name);
        // Si pas trouv� localement, chercher dans le parent
        if (r == null && parent != null) return parent.getReferenceByName(name);
        return r;
    }
}