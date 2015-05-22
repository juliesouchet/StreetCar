package main.java.game;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.java.data.Data;
import main.java.player.PlayerInterface;



/**============================================================
 * Remote Application 
 * URL: rmi://ip:port/gameName
 * @author kassuskley
 ==============================================================*/



@SuppressWarnings("serial")
public class Game extends UnicastRemoteObject implements Runnable, GameInterface
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private static final String messageHeader			= "Street Car application: ";
	public final static int		applicationPort			= 5000;
	public final static String	applicationProtocol		= "rmi";

	public Data data; // temporary public for the debug

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=======================================================================
	 * @return Creates a local application that can be called as a local object
	 * @throws RemoteException			: network trouble	(caught by the IHM)
	 * @throws UnknownBoardNameException: 					(caught by the IHM)
	 * @throws RuntimeException 		: 
	 =========================================================================*/
	public Game(String gameName, String appIP, String boardName, int nbrBuildingInLine) throws RemoteException, UnknownBoardNameException, RuntimeException
	{
		super();
		String url = null;

		try																			// Create the player's remote reference
		{
			url = applicationProtocol + "://" + appIP + ":" + applicationPort + "/" + gameName;
			java.rmi.registry.LocateRegistry.createRegistry(applicationPort);
			Naming.rebind(url, this);
		}
		catch (MalformedURLException e) {e.printStackTrace(); System.exit(0);}

		this.data		= new Data(gameName, boardName, nbrBuildingInLine);			// Init application

		System.out.println("\n===========================================================");
		System.out.println(messageHeader + "URL = " + url);
		System.out.println(messageHeader + "ready");
		System.out.println(messageHeader + "Start waiting for connexion request");
		System.out.println("===========================================================\n");
	}
	/**=======================================================================
	 * @return Creates a remote application cloned to the real application at the given ip
	 * @throws NotBoundException 		: The web host is not configured	(throw RuntimeException)
	 * @throws RemoteException 			: The web host is offline			(catched by IHM)
	 * @throws auther java.rmi.Exception: NetworkError						(catched by IHM)
	 =========================================================================*/
	public static GameInterface getRemoteGame(String appIP, String gameName) throws RemoteException, NotBoundException
	{
		String url = applicationProtocol + "://" + appIP + ":" + applicationPort + "/" + gameName;

////	System.setSecurityManager(new RMISecurityManager());
		try 
		{
			return (GameInterface) Naming.lookup(url);
		}
		catch (MalformedURLException e) {e.printStackTrace(); System.exit(0);}
		return null;
	}
// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// Must be declared in the interface "RemoteApplicationInterface"
// --------------------------------------------
	public void onJoinRequest(PlayerInterface player) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		if (this.data.getNbrPlayer() >= Data.maxNbrPlayer)
		{
			System.out.println("\n===========================================================");
			System.out.println(messageHeader + "join request from player : \"" + player.getName() + "\"");
			System.out.println(messageHeader + "Refusing player, party is currently full.");
			System.out.println("===========================================================\n");
			throw new ExceptionFullParty();
		}
		else if (this.data.containsPlayer(player.getName()))
		{
			System.out.println("\n===========================================================");
			System.out.println(messageHeader + "join request from player : \"" + player.getName() + "\"");
			System.out.println(messageHeader + "Refusing player, name already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerName();
		}
		else if (this.usedColor(player.getColor()))
		{
			System.out.println("\n===========================================================");
			System.out.println(messageHeader + "join request from player : \"" + player.getName() + "\"");
			System.out.println(messageHeader + "Refusing player, color \"" + player.getColor() + "\"  already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerColor();
		}
		else
		{
			this.data.addPlayer(player, player.getName());
			System.out.println("\n===========================================================");
			System.out.println(messageHeader + "join request from player : \"" + player.getName() + "\"");
			System.out.println(messageHeader + "accepted player");
			System.out.println("===========================================================\n");
		}
	}
	public boolean quitGame(String gameName, String playerName) throws RemoteException
	{
		String resS = null;
		boolean res= false;

		if		(!this.data.getGameName().equals(gameName))		{resS = "Unknown game name"; res = false;}
		else
		{
			for (String name: this.data.getPlayerNameList())
			{
				if (name.equals(playerName))
				{
					this.data.removePlayer(name);
					resS= "player logged out";
					res = true;
					break;
				}
			}
			if (resS == null)	{resS = "player not found in the local list";	res = false;}
		}

		System.out.println("\n===========================================================");
		System.out.println(messageHeader + "quitGame");
		System.out.println(messageHeader + "logout result : " + resS);
		System.out.println(messageHeader + "gameName      : " + gameName);
		System.out.println(messageHeader + "playerName    : " + playerName);
		System.out.println("===========================================================\n");
		return res;
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void run()
	{
		
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private boolean usedColor(Color c) throws RemoteException
	{
		PlayerInterface p;

		for (String name: this.data.getPlayerNameList())
		{
			p = this.data.getPlayer(name);
			if (p.getColor().equals(c))	return true;
		}
		return false;
	}
}