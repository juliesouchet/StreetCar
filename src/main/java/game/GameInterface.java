package main.java.game;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.player.PlayerInterface;





public interface GameInterface extends Remote
{
	public void		onJoinGame			(PlayerInterface player, boolean isHost, int iaLevel)	throws RemoteException, ExceptionUsedPlayerName, ExceptionUsedPlayerColor;
	public void		onQuitGame			(String playerName)										throws RemoteException, ExceptionForbiddenAction;

	public Data		getData				(String playerName)										throws RemoteException;
	public LoginInfo[]getLoginInfo		(String playerName)										throws RemoteException;
	public void		setLoginInfo		(String playerName, int playerToChangeIndex, LoginInfo newPlayerInfo)throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification;

	public void		hostStartGame		(String playerName)										throws RemoteException, ExceptionForbiddenAction;
	public void		placeTile			(String playerName, Tile t, Point position)				throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		drawCard			(String playerName, int nbrCards)						throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
	public void		validate			(String playerName)										throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
/*	public void		undoAttempt			(String playerName)										throws RemoteException;
	public void		undoTurn			(String playerName)										throws RemoteException;
	public void		moveTram			(String playerName, Point dest)							throws RemoteException;
	public void		pickTileFromBox		(String playerName)										throws RemoteException;
	public void		pickTileFromPlayer	(String playerName, String chosenPlayer, Tile car)		throws RemoteException;
	public void		replaceTwoTiles		(String playerName, Tile t1, Tile t2, Point p1, Point p2)throws RemoteException;
	public void		startMaidenTravel	(String playerName, Point terminus)						throws RemoteException;
	public void		Validate			(String playerName)										throws RemoteException;
*/
}