package exercice6;

import java.awt.Dimension;
import graphicLayer.GElement;
import graphicLayer.GRect;
import graphicLayer.GOval;
import stree.parser.SNode;

public class SetDim implements Command {
	@Override
	public Reference run(Reference receiver, SNode method, Environment env) {
		Object target = receiver.getReceiver();

		// On récupère la largeur et la hauteur depuis le script
		int w = resolveInt(method.get(2).contents(), env);
		int h = resolveInt(method.get(3).contents(), env);
		Dimension dim = new Dimension(w, h);

		// On vérifie le type réel de l'objet pour appeler la bonne méthode
		if (target instanceof GRect) {
			((GRect) target).setDimension(dim);
		} else if (target instanceof GOval) {
			((GOval) target).setDimension(dim);
		}
		return receiver;
	}

	// Cherche la valeur dans l'env si c'est un nom de paramètre, sinon parse directement
	private int resolveInt(String value, Environment env) {
		Reference r = env.getReferenceByName(value);
		if (r != null && r.getReceiver() instanceof String) {
			return Integer.parseInt((String) r.getReceiver());
		}
		return Integer.parseInt(value);
	}

}