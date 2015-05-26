package main.java.player;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import test.java.player.TestIA;





/**============================================================
 * Remote IHM
 * URL: rmi://ip:port/gameName/playerName
 * @author kassuskley
 ==============================================================*/



public abstract class PlayerAbstract extends UnicastRemoteObject implements PlayerInterface
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private static final long serialVersionUID = -8965945491565879485L;

	protected GameInterface	game;
	protected TestIA		ihm;
	protected String		playerName;
	protected Color			color;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public PlayerAbstract() throws RemoteException
	{
		super();
	}
	public PlayerAbstract(String playerName, Color playerColor, GameInterface game, TestIA ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super();

		this.game		= game;
		this.ihm		= ihm;
		this.playerName	= new String(playerName);
		this.color		= playerColor;
		System.out.println("\n===========================================================");
		System.out.println("Street Car player : playerName  = " + playerName);
		System.out.println("Street Car player : playerColor = " + playerColor);
		System.out.println("Street Car player : ready");
		System.out.println("===========================================================\n");
	}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String 	getPlayerName()	throws RemoteException	{return this.playerName;}
	public Color	getColor()		throws RemoteException	{return this.color;}
	public Data		getGameData()	throws RemoteException	{return this.game.getData(this.playerName);}
	public void		hostStartGame()	throws RemoteException
	{
		this.game.hostStartGame(playerName);
	}


// --------------------------------------------
// Local methodes:
// --------------------------------------------
	public GameInterface getGame()throws RemoteException
	{
		return this.game;
	}
}