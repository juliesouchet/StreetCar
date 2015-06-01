package main.java.automaton;

import main.java.data.Action;
import main.java.data.Data;

public class Evaluator {
	
	/**
	 * Simulates gamesNumber games between the player of the game starting with currentConfig. 
	 * The players are modeled by automata of the given difficulty 
	 * @param gamesNumber
	 * @param currentConfig
	 * @param difficulty
	 * @return
	 */
	static double evaluateSituationQuality(String playerName, int gamesNumber, Data config, String difficulty) {
		double victoryProportion = 0;
		int victoriesNumber = 0;
		boolean win;
		Data currentConfig;
		String[] playerNameList = (String[]) config.getPlayerNameList().toArray();
		PlayerAutomaton[] automatonList = new PlayerAutomaton[playerNameList.length];
		
		// Initializing the automata
		switch (difficulty) {
			case "Dumbest" :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Dumbest(playerNameList[j]);
				}
				break;
			case "Traveler" :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Traveler(playerNameList[j]);
				}
				break;
			default :
				throw new RuntimeException("Undefined difficulty");
		}
		
		// Simulating the games
		//for(int i = 0; i < gamesNumber; i++) {
			currentConfig = config.getClone(playerName);
			
			// A game
			win = false;
			while(!win) {
				// A round
				for (int j = 0; j < automatonList.length; j++) {
					// A player's turn
					String currentPlayerName = playerNameList[j];
					Action action = automatonList[j].makeChoice(currentConfig);
					if(action.isConstructing()) {
						// Building action
						currentConfig.setTile(action.positionTile1.x, action.positionTile1.y, action.tile1);
						if(action.action == Action.BUILD_DOUBLE) {
							currentConfig.setTile(action.positionTile2.x, action.positionTile2.y, action.tile2);
						}
					}
					else {
						if(!currentConfig.hasStartedMaidenTravel(currentPlayerName)
							&& action.action != Action.START_TRIP_NEXT_TURN)
							throw new RuntimeException(currentPlayerName+" tries to travel while in construction (round "+j+")");
						
						if(action.action == Action.START_TRIP_NEXT_TURN)
							currentConfig.startMaidenTravel(currentPlayerName);
						
						else // action.action == Action.MOVE
							currentConfig.setTramPosition(currentPlayerName, action.tramwayMovement[action.tramwayMovement.length-1]);
					}
					
					if(currentConfig.isTrackCompleted(currentPlayerName)) {
						// A player has won => TODO (not counting the maiden travel)
						win = playerName.equals(currentPlayerName);
						break;
					}
				}
				// TODO what if nobody wins ?
				/*if(currentConfig.isEverybodyBlocked()) {
					win = false;
					break;
				}*/
			}
			
			// Have we won ?
			if(win) victoriesNumber++;
		//}
		
		victoryProportion = (double)victoriesNumber / (double)gamesNumber;
		return victoryProportion;
	}
}
