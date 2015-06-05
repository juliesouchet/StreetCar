package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionGameIsOver;
import main.java.game.ExceptionNoPreviousGameToReach;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotEnoughTilesInHand;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionPlayerIsBlocked;
import main.java.game.ExceptionTooManyActions;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.game.ExceptionUncompletedPath;
import main.java.game.ExceptionUsedPlayerColor;







public interface PlayerInterface extends Remote
{
	public Data		getGameData			()											throws RemoteException;
	public String	getPlayerName		()											throws RemoteException;
	public Color	getPlayerColor		()											throws RemoteException;
	public boolean	isHumanPlayer		()											throws RemoteException;

	public LoginInfo[]getLoginInfo		() 											throws RemoteException;
	public void		setLoginInfo		(int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification;
	public void		setPlayerColor		(Color playerColor)							throws RemoteException, ExceptionUsedPlayerColor;
	public void		onQuitGame			(String playerName)							throws RemoteException, ExceptionForbiddenAction;

	public void		hostStartGame		()											throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnoughPlayers;
	public void		excludePlayer		()											throws RemoteException;
	public void		gameHasChanged		(Data data)									throws RemoteException;
	public void		placeTile			(Tile t, Point position)					throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver;
	public void		replaceTwoTiles		(Tile t1, Tile t2, Point p1, Point p2)		throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver;
	public void		validate			()											throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		rollBack			()											throws RemoteException, ExceptionForbiddenAction, ExceptionNotYourTurn, ExceptionNoPreviousGameToReach;
	public void		startMaidenTravel	(String playerName, Point terminus)			throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionUncompletedPath;
	public void		moveTram			(Point[] tramMovement, int ptrTramwayMovement)			throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted;
	public void		drawTile			(int nbrCards)								throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInDeck;
	public void		pickTileFromPlayer	(String chosenPlayer, Tile tile)			throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand;
}