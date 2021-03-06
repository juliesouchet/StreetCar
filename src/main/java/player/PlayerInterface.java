package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionGameIsOver;
import main.java.game.ExceptionMissingStartTerminus;
import main.java.game.ExceptionNoPreviousGameToReach;
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
import main.java.game.ExceptionWrongPlayerTerminus;
import main.java.game.ExceptionWrongTramwayPath;
import main.java.game.ExceptionWrongTramwaySpeed;
import main.java.game.ExceptionWrongTramwayStart;
import main.java.game.ExceptionWrongTramwayStartTerminus;







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
	public void		doAction			(Action action)								throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionMissingStartTerminus, ExceptionWrongPlayerTerminus, ExceptionWrongTramwayPath, ExceptionWrongTramwaySpeed, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver, ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionTrtamwayDoesNotStop;
	public void		placeTile			(Tile t, Point position)					throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver;
	public void		replaceTwoTiles		(Tile t1, Tile t2, Point p1, Point p2)		throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver;
	public void		validate			()											throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		rollBack			()											throws RemoteException, ExceptionForbiddenAction, ExceptionNotYourTurn, ExceptionNoPreviousGameToReach;
	public void		moveTram			(Point[] tramPath, int tramPathSize, Point startTerminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionMissingStartTerminus, ExceptionWrongPlayerTerminus, ExceptionWrongTramwayPath, ExceptionWrongTramwaySpeed, ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionTrtamwayDoesNotStop;
	public void		stopMaidenTravel	(String playerName, Point terminus)			throws ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, RemoteException;
	public void		drawTile			(int nbrCards)								throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInDeck;
	public void		pickTileFromPlayer	(String chosenPlayer, Tile tile)			throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand;


	public void		sendChatMessage		(String message)							throws RemoteException;
	public void		newChatMessage		(String playerName, String message)			throws RemoteException;
}