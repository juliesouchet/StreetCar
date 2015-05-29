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
import main.java.rubbish.InterfaceIHM;

public class TestEvaluator {

	public static void main(String[] args) {
		Game game = null;
		PlayerAI player1 = null, player2 = null;
		InterfaceIHM ihm = null;
		
		try {
			game = new Game("TestEvaluator", "localhost", "newOrleans", 2);
		} catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) {
			e.printStackTrace(); System.out.println("Game creation error");
		}
		
		try {
			player1 = new PlayerAI("Player 1", true, Color.red, game, 1, ihm);
			player2 = new PlayerAI("Player 2", false, Color.blue, game, 1, ihm);
		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName
				| ExceptionUsedPlayerColor e) {
			e.printStackTrace(); System.out.println("Player creation error");
		}
		
		
		//System.out.println("Game initialisation OK");
		
		try {
			game.hostStartGame("Player 1");
		} catch (RemoteException | ExceptionForbiddenAction e) {
			e.printStackTrace();
		}
	}

}
