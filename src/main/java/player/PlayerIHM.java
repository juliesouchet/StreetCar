package main.java.player;

import java.awt.Color;
import java.net.UnknownHostException;
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
	public static void main(String[] args) throws RemoteException, UnknownHostException
	{
		SwingUtilities.invokeLater(new PlayerIHM());
	}
	public void run()
	{
		new TestIHM(this);
	}

// --------------------------------------------
// Attributs:
// --------------------------------------------



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
	public PlayerIHM(String playerName, Color playerColor, GameInterface app, TestIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(playerName, playerColor, app, ihm);
	}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String getName()	throws RemoteException	{return super.name;}
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
			this.game		= new Game(gameName, localIP, boardName, nbrBuildingInLine);
			SwingUtilities	.invokeLater((Game)this.game);
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);		// Remote application pointer

		new PlayerIHM(playerName, playerColor, game, ihm);						// Player Creation
	}



// --------------------------------------------
// Local methodes:
// --------------------------------------------

}