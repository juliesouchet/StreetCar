package main.java.player;

import java.rmi.RemoteException;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.automaton.Strongest;
import main.java.automaton.Traveler;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
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
	 * @throws ExceptionGameHasAlreadyStarted						(caught by IHM)
	 =======================================================================*/
	public PlayerAI(String playerName, boolean isHost, GameInterface app, int iaLevel, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionGameHasAlreadyStarted
	{
		super(playerName, app, ihm);
		switch (iaLevel)
		{
			case PlayerAutomaton.dumbestLvl	:this.automaton	= new Dumbest(playerName);	break;
			case PlayerAutomaton.travelerLvl	:this.automaton = new Traveler(playerName);	break;
			case PlayerAutomaton.strongestLvl	:this.automaton = new Strongest(playerName);	break;
			default	:throw new RuntimeException("Undefined AI difficulty : " + iaLevel);
		}
		super.game.onJoinGame(this, false, isHost, iaLevel);						// Log the player to the application
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
		if (data.getWinner() != null)		{System.out.println("The winner is: " + data.getWinner());	return;}
		if (data.isGameBlocked(playerName))			{System.out.println("The game is blocked");					return;}

		if (data.hasRemainingAction(playerName))					// choix d'action
		{
			if(data.getHandSize(playerName)>0 || data.hasStartedMaidenTravel(playerName) )
			{
				Action a = this.automaton.makeChoice(data.getClone(playerName));
				if (a == null) {System.out.println("AI has no actions left"); return;}
				try
				{
					if ((a.isBUILD_SIMPLE()) || (a.isBUILD_AND_START_TRIP_NEXT_TURN()))		super.placeTile(a.tile1, a.positionTile1);
					else if (a.isTWO_BUILD_SIMPLE())										{super.placeTile(a.tile1, a.positionTile1); super.placeTile(a.tile2, a.positionTile2);}
					else if (a.isBUILD_DOUBLE())											super.replaceTwoTiles(a.tile1, a.tile2, a.positionTile1, a.positionTile2);
					else if (a.isMOVE())
					{
						if (a.startTerminus != null)	super.startMaidenTravel(playerName, a.startTerminus);
						super.moveTram(a.tramwayMovement, a.ptrTramwayMovement);
					}
					else throw new RuntimeException("Unknown action : " + a);

				}
				catch (Exception e) {e.printStackTrace(); return;}
			}
			else System.out.println(playerName + " BLOCKED");
			return;
		}

		int nbrCards = data.getPlayerRemainingTilesToDraw(playerName);
		if (nbrCards > 0 && data.getNbrRemainingDeckTile() > 0)		// pioche
		{
			try
			{
				if (!data.isEnougthTileInDeck(nbrCards))
				{
					nbrCards = data.getNbrRemainingDeckTile();
				}
				super.drawTile(nbrCards);
			}
			catch (Exception e) {e.printStackTrace(); return;}
			return;
		}
		else														// fin de tour
		{
				try			{super.validate();}
				catch (Exception e) {e.printStackTrace(); return;}
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
