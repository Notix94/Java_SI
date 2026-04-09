package exercice4_p2;

import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;
import java.io.FileWriter;
import java.awt.Point;
import java.awt.Color;
import java.lang.reflect.Field;

public class SaveCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        String fileName = method.get(2).contents().replace("\"", "");
        
        try (FileWriter writer = new FileWriter(fileName)) {
            Reference robiRef = env.getReferenceByName("robi");
            Object r = robiRef.getReceiver();
            
            // --- NOUVELLE STRATÉGIE DE RÉFLEXION ---
            Color c = Color.BLACK;
            Field colorField = null;
            Class<?> currentClass = r.getClass();

            // On remonte toute la hiérarchie (GRect -> ... -> GElement -> Object)
            while (currentClass != null && colorField == null) {
                try {
                    colorField = currentClass.getDeclaredField("color");
                } catch (NoSuchFieldException e) {
                    // Si on ne trouve pas ici, on cherche dans la classe parente
                    currentClass = currentClass.getSuperclass();
                }
            }

            if (colorField != null) {
                colorField.setAccessible(true);
                c = (Color) colorField.get(r);
                System.out.println("🎨 Couleur trouvée : " + c);
            } else {
                System.err.println("❌ Champ 'color' introuvable dans toute la hiérarchie.");
            }
            // ---------------------------------------

            Point p = ((GRect)r).getPosition();

            String json = "{\n" +
                "  \"robi\": {\n" +
                "    \"color\": [" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "],\n" +
                "    \"x\": " + p.x + ",\n" +
                "    \"y\": " + p.y + "\n" +
                "  }\n" +
                "}";
            
            writer.write(json);
            System.out.println("✅ Sauvegarde réussie dans : " + fileName);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
        return receiver;
    }
}