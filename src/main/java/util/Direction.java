package main.java.util;

import java.awt.Point;






public enum Direction implements CloneableInterface<Direction>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	WEST	(0),
	NORTH	(1),
	EAST	(2),
	SOUTH	(3);
	public static final Direction[] DIRECTION_LIST = {WEST, NORTH, EAST, SOUTH};

	public int dir;

// --------------------------------------------
// Builder:
// --------------------------------------------
	private Direction(int dir){this.dir = dir;}

// --------------------------------------------
// Local Methods:
// --------------------------------------------
	public Direction getClone()
	{
		Direction res = WEST;
		res.dir = this.dir;
		return res;
	}
	public String toNiceString()
	{
		switch (this)
		{
			case WEST	: return "WEST";
			case NORTH	: return "NORTH";
			case EAST	: return "EAST";
			case SOUTH	: return "SOUTH";
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
	public void turnLeft()
	{
		switch(this.dir)
		{
			case 0	: this.dir = SOUTH.dir;	break;
			case 1	: this.dir = WEST.dir;	break;
			case 2	: this.dir = NORTH.dir;	break;
			case 3	: this.dir = EAST.dir;	break;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public void turnRight()
	{
		switch(this.dir)
		{
			case 0	: this.dir = NORTH.dir;	break;
			case 1	: this.dir = EAST.dir;	break;
			case 2	: this.dir = SOUTH.dir;	break;
			case 3	: this.dir = WEST.dir;	break;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public void turnHalf()
	{
		switch(this.dir)
		{
			case 0	: this.dir = EAST.dir;	break;
			case 1	: this.dir = SOUTH.dir;	break;
			case 2	: this.dir = WEST.dir;	break;
			case 3	: this.dir = NORTH.dir;	break;
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}
	public Point getNeighbour(int x, int y)
	{
		switch(this.dir)
		{
			case 1	: return new Point(x,	y-1);	// North
			case 3	: return new Point(x,	y+1);	// South
			case 0	: return new Point(x-1,	y);		// West
			case 2	: return new Point(x+1,	y);		// East
			default	: throw new RuntimeException("Unknown direction: " + this);
		}
	}

	public static Direction parse(int d)
	{
		switch(d)
		{
			case 0: return WEST;
			case 1: return NORTH;
			case 2: return EAST;
			case 3: return SOUTH;
			default		: throw new RuntimeException("Unknown direction: " + d);
		}
	}
}