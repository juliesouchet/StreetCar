package main.java.data;

import java.awt.Point;
import java.util.LinkedList;


public class PathFinder
{
	/**============================================================
	 * @return the shortest path between pSrc and pDst in the guiven board. If no path exists, null is returned.
	 * Uses the A* algorithm.
	 * The source and destination point are returned in the solution.
	 ==============================================================*/
	public static LinkedList<Point> getPath(Tile[][] board, Point pSrc, Point pDst)
	{
		int w = board.length;
		int h = board[0].length;
		if ((pSrc.x < 0) || (pSrc.x >= w) ||
			(pSrc.y < 0) || (pSrc.y >= h))	throw new RuntimeException("Source point out of the ground: "		+ pSrc);
		if ((pDst.x < 0) || (pDst.x >= w) ||
			(pDst.y < 0) || (pDst.y >= h))	throw new RuntimeException("Destination point out of the ground: "		+ pSrc);
		if (pSrc.equals(pDst))				throw new RuntimeException("pSrc == pDst == " + pSrc);

		Integer[][]	weight		= new Integer[w][h];
		Point[][]	previous	= new Point	 [w][h];
		Fap<Point>	f			= new Fap<Point>();
		LinkedList<Point> voisin;
		NoeudAstar<Point> y;
		int pz;

		for (int i=0; i<t.largeur(); i++)								// Initialisation
		{
			for (int j=0; j<t.hauteur(); j++)
			{
				weight	[i][j] = null;
				previous[i][j] = null;
			}
		}
		f.add(new NoeudAstar<Point>(pSrc, 0, heuristique(pSrc, pDst)));
		weight[pSrc.x][pSrc.y] = 0;

		while (!f.isEmpty())
		{
			y = (NoeudAstar<Point>) f.remove();
			if (y.equals(pDst))	return reversePath(previous, pSrc, pDst);
			voisin	= t.voisinLibre(y.getElem());
			for (Point z: voisin)
			{
				pz = y.getDistPrev()+1;
				if ((weight[z.x][z.y] == null) || (pz < weight[z.x][z.y]))
				{
					weight[z.x][z.y] = pz;
					f.add(new NoeudAstar<Point>(z, pz, heuristique(z, pDst)));
					previous[z.x][z.y] = new Point(y.getElem().x, y.getElem().y);
				}
			}
		}
		return reversePath(previous, pSrc, pDst);
	}

// -----------------------------------------
// Private Methods
// -----------------------------------------
	private int heuristic(Point src, Point dst)
	{
		int dx = dst.x - src.x;
		int dy = dst.y - src.y;
		return (dx*dx + dy*dy);
	}
	/**============================================================
	 * @return Goes the way created by the algorithm and returns the 
	 * list of point if the path exists and nothing if not
	 ==============================================================*/
	private LinkedList<Point> reversePath(Point[][] previous, Point pSrc, Point pDst)
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
// Private Methods
// -----------------------------------------
	private class PriorityQueue
	{
		// Attributs
		private LinkedList<Knot>	queue;

		// Builder
		public PriorityQueue()	{this.queue = new LinkedList<Knot>();}

	// -----------------------------------------
	// Methode locale
	// -----------------------------------------
		public boolean isEmpty()	{return file.isEmpty();}
		public Noeud<E> remove()
		{
			if (file.isEmpty())	throw new RuntimeException("File vide");

			Noeud<E> res = file.get(0);
			file.remove(0);
			return res;
		}
		public void add(Noeud<E> n)
		{
			if (file.isEmpty())	{file.add(n); return;}

			for (int i=0; i<file.size(); i++)
			{
				if (file.get(i).getPrio() < n.getPrio()) continue;
				file.add(i, n);
				return;
			}
			file.addLast(n);
		}
	}
}