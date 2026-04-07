package exercice7;

import java.util.HashMap;

public class Environment {
    private HashMap<String, Reference> variables;
    private Environment parent;

    public Environment() {
        variables = new HashMap<>(); // [cite: 272]
        this.parent = null;
    }
    
 // Constructeur pour créer un environnement local
    public Environment(Environment parent) {
        variables = new HashMap<>();
        this.parent = parent;
    }

    public void addReference(String name, Reference ref) {
        variables.put(name, ref); // [cite: 192]
    }

    public Reference getReferenceByName(String name, Reference ref) {
        return variables.put(name, ref); // [cite: 193]
    }
    
    public Reference getReferenceByName(String name) {
        Reference r = variables.get(name);
        // Si pas trouvé localement, chercher dans le parent
        if (r == null && parent != null) return parent.getReferenceByName(name);
        return r;
    }
    
    public void setReference(String name, Reference ref) {
        if (variables.containsKey(name)) {
            variables.put(name, ref); // met à jour la variable existante
        } else if (parent != null && parent.getReferenceByName(name) != null) {
            parent.setReference(name, ref); // met à jour dans le parent
        } else {
            variables.put(name, ref); // crée une nouvelle variable locale
        }
    }
}