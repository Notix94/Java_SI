package exercice7;

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
    
    public void setReference(String name, Reference ref) {
        if (variables.containsKey(name)) {
            variables.put(name, ref); // met � jour la variable existante
        } else if (parent != null && parent.getReferenceByName(name) != null) {
            parent.setReference(name, ref); // met � jour dans le parent
        } else {
            variables.put(name, ref); // cr�e une nouvelle variable locale
        }
    }
}