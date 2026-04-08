package exercice5;

import exercice5.Exercice5;

/**
 * Classe de test unitaire pour l'exercice 5.
 * Son rôle est de valider le mode d'exécution "One-Shot" : le moteur interprète 
 * un script complet sans passer par l'interface interactive.
 */
public class Example1 {
	
	/**
	 * Scénario de test :
	 * Crée un rectangle nommé "robi" et l'ajoute au conteneur "space".
	 * Valide la commande 'add' et l'instanciation dynamique 'new'.
	 */
	String script = "(space add robi (Rect new))";
	
	public void launch() {
		Exercice5 exo = new Exercice5();
		// Exécution immédiate du script défini ci-dessus
		exo.oneShot(script);
	}
	
	public static void main(String[] args) {
		new Example1().launch();
	}
}