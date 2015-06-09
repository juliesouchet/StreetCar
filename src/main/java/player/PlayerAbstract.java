package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionGameIsOver;
import main.java.game.ExceptionMissingStartTerminus;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotEnoughTilesInHand;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionPlayerIsBlocked;
import main.java.game.ExceptionTooManyActions;
import main.java.game.ExceptionTramwayExceededArrival;
import main.java.game.ExceptionTramwayJumpCell;
import main.java.game.ExceptionTrtamwayDoesNotStop;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.ExceptionWrongPlayerTerminus;
import main.java.game.ExceptionWrongTramwayPath;
import main.java.game.ExceptionWrongTramwaySpeed;
import main.java.game.ExceptionWrongTramwayStart;
import main.java.game.ExceptionWrongTramwayStartTerminus;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;





/**============================================================
 * Remote IHM
 * URL: rmi://ip:port/gameName/playerName
 * @author kassuskley
 ==============================================================*/



public abstract class PlayerAbstract extends UnicastRemoteObject implements PlayerInterface
{
// ----------------------------------------------------------------------------
// Attributs:
// ----------------------------------------------------------------------------
	private static final long serialVersionUID = -8965945491565879485L;

	protected GameInterface	game;
	protected InterfaceIHM	ihm;
	protected String		playerName;
	protected Data			data;

// ----------------------------------------------------------------------------
// Builder:
// ----------------------------------------------------------------------------
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

// ----------------------------------------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// ----------------------------------------------------------------------------
	public synchronized LoginInfo[]	getLoginInfo() throws RemoteException
	{
		return this.game.getLoginInfo(playerName);
	}
	public synchronized void setLoginInfo(int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification
	{
		this.game.setLoginInfo(playerName, playerToChangeIndex, newPlayerInfo);
	}
	public synchronized void setPlayerColor(Color playerColor) throws RemoteException, ExceptionUsedPlayerColor
	{
		this.game.setPlayerColor(playerName, playerColor);
	}

	public synchronized Data	getGameData()
	{
		return (this.data == null) ? null : this.data.getClone(playerName);
	}
	public synchronized String getPlayerName() throws RemoteException
	{
		return this.playerName;
	}
	public synchronized Color getPlayerColor()throws RemoteException
	{
		return this.data.getPlayerColor(playerName);
	}
	public synchronized void gameHasChanged(Data data) throws RemoteException
	{
		this.data = data;
		if (this.ihm != null) this.ihm.refresh(data);
	}
	public synchronized void hostStartGame() throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnoughPlayers
	{
		this.game.hostStartGame(playerName);
	}
	public synchronized void placeTile (Tile t, Point position) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver
	{
System.out.println("----------------------------------------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Pose tuile "+ t.toString()+" a la position: ("+position.x+","+position.y+")");
		this.game.placeTile(playerName, t, position);
	}

	public synchronized void doAction(Action action) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionMissingStartTerminus, ExceptionWrongPlayerTerminus, ExceptionWrongTramwayPath, ExceptionWrongTramwaySpeed, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver, ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionTrtamwayDoesNotStop
	{
		if (action.isMOVE())
		{
			this.moveTram(action.tramwayMovement, action.tramwayMovementSize, action.startTerminus);
			return;
		}
		if (action.isBUILD_SIMPLE())
		{
			this.placeTile(action.tile1, new Point(action.positionTile1.x, action.positionTile1.y));
			return;
		}
		if (action.isTWO_BUILD_SIMPLE())
		{
			this.placeTile(action.tile1, new Point(action.positionTile1.x, action.positionTile1.y));
			this.placeTile(action.tile2, new Point(action.positionTile2.x, action.positionTile2.y));
			return;
		}
		if (action.isBUILD_DOUBLE())
		{
//TODO a corriger
			this.placeTile(action.tile1, new Point(action.positionTile1.x, action.positionTile1.y));
			this.placeTile(action.tile2, new Point(action.positionTile2.x, action.positionTile2.y));
			return;
		}
		if (action.isBUILD_AND_START_TRIP_NEXT_TURN())
		{
//TODO a corriger
			this.placeTile(action.tile1, new Point(action.positionTile1.x, action.positionTile1.y));
throw new RuntimeException("Not implemented yet");
//			game.tryRandomPlaceTile();
//			return;
		}
	}
	
	
	
	public synchronized void drawTile (int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionNotEnoughTilesInDeck, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction
	{
System.out.println("----------------------------------------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Pioche: " + nbrCards + " | Reste " +(data.getNbrRemainingDeckTile()-nbrCards));
		this.game.drawTile(playerName, nbrCards);
	}
	public synchronized void validate() throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
System.out.println("----------------------------------------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Validate");
		this.game.validate(playerName);
	}
	public synchronized void onQuitGame (String playerName) throws RemoteException, ExceptionForbiddenAction
	{
		game.onQuitGame(playerName);
	}
	public synchronized void moveTram(Point[] tramPath, int tramPathSize, Point startTerminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionMissingStartTerminus, ExceptionWrongPlayerTerminus, ExceptionWrongTramwayPath, ExceptionWrongTramwaySpeed, ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionTrtamwayDoesNotStop
	{
System.out.println("----------------------------------------------------------------");
System.out.print("Round: " + data.getRound() + "\t " + playerName +": Move: ");
for(int i = 0; i < tramPathSize; i++) {
	System.out.print("("+tramPath[i].x+","+tramPath[i].y+"), ");
}
System.out.println("\t StartTerminus = " + startTerminus);

		game.moveTram(playerName, tramPath, tramPathSize, startTerminus);
	}

	public synchronized void pickTileFromPlayer(String chosenPlayer, Tile tile) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand 
	{
System.out.println("----------------------------------------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Prend " + tile + " a " + chosenPlayer);
		game.pickTileFromPlayer(chosenPlayer, chosenPlayer, tile);
	}

	public synchronized void replaceTwoTiles(Tile t1, Tile t2, Point p1, Point p2)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver
	{
System.out.println("----------------------------------------------------------------");
System.out.println("Round: " + data.getRound() + "\t " + playerName +": Echange " + t1 + "("+p1.x+","+p1.y+") et " + t2 + " en ("+p2.x+","+p2.y+")");
		game.replaceTwoTiles(playerName, t1, t2, p1, p2);
	}

	public synchronized void stopMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		game.stopMaidenTravel(playerName);
	}
	
	
	
	
	
	public synchronized void sendChatMessage(String message) throws RemoteException
	{
		this.game.sendChatMessage(playerName, message);
	}
	public synchronized void newChatMessage	(String playerName, String message) throws RemoteException
	{
		if (this.ihm != null) this.ihm.refreshMessages(playerName, message);
	}
}