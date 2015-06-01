package main.java.util;

import java.awt.Point;
import java.io.Serializable;







public enum Direction implements Serializable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	WEST	(0),
	NORTH	(1),
	EAST	(2),
	SOUTH	(3),
	WRONG	(-1);
	public static final Direction[] DIRECTION_LIST = {WEST, NORTH, EAST, SOUTH};

	public int dir;

// --------------------------------------------
// Builder:
// --------------------------------------------
	private Direction(int dir){this.dir = dir;}

// --------------------------------------------
// Getter:
// --------------------------------------------
	public int		getVal()							{return this.dir;}
	public int		addDirectionToList	(int dirList)	{return  (dirList | (1 << this.dir));}
	public boolean	isDirectionInList	(int dirList)	{return ((dirList & (1 << this.dir)) != 0);}

// --------------------------------------------
// Local Methods:
// --------------------------------------------
	public Direction turnLeft()
	{
		switch(this.dir)
		{
			case 0	: return SOUTH;
			case 1	: return WEST;
			case 2	: return NORTH;
			case 3	: return EAST;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public Direction turnRight()
	{
		switch(this.dir)
		{
			case 0	: return NORTH;
			case 1	: return EAST;
			case 2	: return SOUTH;
			case 3	: return WEST;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public Direction turnHalf()
	{
		switch(this.dir)
		{
			case 0	: return EAST;
			case 1	: return SOUTH;
			case 2	: return WEST;
			case 3	: return NORTH;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public Point getNeighbour(int x, int y)
	{
		switch(this.dir)
		{
			case 0	: return new Point(x-1,	y);		// West
			case 1	: return new Point(x,	y-1);	// North
			case 2	: return new Point(x+1,	y);		// East
			case 3	: return new Point(x,	y+1);	// South
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}

	public static Direction parse(int d)
	{
		switch(d)
		{
			case 0	: return WEST;
			case 1	: return NORTH;
			case 2	: return EAST;
			case 3	: return SOUTH;
			default	: throw new RuntimeException("Unknown direction: " + d);
		}
	}
	public String toNiceString()
	{
		switch (this.dir)
		{
			case 0	: return "WEST";
			case 1	: return "NORTH";
			case 2	: return "EAST";
			case 3	: return "SOUTH";
			default		: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public String toString()
	{
		switch (this.dir)
		{
			case 0	: return "0";
			case 1	: return "1";
			case 2	: return "2";
			case 3	: return "3";
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
}