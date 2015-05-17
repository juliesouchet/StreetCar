package player;

import game.FullPartyException;
import game.InterfaceGame;
import game.UsedPlayerColorException;
import game.UsedPlayerNameException;
import ihm.TestIHM;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;





/**============================================================
 * Remote IHM
 * URL: rmi://ip:port/gameName/playerName
 * @author kassuskley
 ==============================================================*/



public abstract class AbstractPlayer extends UnicastRemoteObject implements InterfacePlayer
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	protected InterfaceGame	game;
	protected TestIHM		ihm;
	protected String		name;
	protected Color			color;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public AbstractPlayer(String playerName, Color playerColor, InterfaceGame app, TestIHM ihm) throws RemoteException, FullPartyException, UsedPlayerNameException, UsedPlayerColorException
	{
		super();

		this.game	= app;												// Init Player
		this.ihm	= ihm;
		this.name	= new String(playerName);
		this.color	= playerColor;
		System.out.println("\n===========================================================");
		System.out.println("Street Car player : playerName  = " + playerName);
		System.out.println("Street Car player : playerColor = " + playerColor);
		System.out.println("Street Car player : ready and logged");
		System.out.println("===========================================================\n");

		this.game.onJoinRequest(this);									// Log the player to the application
}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String 	getName()	throws RemoteException	{return this.name;}
	public Color	getColor()	throws RemoteException	{return this.color;}

// --------------------------------------------
// Local methodes:
// --------------------------------------------

}