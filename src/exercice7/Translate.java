package exercice7;

import java.awt.Point;
import graphicLayer.GElement;
import stree.parser.SNode;

public class Translate implements Command {
	@Override
	public Reference run(Reference receiver, SNode method, Environment env) {
		// GElement est le "père" de GRect, GOval, GImage... donc ça marche pour TOUT.
		GElement elem = (GElement) receiver.getReceiver();
		int dx = resolveInt(method.get(2).contents(), env);
		int dy = resolveInt(method.get(3).contents(), env);
		elem.translate(new Point(dx, dy));
		return receiver;
	}

	private int resolveInt(String value, Environment env) {
		Reference r = env.getReferenceByName(value);

		if (r != null) {
			Object obj = r.getReceiver();

			if (obj instanceof Integer) {
				return (Integer) obj;
			}
			if (obj instanceof String) {
				return Integer.parseInt((String) obj);
			}
		}

		// sinon c’est un nombre direct
		return Integer.parseInt(value);
	}
}