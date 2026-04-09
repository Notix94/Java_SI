package exercice4_p2;

import exercice6.*;
import graphicLayer.GSpace;
import stree.parser.SNode;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ScreenshotCommand implements Command {
    @Override
    public Reference run(Reference receiver, SNode method, Environment env) {
        // Le receiver est le GSpace du serveur
        GSpace space = (GSpace) receiver.getReceiver();
        
        try {
            // 1. Définir les dimensions (on utilise la dimension de l'espace)
            int width = space.getWidth();
            int height = space.getHeight();
            
            // Sécurité : si la fenêtre n'est pas encore bien initialisée
            if (width <= 0) width = 200;
            if (height <= 0) height = 100;

            // 2. Créer l'image tampon (Buffer)
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            
            // 3. Forcer un rendu de qualité 
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 4. DEMANDER au GSpace de se dessiner dans notre objet Graphics
            // On appelle paint() pour capturer tout ce qui est dans l'espace (fond + robi)
            space.paint(g2d);
            g2d.dispose();
            
            // 5. Convertir l'image en flux d'octets PNG
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            
            // 6. Encodage en Base64
            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            
            System.out.println("📸 Capture d'écran générée (Taille : " + base64Image.length() + " octets)");
            
            // On renvoie la chaîne Base64 brute dans une Reference.
            // SExprServer la détectera et l'enverra telle quelle au client.
            return new Reference(base64Image);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la capture d'écran : " + e.getMessage());
            e.printStackTrace();
        }
        return receiver;
    }
}