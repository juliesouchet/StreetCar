package main.java.util;

import java.awt.Point;
import java.util.Iterator;






public class Direction
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final int	WEST	= 0;		// Must keep this order
	public static final int	NORTH	= 1;
	public static final int	EAST	= 2;
	public static final int	SOUTH	= 3;

	private static int minVal		= 0;
	private static int maxVal		= 3;

// --------------------------------------------
// Local Methods:
// --------------------------------------------
	public static String toString(int d)
	{
		switch (d)
		{
			case WEST	: return "W";
			case NORTH	: return "N";
			case EAST	: return "E";
			case SOUTH	: return "S";
			default		: throw new RuntimeException("Unknown direction: " + d);
		}
	}
	public static int turnLeft(int d)
	{
		checkDirection(d);
		if (d != maxVal)	return d+1;
		else				return minVal;
	}
	public static int turnRight(int d)
	{
		checkDirection(d);
		if (d != minVal)	return d-1;
		else				return maxVal;
	}
	public static int turnHalf(int d)
	{
		checkDirection(d);
		int res = turnLeft(d);
		return turnLeft(res);
	}
	public static void checkDirection(int d)
	{
		if ((d < minVal) || (d > maxVal)) throw new RuntimeException("Unknown direction: " + d);
	}
	public static Point getNeighbour(int x, int y, int d)
	{
		checkDirection(d);
		switch(d)
		{
			case NORTH:	return new Point(x,	y-1);	// North
			case SOUTH:	return new Point(x,	y+1);	// South
			case WEST:	return new Point(x-1,	y);	// West
			default:	return new Point(x+1,	y);	// East
		}
	}
	public static Iterator<Integer> getIterator()
	{
		return new MyIterator();
	}

// --------------------------------------------
// Direction Iterator:
// --------------------------------------------
	private static class MyIterator implements Iterator<Integer>
	{
		private int d = minVal;
		public boolean hasNext(){return (d <= maxVal);}
		public Integer next()
		{
			checkDirection(d);
			int res = d;
			d++;
			return res;
		}
		public void remove()	{throw new RuntimeException("Not implemented");}
	}
}