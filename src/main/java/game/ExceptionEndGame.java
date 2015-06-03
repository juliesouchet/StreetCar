package main.java.game;

public class ExceptionEndGame extends Exception {
	private static final long serialVersionUID = 3879911202438427100L;
	String winner;

	public ExceptionEndGame(String winner) {
		this.winner = winner;
	}
	
	public String getWinner() {
		return winner;
	}
}
