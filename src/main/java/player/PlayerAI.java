package main.java.player;

import java.rmi.Naming;
import java.rmi.RemoteException;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.automaton.Strongest;
import main.java.automaton.Traveler;
import main.java.automaton.Worker;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionNoPreviousGameToReach;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;
import main.java.util.NetworkTools;








@SuppressWarnings("serial")
public class PlayerAI extends PlayerAbstract implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private PlayerAutomaton	automaton;
	private Action			tmp2ndBuildSimple = null;

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
			case PlayerAutomaton.dumbestLvl:	this.automaton = new Dumbest(playerName);	break;
			case PlayerAutomaton.travelerLvl:	this.automaton = new Traveler(playerName);	break;
			case PlayerAutomaton.strongestLvl:	this.automaton = new Strongest(playerName);	break;
			case PlayerAutomaton.workerLvl:		this.automaton = new Worker(playerName);	break;
			default	:throw new RuntimeException("Undefined AI difficulty : " + iaLevel);
		}
		String playerIP = NetworkTools.myIPAddress();
		int playerPort	= NetworkTools.firstFreePort();
		try																				// Create the player's remote reference
		{
			String url = PlayerAbstract.getRemotePlayerURL(playerIP, playerPort, playerName);
			java.rmi.registry.LocateRegistry.createRegistry(playerPort);
			Naming.bind(url, this);
		}
		catch (Exception e) {e.printStackTrace(); throw new RemoteException();}
		super.game.onJoinGame(playerName, playerIP, playerPort, false, isHost, iaLevel);						// Log the player to the application
	}
	/**=======================================================================
	 * @return Creates a remote player cloned to the real player at the given ip
	 =========================================================================*/
	public static PlayerInterface getRemotePlayer(String playerIP, int playerPort, String playerName) throws RemoteException
	{
////	System.setSecurityManager(new RMISecurityManager());
		try
		{
			String url = PlayerAbstract.getRemotePlayerURL(playerIP, playerPort, playerName);
			return (PlayerInterface) Naming.lookup(url);
		}
		catch (Exception e) {e.printStackTrace(); throw new RemoteException();}
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public boolean isHumanPlayer()
	{
		return false;
	}
	public synchronized void rollBack() throws RemoteException, ExceptionForbiddenAction, ExceptionNotYourTurn, ExceptionNoPreviousGameToReach
	{
		throw new RuntimeException("The AI does not implement this method");
	}
	public synchronized void gameHasChanged(Data data) throws RemoteException
	{
		super.gameHasChanged(data);
		if (this.tmp2ndBuildSimple != null)
		{
			Action a = this.tmp2ndBuildSimple;
			this.tmp2ndBuildSimple = null;
			try					{super.placeTile(a.tile2, a.positionTile2);}
			catch (Exception e) {e.printStackTrace();}
			return;
		}
		if (!data.isGameStarted())			return;
		if (!data.isPlayerTurn(playerName)) return;
		if (data.getWinner() != null)		{System.out.println("The winner is: " + data.getWinner());	return;}
//		if (data.isGameBlocked(playerName))	{System.out.println("The game is blocked");					return;}

		if (data.hasRemainingAction(playerName))					// choix d'action
		{
			if(!data.isGameBlocked(playerName) )
			{
				Action a = this.automaton.makeChoice(data.getClone(playerName));
				if (a == null) {System.out.println("AI has no actions left"); return;}
				try
				{
					if ((a.isBUILD_SIMPLE()) || (a.isBUILD_AND_START_TRIP_NEXT_TURN()))		super.placeTile(a.tile1, a.positionTile1);
					else if (a.isTWO_BUILD_SIMPLE())										{this.tmp2ndBuildSimple = a;super.placeTile(a.tile1, a.positionTile1);}
					else if (a.isBUILD_DOUBLE())											super.replaceTwoTiles(a.tile1, a.tile2, a.positionTile1, a.positionTile2);
					else if (a.isMOVE())
					{
						super.moveTram(a.tramwayMovement, a.tramwayMovementSize, a.startTerminus);
					}
					else throw new RuntimeException("??????");

				}
				catch (Exception e) {e.printStackTrace(); return;}
				return;
			}
			else System.out.println(playerName + " BLOCKED");
// Pas de return: doit pouvoir piocher
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