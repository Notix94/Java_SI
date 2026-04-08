package exercice3;

/**
 * Interface pivot du Design Pattern Command.
 * Son intention est de normaliser toutes les actions issues du script 
 * (mouvement, changement de couleur, temporisation) afin que l'interpréteur 
 * puisse les manipuler sans connaître leur logique interne.
 * * Dépendances : Implémentée par toutes les classes d'actions (ex: RobiTranslate, SpaceSleep).
 */
public interface Command {
	/**
	 * Déclenche l'exécution de l'action sur la cible graphique.
	 * Cette méthode permet de retarder l'exécution de la commande après sa création.
	 */
	abstract public void run();
}