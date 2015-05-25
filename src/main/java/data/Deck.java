package main.java.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import main.java.util.CloneableInterface;
import main.java.util.Copier;







public class Deck implements Serializable, CloneableInterface<Deck>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final long	serialVersionUID	= 1683108311031397048L;
	public static final String	stackDirectory		= "src/main/resources/images/tiles/";

	private ArrayList<StackCell>	stack;		// Sorted list using the attribute remaining in a descending order

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
if (t.isDeckTile()) System.out.println(t.toString());
		}
		Collections.sort(this.stack, new StackCell());
		Collections.reverse(stack);
	}
	private Deck(boolean b){}
	public Deck getClone()
	{
		Deck res = new Deck(false);
		Copier<StackCell> cp = new Copier<StackCell>();
		res.stack = cp.copyList(this.stack);
		return res;
	}

// --------------------------------------------
// Setters/getters: pickup 
// --------------------------------------------
	/**=====================================================
	 * @return Pick up an element from the stack using
	 * a uniformly distributed probability distribution
	 * The returned element is removed from the stack.
	 * The stack is kept sorted
	 =======================================================*/
	public Tile drawTile()
	{
		StackCell sc = null, sc1;
		Tile res = null;
		int size	= this.getRemainingStackSize();
		int rnd		= (new Random()).nextInt(size);
		int s=0, i=0;

		if (size == 0)	throw new RuntimeException("Empty stack");

		while(s < size)												// Pick a random element from the stack
		{
			sc = this.stack.get(i);
			s += sc.remaining;
			if (rnd <= s)	{res = sc.t; break;}
			i ++;
		}
		if (s >= size)	throw new RuntimeException();
		sc.remaining --;											// Remove the element from the stack
		for (int j=i+1; j<size-1; j++)								// Keep the stack sorted
		{
			sc1 = this.stack.get(j);
			if (sc1.remaining > sc.remaining) Collections.swap(stack, j, j+1);
			else break;
			sc = sc1;
		}
		return res.getClone();
	}
	/**=====================================================
	 * @return the remaining size of the stack
	 =======================================================*/
	public int getRemainingStackSize()
	{
		int res = 0;

		for (StackCell sc: stack)	res += sc.remaining;
		return res;
	}
	/**=====================================================
	 * @return the number of remaining tiles that match the given tile
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
		return (double)((double)sc.remaining / (double)getRemainingStackSize());
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private StackCell pickStackCell(Tile t)
	{
		for (StackCell sc: stack) if (sc.t.getTileID().equals(t.getTileID())) return sc;
		return null;
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