package main.java.data;


import java.awt.Point;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import main.java.data.Tile.Path;
import main.java.game.UnknownBoardNameException;
import main.java.util.Direction;



public class Data
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final	String					boardDirectory	= "src/main/resources/";
	private Tile[][]							board;
	private String								gameName;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Data(String gameName, String boardName) throws UnknownBoardNameException
	{
		this.board		= scanBoardFile(boardName);
		this.gameName	= new String(gameName);
	}

// --------------------------------------------
// Getter/Setter:
// --------------------------------------------
	public int		getWidth()										{return this.board.length;}
	public int		getHeight()										{return this.board[0].length;}
	public Tile		getTile(int x, int y)							{return new Tile(this.board[x][y]);}
	public void		setTile(int x, int y, Tile t)					{this.board[x][y] = t;}
	public String	getGameName()									{return new String(this.gameName);}
	public boolean	isWithinnBoard(int x, int y)
	{
		if ((x < 0) || (x >= getWidth()))	return false;
		if ((y < 0) || (y >= getHeight()))	return false;
		return true;
	}
	public boolean	isOnEdge(int x, int y)
	{
		if ((x == 0) || (x == getWidth()-1))	return true;
		if ((y == 0) || (y == getHeight()-1))	return true;
		return false;
	}
	/**===============================================================
	 * @return if the deposit of the tile t on the board at the position <x, y> is possible
	 =================================================================*/
	public boolean isAcceptableTilePlacement(int x, int y, Tile t)
	{
		LinkedList<Path> additionalPath = new LinkedList<Path>();
		LinkedList<Integer> accessibleDirection;
		Tile oldT = this.board[x][y];

		if (!oldT.isReplaceable(t, additionalPath))	return false;										// Check whether t contains the old t (remove Tile and Rule C)

		Tile nt = new Tile(additionalPath, t.isTree(),  t.isBuilding(), t.isTerminus(), t.isStop());	// Check whether the new tile is suitable with the <x, y> neighborhood
		accessibleDirection = nt.getAccessibleDirections();
		for (int d: accessibleDirection)
		{
			Point neighbor = Direction.getNeighbour(x, y, d);
////////	if (!this.isWithinnBoard(neighbor.x, neighbor.y))							return false;	//		Neighbor tile out of board
			Tile neighborT = this.board[x][y];
			if ((this.isOnEdge(neighbor.x, neighbor.y)) && (!neighborT.isTerminus()))	return false;	//		Rule A
			if (neighborT.isEmpty())													continue;		//		Rule E (step 1)
			if (neighborT.isBuilding())													return false;	//		Rule B
			if (!neighborT.isPathTo(Direction.turnHalf(d)))								return false;	//		Rule E (step 2)
		}
		LinkedList<Integer> plt = this.getPathsLeadingToTile(x, y);										//		Rule D
		for (int dir: plt)	if (!nt.isPathTo(dir))										return false;
		return true;
	}
	/**============================================================
	 * @return a list of the neighbor coordinates.  This function
	 * does not check whether the neighbour is accessible through a path on the current tile
	 ==============================================================*/
	public LinkedList<Point> getNeighbourPositions(int x, int y)
	{
		LinkedList<Point> res = new LinkedList<Point>();
		int w = getWidth();
		int h = getHeight();

		if (y > 0)		res.add(new Point(x,	y-1));	// North
		if (y < h-1)	res.add(new Point(x,	y+1));	// South
		if (x > 0)		res.add(new Point(x-1,	y));	// West
		if (x < w-1)	res.add(new Point(x+1,	y));	// East
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor coordinates that can be acceded from the <x,y> cell
	 ==============================================================*/
	public LinkedList<Point> getAccessibleNeighboursCoordinates(int x, int y)
	{
		LinkedList<Point>	res = new LinkedList<Point>();
		LinkedList<Integer>	ad	= board[x][y].getAccessibleDirections();	// List of the reachable directions

		for (int d: ad)														// For each accesible position
		{
			Point next = Direction.getNeighbour(x, y, d);
			if (isWithinnBoard(next.x, next.y))	res.add(next);
		}
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor tiles that can be acceded from the <x,y> cell
	 ==============================================================*/
	public LinkedList<Tile> getAccessibleNeighboursTiles(int x, int y)
	{
		LinkedList<Tile>	res = new LinkedList<Tile>();
		LinkedList<Integer>	ad	= board[x][y].getAccessibleDirections();	// List of the reachable directions

		for (int d: ad)														// For each accesible position
		{
			Point next = Direction.getNeighbour(x, y, d);
			if (isWithinnBoard(next.x, next.y))	res.add(new Tile(board[next.x][next.y]));
		}
		return res;
	}
	/**============================================================
	 * @returns the list of directions d such as the neighbor d has a path to the current tile <x, y>
	 ==============================================================*/
	public LinkedList<Integer> getPathsLeadingToTile(int x, int y)
	{
		LinkedList<Integer>	res	= new LinkedList<Integer>();
		Iterator<Integer>	di	= Direction.getIterator();
		int direction, neighborDir;
		Point neighbor;
		Tile neighborTile;

		while (di.hasNext())
		{
			direction	= di.next();
			neighbor	= Direction.getNeighbour(x, y, direction);
			if (!this.isWithinnBoard(neighbor.x, neighbor.y))	continue;
			neighborTile= board[neighbor.x][neighbor.y];
			neighborDir	= Direction.turnHalf(direction);
			if (neighborTile.isPathTo(neighborDir)) res.add(direction);
		}
		return res;
	}
	/**=========================================================================
	 * @param building: Position of a building
	 * @return the position of the stop next to the building
	 * @throws runtimeException if the parameter is not a building
	 ===========================================================================*/
	public Point hasStopNextToBuilding(Point building)
	{
		Tile t = this.board[building.x][building.y];
		Iterator<Integer> dirIt = Direction.getIterator();
		Point p;

		if (!t.isBuilding())	throw new RuntimeException("The point " + building + " is not a building");
		while(dirIt.hasNext())
		{
			p = Direction.getNeighbour(building.x, building.y, dirIt.next());
			if (this.isWithinnBoard(p.x, p.y))	continue;
			t = this.board[p.x][p.y];
			if (t.isStop())	return p;
		}
		return null;
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private Tile[][] scanBoardFile(String boardName) throws UnknownBoardNameException
	{
		File f = new File(boardDirectory + boardName);
		Scanner sc;

		try					{sc = new Scanner(f);}
		catch(Exception e)	{throw new UnknownBoardNameException();}

		sc.close();
		return null;
	}
}