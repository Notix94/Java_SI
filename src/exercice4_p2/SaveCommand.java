package exercice4_p2;

import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;
import java.io.FileWriter;
import java.awt.Point;
import java.awt.Color;
import java.lang.reflect.Field; // Indispensable pour la réflexion


/**
 * Commande de sauvegarde (Exercice 4).
 * Extrait l'état actuel de Robi et l'écrit dans un fichier au format JSON.
 * Utilise la réflexion pour récupérer la couleur sans modifier graphicLayer.
 */
public class SaveCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // 1. On récupère et on nettoie le nom du fichier
        String fileName = method.get(2).contents().replace("\"", "");
        
        try (FileWriter writer = new FileWriter(fileName)) {
            // 2. Récupération de l'objet Robi
            Reference robiRef = env.getReferenceByName("robi");
            Object r = robiRef.getReceiver(); // On le garde en Object pour la réflexion
            
            // --- RÉCUPÉRATION DE LA COULEUR VIA RÉFLEXION ---
            Color c = Color.BLACK; // Valeur par défaut
            try {
                // On cherche l'attribut "color" dans la classe GElement (parent de GRect)
                Field field = r.getClass().getSuperclass().getDeclaredField("color");
                field.setAccessible(true); // On force l'accès (s'il est protected ou private)
                c = (Color) field.get(r);   // On récupère la couleur réelle de l'objet
            } catch (Exception e) {
                System.err.println("⚠️ Attention : Impossible de lire 'color' via réflexion, usage du noir.");
            }
            // ---------------------------------------------------------

            // 3. Récupération de la position (si getPosition() pose problème, utilise la même astuce)
            Point p = ((GRect)r).getPosition();

            // 4. Construction du JSON
            String json = "{\n" +
                "  \"robi\": {\n" +
                "    \"color\": [" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "],\n" +
                "    \"x\": " + p.x + ",\n" +
                "    \"y\": " + p.y + "\n" +
                "  }\n" +
                "}";
            
            writer.write(json);
            System.out.println("✅ Scène sauvegardée proprement dans : " + fileName);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
        return receiver;
    }
}