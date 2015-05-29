package main.java.data;

import java.io.Serializable;
import java.util.LinkedList;

import main.java.util.CloneableInterface;
import main.java.util.Copier;
import main.java.util.Direction;


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
	private static final long		serialVersionUID				= 2787165577586992226L;
	public static final String		tileNamePrefix					= "Tile_";
	public static final String		nonBuildingDescription			= "Z";
	public static final String		nonTerminusDescription			= "Z";
	public static final String[]	acceptedBuildingDescription		= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M"};
	public static final String[]	acceptedTerminusDescription		= {"1", "2", "3", "4", "5", "6"};
	public static final int			nbrBoolAttrChar					= 4;
	public static final int			nbrBuildingDescriptionChar		= 1;
	public static final int			nbrTerminusDescriptionChar		= 1;
	public static final int			nbrCardinalChar					= 2;

	private String					tileID;
	private boolean					isTree;
	private boolean					isBuilding;
	private boolean					isStop;
	private boolean					isTerminus;
	private String					buildingDescription;
	private Integer					terminusDescription;
	private int						cardinal;
	private Direction				tileDirection;
	private LinkedList<Path>		pathList;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Tile(LinkedList<Path> pathList, Tile t) throws RuntimeException
	{
		if ((t.isBuilding)&& (!pathList.isEmpty()))throw new RuntimeException("A tile can not be a building and contain a path");

		this.tileID					= new String(t.tileID);
		this.isTree					= t.isTree;
		this.isBuilding				= t.isBuilding;
		this.isStop					= t.isStop;
		this.isTerminus				= t.isTerminus;
		this.buildingDescription	= (t.buildingDescription == null) ? null : new String(t.buildingDescription);
		this.terminusDescription	= (t.terminusDescription == null) ? null : new Integer(t.terminusDescription);
		this.cardinal				= t.cardinal;
// TODO
		this.tileDirection			= t.tileDirection;
		this.pathList				= new LinkedList<Path>(pathList);
	}
	private Tile(){}
	public Tile getClone()
	{
		Tile res = new Tile();
		Copier<Path> cp = new Copier<Path>();

		res.tileID				= new String(this.tileID);
		res.isTree				= this.isTree;
		res.isBuilding			= this.isBuilding;
		res.isStop				= this.isStop;
		res.isTerminus			= this.isTerminus;
		res.buildingDescription	= (this.buildingDescription == null) ? null : new String(this.buildingDescription);
		res.terminusDescription	= (this.terminusDescription == null) ? null : new Integer(this.terminusDescription);
		res.cardinal			= this.cardinal;
		res.tileDirection		= this.tileDirection;
		res.pathList			= cp.copyList(this.pathList);
		return res;
	}
	public static Tile parseTile(String imageFileName)
	{
		String str;
		Direction d0, d1;
		Tile res = new Tile();

		res.tileID			= new String(imageFileName);										// Init the non scanned values
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
				res.terminusDescription = null;
			}

			l += nbrTerminusDescriptionChar;
			str = imageFileName.substring(l, l+nbrCardinalChar);
			res.cardinal = Integer.parseInt(str);													// Scan the tile cardinal

			l += nbrCardinalChar;
			res.pathList = new LinkedList<Path>();													// Scan the tile path list
			for (int i=l; i<imageFileName.length(); i+=2)
			{
				d0 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i)));
				d1 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i+1)));
				res.pathList.add(new Path(d0, d1));
			}
			return res;
		}
		catch (Exception e){throw new RuntimeException("Tile imageFileName malformed: " + imageFileName + "\n" + e);}
	}

// --------------------------------------------
// Setters/getters:
// --------------------------------------------
	// TODO ajouté equals(Object) -- Julie
	public boolean	equals(Object o)	{return (o instanceof Tile) && ((Tile)o).tileID.equals(this.tileID);}
	public String	getTileID()			{return new String(this.tileID);}
	public int		getCardinal()		{return this.cardinal;}
	public Direction getTileDirection()	{return this.tileDirection;}
	public boolean	isTree()			{return this.isTree;}
	public boolean	isBuilding()		{return this.isBuilding;}
	public boolean	isTerminus()		{return this.isTerminus;}
	public boolean	isStop()			{return this.isStop;}
	public boolean	isEmpty()			{return ((!this.isBuilding) && (!this.isTerminus) && (this.pathList.isEmpty()));}
	public boolean	isDeckTile()		{return ((!this.isBuilding) && (!this.isTerminus) && (!this.pathList.isEmpty()));}
	public void		turnLeft()			{for (Path p: pathList)	p.turnLeft();	this.tileDirection = this.tileDirection.turnLeft();}
	public void		turnRight()			{for (Path p: pathList)	p.turnRight();	this.tileDirection = this.tileDirection.turnRight();}
	public void		turnHalf()			{this.turnLeft(); this.turnLeft();}
	public void		setDirection(Direction dir)	{this.tileDirection = dir;}

	public boolean	isPathTo(Direction dir)
	{
		for (Path p: this.pathList)
		{
			if (p.end0 == dir)	return true;
			if (p.end1 == dir)	return true;
		}
		return false;
	}

	public LinkedList<Direction> getAccessibleDirections()
	{
		LinkedList<Direction> res = new LinkedList<Direction>();

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
	 * ===================================================*/
	public boolean isReplaceable(Tile t, LinkedList<Path> additionalPath)
	{
		LinkedList<Path> lPath	= new LinkedList<Path>(this.pathList);
		LinkedList<Path> tPath	= new LinkedList<Path>(t.pathList);

		if (this.isTree)	 return false;
		if (this.isBuilding) return false;
		if (this.isTerminus) return false;
		if (this.isStop)	 return false;

		for (Path pt: tPath)								// Remove the common paths
			for (Path pl: lPath)
				if (pt.equals(pl))	{tPath.remove(pt); lPath.remove(pl); break;}

		if (!lPath.isEmpty())	return false;				// Case local tile is not contained in t
		if (tPath.isEmpty())	return false;				// Case local tile is equal to t
		if (additionalPath != null)							// Cas replaceable
		{
			additionalPath.clear();
			additionalPath.addAll(tPath);					//		Add all the new paths
		}
		return true;
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
		for(Path p : pathList)	str += "[" + p.end0 + " ; " + p.end1 + "] ";
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
// --------------------------------------------
// Path class :
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
		public void turnLeft()	{end0 = end0.turnLeft();	end1 = end1.turnLeft();}
		public void turnRight()	{end0 = end0.turnRight();	end1 = end1.turnRight();}
		public void turnHalf()	{end0 = end0.turnHalf();	end1 = end1.turnHalf();}
		public String	toString()		{return "(" + end0 + ", " + end1 + ')';}
		public boolean	equals(Path p)	{return ((end0.equals(p.end0)) && (end1.equals(p.end1)));}
	}
}
