package main.java.data;

import java.awt.Point;
import java.io.File;
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




public class Data
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final	String			boardDirectory			= "src/main/resources/boards/";
	public static final	String			lineFile				= "src/main/resources/line/lineDescription_";
	public static final int				maxNbrPlayer			= 6;
	public static final int				minNbrBuildingInLine	= 2;
	public static final int				maxNbrBuildingInLine	= 3;

// TODO AAAAAAAAAAAAAAAAAAA Faire
	private LinkedList<Integer>			existingLine;
	private LinkedList<String[]>		existingLineBuildings;
	private LinkedList<Integer>			remainingLine;
// TODO AAAAAAAAAAAAAAAAAAA Faire

	private String						gameName;
	private Tile[][]					board;
	private Deck						deck;
	private HashMap<String, PlayerInfo>	playerInfoList;
	private int							round;

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

		this.existingLine		= new LinkedList<Integer>();
		this.remainingLine		= new LinkedList<Integer>();
		for (int i=1; i<=maxNbrPlayer; i++)
		{
			this.existingLine.add(i);
			this.remainingLine.add(i);
		}
// TODO AAAAAAAAAAAAAAAAAAA Faire
//		this.initExistingLineBuildings(nbrBuildingInLine);
// TODO AAAAAAAAAAAAAAAAAAA Faire
	}

// --------------------------------------------
// Setter:
// --------------------------------------------
	public void addPlayer(PlayerInterface p, String playerName) throws ExceptionFullParty
	{
		if (this.playerInfoList.size() >= maxNbrPlayer)	throw new ExceptionFullParty();
		PlayerInfo pi = new PlayerInfo(p);
		this.playerInfoList.put(playerName, pi);
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
	public Tile					getTile(int x, int y)							{return new Tile(this.board[x][y]);}
	public void					setTile(int x, int y, Tile t)					{this.board[x][y] = t;}
	public String				getGameName()									{return new String(this.gameName);}
	public Set<String>			getPlayerNameList()								{return this.playerInfoList.keySet();}
	public boolean				containsPlayer(String playerName)				{return this.playerInfoList.containsKey(playerName);}
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
	public LinkedList<Point> getNeighbourPositions(int x, int y)
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
	public LinkedList<Point> getAccessibleNeighboursCoordinates(int x, int y)
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
	public LinkedList<Tile> getAccessibleNeighboursTiles(int x, int y)
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

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private Tile[][] scanBoardFile(Scanner sc)
	{
		Tile[][] res;
		String tileFileName;
		int width, height;

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
				}
			}
		}
		catch (Exception e){throw new RuntimeException("Malformed board file");}

		return null;
	}
// TODO AAAAAAAAAAAAAAAAAAA Faire
	private void initExistingLineBuildings(int nbrBuildingInLine)
// TODO AAAAAAAAAAAAAAAAAAA Faire
	{
		File f = new File(lineFile+nbrBuildingInLine);
		Scanner sc;

		this.existingLineBuildings = new LinkedList<String[]>();
		try
		{
			sc = new Scanner(f);
			for (int i=0; i<maxNbrPlayer; i++)
			{
				String[] strTab = new String[nbrBuildingInLine];
				for (int j=0; j<nbrBuildingInLine; j++) strTab[j] = sc.next();
				this.existingLineBuildings.add(strTab);
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed line file");}
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
	private class PlayerInfo
	{
		// Attributes
		public PlayerInterface		player;
		public Hand					hand;
		public int					line;
		public LinkedList<Action>	history;

		// Builder
		public PlayerInfo(PlayerInterface pi)
		{
			int i;
			this.player = pi;
			this.history= new LinkedList<Action>();
			this.hand	= new Hand();			// TODO remplire la main a partire de la pioche
			i = (new Random()).nextInt(remainingLine.size());
			this.line	= remainingLine.get(i);
// TODO AAAAAAAAAAAAAAAAAAA Faire
// Créer le parcour du joueur
		}
	}
}