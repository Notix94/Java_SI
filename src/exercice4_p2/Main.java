package exercice4_p2;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) throws Exception {
        // Lancer le serveur en arrière-plan
        new Thread(() -> {
            try { SExprServer.main(new String[]{}); }
            catch (Exception e) { e.printStackTrace(); }
        }).start();

        // Attendre que le serveur soit prêt
        Thread.sleep(500);

        // Lancer le client
        SwingUtilities.invokeLater(ClientIHM::new);
    }
}