package exercice4_p2;

import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;
import java.io.FileWriter;
import java.awt.Point;
import java.awt.Color;

/**
 * Commande de sauvegarde (Exercice 4).
 * Extrait l'état actuel de Robi et l'écrit dans un fichier au format JSON.
 */
public class SaveCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Syntaxe attendue : (space save "ma_scene.json")
        String fileName = method.get(2).contents();
        
        try (FileWriter writer = new FileWriter(fileName)) {
            // Récupération de Robi depuis l'environnement
            Reference robiRef = env.getReferenceByName("robi");
            GRect r = (GRect) robiRef.getReceiver();
            
            Color c = r.getColor();
            Point p = r.getPosition();

            // Construction manuelle d'un JSON simple
            String json = "{\n" +
                "  \"robi\": {\n" +
                "    \"color\": [" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "],\n" +
                "    \"x\": " + p.x + ",\n" +
                "    \"y\": " + p.y + "\n" +
                "  }\n" +
                "}";
            
            writer.write(json);
            System.out.println("✅ Scène sauvegardée dans : " + fileName);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
        }
        return receiver;
    }
}