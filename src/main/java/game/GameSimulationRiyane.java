package main.java.game;

import main.java.automaton.PlayerAutomaton;
import main.java.data.Data;






public class GameSimulationRiyane
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Data				data;
	private PlayerAutomaton[]	players;

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**======================================================================
	 * @return Create a game simulator tha begins at the given round of data</br>
	 * @param The given players must be ordered according the current round</br>
	 =======================================================================*/
	public GameSimulationRiyane(Data data, PlayerAutomaton[] players)
	{
		this.data		= data;
		this.players	= players;
	}

// --------------------------------------------
// LocalMethode:
// --------------------------------------------
	/**=========================================================================
	 * @return Start the simulation from the given round od data.
	 * The function returns the name of the winner, or null if the game is blocked
	 ===========================================================================*/
	public String simulate()
	{
		int nbrDoneActions = 0;

	
// TODO
		for (int i=0; i<nbrDoneActions; i++) this.data.rollBack();
		return null;
	}

}