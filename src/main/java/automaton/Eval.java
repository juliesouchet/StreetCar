package main.java.automaton;

import main.java.data.Action;
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
	int evaluateSituationQuality(int gamesNumber, Data config, String difficulty) {
		int nbVictories = 0;
		Data currentConfig, prevConfig;
		String[] playerNameList = (String[]) config.getPlayerNameList().toArray();
		PlayerAutomaton[] automatonList = new PlayerAutomaton[playerNameList.length];
		
		// Initializing the automata
		switch (difficulty) {
			case "Dumbest" :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Dumbest();
				}
				break;
			case "Traveler" :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Traveler();
				}
				break;
			default :
				throw new RuntimeException("Undefined difficulty");
		}
		for (int j = 0; j < automatonList.length; j++) {
			automatonList[j].setName(playerNameList[j]);
		}
			
		// Simulating the games
		for(int i = 0; i < gamesNumber; i++) {
			currentConfig = config.getClone(automaton.getName());
			String winnerName = null;
			// A game
			while(winnerName == null) {
				prevConfig = currentConfig.getClone(automaton.getName());
				// A round
				for (int j = 0; j < automatonList.length; j++) {
					// A player's turn
					Action action = automatonList[j].makeChoice(currentConfig.getHand(playerNameList[j]), currentConfig);
					if(action.isConstructing()) {
						// Building action
						currentConfig.setTile(action.positionTile1.x, action.positionTile1.y, action.tile1);
						if(action.action == Action.BUILD_DOUBLE) {
							currentConfig.setTile(action.positionTile2.x, action.positionTile2.y, action.tile2);
						}
					}
					else {
						// TODO Moving action
						System.out.println("Voyage pas encore implémenté");
					}
					
					if(currentConfig.isTrackCompleted(playerNameList[j])) {
						// A player has won => TODO (not counting the maiden travel)
						winnerName = playerNameList[j];
						break;
					}
				}
				// TODO what if nobody wins ?
				if(prevConfig.equals(currentConfig)) // nothing has been modified => the players are blocked
					break;
			}
			
			// Have we won ?
			if(automaton.getName().equals(winnerName)) nbVictories++;
		}
		
		return nbVictories;
	}
}