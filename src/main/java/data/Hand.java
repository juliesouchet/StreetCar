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
	private Hand(){}
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
		Tile res = this.tileList.get(k);

		this.tileList.remove(k);
		return res;
	}
	
	public String toString()
	{
		String res = "";
		for (Tile t : tileList) {
			res += "\n"+t;
		}
		return res;
	}
	
//--------------------------------------------
// Setter:
//--------------------------------------------
	public void add(Tile t) {
		int rotation = t.getTileDirection().getVal();
		for(int i = 0; i < rotation; i++) { // putting it in the original orientation
			t.turnLeft();
		}
		tileList.add(t);		
	}
	public void remove(Tile t) {
		tileList.remove(t);
	}

}
