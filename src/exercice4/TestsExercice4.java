package exercice4;

/**
 * Tests fonctionnels pour l'Exercice 4.
 *
 * Ex 4.1 : boucle clavier — tu tapes les scripts toi-même dans la console. Ex
 * 4.2 : ajout dynamique d'éléments.
 *
 * Pour Ex4.1, les scripts à taper sont indiqués dans chaque test.
 */
public class TestsExercice4 {

	// -------------------------------------------------------------------------
	// TEST 1 — Boucle clavier (Ex 4.1)
	// Taper ces commandes une par une dans la console après le prompt ">"
	// -------------------------------------------------------------------------
	public static void test1_BoucleClavier() {
		System.out.println("=== TEST 1 Ex4.1 : Boucle clavier ===");
		System.out.println("Taper ces commandes dans la console :");
		System.out.println("  > (space setColor black)");
		System.out.println("  > (robi setColor yellow)");
		System.out.println("  > (robi translate 50 30)");
		System.out.println("  > (space sleep 500)");
		System.out.println("Attendu : chaque commande produit un effet immédiat");

		new Exercice4_1_0();
	}

	// -------------------------------------------------------------------------
	// TEST 2 — Ajout dynamique d'un rectangle (Ex 4.2)
	// Taper dans la console :
	// -------------------------------------------------------------------------
	public static void test2_AjoutRectangle() {
		System.out.println("=== TEST 2 Ex4.2 : Ajout d'un rectangle ===");
		System.out.println("Taper dans la console :");
		System.out.println("  > (space add robi (Rect new))");
		System.out.println("  > (robi setColor yellow)");
		System.out.println("  > (robi translate 130 50)");
		System.out.println("Attendu : un rectangle jaune apparaît en (130, 50)");

		new Exercice4_2_0();
	}

	// -------------------------------------------------------------------------
	// TEST 3 — Script complet du sujet (Ex 4.2)
	// Taper tout d'un coup dans la console sur une ligne
	// -------------------------------------------------------------------------
	public static void test3_ScriptCompletSujet() {
		System.out.println("=== TEST 3 Ex4.2 : Script complet du sujet ===");
		System.out.println("Copier-coller sur UNE SEULE LIGNE dans la console :");
		System.out.println("(space add robi (Rect new)) " + "(robi translate 130 50) " + "(robi setColor yellow) "
				+ "(space add momo (Oval new)) " + "(momo setColor red) " + "(momo translate 80 80)");
		System.out.println("Attendu : rectangle jaune + oval rouge");

		new Exercice4_2_0();
	}

	// -------------------------------------------------------------------------
	// TEST 4 — Suppression d'un élément
	// -------------------------------------------------------------------------
	public static void test4_SuppressionElement() {
		System.out.println("=== TEST 4 Ex4.2 : Suppression d'élément ===");
		System.out.println("Taper dans la console :");
		System.out.println("  > (space add robi (Rect new))");
		System.out.println("  > (robi setColor red)");
		System.out.println("  > (space sleep 1000)");
		System.out.println("  > (space del robi)");
		System.out.println("Attendu : rectangle rouge apparaît puis disparaît");

		new Exercice4_2_0();
	}

	public static void main(String[] args) {
		// test3_ScriptCompletSujet();
		// test1_BoucleClavier();
		test2_AjoutRectangle();
		test4_SuppressionElement();
	}
}