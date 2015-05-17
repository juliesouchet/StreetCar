package player;

import ihm.TestIHM;

import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.SwingUtilities;

import util.NetworkTools;
import application.Game;
import application.FullPartyException;
import application.InterfaceGame;





public class Main implements Runnable
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private InterfaceGame		game;					// Apps attributs: Are established if the player hosts the application

	private TestIHM					ihm;					// Player attributs
	private InterfacePlayer		player;

// --------------------------------------------
// Local methodes:
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
	 * @throws FullPartyException											(caught by IHM)
	 ===========================================================================*/
	public void newGame(String playerName, String gameName, boolean gameCreation, String applicationIP) throws RemoteException, NotBoundException, FullPartyException
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;

		if (gameCreation)															// App thread creation
		{
			this.game		= new Game(gameName, localIP);
			SwingUtilities	.invokeLater((Game)this.game);
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);	// Remote application pointer

		this.player = new TestPlayer(gameName, playerName, localIP, game, ihm);		// Player Creation
	}
}