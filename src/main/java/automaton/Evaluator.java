package main.java.automaton;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.game.Game;

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
		String[] playerNameList = new String[config.getPlayerNameList().size()];
		int x = 0;
		for(String name : config.getPlayerNameList()) {
			playerNameList[x] = name;
			x++;
		}
/*		
	System.out.println("Initialisation des " + playerNameList.length + " joueurs : ");
	for (int j = 0; j < playerNameList.length; j++) {
		System.out.println((String) playerNameList[j]);
	}
*/	
	
		
		// Simulating the games
		for(int i = 0; i < nbrGamesSimulated; i++) {
			// Local version :
			/*
			Data data;
			data = config.getClone(playerName);
			win = localGameSimulation(playerNameList, data, playerName, aiLvl);
			*/
			
			// Remote version :
			Game game = null;			
			for(int j = 0; j < LoginInfo.initialLoginTable.length; j++) { // Game setup
				if(j<playerNameList.length) {					
					boolean isClosed, isHost;
					if(j<playerNameList.length) {
						isHost = j==0;
						isClosed = false;
					}
					else {
						isHost = false;
						isClosed = true;
					}
					LoginInfo.initialLoginTable[j] = new LoginInfo(isClosed, playerNameList[j], isHost, false, aiLvl);
				}
			}			
			try {
				game = new Game("TestEvaluator", "localhost", "newOrleans", 2);
			} catch (Exception e) {
				System.out.println("Game creation error"); e.printStackTrace();
			}			
			String hostName = game.getHostName();			
			try {
				game.hostStartGame(hostName);
			} catch (Exception e) {
				System.out.println("Game start error"); e.printStackTrace();
			}
			
			
			// Have we won ?
			if(win) victoriesNumber++;
		}
		
		victoryProportion = (double)victoriesNumber / (double)nbrGamesSimulated;
		return victoryProportion;
	}
	
	
	
	
	@SuppressWarnings("unused")
	private static boolean localGameSimulation(String[] playerNameList, Data data, String playerName, int aiLvl) {
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
	System.out.println("=======================================");
	System.out.println("Tour " + round + " | " + currentPlayerName);
				Action action = automatonList[j].makeChoice(data);
				
				if(action.isConstructing()) {
	System.out.println(currentPlayerName + " BUILD");
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
						&& action.action != Action.START_TRIP_NEXT_TURN)
						throw new RuntimeException(currentPlayerName+" tries to travel while in construction (round "+j+")");
					
					if(action.action == Action.START_TRIP_NEXT_TURN) {
	System.out.println(currentPlayerName + " START_TRIP_NEXT_TURN");
						data.startMaidenTravel(currentPlayerName);
					}
					
					else { // action.action == Action.MOVE
	System.out.println(currentPlayerName + " MOVE");
						data.setTramPosition(currentPlayerName, action.tramwayMovement[action.tramwayMovement.length-1]);
					}
				}
				
				if(data.isTrackCompleted(currentPlayerName)) {
					// A player has won => TODO (not counting the maiden travel)
					win = playerName.equals(currentPlayerName);
	System.out.println("Winner : " + currentPlayerName);
					break;
				}
				
	System.out.println("=======================================");
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
