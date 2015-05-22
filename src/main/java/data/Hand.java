package main.java.data;

import java.util.LinkedList;





public class Hand
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	private LinkedList<Tile>	tileList;

//--------------------------------------------
// Builder:
//--------------------------------------------
	public Hand( LinkedList<Tile> basicsTiles)
	{
		this.tileList = new LinkedList<Tile> (basicsTiles);
	}

//--------------------------------------------
// Getter:
//--------------------------------------------
	public int size()	{return this.tileList.size();}

}
