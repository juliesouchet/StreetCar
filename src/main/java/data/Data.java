package main.java.data;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import main.java.data.Tile.Path;
import main.java.game.ExceptionFullParty;
import main.java.game.UnknownBoardNameException;
import main.java.player.PlayerInterface;
import main.java.util.Direction;




public class Data implements Serializable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private static final long			serialVersionUID		= -2740586808331187527L;
	public static String				boardDirectory			= "src/main/resources/boards/";
	public static final	String			lineFile				= "src/main/resources/line/lineDescription_";
	public static final String			initialHandFile			= "src/main/resources/initialHand/default";
	public static final int				maxNbrPlayer			= 6;
	public static final int				initialHandSize			= 5;
	public static final int				minNbrBuildingInLine	= 2;
	public static final int				maxNbrBuildingInLine	= 3;
	public static final int				minSpeed				= 1;
	public static final int				maxSpeed				= 10;

	private LinkedList<Integer>			existingLine;
	private LinkedList<String[][]>		existingBuildingInLine;
	private LinkedList<Integer>			remainingLine;
	private LinkedList<String[][]>		remainingBuildingInLine;
	private LinkedList<Tile>			initialHand;

	private String						gameName;
	private Tile[][]					board;
	private Deck						deck;
	private HashMap<String, PlayerInfo>	playerInfoList;
	private int							round;
	private int							maxPlayerSpeed;
	private String[]					playerOrder;  // TODO a choisir lors du debut de partie
	private String						host;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Data(String gameName, String boardName, int nbrBuildingInLine) throws UnknownBoardNameException, RuntimeException
	{
		File f = new File(boardDirectory + boardName);
		Scanner sc;

		if ((nbrBuildingInLine > maxNbrBuildingInLine) || 
			(nbrBuildingInLine < minNbrBuildingInLine))	throw new RuntimeException("Unknown nbr building in a line");

		this.gameName			= new String(gameName);
		try						{sc = new Scanner(f);}
		catch(Exception e)		{e.printStackTrace(); throw new UnknownBoardNameException();}
		this.board				= scanBoardFile(sc);
		sc.close();
		this.deck				= new Deck();
		this.playerInfoList		= new HashMap<String, PlayerInfo>();
		this.maxPlayerSpeed		= minSpeed;

		this.existingLine		= new LinkedList<Integer>();						// Init the existing lines
		for (int i=1; i<=maxNbrPlayer; i++)	this.existingLine.add(i);
		this.remainingLine		= new LinkedList<Integer>(existingLine);

		this.initExistingBuildingInLine(nbrBuildingInLine);							// Init the existing building
		this.remainingBuildingInLine= new LinkedList<String[][]>(existingBuildingInLine);

		this.initInitialHand();
	}

// --------------------------------------------
// Setter:
// --------------------------------------------
	public void addPlayer(PlayerInterface p, String playerName, boolean isHost) throws ExceptionFullParty
	{
		if (this.playerInfoList.size() >= maxNbrPlayer)	throw new ExceptionFullParty();
		if ((isHost) && (this.host != null))			throw new RuntimeException("The host is already set");
		PlayerInfo pi = new PlayerInfo(p);
		this.playerInfoList.put(playerName, pi);
		if (isHost) this.host = new String(playerName);
	}
	public void removePlayer(String playerName)
	{
		PlayerInterface pi = this.playerInfoList.get(playerName).player;
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);
		this.playerInfoList.remove(playerName);
	}

// --------------------------------------------
// Getter:
// --------------------------------------------
	public Tile[][]				getBoard()										{return boardCopy();}
	public int					getWidth()										{return this.board.length;}
	public int					getHeight()										{return this.board[0].length;}
	public int					getNbrPlayer()									{return this.playerInfoList.size();}
	public int					maximumSpeed()									{return this.maxPlayerSpeed;}
	public Tile					getTile(int x, int y)							{return new Tile(this.board[x][y]);}
//////////////// TODO	
	public void					setTile(int x, int y, Tile t)					{this.board[x][y] = t;}
	public String				getGameName()									{return new String(this.gameName);}
	public Set<String>			getPlayerNameList()								{return this.playerInfoList.keySet();}
	public boolean				containsPlayer(String playerName)				{return this.playerInfoList.containsKey(playerName);}
	public boolean				hasDoneFirstAction(String name)					{return this.playerOrder[0].equals(name);}
	public LinkedList<Point>	getShortestPath(Point p0, Point p1)				{return PathFinder.getPath(this, p0, p1);}
	public PlayerInterface		getPlayer(String playerName)
	{
		PlayerInterface pi = this.playerInfoList.get(playerName).player;
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);
		return pi;
	}
	public boolean				isWithinnBoard(int x, int y)
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
	public boolean isAcceptableTilePlacement(int x, int y, Tile t)
	{
		LinkedList<Path> additionalPath = new LinkedList<Path>();
		LinkedList<Integer> accessibleDirection;
		Tile oldT = this.board[x][y];

		if (!oldT.isReplaceable(t, additionalPath))	return false;										// Check whether t contains the old t (remove Tile and Rule C)

		Tile nt = new Tile(additionalPath, t);
		accessibleDirection = nt.getAccessibleDirections();
		for (int d: accessibleDirection)																// Check whether the new tile is suitable with the <x, y> neighborhood
		{
			Point neighbor = Direction.getNeighbour(x, y, d);
////////	if (!this.isWithinnBoard(neighbor.x, neighbor.y))							return false;	//		Neighbor tile out of board
			Tile neighborT = this.board[x][y];
			if ((this.isOnEdge(neighbor.x, neighbor.y)) && (!neighborT.isTerminus()))	return false;	//		Rule A
			if (neighborT.isEmpty())													continue;		//		Rule E (step 1)
			if (neighborT.isBuilding())													return false;	//		Rule B
			if (!neighborT.isPathTo(Direction.turnHalf(d)))								return false;	//		Rule E (step 2)
		}
		LinkedList<Integer> plt = this.getPathsLeadingToTile(x, y);										//		Rule D
		for (int dir: plt)	if (!nt.isPathTo(dir))										return false;
		return true;
	}
	/**============================================================
	 * @return a list of the neighbor coordinates.  This function
	 * does not check whether the neighbour is accessible through a path on the current tile
	 ==============================================================*/
	public LinkedList<Point> getNeighborPositions(int x, int y)
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
	public LinkedList<Point> getAccessibleNeighborsPositions(int x, int y)
	{
		LinkedList<Point>	res = new LinkedList<Point>();
		LinkedList<Integer>	ad	= board[x][y].getAccessibleDirections();	// List of the reachable directions

		for (int d: ad)														// For each accesible position
		{
			Point next = Direction.getNeighbour(x, y, d);
			if (isWithinnBoard(next.x, next.y))	res.add(next);
		}
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor tiles that can be acceded from the <x,y> cell
	 ==============================================================*/
	public LinkedList<Tile> getAccessibleNeighborsTiles(int x, int y)
	{
		LinkedList<Tile>	res = new LinkedList<Tile>();
		LinkedList<Integer>	ad	= board[x][y].getAccessibleDirections();	// List of the reachable directions

		for (int d: ad)														// For each accesible position
		{
			Point next = Direction.getNeighbour(x, y, d);
			if (isWithinnBoard(next.x, next.y))	res.add(new Tile(board[next.x][next.y]));
		}
		return res;
	}
	/**============================================================
	 * @returns the list of directions d such as the neighbor d has a path to the current tile <x, y>
	 ==============================================================*/
	public LinkedList<Integer> getPathsLeadingToTile(int x, int y)
	{
		LinkedList<Integer>	res	= new LinkedList<Integer>();
		Iterator<Integer>	di	= Direction.getIterator();
		int direction, neighborDir;
		Point neighbor;
		Tile neighborTile;

		while (di.hasNext())
		{
			direction	= di.next();
			neighbor	= Direction.getNeighbour(x, y, direction);
			if (!this.isWithinnBoard(neighbor.x, neighbor.y))	continue;
			neighborTile= board[neighbor.x][neighbor.y];
			neighborDir	= Direction.turnHalf(direction);
			if (neighborTile.isPathTo(neighborDir)) res.add(direction);
		}
		return res;
	}
	/**=========================================================================
	 * @param building: Position of a building
	 * @return the position of the stop next to the building
	 * @throws runtimeException if the parameter is not a building
	 ===========================================================================*/
	public Point hasStopNextToBuilding(Point building)
	{
		Tile t = this.board[building.x][building.y];
		Iterator<Integer> dirIt = Direction.getIterator();
		Point p;

		if (!t.isBuilding())	throw new RuntimeException("The point " + building + " is not a building");
		while(dirIt.hasNext())
		{
			p = Direction.getNeighbour(building.x, building.y, dirIt.next());
			if (this.isWithinnBoard(p.x, p.y))	continue;
			t = this.board[p.x][p.y];
			if (t.isStop())	return p;
		}
		return null;
	}
	/**===============================================================
	 * @return the list of the neighbors connected to the current tile
	 * (<x, y> has a path to the neighbor and the neighbor has a path to <x, y>)
	 =================================================================*/
	public LinkedList<Point> getConnectedNeighborPositions(int x, int y)
	{
		LinkedList<Point> neighbor	= this.getAccessibleNeighborsPositions(x, y);
		LinkedList<Point> res		= new LinkedList<Point>();
		LinkedList<Point> neighbor0;
		Point p = new Point(x, y);

		for (Point p0: neighbor)
		{
			neighbor0 = this.getAccessibleNeighborsPositions(p0.x, p0.y);
			if (neighbor0.contains(p)) res.add(p0);
		}

		return res;
	}
	/**===============================================================
	 * @return true if the player is currently building his tracks
	 =================================================================*/
	public boolean isContructing(String name)
	{
		PlayerInfo pi = this.playerInfoList.get(name);

		if (pi == null) throw new RuntimeException("Unknown player: " + name);
		return pi.history.getLast().isConstructing();
	}
	/**===============================================================
	 * @return the positions of the buildings in the player's path
	 =================================================================*/
	public LinkedList<Point> getBuildings(String name)
	{
		PlayerInfo pi = this.playerInfoList.get(name);
		return new LinkedList<Point> (pi.buildingInLine_position);
	}
	/**===============================================================
	 * @return the player's first terminal (top left square)
	 =================================================================*/
	public LinkedList<Point> getTerminus(String name)
	{
		PlayerInfo pi = this.playerInfoList.get(name);
		return new LinkedList<Point>(pi.terminus);
	}
	/**===============================================================
	 * @return true if the player's track is completed (path between the 2 terminus and through all the stops)
	 =================================================================*/
	public boolean isTrackCompleted(String name)
	{
		LinkedList<Point> path;
		Point p0, p1;

		path = this.getTerminus(name);
		path.addAll(this.getBuildings(name));
		p0 = path.getFirst();
		for (int i=1; i<path.size(); i++)
		{
			p1 = path.get(i);
			if (this.getShortestPath(p0, p1) == null)	return false;
		}
		return true;
	}
	/**============================================
	 * @return Create the board from a file
	 ==============================================*/
	public Tile[][] scanBoardFile(Scanner sc)
	{
		Tile[][] res;
		String tileFileName;
		int width, height, rotation;

		try
		{
			width	= sc.nextInt();
			height	= sc.nextInt();
			res		= new Tile[width][height];
			for (int y=0; y<height; y++)
			{
				for (int x=0; x<width; x++)
				{
					tileFileName	= sc.next();
					res[x][y]		= new Tile(tileFileName);
					rotation = sc.nextInt();
					for (int i=0; i<rotation; i++) res[x][y].turnLeft();
				}
			}
		}
		catch (Exception e){throw new RuntimeException("Malformed board file");}

		return res;
	}
	/**============================================
	 * @return Writes a board in a file
	 ==============================================*/
	public void writeBoardInFile(FileWriter fw)
	{
		try 
		{
			fw.write("" + this.getWidth()	+ "\n");
			fw.write("" + this.getHeight()	+ "\n");
			for (int y=0; y<this.getHeight(); y++)
			{
				for (int x=0; x<this.getWidth(); x++)
				{
					fw.write("" + this.board[x][y].getTileID()	+ " " + this.board[x][y].getNbrLeftRotation() + "\n");
				}
				fw.write("\n\n");
			}
		}
		catch(Exception e){throw new RuntimeException("Error while writing the board");}
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	/**============================================
	 * @return Creates the line cards from the correspending file
	 ==============================================*/
	private void initExistingBuildingInLine(int nbrBuildingInLine)
	{
		File f = new File(lineFile+nbrBuildingInLine);
		Scanner sc;

		this.existingBuildingInLine = new LinkedList<String[][]>();
		try
		{
			sc = new Scanner(f);
			for (int l=0; l<maxNbrPlayer; l++)
			{
				String[][] strTab = new String[maxNbrPlayer][nbrBuildingInLine];
				for (int p=0; p<maxNbrPlayer; p++)
				{
					for (int b=0; b<nbrBuildingInLine; b++) strTab[p][b] = sc.next();
				}
				this.existingBuildingInLine.add(strTab);
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed line file");}
	}
	/**============================================
	 * @return Creates the initial hand from the correspending file
	 ==============================================*/
	private void initInitialHand()
	{
		File f = new File(initialHandFile);
		String tileName;
		Scanner sc;

		this.initialHand = new LinkedList<Tile>();
		try
		{
			sc = new Scanner(f);
			for (int i=0; i<initialHandSize; i++)
			{
				tileName = sc.next();
				this.initialHand.add(new Tile(tileName));
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed initial hand file");}
	}
	/**============================================
	 * @return the position of the building which name are given
	 ==============================================*/
	private LinkedList<Point> getBuildingPosition(String[] buildingNameTab)
	{
		LinkedList<Point> res = new LinkedList<Point>();
		String s0;

		for (String s: buildingNameTab)
		{
			for (int x=0; x<this.getWidth(); x++)
			{
				for (int y=0; y<this.getWidth(); y++)
				{
					s0 = this.board[x][y].getBuildingName();
					if ((s0 != null) && (s0.equals(s)))	res.addLast(new Point(x, y));
				}
			}
		}
		return res;
	}
	private LinkedList<Point> getTerminusPosition(int line)
	{
		LinkedList<Point>res = new LinkedList<Point>();
		int w = this.getWidth()-1;
		int h = this.getHeight()-1;
		int i0, i1;
		boolean i0F = false, i1F = false;

		for (int x=0; x<w; x++)
		{
			i0 = this.board[x][0].getTerminusName();
			i1 = this.board[x][h].getTerminusName();
			if ((i0 == line) && (!i0F)) {res.addLast(new Point(x, 0)); i0F = true;}
			if ((i1 == line) && (!i1F)) {res.addLast(new Point(x, h)); i1F = true;}
		}
		if (res.size() == 2) return res;
		i0F = false;
		i1F = false;
		for (int y=0; y<h; y++)
		{
			i0 = this.board[0][y].getTerminusName();
			i1 = this.board[w][y].getTerminusName();
			if ((i0 == line) && (!i0F)) {res.addLast(new Point(0, y)); i0F = true;}
			if ((i1 == line) && (!i1F)) {res.addLast(new Point(w, y)); i1F = true;}
		}
		if (res.size() != 2) throw new RuntimeException("Wrong terminus for line " + line + ": " + res);
		return res;
	}
	private Tile[][] boardCopy()
	{
		int			width	= getWidth();
		int			height	= getHeight();
		Tile[][]	res		= new Tile[width][height];

		for (int w=0; w<width; w++)
		{
			for (int h=0; h<height; h++)
			{
				res[w][h] = new Tile(board[w][h]);
			}
		}
		return res;
	}

// --------------------------------------------
// Player Info class:
// --------------------------------------------
	private class PlayerInfo implements Serializable
	{
		// Attributes
		private static final long	serialVersionUID = -7495867115345261352L;
		public PlayerInterface		player;
		public Hand					hand;
		public int					line;
		public String[]				buildingInLine_name;
		public LinkedList<Point>	buildingInLine_position;
		public LinkedList<Point>	terminus;
		public LinkedList<Action>	history;

		// Builder
		public PlayerInfo(PlayerInterface pi)
		{
			Random rnd = new Random();
			int i;

			this.player = pi;
			this.history= new LinkedList<Action>();
			this.hand	= new Hand(initialHand);
			i			= rnd.nextInt(remainingLine.size());						// Draw a line
			this.line	= remainingLine.get(i);
			remainingLine.remove(i);
			i			= rnd.nextInt(remainingBuildingInLine.size());				// Draw the buildings to go through
			this.buildingInLine_name = remainingBuildingInLine.get(i)[line-1];
			remainingBuildingInLine.remove(i);
			this.buildingInLine_position = getBuildingPosition(buildingInLine_name);// Init the building line position
			this.terminus= getTerminusPosition(this.line);							// Init the terminus position
		}
	}
}