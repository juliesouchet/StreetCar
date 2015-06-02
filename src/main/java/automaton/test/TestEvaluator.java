package main.java.automaton.test;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.Game;
import main.java.player.PlayerAI;
import test.java.player.TestDumbestIHM;

public class TestEvaluator {

	public static void main(String[] args) throws ExceptionGameHasAlreadyStarted {
		Game game = null;
		PlayerAI player1 = null,
				player2 = null;
		String dumbestName = "Dumbest", travelerName = "Traveler";
		int dumbest = 1, traveler = 2;
		String name1, name2;
		int level1,	level2;
		
		name1 = dumbestName + " 1";
		name2 = travelerName + " 2";
		level1 = dumbest;
		level2 = traveler;
		
		try {
			game = new Game("TestEvaluator", "localhost", "newOrleans", 2);
		} catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}

		TestDumbestIHM ihm = new TestDumbestIHM();
		try {
			player1 = new PlayerAI(name1, true, game, level1, ihm);
			player2 = new PlayerAI(name2, false, game, level2, ihm);
			player1.setPlayerColor(Color.red);
			player2.setPlayerColor(Color.green);
		}
		catch (Exception e) {
			System.out.println("Player creation error"); e.printStackTrace();
		}
		
		
		try {
			game.hostStartGame(name1);
		} catch (Exception e) {
			System.out.println("Game start error"); e.printStackTrace();
		}
		
		
	}
}
