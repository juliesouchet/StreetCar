package main.java.ia;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Tile;
import main.java.data.Tile.Path;
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


	/*======================================*
	 *				Local methods			*		
	 *======================================*/
	
	@SuppressWarnings("unused")
	private LinkedList<CoordinatedTile> calculateBestWay() {
		LinkedList<CoordinatedTile> res = new LinkedList<CoordinatedTile>();
		// TODO : calculates the way from the start to the stops to the end (all possible combinations) and keep the best
		return res;		
	}
	
	@SuppressWarnings("unused")
	/**
	 * Used to put the various segments of the way one after another
	 * @param l1
	 * @param l2
	 * @return the concatenation of l1 and l2 with the necessary cross-roads
	 */
	private LinkedList<CoordinatedTile> fusion(LinkedList<CoordinatedTile> l1, LinkedList<CoordinatedTile> l2) {
		LinkedList<CoordinatedTile> res = new LinkedList<CoordinatedTile>();
		// TODO : returns the concatenation of l1 and l2 with the necessary cross-roads
		return res;
	}

	@SuppressWarnings("unused")
	/**
	 * Estimates the opponent's most probable objectives 
	 * based on his previous moves
	 */
	private LinkedList<CoordinatedTile> guessOpponentWay() {
		LinkedList<CoordinatedTile> way = new LinkedList<CoordinatedTile>();
		// TODO
		return way;		
	}
	
	/**
	 * Calculates the best tile to block the opponent's way
	 */
	private Tile blockWay(LinkedList<CoordinatedTile> way) {
		LinkedList<Path> rails = new LinkedList<Path>();
		boolean	hasTree = false;
		boolean	isBuilding = false;
		boolean	hasStop = false;
		boolean	isTerminus = false;
		Tile t = new Tile(rails, hasTree, isBuilding, hasStop, isTerminus);
		// TODO
		return t;
	}
}
