package main.java.data;

import java.io.Serializable;

import main.java.util.CloneableInterface;
import main.java.util.Direction;
import main.java.util.Util;


/**========================================================================
 * @author kassuskley
 * @TileNaming The name of a tile is a fixed length string, composed of the following three part : (prefix)(TBST)(B)(T)(Cardinal)(ab)
 *	@-prefix		: A common prefix to all the tile images 
 *	@-TBST			: A boolean sequence of length nbrBoolAttrDigits representing the attributes: isTree, isBuilding, isStop, isTerminus
 *	@-B				: The building name (between A and M or Z if not a building)
 *	@-T				: The terminus name (between 1 and 6 or Z if not a building)
 *	@-Cardinal		: The number of tiles of the same type in the games. This number must be written on nbrCardinalDigits digits
 *	@-ab			: A sequence of int couples between 0 and 3 representing the paths of the tile
 ==========================================================================*/




public class Tile implements Serializable, CloneableInterface<Tile>
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final long		serialVersionUID				= 2787165577586992226L;
	public static final String		tileNamePrefix					= "Tile_";
	public static final String		nonBuildingDescription			= "Z";
	public static final String		nonTerminusDescription			= "Z";
	public static final String[]	acceptedBuildingDescription		= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M"};
	public static final String[]	acceptedTerminusDescription		= {"1", "2", "3", "4", "5", "6"};
	public static final int			nbrBoolAttrChar					= 4;
	public static final int			nbrBuildingDescriptionChar		= 1;
	public static final int			nbrTerminusDescriptionChar		= 1;
	public static final int			nbrCardinalChar					= 2;
	public static final int			maxNbrPathInTile				= 5;
	public static final int			maxNbrUniqueDirections			= 4;
	
	private String					tileID;
	private boolean					isTree;
	private boolean					isBuilding;
	private boolean					isStop;
	private boolean					isTerminus;
	private String					buildingDescription;
	private int						terminusDescription;
	private int						cardinal;
	private Direction				tileDirection;
	private Direction[]				uniqueDirTab; // TODO liste des directions uniques de la tuile
	private int						uniqueDirPtr; // Last non null direction in uniqueDirTab

	private Path[]					pathTab							= initPathTab();
	private int						ptrPathTab						= -1;					// Last non null path in pathList

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Tile(Path[] pathList, int ptrPathTab, Tile t) throws RuntimeException
	{
		if ((t == null) || (pathList == null))
		{
			this.tileID					= null;
			this.isTree					= false;
			this.isBuilding				= false;
			this.isStop					= false;
			this.isTerminus				= false;
			this.buildingDescription	= null;
			this.terminusDescription	= -1;
			this.cardinal				= -1;
			this.tileDirection			= null;
		}
		else if ((t != null) && (pathList != null))
		{
			if ((t.isBuilding)&& (ptrPathTab > -1))throw new RuntimeException("A tile can not be a building and contain a path");
			this.tileID					= t.tileID;
			this.isTree					= t.isTree;
			this.isBuilding				= t.isBuilding;
			this.isStop					= t.isStop;
			this.isTerminus				= t.isTerminus;
			this.buildingDescription	= t.buildingDescription;
			this.terminusDescription	= t.terminusDescription;
			this.cardinal				= t.cardinal;
			this.tileDirection			= t.tileDirection;
			copyPathTab(t.pathTab, this.pathTab, t.ptrPathTab);
			this.ptrPathTab				= t.ptrPathTab;
		}
		else throw new RuntimeException("Unhandeled case");
	}
	private Tile(){}
//TODO verifier tous ceux qui l'appelent
	public Tile getClone()
	{
		Tile res = new Tile();

		res.tileID				= this.tileID;
		res.isTree				= this.isTree;
		res.isBuilding			= this.isBuilding;
		res.isStop				= this.isStop;
		res.isTerminus			= this.isTerminus;
		res.buildingDescription	= this.buildingDescription;
		res.terminusDescription	= this.terminusDescription;
		res.cardinal			= this.cardinal;
		res.tileDirection		= this.tileDirection;
		copyPathTab(this.pathTab, res.pathTab, this.ptrPathTab);
		res.ptrPathTab			= this.ptrPathTab;
		return res;
	}
	public static Tile parseTile(String imageFileName)
	{
		String str;
		Direction d0, d1;
		Tile res = new Tile();

		res.tileID			= imageFileName;													// Init the non scanned values
		res.tileDirection	= Direction.WEST;

		try
		{
			int l = tileNamePrefix.length();													// Ignore the prefix part

			if		(imageFileName.charAt(l+0) == 'T')	res.isTree = true;						// Scan the boolean part
			else if	(imageFileName.charAt(l+0) == 'F')	res.isTree = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+1) == 'T')	res.isBuilding = true;
			else if	(imageFileName.charAt(l+1) == 'F')	res.isBuilding = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+2) == 'T')	res.isStop = true;
			else if	(imageFileName.charAt(l+2) == 'F')	res.isStop = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+3) == 'T')	res.isTerminus = true;
			else if	(imageFileName.charAt(l+3) == 'F')	res.isTerminus = false;
			else	throw new Exception();

			l += nbrBoolAttrChar;																// Scan the buildingDescription
			str = imageFileName.substring(l, l+nbrBuildingDescriptionChar);
			if (res.isBuilding)
			{
				if (str.equals(nonBuildingDescription))		throw new Exception();
				if (!isAcceptableBuildingDescription(str))	throw new Exception();
				res.buildingDescription = new String(str);
			}
			else
			{
				if (!str.equals(nonBuildingDescription))throw new Exception();
				res.buildingDescription = null;
			}

			l += nbrBuildingDescriptionChar;														// Scan the terminusDescription
			str = imageFileName.substring(l, l+nbrTerminusDescriptionChar);
			if (res.isTerminus)
			{
				if (str.equals(nonTerminusDescription))		throw new Exception();
				if (!isAcceptableTerminusDescription(str))	throw new Exception();
				res.terminusDescription = new Integer(Integer.parseInt(str));
			}
			else
			{
				if (!str.equals(nonTerminusDescription))	throw new Exception();
				res.terminusDescription = -1;
			}

			l += nbrTerminusDescriptionChar;
			str = imageFileName.substring(l, l+nbrCardinalChar);
			res.cardinal = Integer.parseInt(str);													// Scan the tile cardinal

			l += nbrCardinalChar;
			for (int i=l; i<imageFileName.length(); i+=2)
			{
				d0 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i)));
				d1 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i+1)));
				res.ptrPathTab ++;
				res.pathTab[res.ptrPathTab].setPath(d0, d1);
			}
			
			// TODO construire la liste des directions uniques
			// si pour une direction donnée, la tuile obtenue par rotation est différente, on l'ajoute
			for (Direction d : Direction.DIRECTION_LIST) {
				if(d.equals(Direction.WEST)) { // Init
					res.uniqueDirTab = new Direction[maxNbrUniqueDirections];
					res.uniqueDirTab[0] = Direction.WEST;
					res.uniqueDirPtr = 0;
				}
				else {
					// TODO comparer les chemins après rotation : à tester
					res.setDirection(d);
					Tile rotated = res.getClone();
					rotated.turnRight();
					while(!rotated.getTileDirection().equals(Direction.WEST)) { // checking all rotations from d to west
						boolean identical = true;
						int i = 0;
						while(i < res.ptrPathTab && identical) { // all the paths on the original tile
							int j = 0;
							while(j < rotated.ptrPathTab && !res.pathTab[i].equals(rotated.pathTab[j])) { // are they in the rotated tile ?
								j++;
							}
							identical = j != rotated.ptrPathTab;
							i++;
						}
						if(i != res.ptrPathTab){ // non identical tiles
							res.uniqueDirPtr++;
							res.uniqueDirTab[res.uniqueDirPtr] = rotated.getTileDirection();
						}
						rotated.turnRight();
					}
				}
			}
			
			res.setDirection(Direction.WEST);
			return res;
		}
		catch (Exception e){e.printStackTrace(); throw new RuntimeException("Tile imageFileName malformed: " + imageFileName + "\n" + e);}
	}

// --------------------------------------------
// Setters:
// --------------------------------------------
	public void setTile(Tile t)
	{
		if (t == null)
		{
			this.tileID					= null;
			this.isTree					= false;
			this.isBuilding				= false;
			this.isStop					= false;
			this.isTerminus				= false;
			this.buildingDescription	= null;
			this.terminusDescription	= -1;
			this.cardinal				= -1;
			this.tileDirection			= null;
		}
		else
		{
			this.tileID					= t.tileID;
			this.isTree					= t.isTree;
			this.isBuilding				= t.isBuilding;
			this.isStop					= t.isStop;
			this.isTerminus				= t.isTerminus;
			this.buildingDescription	= t.buildingDescription;
			this.terminusDescription	= t.terminusDescription;
			this.cardinal				= t.cardinal;
			this.tileDirection			= t.tileDirection;
			copyPathTab(t.pathTab, this.pathTab, t.ptrPathTab);
			this.ptrPathTab				= t.ptrPathTab;
		}
	}
	public void		turnLeft()			{for (int i=0; i<=this.ptrPathTab; i++) pathTab[i].turnLeft();	tileDirection = tileDirection.turnLeft();}
	public void		turnRight()			{for (int i=0; i<=this.ptrPathTab; i++) pathTab[i].turnRight();	tileDirection = tileDirection.turnRight();}
	public void		turnHalf()			{this.turnLeft(); this.turnLeft();}
	public void		setDirection(Direction dir)
	{
		switch(((this.tileDirection.getVal()-dir.getVal())+4)%4) {
		case 0 : return;
		case 1 : this.turnRight();
		case 2 : this.turnHalf();
		case 3 : this.turnLeft();
		default :
			throw new RuntimeException("Error setDirection : from "+this.tileDirection+" to "+dir);
		}
	}

// --------------------------------------------
// Getters:
// --------------------------------------------
	public boolean	equals(Object o)	{return o != null && (o instanceof Tile) && ((Tile)o).tileID.equals(this.tileID);}
	public String	getTileID()			{return new String(this.tileID);}
	public int		getCardinal()		{return this.cardinal;}
	public Direction getTileDirection()	{return this.tileDirection;}
	public boolean	isTree()			{return this.isTree;}
	public boolean	isBuilding()		{return this.isBuilding;}
	public boolean	isTerminus()		{return this.isTerminus;}
	public boolean	isStop()			{return this.isStop;}
	public boolean	isEmpty()			{return ((!this.isBuilding) && (!this.isTerminus) && (this.ptrPathTab == -1));}
	public boolean	isDeckTile()		{return ((!this.isBuilding) && (!this.isTerminus) && (this.ptrPathTab > -1));}
	public Direction[] getUniqueDirectionTab ()	{return this.uniqueDirTab;}
	public int		getUniqueDirectionPtr	()	{return this.uniqueDirPtr;}

	public boolean	isPathTo(Direction dir)
	{
		for (int i=0; i<=this.ptrPathTab; i++)
		{
			Path p = this.pathTab[i];
			if (p.end0 == dir)	return true;
			if (p.end1 == dir)	return true;
		}
		return false;
	}

	/**==================================================
	 * @return an int that represents the list of accessible directions:
	 * for i between [0 , 3] if the i th bit of the res equals 1, then the i th direction is accessible.
	 * The result may be parsed by Direction.isInList(Direction, res)
	 ====================================================*/
	public int getAccessibleDirections()
	{
		int res = 0;

		for (int i=0; i<=this.ptrPathTab; i++)
		{
			Path p = this.pathTab[i];
			res = p.end0.addDirectionToList(res);
			res = p.end1.addDirectionToList(res);
		}
		return res;
	}
	/**=================================================
	 * @return -1 if the current tile can not be replaced by t.  Otherwise, the function returns the number of additional path.
	 * Those path are filled in additionalPath.
	 * This function does not check if t is suitable for the current tile neighbors
	 * @param additionalPath: output parameter (can be null). Is filled with the t's paths that are not in the current tile
	 * ===================================================*/
	public int isReplaceable(Tile t, Path[] additionalPath)
	{
		if (this.isTree)	 return -1;
		if (this.isBuilding) return -1;
		if (this.isTerminus) return -1;
		if (this.isStop)	 return -1;

		Path[]	lPath	= initPathTab();
		Path[]	tPath	= initPathTab();
		int		lSize	= this.ptrPathTab;
		int		tSize	= t.ptrPathTab;
		copyPathTab(this.pathTab,	lPath, this.ptrPathTab);
		copyPathTab(t.pathTab,		tPath, t.ptrPathTab);
		for (int i=0; i<=tSize; i++)							// Remove the common paths
		{
			Path pt = tPath[i];
			for (int j=0; j<=lSize; j++)
			{
				Path pl = lPath[j];
				if (pt.equals(pl))
				{
					Util.swapTab(tPath, i, tSize);
					Util.swapTab(lPath, j, lSize);
					i--;	tSize--;
					j--;	lSize--;
					break;
				}
			}
		}

		if (lSize != -1)	return -1;							// Case local tile is not contained in t
		if (tSize == -1)	return -1;							// Case local tile is equal to t
		if (additionalPath != null)								// Case replaceable
		{
			for (int i=0; i<=tSize; i++)						//		Add all the new paths
				additionalPath[i].setPath(tPath[i]);	
		}
		return tSize;
	}
	/**=============================================================
	 * @return if t is a building the function returns its name.  Else it returns null
	 ===============================================================*/
	public String getBuildingName()
	{
		if (!isBuilding) return null;

		int l = tileNamePrefix.length() + nbrBoolAttrChar;
		return tileID.substring(l, l+nbrBuildingDescriptionChar);
	}
	/**=============================================================
	 * @return if t is a building the function returns its name(int).  Else it returns -1
	 ===============================================================*/
	public int getTerminusName()
	{
		if (!isTerminus) return -1;

		int l = tileNamePrefix.length() + nbrBoolAttrChar + nbrBuildingDescriptionChar;
		return Integer.parseInt(tileID.substring(l, l+nbrTerminusDescriptionChar));
	}
	public String toString()
	{
		String str = "";

		str += "{ ";
		str += (this.isTree)		? "Tree | ": "____ | ";
		str += (this.isBuilding)	? "Buil | ": "____ | ";
		str += (this.isStop)		? "Stop | ": "____ | ";
		str += (this.isTerminus)	? "Term | ": "____ | ";
		for(int i=0; i<=this.ptrPathTab; i++)
		{
			Path p = this.pathTab[i];
			str += "[" + p.end0 + " ; " + p.end1 + "] ";
		}
		str += "}";
		return str;
	}

// --------------------------------------------
// Private local methods
// --------------------------------------------
	private static boolean isAcceptableBuildingDescription(String bd)
	{
		for (String s: acceptedBuildingDescription) if (bd.equals(s)) return true;
		return false;
	}
	private static boolean isAcceptableTerminusDescription(String td)
	{
		for (String s: acceptedTerminusDescription) if (td.equals(s)) return true;
		return false;
	}
	public static Path[] initPathTab()
	{
		Path[] res = new Path[maxNbrPathInTile];
		for (int i=0; i<maxNbrPathInTile; i++) res[i] = new Path(Direction.WEST, Direction.WEST);
		return res;
	}
	private static void copyPathTab(Path[] src, Path[] dst, int ptrSrc)
	{
		for (int i=0; i<= ptrSrc; i++)	dst[i].setPath(src[i]);
	}

// --------------------------------------------
// Path class:
// Represents a path between two cardinal directions
// --------------------------------------------
	public static class Path implements Serializable, CloneableInterface<Path>
	{
		// Attributes
		private static final long serialVersionUID = 1L;
		public Direction	end0;
		public Direction	end1;

		// Builder
		public Path(Direction d0, Direction d1)
		{
			this.end0	= d0;
			this.end1	= d1;
		}
		private Path(){}
		public Path getClone()
		{
			Path res = new Path();
			res.end0 = this.end0;
			res.end1 = this.end1;
			return res;
		}

		// Setter
		public void turnLeft()							{end0 = end0.turnLeft();	end1 = end1.turnLeft();}
		public void turnRight()							{end0 = end0.turnRight();	end1 = end1.turnRight();}
		public void turnHalf()							{end0 = end0.turnHalf();	end1 = end1.turnHalf();}
		public void setPath(Path p)						{end0 = p.end0;				end1 = p.end1;}
		public void setPath(Direction d0, Direction d1)	{end0 = d0;					end1 = d1;}
		public String	toString()						{return "(" + end0 + ", " + end1 + ')';}
		public boolean	equals(Path p)					{return ((end0.equals(p.end0)) && (end1.equals(p.end1)))
															|| ((end0.equals(p.end1)) && (end1.equals(p.end0)));}
		
		// Getter
		public Path getTurnHalf()
		{
			Path p = this.getClone();
			p.turnHalf();
			return p;
		}
		public Path getTurnLeft()
		{
			Path p = this.getClone();
			p.turnLeft();
			return p;
		}
		public Path getTurnRight()
		{
			Path p = this.getClone();
			p.turnRight();
			return p;
		}
	}
}
