package exercice4;

import java.awt.Dimension;
import graphicLayer.GRect;
import graphicLayer.GOval;
import stree.parser.SNode;

/**
 * Commande de redimensionnement pour les formes géométriques. Traduit les
 * arguments entiers du script en un objet Dimension Java. Dépendances : GRect,
 * GOval.
 */
public class SetDim implements Command {
	@Override
	public Reference run(Reference receiver, SNode method) {
		Object target = receiver.getReceiver();

		// Extraction et conversion des dimensions depuis le script
		int w = Integer.parseInt(method.get(2).contents());
		int h = Integer.parseInt(method.get(3).contents());
		Dimension dim = new Dimension(w, h);

		// Application via transtypage selon le type réel du composant
		if (target instanceof GRect) {
			((GRect) target).setDimension(dim);
		} else if (target instanceof GOval) {
			((GOval) target).setDimension(dim);
		}

		return receiver;
	}
}