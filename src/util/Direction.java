package util;

import java.awt.Point;






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
	public static int turnLeft(int d)
	{
		checkDirection(d);
		if (d != maxVal)	return d++;
		else				return minVal;
	}
	public static int turnRight(int d)
	{
		checkDirection(d);
		if (d != minVal)	return d--;
		else				return maxVal;
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
			case EAST:	return new Point(x+1,	y);	// East
		}
	}
}