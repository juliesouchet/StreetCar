package main.java.data;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

import main.java.util.Direction;







public class PathFinder implements Serializable
{
	private static final long serialVersionUID = -6422634525567983202L;



	/**============================================================
	 * @return the shortest path between pSrc and pDst in the given board, starting from each of of accessible directions.</br>
	 * If no path exists, null is returned.
	 * Uses the A* algorithm.
	 * The source and destination point are returned in the solution.
	 ==============================================================*/
	public  LinkedList<Point> getPath(Data data, Point pSrc, Point pDst)
	{
		LinkedList<Point> res;
		int w = data.getWidth();
		int h = data.getHeight();
		if ((pSrc.x < 0) || (pSrc.x >= w) ||
			(pSrc.y < 0) || (pSrc.y >= h))	throw new RuntimeException("Source point out of the ground: "		+ pSrc);
		if ((pDst.x < 0) || (pDst.x >= w) ||
			(pDst.y < 0) || (pDst.y >= h))	throw new RuntimeException("Destination point out of the ground: "		+ pSrc);
		if (pSrc.equals(pDst))				throw new RuntimeException("pSrc == pDst == " + pSrc);

		if (data.getTile(pSrc).isEmpty()) return null;

		for (Direction initialDir: Direction.DIRECTION_LIST)
		{
			res = this.getPath(data, pSrc, pDst, initialDir);
			if (res != null) return null;
		}
		return null;
	}
	/**============================================================
	 * @return the shortest path between pSrc and pDst in the given board, starting from the given direction.</br>
	 * If no path exists, null is returned.</br>
	 * Uses the A* algorithm.</br>
	 * The source and destination point are returned in the solution.</br>
	 ==============================================================*/
	public LinkedList<Point> getPath(Data data, Point pSrc, Point pDst, Direction initialDir)
	{
		int w			= data.getWidth();
		int h			= data.getHeight();
		Tile t			= data.getTile(pSrc);
		int tileBords	= t.getAccessibleDirections();
		if (!initialDir.isDirectionInList(tileBords)) return null;

		Integer[][]		weight		= new Integer[w][h];
		Point[][]		previous	= new Point	 [w][h];
		PriorityQueue	pq			= new PriorityQueue();
		LinkedList<Point> neighbor;
		Knot y;
		int pz;
		Direction dirNeighbor0;

		for (int i=0; i<w; i++)											// Initialisation
		{
			for (int j=0; j<h; j++)
			{
				weight	[i][j] = null;
				previous[i][j] = null;
			}
		}
		pq.add(new Knot(pSrc, initialDir, 0, heuristic(pSrc, pDst)));
		weight[pSrc.x][pSrc.y] = 0;

		while (!pq.isEmpty())
		{
			y =  pq.remove();
			if (y.getElem().equals(pDst))	return reversePath(previous, pSrc, pDst);
			neighbor = data.getAccessibleNeighborsPositions(y.p.x, y.p.y, y.getDirection());
			for (Point z: neighbor)
			{
				pz	= y.getDistPrev()+1;
				t	= data.getTile(z);
				if ((weight[z.x][z.y] == null) || (pz < weight[z.x][z.y]))
				{
					dirNeighbor0 = y.getDirection().turnHalf();
					for (Direction dirNeighbor1: Direction.DIRECTION_LIST)
					{
						if (dirNeighbor1.equals(dirNeighbor0))		continue;
						if (!t.isPath(dirNeighbor0, dirNeighbor1))	continue;
						weight[z.x][z.y] = pz;
						pq.add(new Knot(z, dirNeighbor1, pz, heuristic(z, pDst)));
						previous[z.x][z.y] = new Point(y.getElem().x, y.getElem().y);
					}
				}
			}
		}
		return null;
	}
// -----------------------------------------
// Private Methods
// -----------------------------------------
	private static int heuristic(Point src, Point dst)
	{
		int dx = dst.x - src.x;
		int dy = dst.y - src.y;
		return (dx*dx + dy*dy);
	}
	/**============================================================
	 * @return Goes the way created by the algorithm and returns the 
	 * list of point if the path exists and nothing if not
	 ==============================================================*/
	private static LinkedList<Point> reversePath(Point[][] previous, Point pSrc, Point pDst)
	{
		LinkedList<Point> res = new LinkedList<Point>();
		Point p = pDst;

		res.addFirst(p);
		while(!p.equals(pSrc))
		{
			p = previous[p.x][p.y];
			if (p == null)	return null;
			res.addFirst(p);
		}
		return res;
	}

// -----------------------------------------
// Private classes
// -----------------------------------------
	private static class Knot
	{
		// Attributes
		private Point		p;
		private Direction	dir;
		private int			distPrev;
		private int			distNext;

		// Builder
		public Knot(Point p, Direction dir, int distPrev, int distNext)
		{
			this.p			= p;
			this.dir		= dir;
			this.distPrev	= distPrev;
			this.distNext	= distNext;
		}

		// Local methods
		public Point		getElem()		{return p;}
		public Direction	getDirection()	{return this.dir;}
		public int			getPrio()		{return (distPrev + distNext);}
		public int			getDistPrev()	{return (distPrev);}
	}
	private static class PriorityQueue
	{
		// Attributes
		private LinkedList<Knot>	queue;

		// Builder
		public PriorityQueue()	{this.queue = new LinkedList<Knot>();}

		// Local methods
		public boolean isEmpty()	{return queue.isEmpty();}
		public Knot remove()
		{
			if (queue.isEmpty())	throw new RuntimeException("Empty queue");

			Knot res = queue.get(0);
			queue.remove(0);
			return res;
		}
		public void add(Knot k)
		{
			if (queue.isEmpty())	{queue.add(k); return;}

			for (int i=0; i<queue.size(); i++)
			{
				if (queue.get(i).getPrio() < k.getPrio()) continue;
				queue.add(i, k);
				return;
			}
			queue.addLast(k);
		}
	}
}