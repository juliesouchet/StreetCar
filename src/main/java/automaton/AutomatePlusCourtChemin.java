package main.java.automaton;

import main.java.data.Action;
import main.java.data.Data;

/**
 * Premiere implementation d'un automate que cherche a poser des rails pour creer le chemin le plus courts de son terminus vers ses points de passage puis vers autre terminus
 * v1 : il crée un chemin vers entre une origine et un destiantion; fonctionne pour l'ia seule sur un terrain vierge
 * @author ulysse
 *
 */
public class AutomatePlusCourtChemin extends PlayerAutomaton {
	int heuristique[][][];
	
	
	public AutomatePlusCourtChemin(int width, int height, int numberOfStop){
		this.heuristique = new int[numberOfStop][width][height];
	}
	
	
	@Override
	public Action makeChoice(Data currentConfig) {
		// TODO Auto-generated method stub
		return null;
	}


}
