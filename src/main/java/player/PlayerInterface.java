package main.java.player;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.data.Tile;







public interface PlayerInterface extends Remote
{
	public String	getName				()											throws RemoteException;
	public Color	getColor			()											throws RemoteException;
	public Data		getGameData			()											throws RemoteException;
	public void		distributeTile		(Tile t)									throws RemoteException;

/*	public boolean	isHumanPlayer		()											throws RemoteException;
	public void		distributeLineCard	()											throws RemoteException;
	public void		distributeRouteCard	()											throws RemoteException;
	public void		tileHasBeenPlaced	(String playerName, Tile t, Point position)	throws RemoteException;
	public void		exchangeTile		(String playerName, Tile t, Point p)		throws RemoteException;
	public void		receiveTileFromPlayer(String chosenPlayer, Tile t)				throws RemoteException;
	public void		placeStop			(Point p)									throws RemoteException;
	public void		revealLine			(String playerName)							throws RemoteException;
	public void		revealRoute			(String playerName)							throws RemoteException;
*/
}