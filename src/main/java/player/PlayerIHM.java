package main.java.player;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionNoPreviousGameToReach;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;
import main.java.util.NetworkTools;









@SuppressWarnings("serial")
public class PlayerIHM extends PlayerAbstract
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=========================================================================
	 * @return Creates a new Game.  If gameCreation==true the application is launched
	 * @param gameCreation equals true if the player shelters a new game, and false if the player join an existing game
	 * @param applicationIP equals null if the player shelters the game
	 * @throws NotBoundException 		: The app IP or game name is wrong	(caught by IHM)
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUnknownBoardName:									(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 
	 * @throws ExceptionUsedPlayerName
	 * @throws ExceptionHostAlreadyExists
	 * @throws ExceptionGameHasAlreadyStarted								(caught by IHM)
 	 ===========================================================================*/
	public static PlayerIHM launchPlayer(String playerName, String gameName, String boardName, int nbrBuildingInLine, boolean gameCreation, String applicationIP, InterfaceIHM ihm) throws RemoteException, NotBoundException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionUnknownBoardName, ExceptionHostAlreadyExists, ExceptionGameHasAlreadyStarted
	{
		String localIP = NetworkTools.myIPAddress();
		GameInterface game;

		if (gameCreation)															// App thread creation
		{
			Game gameT	= new Game(gameName, localIP, boardName, nbrBuildingInLine);
			Thread t	= new Thread(gameT);
			t.start();
			game		= Game.getRemoteGame(localIP, gameName);					// Remote application pointer
		}
		else	game	= Game.getRemoteGame(applicationIP, gameName);				// Remote application pointer

		return new PlayerIHM(gameCreation, playerName, game, ihm);					// Player Creation
	}
	/**=====================================================================
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 									(caught by IHM)
	 * @throws ExceptionUsedPlayerName 									    (caught by IHM)
	 * @throws ExceptionGameHasAlreadyStarted								(caught by IHM)
	 =======================================================================*/
	private PlayerIHM(boolean isHost, String playerName, GameInterface app, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionGameHasAlreadyStarted
	{
		super(playerName, app, ihm);
		String playerIP = NetworkTools.myIPAddress();
		int playerPort	= NetworkTools.firstFreePort();
		try																				// Create the player's remote reference
		{
			String url = PlayerAbstract.getRemotePlayerURL(playerIP, playerPort, playerName);
			java.rmi.registry.LocateRegistry.createRegistry(playerPort);
			Naming.bind(url, this);
		}
		catch (Exception e) {e.printStackTrace(); throw new RemoteException();}
		super.game.onJoinGame(playerName, playerIP, playerPort, true, isHost, -1);								// Log the player to the application
	}
	/**=======================================================================
	 * @return Creates a remote player cloned to the real player at the given ip
	 =========================================================================*/
	public static PlayerInterface getRemotePlayer(String playerIP, int playerPort, String playerName) throws RemoteException
	{
////	System.setSecurityManager(new RMISecurityManager());
		String url = null;
		try
		{
			url = PlayerAbstract.getRemotePlayerURL(playerIP, playerPort, playerName);
			return (PlayerInterface) Naming.lookup(url);
		}
		catch (Exception e) {System.out.println("URL = " + url);e.printStackTrace(); throw new RemoteException();}
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public synchronized void gameHasChanged(Data data) throws RemoteException
	{
		super.gameHasChanged(data);
	}
	public synchronized void excludePlayer() throws RemoteException
	{
		if (super.ihm != null)	super.ihm.excludePlayer();
	}
	public synchronized void rollBack() throws RemoteException, ExceptionForbiddenAction, ExceptionNotYourTurn, ExceptionNoPreviousGameToReach
	{
		super.game.rollBack(playerName);
	}

// --------------------------------------------
// Local methods
// --------------------------------------------
	public boolean isHumanPlayer(){return true;}
}