package main.java.data;

import java.util.LinkedList;





public class Hand
{
	private LinkedList<Tile> playerTiles;	// Move Attributes
	private int size;
public Hand(){}
	/**
	 *  @param Tiles
	 *  basicsTiles les tuiles standard en début de jeu
	 */
	public Hand( LinkedList<Tile> basicsTiles){
		this.size = basicsTiles.size();
		this.playerTiles = basicsTiles;
	}
	
	public int size(){
		return this.size;
	}
	
	public Tile get(int index){
		return this.playerTiles.get(index);
	}
	
	
	
}
