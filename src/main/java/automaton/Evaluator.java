package main.java.automaton;

import main.java.data.Data;
import main.java.game.GameSimulation;






public class Evaluator
{
	/**=======================================================================================
	 * @return The victory proportion of the given player.  The game is simulated nbrGamesSimulated.
	 * All the data players are simulated, starting with currentConfig.</br>
	 * The players are modeled by automata of the same given difficulty.</br>
	 =========================================================================================*/
	public static double evaluateSituationQuality(String playerName, int nbrGamesSimulated, Data data, int aiLevel)
	{
		GameSimulation simulator = new GameSimulation(data, aiLevel);
		double victoryProportion = 0;
		int victoriesNumber = 0;
		String winner;

		for(int i = 0; i < nbrGamesSimulated; i++)
		{
			winner = simulator.simulate();
			if ((winner != null) && (winner.equals(playerName))) victoriesNumber++;
System.out.println("Simulation " + i + "\tThe winner is: " + winner);
		}
		victoryProportion = (double)victoriesNumber / (double)nbrGamesSimulated;
		return victoryProportion;
	}
}