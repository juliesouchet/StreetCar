package main.java.ia;

import java.awt.Point;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Tile;

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
	
	
	/*======================================*
	 *				Local methods			*		
	 *======================================*/
	
	@SuppressWarnings("unused")
	private int heuristic(Point start, Point end) {
		int res = 0;
		// TODO : choose an efficient function (admissible too)
		return res;
	}
	
	@SuppressWarnings("unused")
	private LinkedList<Arc> getAllowedArcs(int x, int y) {
		LinkedList<Arc> res = new LinkedList<Arc>();
		// TODO : returns the arcs linked to a node with their weight
		return res;
	}
	
	/*
	@SuppressWarnings("unused")
	private Tile getMinimalTile(LinkedList<Arc> arcs) {
		LinkedList<Arc> tracks = new LinkedList<Arc>();
		boolean	hasTree = false;
		boolean	isBuilding = false;
		boolean	hasStop = false;
		boolean	isTerminus = false;
		Tile res = new Tile(tracks, hasTree, isBuilding, hasStop, isTerminus);
		// TODO : check the arcs against the various tiles types and take the most common
		return res;		
	}*/
	
	@SuppressWarnings("unused")
	private int getTileCost(Tile t) {
		int res = 0;
		// TODO : check the tile rarity in the current game configuration
		return res;
	}

	/*======================================*
	 *				Local class				*		
	 *======================================*/
	@SuppressWarnings("unused")
	private class Arc{
		public Point start, end;
		public int weight;
		
		public Arc() {
			start = end = null;
			weight = 0;
		}
		
		public Arc(Point s, Point e, int w) {
			start = s;
			end = e;
			weight = w;
		}
		
		
		public int getWeight() {
			int w = 0;
			// TODO : calculates the weight of the arc using the tile already there and those we can put down (minimal tile)
			return w;
		}
	}
}
