package data;

import java.util.LinkedList;
import util.Direction;





public class Tile
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private LinkedList<Path>	pathList;
	private boolean				replaceable;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Tile()
	{
		this.pathList = new LinkedList<Path>();
	}

// --------------------------------------------
// Local Methods:
// --------------------------------------------
	public boolean	replaceable()	{return this.replaceable;}
	public void		turnLeft()		{for (Path p: pathList)	p.turnLeft();}
	public void		turnRight()		{for (Path p: pathList)	p.turnRight();}
	public void		turnHalf()		{for (Path p: pathList)	p.turnHalf();}

// --------------------------------------------
// Class path:
// Represents a path between two cardinal directions
// --------------------------------------------
	public class Path
	{
		public int d0;
		public int d1;
		public Path(int d0, int d1)
		{
			this.d0	= d0;
			this.d1	= d1;
		}
		public void turnLeft()	{d0 = Direction.turnLeft(d0);	d1 = Direction.turnLeft(d1);}
		public void turnRight()	{d0 = Direction.turnRight(d0);	d1 = Direction.turnRight(d1);}
		public void turnHalf()
		{
			d0 = Direction.turnLeft(d0);
			d0 = Direction.turnLeft(d0);
			d1 = Direction.turnRight(d1);
			d1 = Direction.turnRight(d1);
		}
	}
}