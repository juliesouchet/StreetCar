package player;

import ihm.TestIHM;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import util.NetworkTools;
import application.FullPartyException;
import application.InterfaceGame;







public class TestPlayer extends AbstractPlayer
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private InterfaceGame	game;
	private TestIHM				ihm;
	private String			gameName;
	private String			playerName;

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**
	 * @return Creates a local player that can be called as a local object
	 * @throws FullPartyException
	 */
	public TestPlayer(String gameName, String playerName, String playerIP, InterfaceGame app, TestIHM ihm) throws RemoteException, FullPartyException
	{
		super();
		String url = null;
		int playerPort = NetworkTools.firstFreePort();

		try																			// Create the player's remote reference
		{
			java.rmi.registry.LocateRegistry.createRegistry(playerPort);
			url = playerProtocol + "://" + playerIP + ":" + playerPort + "/" + gameName + "/" + playerName;
			Naming.rebind(url, this);
		}
		catch (MalformedURLException e) {e.printStackTrace(); System.exit(0);}

		this.game		= app;														// Init Player
		this.ihm		= ihm;
		this.gameName	= new String(gameName);
		this.playerName	= new String(playerName);
		System.out.println("\n===========================================================");
		System.out.println("Street Car player : URL = " + url);
		System.out.println("Street Car player : ready and logged");
		System.out.println("===========================================================\n");

		this.game.onJoinRequest(this);									// Log the player to the application
}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String getPlayerName()	throws RemoteException	{return this.playerName;}
	public String getGameName()		throws RemoteException	{return this.gameName;}

// --------------------------------------------
// Local methodes:
// --------------------------------------------

}