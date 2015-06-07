package main.java.data;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

import main.java.util.CloneableInterface;
import main.java.util.Copier;





public class Hand implements Serializable, CloneableInterface<Hand>
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	private static final long	serialVersionUID	= -3100388822802450220L;
	public static final int		maxHandSize			= 5;
	public static final int		initialHandSize		= 5;
	public static final String	initialHandFile		= "src/main/resources/initialHand/default";
	public static final Hand	initialHand			= initInitialHand();

	private LinkedList<Tile>	tileList;

//--------------------------------------------
// Builder:
//--------------------------------------------
	public Hand(LinkedList<Tile> basicTiles)
	{
		if (basicTiles.size() > maxHandSize)	throw new RuntimeException("Too big hand size");

		this.tileList = new Copier<Tile>().copyList(basicTiles);
	}
	public Hand getClone(){return new Hand(this.tileList);}

//--------------------------------------------
// Getter:
//--------------------------------------------
	public boolean isFull()			{return this.tileList.size() == maxHandSize;}
	public int	getSize()			{return this.tileList.size();}
	public Tile	get(int k)			{return this.tileList.get(k);}
	public boolean isInHand(Tile t)	{return this.tileList.contains(t);}
	public String toString()
	{
		String res = "";
		for (Tile t : tileList) {
			res += "\n"+t.getTileID();
		}
		return res;
	}
	
//--------------------------------------------
// Setter:
//--------------------------------------------
	public void add(Tile t)
	{
		int rotation = t.getTileDirection().getVal();

		if (this.tileList.size() >= maxHandSize)	throw new RuntimeException("Too big hand size");
		if (!t.isDeckTile())						throw new RuntimeException("Not a deck tile: " + t);

		for(int i = 0; i < rotation; i++)t.turnLeft();
		t.setStop(false);
		tileList.add(t);
	}
	public void remove(Tile t)
	{
		boolean test = tileList.remove(t);
		if (!test) throw new RuntimeException("Can't find the given tile: " + t);
	}
	public void setHand(Hand hand)
	{
		this.tileList.clear();
		for (Tile t: hand.tileList)
		{
			this.tileList.add(t.getClone());
		}
	}

//--------------------------------------------
// Private Methodes:
//--------------------------------------------
	/**============================================
	 * @return Creates the initial hand from the corresponding file
	 ==============================================*/
	private static Hand initInitialHand()
	{
		File f = new File(initialHandFile);
		String tileName;
		Scanner sc;

		LinkedList<Tile> initialTileList= new LinkedList<Tile>();
		try
		{
			sc = new Scanner(f);
			for (int i=0; i<initialHandSize; i++)
			{
				tileName = sc.next();
				initialTileList.add(Tile.parseTile(tileName));
			}
			sc.close();
			return new Hand(initialTileList);
		}
		catch (Exception e){e.printStackTrace(); throw new RuntimeException("Malformed initial hand file");}
	}
}