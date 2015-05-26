package main.java.player;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import main.java.ia.Dumbest;
import main.java.ia.PlayerAutomaton;





@SuppressWarnings("serial")
public class PlayerIA extends PlayerAbstract
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
		super(false, playerName, playerColor, app, null);
		this.automaton	= new Dumbest();
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
		if (data.isPlayerTurn(playerName));
	}
}