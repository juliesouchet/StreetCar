package main.java.data;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

import main.java.util.Direction;







public class PathFinder implements Serializable
{
	private static final long serialVersionUID = -6422634525567983202L;

	
	
	
	/**============================================================
	 * @return the shortest path between pSrc and pDst in the given board. If no path exists, null is returned.
	 * Uses the A* algorithm.
	 * The source and destination point are returned in the solution.
	 ==============================================================*/
	public  LinkedList<Point> getPath(Data data, Point previousInitial, Point pSrc, Point pDst)
	{
		int w = data.getWidth();
		int h = data.getHeight();
		if ((pSrc.x < 0) || (pSrc.x >= w) ||
			(pSrc.y < 0) || (pSrc.y >= h))	throw new RuntimeException("Source point out of the ground: "		+ pSrc);
		if ((pDst.x < 0) || (pDst.x >= w) ||
			(pDst.y < 0) || (pDst.y >= h))	throw new RuntimeException("Destination point out of the ground: "		+ pSrc);
		if (pSrc.equals(pDst))				throw new RuntimeException("pSrc == pDst == " + pSrc);

		Integer[][]		weight		= new Integer[w][h];
		Point[][]		previous	= new Point	 [w][h];
		PriorityQueue	pq			= new PriorityQueue();
		LinkedList<Point> neighbor;
		Knot y;
		int pz;
		Point pOld, p;

		for (int i=0; i<w; i++)											// Initialisation
		{
			for (int j=0; j<h; j++)
			{
				weight	[i][j] = null;
				previous[i][j] = null;
			}
		}
		pq.add(new Knot(pSrc, 0, heuristic(pSrc, pDst)));
		weight[pSrc.x][pSrc.y] = 0;
		previous[pSrc.x][pSrc.y] = previousInitial;

		while (!pq.isEmpty())
		{
			y		=  pq.remove();
			p		= y.getElem();
			pOld	= previous[p.x][p.y];
			if (p.equals(pDst))	return reversePath(previous, pSrc, pDst);
			neighbor = data.getAccessibleNeighborsPositions(p.x, p.y);
			for (Point pNext: neighbor)
			{
				if ((pOld != null) && (!isSimplePath(data, pOld, p, pNext)))	continue;
				pz = y.getDistPrev()+1;
				if ((weight[pNext.x][pNext.y] == null) || (pz < weight[pNext.x][pNext.y]))
				{
					weight[pNext.x][pNext.y] = pz;
					pq.add(new Knot(pNext, pz, heuristic(pNext, pDst)));
					previous[pNext.x][pNext.y] = new Point(y.getElem().x, y.getElem().y);
				}
			}
		}
		return null;
//		return reversePath(previous, pSrc, pDst);
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
	public boolean isSimplePath(Data data, Point pOld, Point p, Point pNext)
	{
		Tile tOld	= data.getTile(pOld);
		Tile t		= data.getTile(p);
		Tile tNext	= data.getTile(pNext);
/*
System.out.println("pOld : " + pOld + ",    tOld = " + t);
System.out.println("p    : " + p	+ ",    t    = " + t);
System.out.println("pNext: " + pNext+ ",    tNext= " + t);
System.out.println("\n--------------------------------------------\n");
*/



		for (Direction dir0: Direction.DIRECTION_LIST)
		{
			if (!tOld.isPathTo(dir0))							continue;
			if (!p.equals(dir0.getNeighbour(pOld.x, pOld.y)))	continue;
			for (Direction dir1: Direction.DIRECTION_LIST)
			{
				if (!t.isPathTo(dir0.turnHalf()))				continue;
				if (!t.isPathTo(dir1))							continue;
				if (!pNext.equals(dir1.getNeighbour(p.x, p.y)))	continue;
				if (!t.isPath(dir0.turnHalf(), dir1))			continue;
				if (!tNext.isPathTo(dir1.turnHalf()))			continue;
				else											return true;
			}
		}
		return false;
	}

// -----------------------------------------
// Private classes
// -----------------------------------------
	private static class Knot
	{
		// Attributes
		private Point	p;
		private int		distPrev;
		private int		distNext;

		// Builder
		public Knot(Point p, int distPrev, int distNext)
		{
			this.p			= p;
			this.distPrev	= distPrev;
			this.distNext	= distNext;
		}

		// Local methods
		public Point	getElem()		{return p;}
		public int		getPrio()		{return (distPrev + distNext);}
		public int		getDistPrev()	{return (distPrev);}
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