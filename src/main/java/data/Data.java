package main.java.data;


import java.awt.Point;
import java.util.LinkedList;

import main.java.data.Tile.Path;
import main.java.util.Direction;








public class Data
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Tile[][]	board;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Data(int width, int height)
	{
		this.board = new Tile[3][3];
	}

// --------------------------------------------
// Getter/Setter:
// --------------------------------------------
	public int		getWidth()	{return this.board.length;}
	public int		getHeight()	{return this.board[0].length;}
	public boolean	isInBoard(int x, int y)
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
/***	public boolean isAcceptableTilePut(int x, int y, Tile t)
	{
		LinkedList<Path> additionalPath = new LinkedList<Path>();
		LinkedList<Integer> accessibleDirection;
		Tile oldT = this.board[x][y];

		if (!oldT.isReplaceable(t, additionalPath))	return false;										// Check wether t contains the old t (remove Tile and Rule C)

		Tile nt = new Tile(additionalPath, t.isTree(),  t.isBuilding(), t.isTerminus());				// Check wether the new tile is suitable with the <x, y> neighborhoud
		accessibleDirection = nt.getAccessibleDirections();
		for (int d: accessibleDirection)
		{
			Point neighbor = Direction.getNeighbour(x, y, d);
			Tile neighborT = this.board[x][y];
			if (!this.isInBoard(neighbor.x, neighbor.y))								return false;	//		Neighbor tile out of board
			if ((this.isOnEdge(neighbor.x, neighbor.y)) && (!neighborT.isTerminus()))	return false;	//		Rule A
			if (neighborT.isBuilding())													return false;	//		Rule B
// TODO: RULE E, F
		}
	}
*/	/**============================================================
	 * @return a list of the neighbor coordinates
	 ==============================================================*/
	public LinkedList<Point> getNeighboursPosition(int x, int y)
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
			if (isInBoard(next.x, next.y))	res.add(next);
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
			if (isInBoard(next.x, next.y))	res.add(new Tile(board[next.x][next.y]));
		}
		return res;
	}	
}