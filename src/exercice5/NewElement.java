package exercice5;

import graphicLayer.GElement;
import stree.parser.SNode;

/**
 * Usine à objets dynamique  pour l'exercice 5.
 * En plus d'instancier les composants, elle leur injecte la capacité 
 * de devenir des conteneurs, permettant ainsi une hiérarchie infinie.
 */
public class NewElement implements Command {
    private Environment env;

    /**
     * Le constructeur reçoit l'environnement global pour permettre 
     * aux futurs objets enfants d'y être enregistrés.
     */
    public NewElement(Environment env) {
        this.env = env;
    }

    @Override
    public Reference run(Reference reference, SNode method) {
        try {
            // INSTANCIATION RÉFLEXIVE
            // On récupère la classe Java (ex: GRect.class) pour créer l'instance.
            @SuppressWarnings("unchecked")
            Class<GElement> type = (Class<GElement>) reference.getReceiver();
            GElement e = type.getDeclaredConstructor().newInstance();
            
            // CRÉATION DE LA RÉFÉRENCE
            // Le nom est initialisé à null car l'identité finale (chemin composé) 
            // est déterminée par la commande 'add' du parent.
            Reference ref = new Reference(e, null);
            
            // CATALOGUE DE COMMANDES DE BASE
            ref.addCommand("setColor", new SetColor());
            ref.addCommand("translate", new Translate());
            ref.addCommand("setDim", new SetDim());
            
            // --- LOGIQUE HIÉRARCHIQUE (EXERCICE 5) ---
            // On rend l'objet "capable" d'ajouter des sous-éléments. 
            // C'est cette ligne qui permet de faire (robi add bras (Rect new)).
            ref.addCommand("add", new AddElement(env));
            
            return ref;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}