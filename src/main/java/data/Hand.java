package main.java.data;

import java.io.Serializable;
import java.util.LinkedList;

import main.java.util.CloneableInterface;
import main.java.util.Copier;





public class Hand implements Serializable, CloneableInterface<Hand>
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	private static final long serialVersionUID = -3100388822802450220L;

	private LinkedList<Tile>	tileList;

//--------------------------------------------
// Builder:
//--------------------------------------------
	public Hand(LinkedList<Tile> basicsTiles)
	{
		this.tileList = new LinkedList<Tile> (basicsTiles);
	}
	
	private Hand() {}
	
	public Hand getClone()
	{
		Hand res = new Hand();

		res.tileList	= (new Copier<Tile>()).copyList(tileList);
		return res;
	}

//--------------------------------------------
// Getter:
//--------------------------------------------
	public int size()	{return this.tileList.size();}

	public Tile get(int k)
	{
		return tileList.get(k);
	}
	
	public void addTile(Tile t)
	{
		tileList.add(t);
	}
	
	public LinkedList<Tile> getTiles()
	{
		return tileList;
	}

}
