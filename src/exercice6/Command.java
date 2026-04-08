package exercice6;
import stree.parser.SNode;

//Interface Command — représente une commande exécutable par un objet graphique.
public interface Command {
    // La méthode doit retourner une Reference 
    abstract public Reference run(Reference receiver, SNode method, Environment env);
}