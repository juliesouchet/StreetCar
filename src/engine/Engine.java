package engine;

import game.Player;

import java.util.ArrayList;

public class Engine {

	private ArrayList<Player> players;

	public Engine() { }

	public void newGame() {
		players = new ArrayList<Player>();
	}
	
	private void beginGame() {
		while(!gameIsFinished()) {
			// TODO game
		}
	}

	private boolean gameIsFinished() {
		return false; // TODO
	}
}
