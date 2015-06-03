package main.java.data;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;






public class PathFinderMulti implements Serializable
{
	private static final long serialVersionUID = 696466158277006823L;

	
	
	
	
	
	
	/**========================================================
	 * @return the number of diffrent paths that:</br>
	 * 		- Have the given length</br>
	 * 		- Start from the given point</br>
	 * Each path p is returned into the input parameter pathMatrix[p]
	 ===========================================================*/
	public int getAllFixedLengthPath(Data data, Point pSrc, int pathLength, Point[][] pathMatrix)
	{
		int			width		= data.getWidth();
		int			height		= data.getHeight();
		Integer[][]	weight		= new Integer[data.getWidth()][data.getHeight()];
		Noeud[][]	previous	= new Noeud[data.getWidth()][data.getHeight()];
		Fap			f			= new Fap();
		LinkedList<Point> voisin;
		Noeud y;
		int pz;

		if (pathLength <= 0) throw new RuntimeException("Wrong path length: " + pathLength);

		for (int i=0; i<width; i++)								// Initialisation
		{
			for (int j=0; j<height; j++)
			{
				weight	[i][j] = null;
				previous[i][j] = null;
			}
		}
		f.add(new Noeud(pSrc, 0));
		weight[pSrc.x][pSrc.y] = 0;

		while (!f.isEmpty())
		{
			y		= f.remove();
			voisin	= data.getAccessibleNeighborsPositions(y.getElem().x, y.getElem().y);
			for (Point z: voisin)
			{
				pz = y.getPrio() + 1;
				if (pz > pathLength) continue;
				if ((weight[z.x][z.y] == null) || (pz < weight[z.x][z.y]))
				{
					weight[z.x][z.y] = pz;
					f.add(new Noeud(z, pz));
					previous[z.x][z.y] = new Noeud(y.getElem(), y.getPrio());
				}
			}
		}
		return scanRes(previous, pathLength, pSrc, pathMatrix);
	}
	
	
	
	
	private int scanRes(Noeud[][] previous, int pathLength, Point pSrc, Point[][] pathMatrix)
	{
		int res = 0;
		Noeud n;

		for (int x=0; x<previous.length; x++)
		{
			for (int y=0; y<previous[0].length; y++)
			{
				if ((previous[x][y] == null) || (previous[x][y].getPrio() != pathLength)) continue;
				n = previous[x][y];
				for (int i=0; i<pathLength; i++) {n = previous[x][y];}
				if (!n.getElem().equals(pSrc)) throw new RuntimeException("?????");
				res ++;
			}
		}
		return res;
	}

// -----------------------------------------
// Private classes
// -----------------------------------------
	public class Fap
	{
		// Attributs
		private LinkedList<Noeud>	file;

		// Constructeur
		public Fap()
		{
			this.file = new LinkedList<Noeud>();
		}

		// Methode locale
		public boolean isEmpty()
		{
			return file.isEmpty();
		}
		public Noeud remove()
		{
			if (file.isEmpty())	throw new RuntimeException("File vide");

			Noeud res = file.get(0);
			file.remove(0);
			return res;
		}
		public void add(Noeud n)
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

// -----------------------------------------
// Private classes
// -----------------------------------------
	public class Noeud
	{
		// Attributs
		private Point	elem;
		private int		prio;

		// Constructeur
		public Noeud(Point elem, int prio)
		{
			this.elem	= elem;
			this.prio	= prio;
		}
		public Noeud(int elemX, int elemY, int prio)
		{
			this.elem	= new Point(elemX, elemY);
			this.prio	= prio;
		}

		// Methode locale
		public Point	getElem()	 {return elem;}
		public int		getPrio()	 {return prio;}
	}
}