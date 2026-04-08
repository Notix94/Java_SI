package exercice4;
import graphicLayer.GElement;
import stree.parser.SNode;

/**
 * Usine à objets utilisant la réflexion Java.
 * Instancie dynamiquement un GElement à partir de sa classe et lui attache 
 * son catalogue de commandes de base (setColor, translate, setDim).
 */
public class NewElement implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        try {
            // Instanciation dynamique par introspection
            @SuppressWarnings("unchecked")
            Class<GElement> type = (Class<GElement>) reference.getReceiver();
            GElement e = type.getDeclaredConstructor().newInstance();
            
            Reference ref = new Reference(e);
            
            // Configuration des capacités par défaut du nouvel objet
            ref.addCommand("setColor", new SetColor());
            ref.addCommand("translate", new Translate());
            ref.addCommand("setDim", new SetDim());
            
            return ref;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}