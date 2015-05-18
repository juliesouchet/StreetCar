package main.java.game;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

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
// Attributs:
// --------------------------------------------
	private static final String messageHeader			=  "Street Car application: ";
	public final static int		applicationPort			= 5000;
	public final static String	applicationProtocol		= "rmi";
	public final static int		maxNbrPlayer			= 6;

	private String								gameName;
	private HashMap<String, PlayerInterface>	playerList;

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=======================================================================
	 * @return Creates a local application that can be called as a local object
	 =========================================================================*/
	public Game(String gameName, String appIP) throws RemoteException
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

		this.playerList = new HashMap<String, PlayerInterface>();					// Init application
		this.gameName	= new String(gameName);

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
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// Must be declared in the interface "RemoteApplicationInterface"
// --------------------------------------------
	public void onJoinRequest(PlayerInterface player) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		System.out.println("\n===========================================================");
		System.out.println(messageHeader + "join request from player : \"" + player.getName() + "\"");
		if (this.playerList.size() >= maxNbrPlayer)
		{
			System.out.println(messageHeader + "Refusing player, party is currently full.");
			System.out.println("===========================================================\n");
			throw new ExceptionFullParty();
		}
		else if (playerList.containsKey(player.getName()))
		{
			System.out.println(messageHeader + "Refusing player, name already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerName();
		}
		else if (usedColor(player.getColor()))
		{
			System.out.println(messageHeader + "Refusing player, color \"" + player.getColor() + "\"  already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerColor();
		}
		else
		{
			this.playerList.put(player.getName(), player);
			System.out.println(messageHeader + "accepted player");
			System.out.println("===========================================================\n");
		}
	}
	public boolean quitGame(String gameName, String playerName) throws RemoteException
	{
		String resS = "";
		boolean res= false;
		int i, size = playerList.size();

		if (!this.gameName.equals(gameName))		{resS = "Unknown game name"; res = false;}
		else
		{
			for (i=0; i<size; i++)
			{
				PlayerInterface p = playerList.get(i);
				if (p.getName().equals(playerName))
				{
					playerList.remove(i);
					resS= "player logged out";
					res = true;
					break;
				}
			}
			if (i == size)	{resS = "player not found in the local list"; res = false;}
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
// Local methodes:
// --------------------------------------------
	public void run()
	{
		
	}

// --------------------------------------------
// Private methodes:
// --------------------------------------------
	private boolean usedColor(Color c) throws RemoteException
	{
		for (PlayerInterface p: playerList.values())
		{
			if (p.getColor().equals(c))	return true;
		}
		return false;
	}
}