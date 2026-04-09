package exercice4_p2;

import exercice6.*;
import stree.parser.SNode;
import stree.parser.SParser;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class LoadCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Cela permet de trouver le fichier propre créé par SaveCommand
        String fileName = method.get(2).contents().replace("\"", "");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Lecture de tout le fichier
            String json = reader.lines().collect(Collectors.joining());
            
            // ÉTAPE 1 : Nettoyage pour éviter les erreurs de parsing
            // On enlève les espaces, les retours à la ligne et les guillemets internes
            String clean = json.replaceAll("\\s+", "").replaceAll("\"", "");

            // ÉTAPE 2 : Extraction de la partie couleur [R,G,B]
            // On prend ce qu'il y a entre "color:[" et "]"
            String colorPart = clean.split("color:\\[")[1].split("\\]")[0];
            String[] rgb = colorPart.split(",");
            
            int r = Integer.parseInt(rgb[0]);
            int g = Integer.parseInt(rgb[1]);
            int b = Integer.parseInt(rgb[2]);

            // ÉTAPE 3 : Extraction de X et Y
            // On cherche "x:" puis on s'arrête à la virgule suivante
            int x = Integer.parseInt(clean.split("x:")[1].split(",")[0]);
            // On cherche "y:" puis on s'arrête à l'accolade de fin
            int y = Integer.parseInt(clean.split("y:")[1].split("\\}")[0]);

            // ÉTAPE 4 : Création du script de reconstruction
            // C'est ce script qui sera envoyé au Client et exécuté par le Serveur
            String script = "(robi setColor " + r + " " + g + " " + b + ") (robi setPosition " + x + " " + y + ")";
            
            System.out.println("🔄 Chargement depuis : " + fileName);
            System.out.println("🔄 Reconstruction : " + script);

            SParser<SNode> parser = new SParser<>();
            List<SNode> nodes = parser.parse(script);
            
            // Mise à jour immédiate du rendu du SERVEUR
            Interpreter interpreter = new Interpreter();
            for (SNode n : nodes) {
                interpreter.compute(env, n);
            }

            // On renvoie les nouveaux nœuds pour que SExprServer les envoie au CLIENT
            return new Reference(nodes); 

        } catch (FileNotFoundException e) {
            System.err.println("❌ Fichier introuvable : " + fileName);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement du fichier " + fileName);
            e.printStackTrace();
        }
        return receiver;
    }
}