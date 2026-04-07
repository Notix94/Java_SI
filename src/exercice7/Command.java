package exercice7;
import stree.parser.SNode;

public interface Command {
    // La méthode doit retourner une Reference 
    abstract public Reference run(Reference receiver, SNode method, Environment env);
}