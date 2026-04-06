package exercice4;

import java.util.HashMap;

public class Environment {
    private HashMap<String, Reference> variables;

    public Environment() {
        variables = new HashMap<String, Reference>(); // [cite: 272]
    }

    public void addReference(String name, Reference ref) {
        variables.put(name, ref); // [cite: 192]
    }

    public Reference getReferenceByName(String name) {
        return variables.get(name); // [cite: 193]
    }
}