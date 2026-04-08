package exercice7;
import stree.parser.SNode;

public class AddScript implements Command {
    private Environment env;

    public AddScript(Environment env) { this.env = env; }

    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        String scriptName = method.get(2).contents();  
        SNode definition = method.get(3);              
        receiver.addCommand(scriptName, new ScriptCommand(definition, this.env));
        return receiver;
    }
}