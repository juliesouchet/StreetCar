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

	public static final String nonRealTileID = "SPECIAL_UNREAL0000";
	
	private String					tileID;
	private boolean					isTree;
	private boolean					isBuilding;
	private boolean					isStop;
	private boolean					isTerminus;
	private String					buildingDescription;
	private int						terminusDescription;
	private int						cardinal;
	private Direction				tileDirection;

	private Path[]					pathTab							= initPathTab();
	private int						ptrPathTab						= -1;					// Last non null path in pathList

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**
	 * Constructeur de tuile spécial:</br>
	 * Permet de construire une tuile "irréelle" en forcant ces chemins à la valeur donnée en argument.</br>
	 * Copie le reste des propriétés depuis la tuile données en argument.</br>
	 * PRECONDITIONS: </br>
	 * 	*pathList non null => retourne une tuile nulle (String null, booléens à faux, entiers à -1)</br>
	 * 	*tile non null  =>retourne une tuile nulle (String null, booléens à faux, entiers à -1)</br>
	 * 	*ptrPathTab > -1 => /!\ lève une exception</br>
	 * @param pathList
	 *	Le tableau contenant les voies de la tuile.
	 * @param ptrPathTab
	 * 	L'indice de fin des valeurs significatives du tableau.
	 * @param tile
	 * 	La tuile source d'où l'on copie le reste des attributs.
	 * @throws RuntimeException
	 * 	Si ptrPathTab > -1 && la tuile est un building
	 */
	public static Tile specialNonRealTileConstructor(Path[] pathList, int ptrPathTab, Tile tile) throws RuntimeException{
		return new Tile(pathList, ptrPathTab, tile);
	}
	
	/**
	 * Voir signature de specialNonRealTileConstructorTile 
	 */
	private Tile(Path[] pathList, int ptrPathTab, Tile tile) throws RuntimeException
	{
		if ((tile == null) || (pathList == null))
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
		else if ((tile != null) && (pathList != null))
		{
			if ((tile.isBuilding)&& (ptrPathTab > -1))throw new RuntimeException("A tile can not be a building and contain a path");
			this.tileID					= null;
			this.isTree					= tile.isTree;
			this.isBuilding				= tile.isBuilding;
			this.isStop					= tile.isStop;
			this.isTerminus				= tile.isTerminus;
			this.buildingDescription	= tile.buildingDescription;
			this.terminusDescription	= tile.terminusDescription;
			this.cardinal				= tile.cardinal;
			this.tileDirection			= tile.tileDirection;
			copyPathTab(pathList, this.pathTab, ptrPathTab);
			this.ptrPathTab				= tile.ptrPathTab;
		}
		else throw new RuntimeException("Unhandeled case");
	}
	
	/**
	 * Constructeur générique
	 */
	private Tile(){}
	
	/**
	 * Clone la tuile (Deep clone: toutes les attributs sont clones)
	 * @return
	 * Retourne une référence vers une nouvelle instance de tuile égale a la tuile appelante
	 */
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
	
	/**
	 * Constructeur statique: instancie une tuile a partir de son code (nom de l'image dans les ressources) avec les propriétés correspondantes.
	 * @param imageFileName
	 * 	Nom du fichier image correspondant à la tuile souhaitée
	 * @return
	 * 	Un objet tuile instancié avec les caractéristiques correspondant à l'image.
	 */
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

			l += nbrBuildingDescriptionChar;													// Scan the terminusDescription
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
			res.cardinal = Integer.parseInt(str);												// Scan the tile cardinal

			l += nbrCardinalChar;
			for (int i=l; i<imageFileName.length(); i+=2)
			{
				d0 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i)));
				d1 = Direction.parse(Integer.parseInt(""+imageFileName.charAt(i+1)));
				res.ptrPathTab ++;
				res.pathTab[res.ptrPathTab].setPath(d0, d1);
			}
			return res;
		}
		catch (Exception e){e.printStackTrace(); throw new RuntimeException("Tile imageFileName malformed: " + imageFileName + "\n" + e);}
	}

// --------------------------------------------
// Setters:
// --------------------------------------------
	/**
	 * Copie le contenu de la tuile passée en argument dans la tuile appelante.
	 * Remarque: ne fait pas de nouvelle allocation.
	 * @param t
	 * Tuile à recopier.
	 */
	public void copy(Tile t)
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
	/**
	 * Setter sur l'attibut booléen isStop de la tuile
	 * @param b
	 * True ou False
	 */
	public void setStop(boolean b){this.isStop = b;}

	/**
	 * Setter sur l'attribut tileDirection de la tuile 
	 * (fait aussi tourner les path)
	 * @param dir
	 */
	public void setDirection(Direction dir)
	{
		int val = ((dir.getVal() - this.tileDirection.getVal() + 4)%4);
		switch(val)
		{
			case 0	:					return;
			case 1	:this.turnRight();	return;
			case 2	:this.turnHalf();	return;
			case 3	:this.turnLeft();	return;
			default	:throw new RuntimeException("Error tile.setDirection : from " + this.tileDirection + " to " + dir + " : " + val);
		}
	}
// --------------------------------------------
// Getters:
// --------------------------------------------
	/**
	 *	Vrai si l'ID des 2 tuiles est le même. (donc les tuiles sont sensés être les mêmes à la rotation et au stop près) </br>
	 *	/!\ Ne prend pas du tout en compte les autres attributs 
	 *	@param
	 *	L'objet à comparer, retourne faux si null.
	 *	@return
	 *	Vrai si les instances représentent la même tuile
	 *	Faux sinon
	 */
	public boolean	equals(Object o)	{return (o != null) && (o instanceof Tile) && ((Tile)o).tileID.equals(this.tileID);}

	
/**
 * Compare la tuile intégralement: </br>
 * (Prend en compte l'orientation)
 * @param o
 * L'objet à comparer.
 * @return
 * Vrai si les 2 tuiles sont strictement identiques </br>
 * Faux sinon.
 */
	public boolean equalsStrong(Object o){
		Tile comparedTile = null;
		
		if (o==null){return false;}
		if (!(o instanceof Tile)){return false;}
		comparedTile = (Tile) o;

		
		
		
		if (!((Tile)o).tileID.equals(this.tileID)){return false;}
//TODO condition verifiee par tileID
/*		if (this.isTree()!=comparedTile.isTree() || this.isBuilding()!=comparedTile.isBuilding() || this.isStop()!=comparedTile.isStop() || this.isTerminus()!=comparedTile.isTerminus() ){
			return false;
		}
//TODO condition verifiee par tileID
		if ((this.buildingDescription==null && comparedTile.buildingDescription!=null) || (this.buildingDescription!=null && !this.buildingDescription.equals(comparedTile.buildingDescription)) ){
			return false;
		}

//TODO condition verifiee par tileID
		if (this.terminusDescription!=comparedTile.terminusDescription){
			return false;
		}
//TODO condition verifiee par tileID
		if (this.cardinal!=comparedTile.cardinal){
			return false;
		}
****/
		if  ((this.tileDirection==null && comparedTile.tileDirection!=null) || (this.tileDirection!=null && !this.tileDirection.equals(comparedTile.tileDirection))){
			return false;
		}

		if (this.ptrPathTab!=comparedTile.ptrPathTab){
			return false;
		}
		for( int i=0; i<this.ptrPathTab;i++ ){
			if (!this.pathTab[i].equals(comparedTile.pathTab[i])){
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * @return
	 * Retourne l'attribut tileID qui identifie la tuile.
	 */
	public String	getTileID()			{return new String(this.tileID);}
	
	/**
	 * @return
	 * Le nombre de tuile de ce type dans la pioche au départ, ne compte pas les mains initiales.
	 */
	public int		getCardinal()		{return this.cardinal;}
	
	/**
	 * @return
	 * L'orientation de la tuile :	WEST (0), NORTH	(1), EAST (2), SOUTH (3).</br>
	 * REMARQUE: Par défault à WEST.</br>
	 */
	public Direction getTileDirection()	{return this.tileDirection;}
	
	/**
	 * @return
	 * Vrai si la tuile est une tuile contenant un arbre (donc inamovible).</br>
	 * Faux sinon.
	 */
	public boolean	isTree()			{return this.isTree;}
	
	/**
	 * @return
	 * Vrai si la tuile représente un batiment (une station)</br>
	 * Faux sinon.
	 */
	public boolean	isBuilding()		{return this.isBuilding;}
	
	/**
	 * @return
	 * Vrai si la tuile représente un terminus.
	 */
	public boolean	isTerminus()		{return this.isTerminus;}
	
	/**
	 * @return
	 * Vrai si la tuile représente un stop.</br>
	 * Remarque1: elle a été posé la première à côté d'un batiment.</br>
	 * Remarque2: lorsqu'un tramway circule il est forcé de arreter son déplacement.</br>
	 */
	public boolean	isStop()			{return this.isStop;}
	
	/**
	 * @return
	 * Vrai si la tuile représente un vide (n'est ni un batiment, ni un terminus, ni des rails... un carré d'herbe ou de pavé.)</br>
	 * Faux sinon.</br>
	 */
	public boolean	isEmpty()			{return ((!this.isBuilding) && (!this.isTerminus) && (this.ptrPathTab == -1));}

	/**
	 * @return
	 * Vrai si la tuile peut être une tuile du deck (c'est une tuile de rails).
	 */
	public boolean	isDeckTile()		{return ((!this.isBuilding) && (!this.isTerminus) && (this.ptrPathTab > -1));}
	
	/**
	 * Modifie l'orientation de la tuile: rotation de 1 vers la gauche.
	 */
	public void		turnLeft()			{for (int i=0; i<=this.ptrPathTab; i++) pathTab[i].turnLeft();	tileDirection = tileDirection.turnLeft();}
	
	/**
	 * Modifie l'orientation de la tuile: rotation de 1 vers la droite.
	 */
	public void		turnRight()			{for (int i=0; i<=this.ptrPathTab; i++) pathTab[i].turnRight();	tileDirection = tileDirection.turnRight();}
	
	/**
	 * Modifie l'orientation de la tuile: retourne la tuile (équivament à double rotation à droite ou à gauche).
	 */
	public void		turnHalf()			{this.turnLeft(); this.turnLeft();}
 
	/**
	 * Indique si la direction donnee est accessible par cette tuile.
	 * @param dir
	 * La direction où l'on veut aller.
	 * @return
	 * Vrai si il existe une voie depuis la tuile vers la direction donnée.
	 */
	public boolean	isPathTo(Direction dir) //TODO est ce suffisant ? on peut aller du sud vers le nord mais pas forcément de l'ouest vers le nord... isPathBetween(Direction from, Dz
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
	 * @return
	 *  The list of accessibles directions, represented by an int:</br>
	 * 	For i in [0 , 3] if the digit i of the returned value equals 1, then the i th direction is accessible.</br>
	 * The result may be parsed by Direction.isInList(Direction, res)</br>
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
	 *	Test if the calling tile can be replaced by the tile given as argument.
	 * @return 
	 * 	-1 if the current tile can not be replaced by addedTile.</br>
	 * 	The number of additional path, is the current tile can be replaced by addedTile.</br>
	 * 	Those path are added in additionalPath.</br>
	 * 	REMARK: /!\ This function does not check if t is suitable for the current tile neighbors</br>
	 * @Param addedTile
	 * @param additionalPath: output parameter (can be null). Is filled with the t's paths that are not in the current tile
	 * ===================================================*/
	public int isReplaceable(Tile addedTile, Path[] additionalPath)
	{
		if (this.isTree)	 return -1;
		if (this.isBuilding) return -1;
		if (this.isTerminus) return -1;
		if (this.isStop)	 return -1;

		Path[]	localPath	= initPathTab();
		Path[]	addedTilePath	= initPathTab();
		int		localTileNumberOfPath	= this.ptrPathTab;
		int		addedTileNumberOfPath	= addedTile.ptrPathTab;
		copyPathTab(this.pathTab,	localPath, this.ptrPathTab);
		copyPathTab(addedTile.pathTab,		addedTilePath, addedTile.ptrPathTab);
		for (int i=0; i<=addedTileNumberOfPath; i++)							// Remove the common paths
		{
			Path bufferPathAddedTile = addedTilePath[i];
			for (int j=0; j<=localTileNumberOfPath; j++)
			{
				Path bufferPathLocalTile = localPath[j];
				if (bufferPathAddedTile.equals(bufferPathLocalTile))
				{
					Util.swapTab(addedTilePath, i, addedTileNumberOfPath);
					Util.swapTab(localPath, j, localTileNumberOfPath);
					i--;	addedTileNumberOfPath--;
					j--;	localTileNumberOfPath--;
					break;
				}
			}
		}

		if (localTileNumberOfPath != -1)	return -1;							// Case local tile is not contained in t
		if (addedTileNumberOfPath == -1)	return -1;							// Case local tile is equal to t
		if (additionalPath != null)								// Case replaceable
		{
			for (int i=0; i<=addedTileNumberOfPath; i++)						//		Add all the new paths
				additionalPath[i].setPath(addedTilePath[i]);	

		}
		return addedTileNumberOfPath;
	}
	/**=============================================================
	 * @return the number of different tiles that may be created by rotating the current one.</br>
	 * This tiles are added to the input tab.</br>
	 * The current tile is returned too.</br>
	 * The input tab size must be 4 (or  higher).  Each one of its celle must have been initialized
	 ===============================================================*/
	public int getUniqueRotationList(Tile[] resTab)
	{
		int res = 0, i;
		Tile tmp = this.getClone();

		for (Direction dir: Direction.DIRECTION_LIST)
		{
			resTab[res].copy(tmp);
			tmp.turnLeft();
			for (i=0; i<res; i++) if (resTab[res].equalsStrong(resTab[i])) break;
			if (i == res) res ++;
		}
// TODO a enlever apres les test
if ((res <= 0) || (res > 4)) throw new RuntimeException("??????");


		return res;
	}
	/**=============================================================
	 * @return if this is a building the function returns its name (string).</br>  Else it returns null
	 ===============================================================*/
	public String getBuildingName()
	{
		if (!isBuilding) return null;

		int l = tileNamePrefix.length() + nbrBoolAttrChar;
		return tileID.substring(l, l+nbrBuildingDescriptionChar);
	}
	/**=============================================================
	 * @return if this is a terminus, the method returns its name(int).  Else it returns -1
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
	
	/**
	 * Alloue un tableau de path, initialisé avec des paths West-West (donc déjà alloués)
	 * @return Un tableau de Path de taille maxNbrPathInTile (5)
	 */
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
	}
}
