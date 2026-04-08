package exercice6;
import stree.parser.SNode;

/**
 * Moteur d'exécution des scripts personnalisés.
 * Cette classe transforme un bloc d'instructions (SNode) en une commande 
 * exécutable, en gérant la création d'un environnement local pour les paramètres.
 */
public class ScriptCommand implements Command {
    private SNode definition;   
    private Environment globalEnv;

    /**
     * @param definition Le nœud contenant les paramètres et les instructions.
     * @param globalEnv L'environnement global pour la résolution des noms externes.
     */
    public ScriptCommand(SNode definition, Environment globalEnv) {
        this.definition = definition;
        this.globalEnv = globalEnv;
    }

    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // CRÉATION DU CONTEXTE LOCAL
        // On crée un environnement spécifique pour l'exécution du script 
        // afin de ne pas polluer l'environnement global avec les variables locales.
        Environment localEnv = new Environment(globalEnv);

        // GESTION DES PARAMÈTRES
        // definition.get(0) contient la liste des paramètres (ex: (self w h))
        SNode params = definition.get(0); 

        // Liaison du mot-clé "self" (ou le 1er paramètre) au récepteur actuel
        localEnv.addReference(params.get(0).contents(), receiver);

        // MAPPING DES ARGUMENTS
        // On parcourt les paramètres restants et on leur associe les valeurs 
        // fournies lors de l'appel du script.
        for (int i = 1; i < params.size(); i++) {
            String paramName = params.get(i).contents();
            // L'argument dans 'method' commence à l'index 2 (0: objet, 1: nom_script, 2: arg1...)
            String argValue = method.get(i + 1).contents();

            localEnv.addReference(paramName, new Reference(argValue));
        }

        // EXÉCUTION DU CORPS DU SCRIPT
        // On itère sur chaque instruction du script à partir de l'index 1 
        // (l'index 0 étant réservé à la liste des paramètres).
        Reference result = receiver;
        for (int i = 1; i < definition.size(); i++) {
            // Chaque ligne est évaluée par l'interpréteur dans le contexte local
            result = new Interpreter().compute(localEnv, definition.get(i));
        }
        
        // On retourne le résultat de la dernière instruction du script
        return result;
    }
}