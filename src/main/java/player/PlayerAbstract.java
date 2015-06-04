package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionEndGame;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotEnoughTilesInHand;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTooManyActions;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.game.ExceptionUncompletedPath;
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
	protected Data			data;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public PlayerAbstract() throws RemoteException
	{
		super();
	}
	
	public PlayerAbstract(String playerName, GameInterface game, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super();

		this.game		= game;
		this.ihm		= ihm;
		this.playerName	= new String(playerName);
		this.data		= null;
		System.out.println("\n===========================================================");
		System.out.println("Street Car player : playerName  = " + playerName);
		System.out.println("Street Car player : ready");
		System.out.println("===========================================================\n");
	}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public synchronized LoginInfo[]	getLoginInfo() throws RemoteException	{return this.game.getLoginInfo(playerName);}
	public synchronized void setLoginInfo(int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification
	{
		this.game.setLoginInfo(playerName, playerToChangeIndex, newPlayerInfo);
	}
	public synchronized void setPlayerColor(Color playerColor) throws RemoteException, ExceptionUsedPlayerColor
	{
		this.game.setPlayerColor(playerName, playerColor);
	}

	public synchronized Data	getGameData()							{return (this.data == null) ? null : this.data.getClone(playerName);}
	public synchronized String 	getPlayerName()	throws RemoteException	{return this.playerName;}
	public synchronized Color	getPlayerColor()throws RemoteException	{return this.data.getPlayerColor(playerName);}
	public synchronized void	gameHasChanged(Data data) throws RemoteException
	{
		this.data = data;
		if (this.ihm != null) this.ihm.refresh(data);
	}
	public synchronized void hostStartGame()	throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnoughPlayers
	{
		this.game.hostStartGame(playerName);
	}
	public synchronized void placeTile (Tile t, Point position) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions
	{
System.out.println("--------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Pose tuile "+ t.toString()+" a la position: ("+position.x+","+position.y+")");
		this.game.placeTile(playerName, t, position);
	}
	public synchronized void drawTile (int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionNotEnoughTilesInDeck, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction
	{
System.out.println("--------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Pioche: " + nbrCards + " | Reste " +(data.getNbrRemainingDeckTile()-nbrCards));
		this.game.drawTile(playerName, nbrCards);
	}
	public synchronized void validate() throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionEndGame
	{
System.out.println("--------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Validate");
		this.game.validate(playerName);
	}
	public synchronized void onQuitGame (String playerName) throws RemoteException, ExceptionForbiddenAction
	{
		game.onQuitGame(playerName);
	}
	public synchronized void moveTram(LinkedList<Point> tramMovement) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		game.moveTram(playerName, tramMovement);
	}

	public synchronized void pickTileFromPlayer(String chosenPlayer, Tile tile) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand 
	{
		game.pickTileFromPlayer(chosenPlayer, chosenPlayer, tile);
	}

	public void replaceTwoTiles(Tile t1, Tile t2, Point p1, Point p2)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		game.replaceTwoTiles(playerName, t1, t2, p1, p2);
	}

	public void	startMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionUncompletedPath
	{
		game.startMaidenTravel(playerName, terminus);
	}
}