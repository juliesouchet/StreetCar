package main.java.player;

import java.awt.Color;
import java.rmi.RemoteException;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.automaton.Traveler;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;





@SuppressWarnings("serial")
public class PlayerAI extends PlayerAbstract implements Runnable
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
// TODO: remove the ihm parameter
	public PlayerAI(String playerName, boolean isHost, Color playerColor, GameInterface app, int iaLevel, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(playerName, playerColor, app, ihm);
		switch (iaLevel)
		{
			case 1	:this.automaton	= new Dumbest(playerName);	break;
			case 2	:this.automaton = new Traveler(playerName);	break;
			default	:throw new RuntimeException("Undefined AI difficulty : " + iaLevel);
		}

		super.game.onJoinGame(this, isHost, iaLevel);						// Log the player to the application
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public boolean isHumanPlayer()
	{
		return false;
	}
	public synchronized void gameHasChanged(Data data) throws RemoteException
	{
		super.gameHasChanged(data);
		if (!data.isGameStarted())			return;
		if (!data.isPlayerTurn(playerName)) return;

		if (data.playerHasRemainingAction(playerName))
		{
			Action a = this.automaton.makeChoice(data.getClone(playerName));
			try					{super.placeTile(a.tile1, a.positionTile1);}
			catch (Exception e) {e.printStackTrace(); System.exit(0);}
			return;
		}

		int nbrCards = data.getPlayerRemainingCardsToDraw(playerName);
		if (nbrCards > 0)
		{
			try					{super.drawTile(nbrCards);}
			catch (Exception e) {e.printStackTrace(); System.exit(0);}
			return;
		}
		else
		{
			try					{super.validate();}
			catch (Exception e) {e.printStackTrace(); System.exit(0);}
		}
	}
	public synchronized void excludePlayer() throws RemoteException
	{
		if (super.ihm != null)	super.ihm.excludePlayer();
	}

// --------------------------------------------
// Local methods
// --------------------------------------------
	public void run()
	{
		
	}
}