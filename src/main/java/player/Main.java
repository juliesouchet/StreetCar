package main.java.player;

import java.awt.Color;
import java.io.FileNotFoundException;
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






public class Main implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private GameInterface		game;					// Apps attributes: Are established if the player hosts the application

	private TestIHM				ihm;					// Player attributes
	private PlayerInterface		player;

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public static void main(String[] args) throws RemoteException, UnknownHostException
	{
		SwingUtilities.invokeLater(new Main());
	}
	public void run()
	{
		this.ihm = new TestIHM(this);
	}
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
	public void newGame(String playerName, String gameName, String boardName, Color playerColor, boolean gameCreation, String applicationIP) throws RemoteException, NotBoundException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, UnknownBoardNameException
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;

		if (gameCreation)														// App thread creation
		{
			this.game		= new Game(gameName, localIP, boardName);
			SwingUtilities	.invokeLater((Game)this.game);
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);		// Remote application pointer

		this.player = new PlayerTest(playerName, playerColor, game, ihm);		// Player Creation
	}
}