package main.java.game;

import java.awt.Color;
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
	public void		onJoinGame			(PlayerInterface player, boolean isHuman, boolean isHost, int iaLevel)	throws RemoteException, ExceptionUsedPlayerName, ExceptionGameHasAlreadyStarted;
	public void		onQuitGame			(String playerName)										throws RemoteException, ExceptionForbiddenAction;

	public Data		getData				(String playerName)										throws RemoteException;
	public LoginInfo[]getLoginInfo		(String playerName)										throws RemoteException;
	public void		setLoginInfo		(String playerName, int playerToChangeIndex, LoginInfo newPlayerInfo)throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification;
	public void		setPlayerColor		(String playerName, Color playerColor)					throws RemoteException, ExceptionUsedPlayerColor;

	public void		hostStartGame		(String playerName)										throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnougthPlayers;
	public void		placeTile			(String playerName, Tile t, Point position)				throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions;
	public void		replaceTwoTiles		(String playerName, Tile t1, Tile t2, Point p1, Point p2)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		validate			(String playerName)										throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		startMaidenTravel	(String playerName, Point terminus)						throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionUncompletedPath;
	public void		moveTram			(String playerName, LinkedList<Point> tramMovement)		throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted;
	public void		drawTile			(String playerName, int nbrCards)						throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionNotEnougthTileInDeck, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction;
	public void		pickTileFromPlayer	(String playerName, String chosenPlayer, Tile car)		throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnougthTileInHand; // TODO maybe instead of Tile should be int (position of card in player hand)
	
	
	/*
	public void		undoAttempt			(String playerName)										throws RemoteException;
	public void		undoTurn			(String playerName)										throws RemoteException;
	 */
}