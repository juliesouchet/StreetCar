package main.java.engine.player;

import java.awt.Color;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.SwingUtilities;

import main.java.engine.game.ExceptionFullParty;
import main.java.engine.game.ExceptionUsedPlayerColor;
import main.java.engine.game.ExceptionUsedPlayerName;
import main.java.engine.game.Game;
import main.java.engine.game.GameInterface;
import main.java.engine.util.NetworkTools;
import test.java.engine.TestIHM;






public class Main implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private GameInterface		game;					// Apps attributes: Are established if the player hosts the application

	private TestIHM				ihm;					// Player attributes
	@SuppressWarnings("unused")
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
	 * @throws ExceptionUsedPlayerColor 
	 * @throws ExceptionUsedPlayerName
	 * 	 ===========================================================================*/
	public void newGame(String playerName, String gameName, Color playerColor, boolean gameCreation, String applicationIP) throws RemoteException, NotBoundException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;

		if (gameCreation)														// App thread creation
		{
			this.game		= new Game(gameName, localIP);
			SwingUtilities	.invokeLater((Game)this.game);
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);		// Remote application pointer

		this.player = new PlayerTest(playerName, playerColor, game, ihm);		// Player Creation
	}
}