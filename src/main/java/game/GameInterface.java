package main.java.game;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.player.PlayerInterface;





public interface GameInterface extends Remote
{
	public void		onJoinGame			(PlayerInterface player, boolean isHost, int iaLevel)	throws RemoteException, ExceptionUsedPlayerName, ExceptionUsedPlayerColor;
	public void		onQuitGame			(String playerName)										throws RemoteException, ExceptionForbiddenAction;
	public void 	onExcludePlayer		(String playerWhoExcludes, String playerExcluded)		throws RemoteException, ExceptionForbiddenAction;

	public Data		getData				(String playerName)										throws RemoteException;
	public LoginInfo[]getLoginInfo		(String playerName)										throws RemoteException;
	public void		setLoginInfo		(String playerName, int playerToChangeIndex, LoginInfo newPlayerInfo)throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification;

	public void		hostStartGame		(String playerName)										throws RemoteException, ExceptionForbiddenAction;
	public void		replaceTile			(String playerName, Tile t, Point position)				throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		placeTwoTiles		(String playerName, Tile t1, Tile t2, Point p1, Point p2)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
	public void		validate			(String playerName)										throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
	public void		startMaidenTravel	(String playerName, Point terminus)						throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted;
	public void		moveTram			(String playerName, LinkedList<Point> tramMovement)		throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted;
	public void		drawTile			(String playerName, int nbrCards)						throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
	public void		pickTileFromPlayer	(String playerName, String chosenPlayer, Tile car)		throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn; // TODO maybe instead of Tile should be int (position of card in player hand)
	
	
	/*
	public void		undoAttempt			(String playerName)										throws RemoteException;
	public void		undoTurn			(String playerName)										throws RemoteException;
	 */
}