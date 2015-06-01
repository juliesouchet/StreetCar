package main.java.automaton.test;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.Game;
import main.java.player.PlayerAI;
import test.java.player.TestDumbestIHM;

public class TestEvaluator {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws ExceptionGameHasAlreadyStarted {
		Game game = null;
		PlayerAI player1 = null,
				player2 = null;
		String name1 = "Dumbest 1",
				name2 = "Traveler 2";
		int level1 = 1, // 1 = dumbest
			level2 = 1; // 2 = traveler
		
		try {
			game = new Game("TestEvaluator", "localhost", "newOrleans", 2);
		} catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) {
			System.out.println("Game creation error"); e.printStackTrace();
		}

		TestDumbestIHM ihm = new TestDumbestIHM();
		try {
			player1 = new PlayerAI(name1, true, Color.red, game, level1, ihm);
			player2 = new PlayerAI(name2, false, Color.blue, game, level2, ihm);
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
