package main.java.ia;

import java.util.LinkedList;

import main.java.data.Data;

public class WayBuilder {
	Data data;
	
	public WayBuilder(Data d) {
		data = d;
	}
	
	
	/**
	 * Calculates the player's optimal way from one terminus to another 
	 * while visiting the stops
	 * (in the sense of a list of tiles)
	 */
	public LinkedList<CoordinatedTile> getOptimalWay(CoordinatedTile start, int startingDirection, CoordinatedTile end, int endingDirection) {
		LinkedList<CoordinatedTile> way = new LinkedList<CoordinatedTile>();
		/* TODO :
		 * 	- makes graph model of the board with
		 * 		+ nodes = cardinal points of a board square
		 *		+ arcs = ways on the tile on the square
		 *		+ weight of the arcs = cost of the minimal tile
		 *	- calculates optimal path from start to end using A*
		 *		+ heuristic : distance from p1 to p2
		 */
		
		
		
		return way;
	}
	
	
	
}
