package exercice5;

import java.util.HashMap;

/**
 * Table des symboles centralisée du projet.
 * Dans l'exercice 5, elle assure la correspondance entre les chemins 
 * hiérarchiques (ex: "space.robi.bras") et les instances de Reference.
 */
public class Environment {
    private HashMap<String, Reference> variables;

    public Environment() {
        variables = new HashMap<String, Reference>();
    }

    /**
     * Enregistre une référence sous son identifiant unique (chemin absolu).
     */
    public void addReference(String name, Reference ref) {
        variables.put(name, ref);
    }

    /**
     * Récupère une référence à partir de son nom composé dans l'annuaire.
     */
    public Reference getReferenceByName(String name) {
        return variables.get(name);
    }
}