package exercice6;

import graphicLayer.GRect;
import java.awt.Point;
import stree.parser.SNode;

public class SetPosition implements Command {

    @Override
    public Reference run(Reference ref, SNode method, Environment env) {
        Object receiver = ref.getReceiver();

        if (receiver instanceof GRect) {
            GRect rect = (GRect) receiver;

            int x = Integer.parseInt(method.get(2).contents());
            int y = Integer.parseInt(method.get(3).contents());

            rect.setPosition(new Point(x, y));
        }

        return ref;
    }
}