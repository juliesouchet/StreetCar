package main.java.automaton;

import java.io.FileWriter;

import main.java.data.Action;
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
		boolean win = false;
		
		// Simulating the games			
		GameSimulation game = null;
		try {
			String boardName = Data.boardDirectory + "tmp";
			FileWriter fw = new FileWriter(boardName);
			config.writeBoardInFile(fw);
			fw.close();
			game = GameSimulation.newGameSimulation(config.getClone(playerName), boardName, aiLvl);
		} catch (Exception e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}
		
		for(int i = 0; i < nbrGamesSimulated; i++) {
			
			win = game.isWinner(playerName);
			
			// Have we won ?
			if(win) victoriesNumber++;
		}
		
		victoryProportion = (double)victoriesNumber / (double)nbrGamesSimulated;
		return victoryProportion;
	}
	
	
	
	
	@SuppressWarnings("unused")
	private static boolean localGameSimulation(String[] playerNameList, Data data, String playerName, int aiLvl) throws ExceptionUnknownNodeType {
		PlayerAutomaton[] automatonList = new PlayerAutomaton[playerNameList.length];
		
		// Initializing the automata
		switch (aiLvl) {
			case PlayerAutomaton.dumbestLvl :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Dumbest((String) playerNameList[j]);
				}
				break;
			case PlayerAutomaton.travelerLvl :
				for (int j = 0; j < automatonList.length; j++) {
					automatonList[j] = new Traveler((String) playerNameList[j]);
				}
				break;
			default :
				throw new RuntimeException("Undefined difficulty");
		}		
		
		// A game
		int round = 0;
		boolean win = false;
		while(!win) {
			// A round
			for (int j = 0; j < automatonList.length; j++) {
				// A player's turn
				String currentPlayerName = playerNameList[j];
				Action action = null;
				action = automatonList[j].makeChoice(data);
				
				if(action.isConstructing()) {
					// Building action
					int cardsToDraw = 0;
					
					if(data.getTile(action.positionTile1).isEmpty()) cardsToDraw++;
					data.placeTile(currentPlayerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
					
					if(action.action == Action.BUILD_DOUBLE) {
						if(data.getTile(action.positionTile2).isEmpty()) cardsToDraw++;
						data.placeTile(currentPlayerName, action.positionTile2.x, action.positionTile2.y, action.tile2);
					}
					
					data.drawTile(currentPlayerName, cardsToDraw);
				}
				
				else { // MOVE action
					if(!data.hasStartedMaidenTravel(currentPlayerName)
						&& action.action != Action.BUILD_AND_START_TRIP_NEXT_TURN)
						throw new RuntimeException(currentPlayerName+" tries to travel while in construction (round "+j+")");
					
					if(action.action == Action.BUILD_AND_START_TRIP_NEXT_TURN) {
						data.startMaidenTravel(currentPlayerName);
					}
					
					else { // action.action == Action.MOVE
						data.setTramPosition(currentPlayerName, action.tramwayMovement[action.tramwayMovement.length-1]);
					}
				}
				
				if(data.isTrackCompleted(currentPlayerName)) {
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
			round++;
		}
		
		return win;
	}
	
}
