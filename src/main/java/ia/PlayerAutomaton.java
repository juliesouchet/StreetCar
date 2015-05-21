package main.java.ia;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;


/**
 * 
 * @author ulysse de la pampa
 * Interface de joueur automatique:
 * le moteur demande a l'automate de lui donner son choix d'action lorsque c'est son otur.
 * L'automate peut:
 * 	faire un coup simple (cad il pose(/ou échange) 1 tuile (dans ce cas il va etre ré appeler pour terminer ce tour)
 * 	faire un coup double (il pause 2 tuiles simultanément)
 * 	commencer son voyage
 * 	continuer son voyage (et donc faire son déplacement)
 */
public abstract class PlayerAutomaton {
	
	/**
	 * Donne la décision d'action faite par l'automate
	 */
	public abstract Action makeChoice(Hand myHand, Data currentConfig );
	
	
}
