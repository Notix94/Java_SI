package exercice7;
import graphicLayer.GElement;
import stree.parser.SNode;

public class NewElement implements Command {
    @Override
    public Reference run(Reference reference, SNode method, Environment env) {
        try {
            // Utilisation de la réflexivité comme demandé
            @SuppressWarnings("unchecked")
            Class<GElement> type = (Class<GElement>) reference.getReceiver();
            GElement e = type.getDeclaredConstructor().newInstance();
            
            Reference ref = new Reference(e);
            // On configure la référence avec les commandes de base
            ref.addCommand("setColor", new SetColor());
            ref.addCommand("translate", new Translate());
            ref.addCommand("setDim", new SetDim());
            ref.addCommand("add",       new AddElement(env));
            ref.addCommand("del",       new DelElement(env));
            ref.addCommand("addScript", new AddScript(env));
            
            return ref;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}