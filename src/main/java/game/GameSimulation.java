package main.java.game;

import java.util.HashMap;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.automaton.Strongest;
import main.java.automaton.Traveler;
import main.java.data.Action;
import main.java.data.Data;






public class GameSimulation
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Data								data;
	private HashMap<String, PlayerAutomaton>	players;

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**======================================================================
	 * @return Create a game simulator tha begins at the given round of data.</br>
	 =======================================================================*/
	public GameSimulation(Data data, int aiLevel)
	{
		PlayerAutomaton automaton;

		this.data		= data;
		this.players	= new HashMap<String, PlayerAutomaton>();

		for (String playerName: data.getPlayerNameList())
		{
			switch (aiLevel)
			{
				case PlayerAutomaton.dumbestLvl:	automaton = new Dumbest(playerName);	break;
				case PlayerAutomaton.travelerLvl:	automaton = new Traveler(playerName);	break;
				case PlayerAutomaton.strongestLvl:	automaton = new Strongest(playerName);	break;
				default	:throw new RuntimeException("Undefined AI difficulty : " + aiLevel);
			}
			this.players.put(playerName, automaton);
		}
	}

// --------------------------------------------
// LocalMethode:
// --------------------------------------------
	/**=========================================================================
	 * @return Start the simulation from the given round of data.</br>
	 * The function returns the name of the winner, or null if the game is blocked.</br>
	 ===========================================================================*/
	public String simulate()
	{
//TODO to remove;
//Data tmpData = this.data.getClone(null);
		String			winner			= this.data.getWinner();
		String			playerTurn		= this.data.getPlayerTurn();
		PlayerAutomaton	player			= this.players.get(playerTurn);
		int				nbrDoneActions	= 0;
		int				nbrEmptyActions	= 0;
		int				nbrPlayers		= this.players.size();
		Action action;
		int nbrCards;

		while ((winner == null) && (nbrEmptyActions < nbrPlayers))
		{
			if (data.isGameBlocked(playerTurn))
			{
				nbrCards = data.getPlayerRemainingTilesToDraw(playerTurn);
				if (nbrCards<=data.getNbrRemainingDeckTile()) data.drawTile(playerTurn, nbrCards);
				data.skipTurn();
				playerTurn	= this.data.getPlayerTurn();
				player		= this.players.get(playerTurn);
				nbrEmptyActions ++;
				continue;
			}
			nbrEmptyActions = 0;
			action = player.makeChoice(this.data);
			this.data.doAction(playerTurn, action);
			if (!data.hasRemainingAction(playerTurn))
			{
				nbrCards = data.getPlayerRemainingTilesToDraw(playerTurn);
				if (nbrCards<=data.getNbrRemainingDeckTile()) data.drawTile(playerTurn, nbrCards);
				data.skipTurn();
			}
			nbrDoneActions	++;
			playerTurn	= this.data.getPlayerTurn();
			player		= this.players.get(playerTurn);
			winner		= data.getWinner();
		}
//this.data = tmpData;
		for (int i=0; i<nbrDoneActions; i++) this.data.rollBack();
		return winner;
	}
}