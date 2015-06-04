package main.java.automaton;

import main.java.data.Data;
import main.java.game.GameSimulation;

public class Evaluator {
	
	/**
	 * Simulates nbrGamesSimulated games between the player of the game starting with currentConfig. 
	 * The players are modeled by automata of the given difficulty 
	 * @param nbrGamesSimulated
	 * @param currentConfig
	 * @param aiLvl
	 * @return
	 */
	public static double evaluateSituationQuality(String playerName, int nbrGamesSimulated, Data config, int aiLvl) {
		double victoryProportion = 0;
		int victoriesNumber = 0;
		
		// Simulating the games			
		
		for(int i = 0; i < nbrGamesSimulated; i++) {
System.out.println("********************** SIMULATION "+i+" **********************");
			GameSimulation gameSim = null;
			try {
				gameSim = GameSimulation.newGameSimulation(config, aiLvl);
			} catch (Exception e) {
				System.out.println("Game creation error"); e.printStackTrace();
			}
			
			// Have we won ?
			if(gameSim.isWinner(playerName)) victoriesNumber++;
		}
		
		victoryProportion = (double)victoriesNumber / (double)nbrGamesSimulated;
		return victoryProportion;
	}
	
}
