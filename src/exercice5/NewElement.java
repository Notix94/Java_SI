package exercice5;

import graphicLayer.GElement;
import stree.parser.SNode;

public class NewElement implements Command {
    private Environment env;

    // MODIF : On ajoute un constructeur pour recevoir l'environnement
    public NewElement(Environment env) {
        this.env = env;
    }

    @Override
    public Reference run(Reference reference, SNode method) {
        try {
            @SuppressWarnings("unchecked")
            Class<GElement> type = (Class<GElement>) reference.getReceiver();
            GElement e = type.getDeclaredConstructor().newInstance();
            
            // MODIF : On crée la référence avec un nom à null 
            // (C'est AddElement qui lui donnera son nom définitif "parent.enfant")
            Reference ref = new Reference(e, null);
            
            // Commandes de base
            ref.addCommand("setColor", new SetColor());
            ref.addCommand("translate", new Translate());
            ref.addCommand("setDim", new SetDim());
            
            // --- MODIF EXERCICE 5 ---
            // On permet à ce nouvel objet d'être lui-même un conteneur
            ref.addCommand("add", new AddElement(env));
            // ------------------------------------
            
            return ref;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}