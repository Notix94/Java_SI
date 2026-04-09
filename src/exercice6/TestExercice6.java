package exercice6;

/**
 * Tests fonctionnels pour l'Exercice 6.
 *
 * Vérifie que addScript et les scripts utilisateurs fonctionnent correctement.
 * Chaque test ouvre une fenêtre et exécute un script automatiquement.
 */
public class TestExercice6 {

	// -------------------------------------------------------------------------
	// TEST 1 — Script "empty" sans paramètre (exemple du sujet)
	// Attendu : robi rouge apparaît, puis disparaît après 1 seconde
	// -------------------------------------------------------------------------
	public static void test1_ScriptEmpty() {
		System.out.println("=== TEST 1 Ex6 : Script empty ===");
		System.out.println("Attendu : robi rouge apparaît, puis espace se vide");

		new Exercice6("(space add robi (Rect new)) " + "(robi setColor red) " + "(space sleep 1500) "
				+ "(space addScript vider ( (self) (self clear) )) " + "(space vider)");
	}

	// -------------------------------------------------------------------------
	// TEST 2 — Script avec un paramètre : addImage (exemple du sujet)
	// Attendu : l'image alien.gif apparaît dans la fenêtre
	// -------------------------------------------------------------------------
	public static void test2_ScriptAvecParametre() {
		System.out.println("=== TEST 2 Ex6 : Script addImage avec paramètre ===");
		System.out.println("Attendu : l'image alien.gif apparaît dans la fenêtre");

		new Exercice6("(space addScript addImage ( (self filename) (self add im (Image new filename)) )) "
				+ "(space addImage alien.gif)");
	}

	// -------------------------------------------------------------------------
	// TEST 3 — Script addRect avec plusieurs paramètres (exemple du sujet)
	// Attendu : un carré jaune 30x30 apparaît dans robi
	// -------------------------------------------------------------------------
	public static void test3_ScriptMultiParams() {
		System.out.println("=== TEST 3 Ex6 : Script addRect multi-paramètres ===");
		System.out.println("Attendu : rectangle blanc contenant un carré jaune 30x30");

		new Exercice6("(space add robi (Rect new)) " + "(space.robi setColor white) " + "(space.robi setDim 100 80) "
				+ "(space.robi addScript addRect ( " + "(self name w c) " + "(self add name (Rect new)) "
				+ "(self.name setColor c) " + "(self.name setDim w w) )) " + "(space.robi addRect mySquare 30 yellow)");
	}

	// -------------------------------------------------------------------------
	// TEST 4 — Appel multiple d'un même script
	// Attendu : 3 rectangles rouges créés dans robi
	// -------------------------------------------------------------------------
	public static void test4_AppelMultipleScript() {
		System.out.println("=== TEST 4 Ex6 : Appel multiple du même script ===");
		System.out.println("Attendu : 3 rectangles rouges à des positions différentes");

		new Exercice6("(space add robi (Rect new)) " + "(space.robi setColor white) " + "(space.robi setDim 150 150) "
				+ "(space.robi addScript creerRouge ( (self nom x) " + "    (self add nom (Rect new)) "
				+ "    (self.nom setColor red) " + "    (self.nom translate x 10) )) "
				+ "(space.robi creerRouge r1 10) " + "(space.robi creerRouge r2 40) "
				+ "(space.robi creerRouge r3 70)");
	}

	// -------------------------------------------------------------------------
	// TEST 5 — Script qui modifie la couleur (script peindre)
	// Attendu : robi change de couleur quand on appelle le script
	// -------------------------------------------------------------------------
	public static void test5_ScriptCouleur() {
		System.out.println("=== TEST 5 Ex6 : Script peindre avec paramètre couleur ===");
		System.out.println("Attendu : robi commence bleu, puis devient rouge après 1s");

		new Exercice6("(space add robi (Rect new)) " + "(robi setColor blue) " + "(space sleep 1000) "
				+ "(robi addScript peindre ( (self c) (self setColor c) )) " + "(robi peindre red)");
	}

	// -------------------------------------------------------------------------
	// TEST 6 — Mode interactif avec addScript
	// Taper les scripts dans la console
	// -------------------------------------------------------------------------
	public static void test6_ModeInteractif() {
		System.out.println("=== TEST 6 Ex6 : Mode interactif ===");
		System.out.println("Taper dans la console :");
		System.out.println("  > (space add robi (Rect new))");
		System.out.println("  > (robi addScript bouger ( (self dx dy) (self translate dx dy) ))");
		System.out.println("  > (robi bouger 50 30)");
		System.out.println("  > (robi bouger 20 0)");

		new Exercice6();
	}

	public static void main(String[] args) {
		//test3_ScriptMultiParams();
		 //test1_ScriptEmpty();
		 test2_ScriptAvecParametre();
		// test4_AppelMultipleScript();
		 //test5_ScriptCouleur();
		// test6_ModeInteractif();
	}
}
