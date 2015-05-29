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
import main.java.game.ExceptionNotYourTurn;







public interface PlayerInterface extends Remote
{
	public Data		getGameData			()											throws RemoteException;
	public LoginInfo[]getLoginInfo		() 											throws RemoteException;
	public void		setLoginInfo		(int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification;

	public String	getPlayerName		()											throws RemoteException;
	public Color	getPlayerColor		()											throws RemoteException;
	public boolean	isHumanPlayer		()											throws RemoteException;
	public void		hostStartGame		()											throws RemoteException, ExceptionForbiddenAction;
	public void		excludePlayer		()											throws RemoteException;
	public void		gameHasChanged		(Data data)									throws RemoteException;
	public void		placeTile			(Tile t, Point position)					throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction;
	public void		drawCard			(int nbrCards)								throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn;
/*	public void		distributeLineCard	()											throws RemoteException;
	public void		distributeRouteCard	()											throws RemoteException;
	public void		tileHasBeenPlaced	(String playerName, Tile t, Point position)	throws RemoteException;
	public void		exchangeTile		(String playerName, Tile t, Point p)		throws RemoteException;
	public void		distributeTile		(Tile t)									throws RemoteException;
	public void		receiveTileFromPlayer(String chosenPlayer, Tile t)				throws RemoteException;
	public void		placeStop			(Point p)									throws RemoteException;
	public void		revealLine			(String playerName)							throws RemoteException;
	public void		revealRoute			(String playerName)							throws RemoteException;
*/
}