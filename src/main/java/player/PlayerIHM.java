package main.java.player;

import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.SwingUtilities;

import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.game.UnknownBoardNameException;
import main.java.util.NetworkTools;
import test.java.player.TestIHM;









@SuppressWarnings("serial")
public class PlayerIHM extends PlayerAbstract implements Runnable
{
	public static void main(String[] args)
	{
		try					{SwingUtilities.invokeLater(new PlayerIHM());}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}
	public void run()
	{
		new TestIHM(this);
	}

// --------------------------------------------
// Builder:
// --------------------------------------------
	public PlayerIHM() throws RemoteException
	{
		super();
	}
	/**=====================================================================
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 									(caught by IHM)
	 * @throws ExceptionUsedPlayerName 									    (caught by IHM)
	 =======================================================================*/
	public PlayerIHM(boolean isHost, String playerName, Color playerColor, GameInterface app, TestIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(isHost, playerName, playerColor, app, ihm);
	}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	/**=========================================================================
	 * @return Creates a new Game.  If gameCreation==true the application is launched
	 * @param gameCreation equals true if the player shelters a new game, and false if the player join an existing game
	 * @param applicationIP equals null if the player shelters the game
	 * @throws NotBoundException 		: The app IP or game name is wrong	(caught by IHM)
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws UnknownBoardNameException:									(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 
	 * @throws ExceptionUsedPlayerName
	 * 	 ===========================================================================*/
	public void launchPlayer(String playerName, String gameName, String boardName, int nbrBuildingInLine, Color playerColor, boolean gameCreation, String applicationIP) throws RemoteException, NotBoundException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, UnknownBoardNameException
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;

		if (gameCreation)														// App thread creation
		{
			Game game		= new Game(gameName, localIP, boardName, nbrBuildingInLine);
			Thread t		= new Thread(game);
			t.start();
			this.game		= Game.getRemoteGame(localIP, gameName);			// Remote application pointer
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);		// Remote application pointer
		new PlayerIHM(gameCreation, playerName, playerColor, game, ihm);		// Player Creation
	}

// --------------------------------------------
// Local methodes:
// --------------------------------------------

}