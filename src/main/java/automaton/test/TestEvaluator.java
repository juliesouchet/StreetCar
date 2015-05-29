package main.java.automaton.test;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.player.PlayerAI;

public class TestEvaluator {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Game game = null;
		PlayerAI player1 = null, player2 = null;
		
		try {
			game = new Game("TestEvaluator", "localhost", "newOrleans", 2);
		} catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}
		
		
		try {
			player1 = new PlayerAI("Player 1", true, Color.red, game, 1, null);
			player2 = new PlayerAI("Player 2", false, Color.blue, game, 1, null);
		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName
				| ExceptionUsedPlayerColor e) {
			System.out.println("Player creation error"); e.printStackTrace();
		}
		
		
		try {
			game.hostStartGame("Player 1");
		} catch (RemoteException | ExceptionForbiddenAction e) {
			System.out.println("Game start error"); e.printStackTrace();
		}
		
		
	}
}
