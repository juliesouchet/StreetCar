package main.java.ia;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.engine.data.Tile;
import main.java.engine.data.Tile.Path;
import main.java.player.PlayerInterface;

public class Hard implements PlayerInterface {
	/*======================================*
	 *			Inherited methods			*		
	 *======================================*/
	
	@Override
	public String getName() throws RemoteException {
		return "Hard";
	}

	@Override
	public Color getColor() throws RemoteException {
		// TODO Which color ?
		return null;
	}

	@Override
	public boolean isHumanPlayer() throws RemoteException {
		return false;
	}

	@Override
	public void distributeLineCard() throws RemoteException {
		// TODO To be implemented
	}

	@Override
	public void distributeRouteCard() throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void tileHasBeenPlaced(String playerName, Tile t, Point position)
			throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void exchangeTile(String playerName, Tile t, Point p)
			throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void distributeTile(Tile t) throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void receiveTileFromPlayer(String chosenPlayer, Tile t)
			throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void placeStop(Point p) throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void revealLine(String playerName) throws RemoteException {
		// TODO To be implemented

	}

	@Override
	public void revealRoute(String playerName) throws RemoteException {
		// TODO To be implemented

	}

	
	


	/*======================================*
	 *				Local methods			*		
	 *======================================*/
	
	@SuppressWarnings("unused")
	/**
	 * Calculates the player's optimal way 
	 * from one terminus to another 
	 * (in the sense of a list of tiles)
	 */
	private LinkedList<Square> getOptimalWay() {
		LinkedList<Square> way = new LinkedList<Square>();
		//TODO
		return way;
	}
	
	@SuppressWarnings("unused")
	/**
	 * Estimates the opponent's most probable objectives 
	 * based on his previous moves
	 */
	private LinkedList<Square> guessOpponentWay() {
		LinkedList<Square> way = new LinkedList<Square>();
		//TODO
		return way;		
	}
	
	@SuppressWarnings("unused")
	/**
	 * Calculates the best tile to block the opponent's way
	 */
	private Tile blockWay(LinkedList<Square> way) {
		LinkedList<Path> rails = new LinkedList<Path>();
		boolean	hasTree = false;
		boolean	isBuilding = false;
		boolean	hasStop = false;
		boolean	isTerminus = false;
		Tile t = new Tile(rails, hasTree, isBuilding, hasStop, isTerminus);
		// TODO
		return t;
	}
	
	/*======================================*
	 *				Local class				*		
	 *======================================*/
	@SuppressWarnings("unused")
	private class Square {
		public Point coordinates;
		public Tile tile;
		
		public Square() {
			coordinates = new Point(0,0);
			tile = new Tile(new LinkedList<Path>(), false, false, false, false);
		}
		public Square(Point c, Tile t) {
			coordinates = c;
			tile = t;
		}
	}
}
