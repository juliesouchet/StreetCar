package main.java.data;

import java.io.Serializable;
import java.util.LinkedList;





public class Hand implements Serializable
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	private static final long serialVersionUID = -3100388822802450220L;

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

	public Tile get(int k) {
		// TODO Auto-generated method stub
		return null;
	}

}
