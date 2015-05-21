package main.java.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;







public class Stack
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final String	stackDirectory	= "src/main/resources/stack";

	private ArrayList<StackCell>	stack;		// Sorted list using the attribute remaining in a descending order

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Stack() throws RuntimeException
	{
		File f;
		String tileList[];

		this.stack	= new ArrayList<StackCell>();
		f			= new File(stackDirectory);
		tileList	= f.list();
		for (String str: tileList)
		{
			Tile t = new Tile(str);
			if (t.isStackTile()) this.stack.add(new StackCell(t));
		}
		Collections.sort(this.stack, new StackCell());
		Collections.reverse(stack);
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
			if (sc.remaining >= sc.remaining) Collections.swap(stack, j, j+1);
			else break;
			sc = sc1;
		}
		return new Tile(res);
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
	public class StackCell implements Comparator<StackCell>
	{
		// Attributes
		public Tile	t;
		public int	remaining;

		// Builder
		public StackCell(){};
		public StackCell(Tile t)
		{
			this.t			= t;
			this.remaining	= t.getCardinal();
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