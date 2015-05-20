package main.java.data;

import java.util.LinkedList;

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




public class Tile
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final String		tileNamePrefix					= "Tile_";
	public static final String		nonBuildingDescription			= "Z";
	public static final String		nonTerminusDescription			= "Z";
	public static final String[]	acceptedBuildingDescription		= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M"};
	public static final String[]	acceptedTerminusDescription		= {"1", "2", "3", "4", "5", "6"};
	public static final int			nbrBoolAttrDigits				= 4;
	public static final int			nbrBuildingDescriptionDigits	= 1;
	public static final int			nbrTerminusDescriptionDigits	= 1;
	public static final int			nbrCardinalDigits				= 2;

	private String					imageFileName;
	private boolean					isTree;
	private boolean					isBuilding;
	private boolean					isStop;
	private boolean					isTerminus;
	private String					buildingDescription;
	private Integer					terminusDescription;
	private int						cardinal;
	private int						nbrLeftRotation;	// Number of left rotation of the original image (belongs to [0, 3])
	private LinkedList<Path>		pathList;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Tile(LinkedList<Path> p) throws ExceptionNonExistantTile
	{
// TODO
	}
	public Tile(Tile t)
	{
		this.imageFileName			= new String(imageFileName);
		this.isTree					= t.isTree;
		this.isBuilding				= t.isBuilding;
		this.isStop					= t.isStop;
		this.isTerminus				= t.isTerminus;
		this.buildingDescription	= (t.buildingDescription == null) ? null : new String(t.buildingDescription);
		this.terminusDescription	= (t.terminusDescription == null) ? null : new Integer(t.terminusDescription);
		this.cardinal				= t.cardinal;
		this.nbrLeftRotation		= t.nbrLeftRotation;
		this.pathList				= new LinkedList<Path>(t.pathList);
	}
	public Tile(LinkedList<Path> pathList, Tile t) throws RuntimeException
	{
		if ((t.isBuilding)&& (!pathList.isEmpty()))throw new RuntimeException("A tile can not be a building and contain a path");

		this.imageFileName			= new String(t.imageFileName);
		this.isTree					= t.isTree;
		this.isBuilding				= t.isBuilding;
		this.isStop					= t.isStop;
		this.isTerminus				= t.isTerminus;
		this.buildingDescription	= (t.buildingDescription == null) ? null : new String(t.buildingDescription);
		this.terminusDescription	= (t.terminusDescription == null) ? null : new Integer(t.terminusDescription);
		this.cardinal				= t.cardinal;
		this.nbrLeftRotation		= t.nbrLeftRotation;
		this.pathList				= new LinkedList<Path>(pathList);
	}
	public Tile(String imageFileName)
	{
		String str;
		int d0, d1;

		this.imageFileName		= new String(imageFileName);										 // Init the non scanned values
		this.nbrLeftRotation	= 0;
		try
		{
			int l = tileNamePrefix.length();														// Ignore the prefix part

			if		(imageFileName.charAt(l+0) == 'T')	this.isTree = true;							// Scan the boolean part
			else if	(imageFileName.charAt(l+0) == 'F')	this.isTree = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+1) == 'T')	this.isBuilding = true;
			else if	(imageFileName.charAt(l+1) == 'F')	this.isBuilding = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+2) == 'T')	this.isStop = true;
			else if	(imageFileName.charAt(l+2) == 'F')	this.isStop = false;
			else	throw new Exception();

			if		(imageFileName.charAt(l+3) == 'T')	this.isTerminus = true;
			else if	(imageFileName.charAt(l+3) == 'F')	this.isTerminus = false;
			else	throw new Exception();

			l += nbrBoolAttrDigits;																	// Scan the buildingDescription
			str = imageFileName.substring(l, l+nbrBuildingDescriptionDigits);
			if (this.isBuilding)
			{
				if (str.equals(nonBuildingDescription))		throw new Exception();
				if (!isAcceptableBuildingDescription(str))	throw new Exception();
				this.buildingDescription = new String(str);
			}
			else
			{
				if (!str.equals(nonBuildingDescription))throw new Exception();
				this.buildingDescription = null;
			}

			l += nbrBuildingDescriptionDigits;														// Scan the terminusDescription
			str = imageFileName.substring(l, l+nbrTerminusDescriptionDigits);
			if (this.isTerminus)
			{
				if (str.equals(nonTerminusDescription))		throw new Exception();
				if (!isAcceptableTerminusDescription(str))	throw new Exception();
				this.terminusDescription = new Integer(Integer.parseInt(str));
			}
			else
			{
				if (!str.equals(nonTerminusDescription))	throw new Exception();
				this.terminusDescription = null;
			}

			l += nbrTerminusDescriptionDigits;
			str = imageFileName.substring(l, l+nbrCardinalDigits);
			this.cardinal = Integer.parseInt(str);													// Scan the tile cardinal

			l += nbrCardinalDigits;
			this.pathList = new LinkedList<Path>();													// Scan the tile path list
			for (int i=l; i<imageFileName.length(); i+=2)
			{
				d0 = Integer.parseInt(""+imageFileName.charAt(i));
				d1 = imageFileName.charAt(i+1);
				this.pathList.add(new Path(d0, d1));
			}
		}
		catch (Exception e){throw new RuntimeException("Tile imageFileName malformed: " + imageFileName + "\n" + e);}
	}
	public Tile cloneTile(Tile t)
	{
		return new Tile(t);
	}

// --------------------------------------------
// Setters/getters:
// --------------------------------------------
	public void		turnHalf()			{for (Path p: pathList)	p.turnHalf();}
	public String	getImageFileName()	{return new String(this.imageFileName);}
	public int		getCardinal()		{return this.cardinal;}
	public int		getNbrLeftRotation(){return this.nbrLeftRotation;}
	public boolean	isTree()			{return this.isTree;}
	public boolean	isBuilding()		{return this.isBuilding;}
	public boolean	isTerminus()		{return this.isTerminus;}
	public boolean	isStop()			{return this.isStop;}
	public boolean	isEmpty()			{return ((!this.isBuilding) && (!this.isTree) && (this.pathList.isEmpty()));}
	public void		turnLeft()			{for (Path p: pathList)	p.turnLeft(); nbrLeftRotation = (nbrLeftRotation == 3) ? 0 : nbrLeftRotation+1;}
	public void		turnRight()			{for (Path p: pathList)	p.turnRight();nbrLeftRotation = (nbrLeftRotation == 0) ? 3 : nbrLeftRotation-1;}
	public boolean	isPathTo(int dir)
	{
		Direction.checkDirection(dir);
		for (Path p: this.pathList)
		{
			if (p.end0 == dir)	return true;
			if (p.end1 == dir)	return true;
		}
		return false;
	}

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
// Private local methods
// --------------------------------------------
	private boolean isAcceptableBuildingDescription(String bd)
	{
		for (String s: acceptedBuildingDescription) if (bd.equals(s)) return true;
		return false;
	}
	private boolean isAcceptableTerminusDescription(String td)
	{
		for (String s: acceptedTerminusDescription) if (td.equals(s)) return true;
		return false;
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
		public String	toString()		{return "(" + Direction.toString(end0) + ", " + Direction.toString(end1) + ')';}
		public boolean	equals(Path p)	{return ((end0 == p.end0) && (end1 == p.end1));}
	}
}