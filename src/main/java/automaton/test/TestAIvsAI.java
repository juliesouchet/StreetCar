package main.java.automaton.test;

import main.java.automaton.PlayerAutomaton;
import main.java.data.LoginInfo;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.Game;

public class TestAIvsAI {
	
	public static void main(String[] args) throws ExceptionGameHasAlreadyStarted {
		Game game = null;
		
		// Game setup
		LoginInfo[] initialLoginTable = 
				// LoginInfo(isClosed, playerName, isHost, isHuman, iaLevel)
				{
				new LoginInfo(false,	"1",	true,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(false,	"2",	false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(false,	"3",	false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl)
				};
		for(int i = 0; i < initialLoginTable.length; i++) {
			LoginInfo.initialLoginTable[i] = initialLoginTable[i];
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
		
		
	}
}
