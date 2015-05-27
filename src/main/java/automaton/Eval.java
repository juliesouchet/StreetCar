package main.java.automaton;

import main.java.data.Data;

public class Eval {
	PlayerAutomaton automaton;
	
	public Eval(PlayerAutomaton player) {
		automaton = player;
	}
	
	/**
	 * Simulates gamesNumber games between the player of the game starting with currentConfig. 
	 * The players are modeled by automata of the given difficulty
	 * @param gamesNumber
	 * @param currentConfig
	 * @param difficulty
	 * @return
	 */
	int evaluateSituationQuality(int gamesNumber, Data currentConfig, String difficulty) {
		int nbVictories = 0;
		PlayerAutomaton player1 = new Dumbest();
		String[] playerList = (String[]) currentConfig.getPlayerNameList().toArray();
		//PlayerAutomaton[] otherPlayers = new
		Data config;
		
		for(int i = 0; i < gamesNumber; i++) {
			config = currentConfig.getClone(automaton.name);
			switch (difficulty) {
				case "Dumbest" :
					break;
				case "Traveler" :
					player1 = new Traveler();
					break;
				default :
					throw new RuntimeException("Undefined difficulty");
			}			
			player1.name = automaton.name;
		}
				
		/* TODO simuler 100 parties entre deux automates de niveau "difficulty"
		 * et rendre le nombre de victoires de l'automate nous représentant (player1)
		 */
		return nbVictories;
	}
}
