package main.java.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.player.PlayerInterface;





public interface GameInterface extends Remote
{
//<<<<<<< HEAD
//	public void		onJoinRequest		(PlayerInterface player, boolean isHost)				throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor;
//	public boolean	quitGame			(String playerName, String gameName)					throws RemoteException;
//	public Data		getDataClone				(String playerName)										throws RemoteException;
//	public void		hostStartGame		(String playerName)										throws RemoteException, ExceptionTooFewPlayers, ExceptionTooManyPlayers, ExceptionOnlyHostCanStartGame;
//=======
	public void		onJoinGame			(PlayerInterface player, boolean isHost)				throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor;
	public boolean	onQuitGame			(String playerName)										throws RemoteException;
	public Data		getData				(String playerName)										throws RemoteException;
	public void		hostStartGame		(String playerName)										throws RemoteException, ExceptionForbiddenAction;
//>>>>>>> 5ed7de7abbf3421528b69121a619ae37ba702fb1
/*	public void		undoAttempt			(String playerName)										throws RemoteException;
	public void		undoTurn			(String playerName)										throws RemoteException;
	public void		placeTile			(String playerName, Tile t, Point position)				throws RemoteException;
	public void		moveTram			(String playerName, Point dest)							throws RemoteException;
	public void		pickTileFromBox		(String playerName)										throws RemoteException;
	public void		pickTileFromPlayer	(String playerName, String chosenPlayer, Tile car)		throws RemoteException;
	public void		replaceTwoTiles		(String playerName, Tile t1, Tile t2, Point p1, Point p2)throws RemoteException;
	public void		startMaidenTravel	(String playerName, Point terminus)						throws RemoteException;
	public void		Validate			(String playerName)										throws RemoteException;
*/
}