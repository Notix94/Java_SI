package exercice7;
import stree.parser.SNode;

public class Interpreter {
    public Reference compute(Environment env, SNode next) {
    	
    	//  Si c’est une feuille (nombre, variable, symbole)
        if (next.isLeaf()) {
        	Reference r = env.getReferenceByName(next.contents());
        	if( r != null) return r;
        	try{
        		// si c’est un nombre, le convertir en Reference
        		int val = Integer.parseInt(next.contents());
        		return new Reference(val);
        	} catch (NumberFormatException e){
        		return new Reference(next.contents());
        	}
        }
        
        String op = next.get(0).contents();
        
        if(op.equals("+")) {
        	int a = compute(env, next.get(1)).asInt();
        	int b = compute(env, next.get(2)).asInt();
        	return new Reference(a + b);
        }
        
        if(op.equals("-")) {
        	int a = compute(env, next.get(1)).asInt();
        	int b = compute(env, next.get(2)).asInt();
        	return new Reference(a - b);
        }
        
        if(op.equals("*")) {
        	int a = compute(env, next.get(1)).asInt();
        	int b = compute(env, next.get(2)).asInt();
        	return new Reference(a * b);
        }
        
        if(op.equals("/")) {
        	int a = compute(env, next.get(1)).asInt();
        	int b = compute(env, next.get(2)).asInt();
        	return new Reference(a / b);
        }
        
        if (op.equals(">")) {
            int a = compute(env, next.get(1)).asInt();
            int b = compute(env, next.get(2)).asInt();
            return new Reference(a > b);  // renvoie true ou false
        }
        
        if (op.equals("<")) {
            int a = compute(env, next.get(1)).asInt();
            int b = compute(env, next.get(2)).asInt();
            return new Reference(a < b);
        }
        
        if (op.equals(">=")) {
            int a = compute(env, next.get(1)).asInt();
            int b = compute(env, next.get(2)).asInt();
            return new Reference(a >= b);
        }
        
        if (op.equals("<=")) {
            int a = compute(env, next.get(1)).asInt();
            int b = compute(env, next.get(2)).asInt();
            return new Reference(a <= b);
        }
        
        if (op.equals("=")) {
            int a = compute(env, next.get(1)).asInt();
            int b = compute(env, next.get(2)).asInt();
            return new Reference(a == b);
        }
        
        if(op.equals("if")) {
        	Reference cond = compute(env, next.get(1));
        	if (cond.isTrue()) {
                return compute(env, next.get(2));
            } else {
                return compute(env, next.get(3));
            }
        }
        
        if (op.equals("while")) {
            Reference result = null;
            while (compute(env, next.get(1)).isTrue()) {
                result = compute(env, next.get(2));
            }
            return result;
        }
        
        if (op.equals("set")) {
            String varName = next.get(1).contents();
            Reference value = compute(env, next.get(2));
            env.setReference(varName, value);
            return value;
        }
        Reference receiver = env.getReferenceByName(next.get(0).contents());
        if (receiver == null) throw new Error("Objet inconnu : " + next.get(0).contents());
        return receiver.run(next, env);
    }
}