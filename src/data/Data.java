package data;


import java.awt.Point;
import java.util.LinkedList;

import util.Direction;

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
// Local Methods:
// --------------------------------------------
	public int		getWidth()	{return this.board.length;}
	public int		getHeight()	{return this.board[0].length;}
	public boolean	isAccessibleNeighbor(int x, int y, int direction)
	{
		Direction.checkDirection(direction);
		
	}
	/**============================================================
	 * @return a list of the neighbor coordinates
	 ==============================================================*/
	public LinkedList<Point> getNeighbour(int x, int y)
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
	 * @return a list of the neighbor tiles that can be acceded
	 * from the <x,y> tile
	 ==============================================================*/
	public LinkedList<Tile> getAccessibleNeighbourTile(int x, int y)
	{
		LinkedList<Tile> res = new LinkedList<Tile>();
		Tile next;

		for (Path p: pathList)
		{
			next = getCell()
		}
	}

}