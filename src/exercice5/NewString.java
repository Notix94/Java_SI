package exercice5;

import graphicLayer.GString;
import stree.parser.SNode;

public class NewString implements Command {
    @Override
    public Reference run(Reference reference, SNode method) {
        // (Label new "Hello world") -> Le texte est à l'index 2
        String text = method.get(2).contents();
        GString label = new GString(text);
        
        // MODIF : On ajoute "null" en deuxième argument
        Reference newRef = new Reference(label, null);
        
        // On donne les pouvoirs de base au texte
        newRef.addCommand("setColor", new SetColor());
        newRef.addCommand("translate", new Translate());
        
        // Note : On n'ajoute pas "add" ici car un Label n'est pas censé E
        
        return newRef;
    }
}