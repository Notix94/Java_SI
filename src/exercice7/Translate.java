package exercice7;
import java.awt.Point;
import graphicLayer.GElement;
import stree.parser.SNode;

public class Translate implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // GElement est le "père" de GRect, GOval, GImage... donc ça marche pour TOUT.
        GElement elem = (GElement) receiver.getReceiver();
        int dx = Integer.parseInt(method.get(2).contents());
        int dy = Integer.parseInt(method.get(3).contents());
        elem.translate(new Point(dx, dy));
        return receiver;
    }
}