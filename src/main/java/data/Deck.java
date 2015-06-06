package main.java.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import main.java.util.CloneableInterface;
import main.java.util.Copier;







public class Deck implements Serializable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final long	serialVersionUID	= 1683108311031397048L;
	public static final String	stackDirectory		= "src/main/resources/images/tiles/";

	private ArrayList<StackCell>	stack;		// Sorted list using the attribute remaining in a descending order
	private int						stackSize;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Deck() throws RuntimeException
	{
		File f;
		String tileList[];
		this.stack	= new ArrayList<StackCell>();
		f			= new File(stackDirectory);
		tileList	= f.list();
		for (String str: tileList)
		{
			Tile t = Tile.parseTile(str);
			if (t.isDeckTile()) this.stack.add(new StackCell(t));
		}
		Collections.sort(this.stack, new StackCell());
		Collections.reverse(stack);
		this.stackSize = this.computeRemainingDeckSize();
	}
	private Deck(boolean b){}
	public Deck getClone()
	{
		Deck res		= new Deck(false);
		res.stack		= (new Copier<StackCell>()).copyList(this.stack);
		res.stackSize	= this.stackSize;
		return res;
	}

// --------------------------------------------
// Setters/getters: pickup 
// --------------------------------------------
	public boolean	isEmpty()					{return (this.getNbrRemainingDeckTile() == 0);}
	public int		getNbrRemainingDeckTile()	{return (this.stackSize);}
	/**=====================================================
	 * @return Pick up an element from the stack using a uniformly distributed probability distribution.</br>
	 * The returned element is removed from the stack.</br>
	 * The stack is kept sorted.</br>
	 =======================================================*/
	public Tile drawTile()
	{
		int size	= this.getNbrRemainingDeckTile();
		if (size == 0)	throw new RuntimeException("Empty stack");

		StackCell sc = null, sc1;
		Tile res = null;
		int nbTileTypes = stack.size();
		int rnd		= (new Random()).nextInt(size);
		int s=0, i=0;

		while(s <= size && i < nbTileTypes)											// Pick a random element from the stack
		{
			sc = this.stack.get(i);
			s += sc.remaining;
			if (rnd <= s)	{res = sc.t;break;}
			i ++;
		}
		if (s > size)	throw new RuntimeException("Not enough cards in deck");
		sc.remaining --;															// Remove the element from the stack
		
		for (int j=i+1; j< nbTileTypes-1; j++)										// Keep the stack sorted
		{
			sc1 = this.stack.get(j);
			if (sc1.remaining > sc.remaining) Collections.swap(stack, j, j+1);
			else break;
			sc = sc1;
		}
		this.stackSize --;
		return res.getClone();
	}
	/**===========================================================
	 * @return Put back the given tile in the deck</br>
	 =============================================================*/
	public void undrawTile(Tile drawnTile)
	{
		boolean founded = false;

		for (StackCell sc: this.stack)
		{
			if (!sc.t.equals(drawnTile)) continue;
			if (sc.remaining+1 > drawnTile.getCardinal())	throw new RuntimeException("Too many tiles \"" + drawnTile.getTileID() + "\" in the deck");
			sc.remaining ++;
			founded = true;
		}
		if (!founded) throw new RuntimeException("Cant found the tile to put back in deck");
		this.stackSize ++;
	}
	/**=====================================================
	 * @return the number of remaining tiles that match the given tile</br>
	 =======================================================*/
	public int getNbrRemainingTile(Tile t)
	{
		StackCell sc = this.pickStackCell(t);

		if (sc == null)	throw new RuntimeException("Unknown tile: " + t.getTileID());
		return sc.remaining;
	}
	/**=====================================================
	 * @return the probability to pickup the given tile in the stack
	 =======================================================*/
	public double getPickProba(Tile t)
	{
		StackCell sc = this.pickStackCell(t);

		if (sc == null)	throw new RuntimeException("Unknown tile: " + t.getTileID());
		return (double)((double)sc.remaining / (double)this.stackSize);
	}
	public void setDeck(Deck deck)
	{
		this.stackSize = 0;
		this.stack.clear();
		for (StackCell sc: deck.stack)
		{
			this.stack.add(sc.getClone());
			this.stackSize += sc.remaining;
		}
		if (this.stackSize != deck.stackSize)	throw new RuntimeException("Pfffff: this.size = " + this.stackSize + ", inputSize = " + deck.stackSize);
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private StackCell pickStackCell(Tile t)
	{
		for (StackCell sc: stack) if (sc.t.getTileID().equals(t.getTileID())) return sc;
		return null;
	}
	/**=====================================================
	 * @return the remaining size of the stack
	 =======================================================*/
	private int computeRemainingDeckSize()
	{
		int res = 0;

		for (StackCell sc: stack)	res += sc.remaining;
		return res;
	}

// --------------------------------------------
// Stack cell class:
// Gathers a list of informations corresponding to a tile
// --------------------------------------------
	public class StackCell implements Comparator<StackCell>, Serializable, CloneableInterface<StackCell>
	{
		// Attributes
		private static final long serialVersionUID = -8270266931462832239L;
		public Tile	t;
		public int	remaining;

		// Builder
		public StackCell(){};
		public StackCell(Tile t)
		{
			this.t			= t;
			this.remaining	= t.getCardinal();
		}
		public StackCell getClone()
		{
			StackCell res = new StackCell();
			res.t			= this.t.getClone();
			res.remaining	= this.remaining;
			return res;
		}

		// Local methods
		public boolean equals(StackCell sc)	{return (sc.t.getTileID().equals(t.getTileID()));}
		public int compare(StackCell arg0, StackCell arg1)
		{
			if (arg0.remaining   < arg1.remaining)	return -1;
			if (arg0.remaining   > arg1.remaining)	return 1;
			else									return 0;
		}
	}
}