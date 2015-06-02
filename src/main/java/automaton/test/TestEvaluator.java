package main.java.automaton.test;

import java.rmi.RemoteException;

import main.java.automaton.Evaluator;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.game.Game;


public class TestEvaluator {

	public static void main(String[] args) {
		String boardName = "src/test/resources/boards/test";
		int nbrBuildingInLine = 2;
		Data data = null;
		String gameName = "Simulation";
		int nbrGamesSimulated  = 1;
		
		/*================*
		 *	Game setup
		 *================*/
		Game game = null;
		LoginInfo[] initialLoginTable = 
				// LoginInfo(isClosed, playerName, isHost, isHuman, iaLevel)
				{
				new LoginInfo(false,	null,	true,	false,	PlayerAutomaton.travelerLvl),
				new LoginInfo(false,	null,	false,	false,	PlayerAutomaton.travelerLvl),
				new LoginInfo(false,	null,	false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl)
				};
		for(int i = 0; i < initialLoginTable.length; i++) {
			LoginInfo.initialLoginTable[i] = initialLoginTable[i];
		}
		Data.boardDirectory = "";
		try {
			game = new Game(gameName, "localhost", boardName, nbrBuildingInLine);
		} catch (Exception e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}
		
		String playerName = game.getHostName();
		try {
			data = game.getData(playerName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		/*================*
		 *	Simulation(s)
		 *================*/
		
		nbrGamesSimulated = 1;
		double victoryProb = Evaluator.evaluateSituationQuality(playerName, nbrGamesSimulated, data.getClone(playerName), PlayerAutomaton.dumbestLvl);
		
		
		/*=========================*
		 *	Displaying the result
		 *=========================*/
		
		System.out.println("=========================");
		System.out.println("=========================");
		System.out.println(playerName + " a une chance de gagner de " + victoryProb + "%");		
		System.out.println("=========================");
		System.out.println("=========================");
		
	}
}
