package main.java.ia;

import java.util.LinkedList;

import main.java.data.Tile;
import main.java.data.Tile.Path;

public class CoordinatedTile {
	public int x,y, cost;
	public Tile tile;
	
	/**
	 * Creates an empty tile on (0,0)
	 */
	public CoordinatedTile() {
		x = y = cost = 0;
		try {
			tile = new Tile(new LinkedList<Path>());
		}
		catch (NonExistantTile e) {
			System.out.println("Error creation empy tile on ("+x+","+y+")");
		}
	}
	/**
	 * Creates a tile on (x,y) corresponding to the given tracks
	 * @param x
	 * @param y
	 * @param tracks
	 */
	public CoordinatedTile(int x, int y, LinkedList<Path> tracks) {
		this.x = x;
		this.y = y;
		try {
			tile = new Tile(tracks);
		}
		catch (NonExistantTile e) {
			System.out.println("Error creation tile "+tracks+" on ("+x+","+y+")");
		}
	}
	public CoordinatedTile(int x, int y, Tile t) {
		this.x = x;
		this.y = y;
		tile = t;
	}
}