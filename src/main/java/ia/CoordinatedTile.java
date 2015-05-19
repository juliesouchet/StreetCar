package main.java.ia;

import java.awt.Point;
import java.util.LinkedList;

import main.java.data.Tile;
import main.java.data.Tile.Path;

public class CoordinatedTile {
	public int x,y;
	public Tile tile;
	
	public CoordinatedTile() {
		x = y = 0;
		tile = new Tile(new LinkedList<Path>(), false, false, false, false);
	}
	public CoordinatedTile(int x, int y, Tile t) {
		this.x = x;
		this.y = y;
		tile = t;
	}
	public CoordinatedTile(Point c, Tile t) {
		x = c.x;
		y = c.y;
		tile = t;
	}
}