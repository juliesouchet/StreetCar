package player;

import game.FullPartyException;
import game.Game;
import game.InterfaceGame;
import game.UsedPlayerColorException;
import game.UsedPlayerNameException;
import ihm.TestIHM;

import java.awt.Color;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.SwingUtilities;

import util.NetworkTools;





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
	 * @throws UsedPlayerColorException 
	 * @throws UsedPlayerNameException
	 * 	 ===========================================================================*/
	public void newGame(String playerName, String gameName, Color playerColor, boolean gameCreation, String applicationIP) throws RemoteException, NotBoundException, FullPartyException, UsedPlayerNameException, UsedPlayerColorException
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;

		if (gameCreation)														// App thread creation
		{
			this.game		= new Game(gameName, localIP);
			SwingUtilities	.invokeLater((Game)this.game);
		}
		else	this.game	= Game.getRemoteGame(applicationIP, gameName);		// Remote application pointer

		this.player = new TestPlayer(playerName, playerColor, game, ihm);		// Player Creation
	}
}