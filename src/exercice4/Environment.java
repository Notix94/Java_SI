package exercice4;

import java.util.HashMap;

/**
 * Annuaire central (Table des symboles) du projet.
 * Maintient la correspondance entre les identifiants textuels du script 
 * et les instances réelles (Reference).
 */
public class Environment {
    private HashMap<String, Reference> variables;

    public Environment() {
        variables = new HashMap<String, Reference>();
    }

    /**
     * Enregistre ou met à jour une référence dans l'environnement.
     */
    public void addReference(String name, Reference ref) {
        variables.put(name, ref);
    }

    /**
     * Recherche un objet par son nom.
     */
    public Reference getReferenceByName(String name) {
        return variables.get(name);
    }
}