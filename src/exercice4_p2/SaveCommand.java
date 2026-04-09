package exercice4_p2;

import exercice6.*;
import graphicLayer.*;
import stree.parser.SNode;
import java.io.FileWriter;
import java.awt.Point;
import java.awt.Color;
import java.lang.reflect.Field;

<<<<<<< HEAD

/**
 * Commande de sauvegarde (Exercice 4).
 * Extrait l'état actuel de Robi et l'écrit dans un fichier au format JSON.
 * Utilise la réflexion pour récupérer la couleur sans modifier graphicLayer.
 */
=======
>>>>>>> 459acaec2b5c118a49479ebec96c692433a22475
public class SaveCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        String fileName = method.get(2).contents().replace("\"", "");
        
        try (FileWriter writer = new FileWriter(fileName)) {
            Reference robiRef = env.getReferenceByName("robi");
            Object r = robiRef.getReceiver();
            
<<<<<<< HEAD
            // --- RÉCUPÉRATION DE LA COULEUR VIA RÉFLEXION ---
            Color c = Color.BLACK; // Valeur par défaut
            try {
                // On cherche l'attribut "color" dans la classe GElement (parent de GRect)
                Field field = findFieldInClassHierarchy(r.getClass(),"color");
                field.setAccessible(true); // On force l'accès (s'il est protected ou private)
                c = (Color) field.get(r);   // On récupère la couleur réelle de l'objet
            } catch (Exception e) {
                System.err.println("⚠️ Attention : Impossible de lire 'color' via réflexion, usage du noir.");
            }
            // ---------------------------------------------------------
=======
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
>>>>>>> 459acaec2b5c118a49479ebec96c692433a22475

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
 // Ajoute cette méthode dans ta classe SaveCommand
    private Field findFieldInClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + fieldName + " not found in class hierarchy");
    }
}