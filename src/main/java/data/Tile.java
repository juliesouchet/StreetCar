package main.java.data;

import java.util.LinkedList;

import main.java.util.Direction;





public class Tile
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private LinkedList<Path>	pathList;
	private boolean				isTree;
	private boolean				isBuilding;
	private boolean				isStop;
	private boolean				isTerminus;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Tile(Tile t)
	{
		this.isTree		= t.isTree;
		this.isBuilding	= t.isBuilding;
		this.isTerminus	= t.isTerminus;
		this.isStop		= t.isStop;
		this.pathList	= new LinkedList<Path>(t.pathList);
	}
	public Tile(LinkedList<Path> pathList, boolean isTree, boolean isBuilding, boolean isTerminus, boolean isStop) throws RuntimeException
	{
		if ((isBuilding)&& (!pathList.isEmpty()))throw new RuntimeException("A tile can not be a building and contain a path");
		if ((isBuilding)&& (isTerminus))		 throw new RuntimeException("A tile can not be a building and contain a terminus");

		this.pathList	= new LinkedList<Path>(pathList);
		this.isBuilding	= isBuilding;
		this.isTree		= isTree;
		this.isTerminus	= isTerminus;
		this.isStop		= isStop;
	}
	
	public Tile cloneTile(Tile t)
	{
		return new Tile(t);
	}

// --------------------------------------------
// Setters/getters:
// --------------------------------------------
	public void		turnLeft()		{for (Path p: pathList)	p.turnLeft();}  // counterclockwise
	public void		turnRight()		{for (Path p: pathList)	p.turnRight();} // clockwise
	public void		turnHalf()		{for (Path p: pathList)	p.turnHalf();}
	public boolean	isTree()		{return this.isTree;}
	public boolean	isBuilding()	{return this.isBuilding;}
	public boolean	isTerminus()	{return this.isTerminus;}
	public boolean	isStop()		{return this.isStop;}
	public boolean	isEmpty()		{return ((!this.isBuilding) && (!this.isTree) && (this.pathList.isEmpty()));}
	public LinkedList<Integer> getAccessibleDirections()
	{
		LinkedList<Integer> res = new LinkedList<Integer>();

		for (Path p: pathList)
		{
			if (!res.contains(p.end0))	res.add(p.end0);
			if (!res.contains(p.end1))	res.add(p.end1);
		}
		return res;
	}
	/**=================================================
	 * @return true if the current tile can be replaced by t
	 * This function does not check if t is suitable for the current tile neighbors
	 * @param additionalPath: output parameter (can be null). Is filled with the t's paths that are not in the current tile
	 ===================================================*/
	public boolean isReplaceable(Tile t, LinkedList<Path> additionalPath)
	{
		LinkedList<Path> lPath	= new LinkedList<Path>(this.pathList);
		LinkedList<Path> tPath	= new LinkedList<Path>(t.pathList);

		if (this.isTree)	 return false;
		if (this.isBuilding) return false;
		if (this.isTerminus) return false;

		for (Path pt: tPath)								// Remove the common paths
			for (Path pl: tPath)
				if (pt.equals(pl))	{tPath.remove(pt); lPath.remove(pl); break;}

		if (!lPath.isEmpty())	return false;				// Case local tile is not contained in t
		if (additionalPath != null)							// Cas replaceable
		{
			additionalPath.clear();
			additionalPath.addAll(tPath);					//		Add all the new paths
		}
		return true;
	}

// --------------------------------------------
// class Path :
// Represents a path between two cardinal directions
// --------------------------------------------
	public class Path
	{
		// Attributes
		public int end0;
		public int end1;

		// Builder
		public Path(int d0, int d1)
		{
			Direction.checkDirection(d0);
			Direction.checkDirection(d1);
			this.end0	= d0;
			this.end1	= d1;
		}
		// Setter
		public void turnLeft()	{end0 = Direction.turnLeft(end0);	end1 = Direction.turnLeft(end1);}
		public void turnRight()	{end0 = Direction.turnRight(end0);	end1 = Direction.turnRight(end1);}
		public void turnHalf()
		{
			end0 = Direction.turnLeft(end0);
			end0 = Direction.turnLeft(end0);
			end1 = Direction.turnRight(end1);
			end1 = Direction.turnRight(end1);
		}
		public boolean equals(Path p)	{return ((end0 == p.end0) && (end1 == p.end1));}
	}
}