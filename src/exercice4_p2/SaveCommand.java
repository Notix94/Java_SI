package exercice4_p2;

import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;
import java.io.FileWriter;
import java.awt.Point;
import java.awt.Color;
import java.lang.reflect.Field;

/**
 * Commande de sauvegarde — Version Robuste.
 * Utilise la réflexion pour extraire les données privées de la bibliothèque.
 */
public class SaveCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Nettoyage du nom de fichier
        String fileName = method.get(2).contents().replace("\"", "");
        
        try (FileWriter writer = new FileWriter(fileName)) {
            Reference robiRef = env.getReferenceByName("robi");
            Object r = robiRef.getReceiver();
            
            // --- RÉCUPÉRATION DE LA COULEUR PAR RÉFLEXION ---
            Color c = Color.BLACK;
            Field colorField = findField(r.getClass(), "color");
            
            if (colorField != null) {
                colorField.setAccessible(true);
                c = (Color) colorField.get(r);
                System.out.println("🎨 [DEBUG] Couleur lue : " + c);
            } else {
                System.err.println("❌ Erreur : Champ 'color' introuvable !");
            }

            // --- RÉCUPÉRATION DE LA POSITION ---
            // On essaie getPosition(), sinon on passe par les champs x et y
            int x = 0, y = 0;
            try {
                Point p = ((GRect)r).getPosition();
                x = p.x;
                y = p.y;
            } catch (Throwable t) {
                // Secours par réflexion si getPosition() n'existe pas
                Field xField = findField(r.getClass(), "x");
                Field yField = findField(r.getClass(), "y");
                if (xField != null && yField != null) {
                    xField.setAccessible(true);
                    yField.setAccessible(true);
                    x = (int) xField.get(r);
                    y = (int) yField.get(r);
                }
            }

            // --- ÉCRITURE DU JSON ---
            // Format strict pour que ton LoadCommand puisse splitter facilement
            String json = "{\n" +
                "  \"robi\": {\n" +
                "    \"color\": [" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "],\n" +
                "    \"x\": " + x + ",\n" +
                "    \"y\": " + y + "\n" +
                "  }\n" +
                "}";
            
            writer.write(json);
            System.out.println("✅ Scène sauvegardée dans : " + fileName);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
        return receiver;
    }

    /**
     * Méthode utilitaire pour chercher un champ dans toute la hiérarchie de classes.
     */
    private Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}