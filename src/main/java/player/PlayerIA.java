package main.java.player;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;





@SuppressWarnings("serial")
public class PlayerIA extends PlayerAbstract implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private PlayerAutomaton	automaton;
	
// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=====================================================================
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline	(caught by IHM)
	 * @throws ExceptionFullParty									(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 							(caught by IHM)
	 * @throws ExceptionUsedPlayerName 								(caught by IHM)
	 =======================================================================*/
	public PlayerIA(String playerName, Color playerColor, GameInterface app, int iaLevel) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(playerName, playerColor, app, null);
		this.automaton	= new Dumbest();
		super.game.onJoinGame(this, false);						// Log the player to the application
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public boolean isHumanPlayer() throws RemoteException
	{
		return false;
	}
	public void gameHasChanged(Data data) throws RemoteException
	{
		if (!data.isPlayerTurn(playerName)) return;

		Hand myHand = data.getHand(playerName);
		Action a = this.automaton.makeChoice(myHand, data);
		try {this.game.placeTile(playerName, a.tile1, a.positionTile1);}
		catch (Exception e) {e.printStackTrace(); System.exit(0);}
	}

// --------------------------------------------
// Local methods
// --------------------------------------------
	public void run()
	{
		
	}
}