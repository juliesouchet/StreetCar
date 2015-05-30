package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;





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
	protected InterfaceIHM	ihm;
	protected String		playerName;
	protected Color			color;
	protected Data			data;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public PlayerAbstract() throws RemoteException
	{
		super();
	}
	public PlayerAbstract(String playerName, Color playerColor, GameInterface game, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super();

		this.game		= game;
		this.ihm		= ihm;
		this.playerName	= new String(playerName);
		this.color		= playerColor;
		this.data		= null;
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
	public Data		getGameData()							{return (this.data == null) ? null : this.data.getClone(playerName);}
	public LoginInfo[]getLoginInfo() throws RemoteException	{return this.game.getLoginInfo(playerName);}
	public void setLoginInfo(int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification
	{
		this.game.setLoginInfo(playerName, playerToChangeIndex, newPlayerInfo);
	}

	public String 	getPlayerName()	throws RemoteException	{return this.playerName;}
	public Color	getPlayerColor()throws RemoteException	{return this.color;}
	public void		gameHasChanged(Data data) throws RemoteException
	{
		this.data = data;
		if (this.ihm != null) this.ihm.refresh(data);
	}
	public void	hostStartGame()	throws RemoteException, ExceptionForbiddenAction
	{
		this.game.hostStartGame(playerName);
	}
	public void placeTile (Tile t, Point position) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		this.game.placeTile(playerName, t, position);
	}
	public void	drawCard (int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		this.game.drawCard(playerName, nbrCards);
	}
	public void validate() throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		this.game.validate(playerName);
	}
	public void onQuitGame (String playerName) throws RemoteException, ExceptionForbiddenAction
	{
		game.onQuitGame(playerName);
	}
	public void onExcludePlayer	(String playerExcluded) throws RemoteException, ExceptionForbiddenAction
	{
		game.onExcludePlayer(playerName, playerExcluded);
	}
	
	public void moveTram(LinkedList<Point> tramMovement) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		game.moveTram(playerName, tramMovement);
	}
	
	public void pickTileFromBox() throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		game.pickTileFromBox(playerName);
	}
	
	public void pickTileFromPlayer(String chosenPlayer, Tile tile) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		game.pickTileFromPlayer(chosenPlayer, chosenPlayer, tile);
	}
	
	public void replaceTwoTiles(Tile t1, Tile t2, Point p1, Point p2)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		game.replaceTwoTiles(playerName, t1, t2, p1, p2);
	}
	
	public void	startMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		game.startMaidenTravel(playerName, terminus);
	}

}