package exercice6;
import java.awt.Color;
import java.util.Map;
import graphicLayer.GElement;
import graphicLayer.GSpace;
import stree.parser.SNode;

public class SetColor implements Command {
    private final Map<String, Color> colors = Map.of(
        "black", Color.BLACK, "white", Color.WHITE, "red", Color.RED, 
        "yellow", Color.YELLOW, "blue", Color.BLUE, "green", Color.GREEN
    );

    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        Object target = receiver.getReceiver();
        String colorName = method.get(2).contents();
        
        
        
        Reference r = env.getReferenceByName(colorName);
        if (r != null && r.getReceiver() instanceof String) {
            colorName = (String) r.getReceiver();
        }
        
        Color c = colors.getOrDefault(colorName.toLowerCase(), Color.BLACK);
        
        if (target instanceof GElement) ((GElement) target).setColor(c);
        else if (target instanceof GSpace) ((GSpace) target).setColor(c);
        
        return receiver;
    }
}