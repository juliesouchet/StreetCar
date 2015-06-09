package main.java.automaton.test;

import main.java.automaton.PlayerAutomaton;
import main.java.data.LoginInfo;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.Game;
import main.java.util.TraceDebugAutomate;

public class TestAIvsAI {
	
	public static void main(String[] args) throws ExceptionGameHasAlreadyStarted {
		Game game = null;
		
		// Game setup
		LoginInfo[] initialLoginTable = 
				// LoginInfo(isClosed, playerName, isHost, isHuman, iaLevel)
				{
				new LoginInfo(false,	null,		true,	false,	PlayerAutomaton.strongestLvl),
				new LoginInfo(false,	null,		false,	false,	PlayerAutomaton.travelerLvl),
				new LoginInfo(true,		null,		false,	false,	PlayerAutomaton.strongestLvl),
				new LoginInfo(true,		null,		false,	false,	PlayerAutomaton.dumbestLvl),
				new LoginInfo(true,		null,		false,	false,	PlayerAutomaton.dumbestLvl)
				};
		for(int i = 0; i < initialLoginTable.length; i++) {
			LoginInfo.initialLoginTable[i] = initialLoginTable[i];
		}
		
		try {
			game = new Game("TestEvaluator", "localhost", "quasi_full", 2);
		} catch (Exception e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}
		
		String hostName = game.getTestHostName();
		
		//TraceDebugAutomate.dumbestTrace = true;
		//TraceDebugAutomate.travelerTrace = true;
		TraceDebugAutomate.decisionTableTrace = true;
		TraceDebugAutomate.decisionNodeTrace = true;
		
		try {
			game.hostStartGame(hostName);
		} catch (Exception e) {
			System.out.println("Game start error"); e.printStackTrace();
		}
		
		
	}
}
