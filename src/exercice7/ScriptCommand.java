package exercice7;
import stree.parser.SNode;

public class ScriptCommand implements Command {
    private SNode definition;   
    private Environment globalEnv;

    public ScriptCommand(SNode definition, Environment globalEnv) {
        this.definition = definition;
        this.globalEnv = globalEnv;
    }

    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {

        Environment localEnv = new Environment(globalEnv);

        SNode params = definition.get(0); 

        localEnv.addReference(params.get(0).contents(), receiver);


        for (int i = 1; i < params.size(); i++) {
            String paramName = params.get(i).contents();
            String argValue = method.get(i + 1).contents();

            localEnv.addReference(paramName, new Reference(argValue));
        }


        Reference result = receiver;
        for (int i = 1; i < definition.size(); i++) {
            result = new Interpreter().compute(localEnv, definition.get(i));
        }
        return result;
    }
}