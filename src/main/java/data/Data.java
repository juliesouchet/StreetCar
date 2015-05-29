package main.java.data;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import main.java.data.Tile.Path;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionUnknownBoardName;
import main.java.player.PlayerInterface;
import main.java.util.Copier;
import main.java.util.Direction;
import main.java.util.Util;




public class Data implements Serializable
{
	//TODO Wish list de Ulysse:
		public class possibleActionsSet{
			private int cardinal;
			Action[] acceptablesActions; 
			private possibleActionsSet(int size){ // majorant du nombre d'actions possibles
				this.cardinal= 0;
				this.acceptablesActions = new Action[size];
			}
			
			public int getCardinal(){
				return this.cardinal;
			}
			
			public Action getAction(int index){
				return this.acceptablesActions[index];
			}
		}
		/**
		 * L'ensemble des actions possible pour le joueur spécifié (c'est son tour de jouer)
		 * @return
		 */
		public possibleActionsSet getPossibleActions(String playerName){
			return null; //TODO implémenter la méthode
		}
		// END ulysse'swish list
	
	
	
	
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private static final long			serialVersionUID		= -2740586808331187527L;
	public static String				boardDirectory			= "src/main/resources/boards/";
	public static final	String			lineFile				= "src/main/resources/line/lineDescription";
	public static final	String			buildingInLineFile		= "src/main/resources/line/buildingInLineDescription_";
	public static final String			initialHandFile			= "src/main/resources/initialHand/default";
	public static final int				minNbrPlayer			= 1; //TODO modifie par ulysse pour permettre tests basiques des automates. remettre a 2
	public static final int				maxNbrPlayer			= 6;
	public static final int				initialHandSize			= 5;
	public static final int				minNbrBuildingInLine	= 2;
	public static final int				maxNbrBuildingInLine	= 3;
	public static final int				minSpeed				= 1;
	public static final int				maxSpeed				= 10;
	public static LinkedList<Integer>	existingLine;
	public static LinkedList<String[][]>existingBuildingInLine;
	public static LinkedList<Color>		existingColors;

	private LinkedList<String[][]>		remainingBuildingInLine;
	private LinkedList<Tile>			initialHand;

	private String						gameName;
	private Tile[][]					board;
	private Deck						deck;
	private HashMap<String, PlayerInfo>	playerInfoList;
	private int							round;
	private int							maxPlayerSpeed;
	private String[]					playerOrder;
	private String						host;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Data(String gameName, String boardName, int nbrBuildingInLine) throws ExceptionUnknownBoardName, RuntimeException
	{
		File f = new File(boardDirectory + boardName);
		Scanner sc;

		if ((nbrBuildingInLine > maxNbrBuildingInLine) || 
			(nbrBuildingInLine < minNbrBuildingInLine))	throw new RuntimeException("Unknown nbr building in a line");

		this.gameName			= new String(gameName);
		try						{sc = new Scanner(f);}
		catch(Exception e)		{e.printStackTrace(); throw new ExceptionUnknownBoardName();}
		this.board				= scanBoardFile(sc);
		sc.close();
		this.deck				= new Deck();
		this.playerInfoList		= new HashMap<String, PlayerInfo>();
		this.maxPlayerSpeed		= minSpeed;
		this.parseStaticGameInformations(nbrBuildingInLine);							// Init the existing buildings, lines (and corresponding colors)
		this.remainingBuildingInLine= new LinkedList<String[][]>(existingBuildingInLine);

		this.initInitialHand();
	}
	private Data(){}
	public Data getClone(String playerName)
	{
		Data res = new Data();
		Copier<Tile> 		cpT		= new Copier<Tile>();
		Copier<String> 		cpS		= new Copier<String>();

		res.remainingBuildingInLine	= null;
		res.initialHand				= cpT.copyList(this.initialHand);

		res.gameName				= new String(this.gameName);
		res.board					= cpT.copyMatrix(this.board);
		res.deck					= null;
		res.playerInfoList			= getPlayerCopyOfPlayerInfoList(playerName);
		res.round					= this.round;
		res.maxPlayerSpeed			= this.maxPlayerSpeed;
		res.playerOrder				= (this.playerOrder == null)? null : cpS.copyTab(this.playerOrder);
		res.host					= (this.host == null)		? null : new String(this.host);

		return res;
	}


// --------------------------------------------
// Setter:
// --------------------------------------------
	/**================================================
	 * Add a player to the present game
	 ==================================================*/
	public void addPlayer(PlayerInterface p, String playerName, Color playerColor, boolean isHost) throws ExceptionFullParty
	{
		if (this.playerInfoList.size() >= maxNbrPlayer)	throw new ExceptionFullParty();
		if ((isHost) && (this.host != null))			throw new ExceptionHostAlreadyExists();

		PlayerInfo pi = new PlayerInfo(p, playerName, playerColor);
		this.playerInfoList.put(playerName, pi);
		if (isHost) this.host = new String(playerName);
	}
	/**================================================
	 * Remove a player from the present game
	 ==================================================*/
	public void removePlayer(String playerName)
	{
		PlayerInterface pi = this.playerInfoList.get(playerName).player;
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);
		this.playerInfoList.remove(playerName);
	}
	/**================================================
	 * Start the game:
	 * Check whether all the parameters have been set
	 * Pick the player order (random)
	 ==================================================*/
	public void hostStartGame(String host)
	{
		if (!this.gameCanStart())	throw new RuntimeException("The game definition is not complete"); 
		if (!this.host.equals(host))throw new RuntimeException("The starting host does not correspond the Data known host");

		LinkedList<String> players = new LinkedList<String> (this.getPlayerNameList());
		Random rnd = new Random();
		int i, size = players.size();

		this.round			= 0;
		this.playerOrder	= new String[size];
		for (int s=size-1; s>=0; s--)
		{
			i = rnd.nextInt(s+1);
			playerOrder[s] = players.get(i);
			players.remove(i);
		}
	}

////////////////TODO to remove
	public void skipTurn(){this.round ++;} // goes to the next player's turn
////////////////TODO 
// TODO toremove
	public void setTile(int x, int y, Tile t)
	{
		if (this.isGameStarted()) throw new RuntimeException("This methode is kept for the tests...");
		this.board[x][y] = t;
	}
	public void	placeTile(String playerName, int x, int y, Tile t)
	{
		Hand hand = this.playerInfoList.get(playerName).hand;
		Tile oldT = null;

		if (!this.board[x][y].isEmpty()) oldT = this.board[x][y];
		this.board[x][y] = t;
		hand.remove(t);
		if (oldT != null) hand.add(oldT);
	}
	public void drawCard(String playerName, int nbrCards)
	{
		Hand hand = this.playerInfoList.get(playerName).hand;
		Tile t;

		for (int i=0; i<nbrCards; i++)
		{
			t = this.deck.drawTile();
			hand.add(t);
		}
	}

// --------------------------------------------
// Getter:
// --------------------------------------------
	public Tile[][]				getBoard()										{return new Copier<Tile>().copyMatrix(this.board);}
	public String				getHost()										{return new String(this.host);}
	public int					getWidth()										{return this.board.length;}
	public int					getHeight()										{return this.board[0].length;}
	public int					getNbrPlayer()									{return this.playerInfoList.size();}
	public int					getMaximumSpeed()								{return this.maxPlayerSpeed;}
	public int					getRound()										{return this.round;}
	public Tile					getTile(int x, int y)							{return this.board[x][y].getClone();}
	public String				getGameName()									{return new String(this.gameName);}
	public Set<String>			getPlayerNameList()								{return this.playerInfoList.keySet();}
	public int					getPlayerLine(String playerName)				{return this.playerInfoList.get(playerName).line;}
	public Color				getPlayerColor(String playerName)				{return this.playerInfoList.get(playerName).color;}
	public boolean				containsPlayer(String name)						{return this.playerInfoList.containsKey(name);}
	public boolean				hasDoneFirstAction(String name)					{return this.playerOrder[0].equals(name);}
	public boolean				gameCanStart()									{return (this.playerInfoList.size() >= minNbrPlayer);}
	public LinkedList<Point>	getShortestPath(Point p0, Point p1)				{return PathFinder.getPath(this, p0, p1);}
	public Hand					getHand(String name)							{return this.playerInfoList.get(name).hand.getClone();}
	public boolean				isGameStarted()									{return this.playerOrder != null;}
	public boolean				isPlayerTurn(String playerName)
	{
		if (this.playerOrder == null) return false;
		int turn = this.round%this.playerOrder.length;
		return playerName.equals(playerOrder[turn]);
	}
	public PlayerInterface		getPlayer(String playerName)
	{
		PlayerInterface pi = this.playerInfoList.get(playerName).player;
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);
		return pi;
	}
	public boolean isWithinnBoard(int x, int y)
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
		LinkedList<Direction> accessibleDirection;
		Tile oldT = this.board[x][y];

		if (!oldT.isReplaceable(t, additionalPath))	return false;										// Check whether t contains the old t (remove Tile and Rule C)
		if (this.isOnEdge(x, y))					return false;

		Tile nt = new Tile(additionalPath, t);
		accessibleDirection = nt.getAccessibleDirections();
		for (Direction d: accessibleDirection)															// Check whether the new tile is suitable with the <x, y> neighborhood
		{
			Point neighbor = d.getNeighbour(x, y);

			Tile neighborT = this.board[neighbor.x][neighbor.y];
			if ((this.isOnEdge(neighbor.x, neighbor.y)) && (!neighborT.isTerminus()))	return false;	//		Rule A
			if (neighborT.isEmpty())													continue;		//		Rule E (step 1)
			if (neighborT.isBuilding())													return false;	//		Rule B
			d = d.turnHalf();
			if (!neighborT.isPathTo(d))													return false;	//		Rule E (step 2)
		}
		LinkedList<Direction> plt = this.getPathsLeadingToTile(x, y);									//		Rule D
		for (Direction dir: plt)	if (!t.isPathTo(dir))								return false;
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
		LinkedList<Point>		res = new LinkedList<Point>();
		LinkedList<Direction>	ad	= board[x][y].getAccessibleDirections();			// List of the reachable directions

		for (Direction d: ad)															// For each accesible position
		{
			Point next = d.getNeighbour(x, y);
			if (isWithinnBoard(next.x, next.y))	res.add(next);
		}
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor tiles that can be acceded from the <x,y> cell
	 ==============================================================*/
	public LinkedList<Tile> getAccessibleNeighborsTiles(int x, int y)
	{
		LinkedList<Tile>		res = new LinkedList<Tile>();
		LinkedList<Direction>	ad	= board[x][y].getAccessibleDirections();		// List of the reachable directions

		for (Direction d: ad)														// For each accesible position
		{
			Point next = d.getNeighbour(x, y);
			if (isWithinnBoard(next.x, next.y))	res.add(board[next.x][next.y].getClone());
		}
		return res;
	}
	/**============================================================
	 * @returns the list of directions d such as the neighbor d has a path to the current tile <x, y>
	 ==============================================================*/
	public LinkedList<Direction> getPathsLeadingToTile(int x, int y)
	{
		LinkedList<Direction> res	= new LinkedList<Direction>();
		Point neighbor;
		Tile neighborTile;

		for (Direction dir: Direction.DIRECTION_LIST)
		{
			neighbor	= dir.getNeighbour(x, y);
			if (!this.isWithinnBoard(neighbor.x, neighbor.y))	continue;
			neighborTile= board[neighbor.x][neighbor.y];
			dir = dir.turnHalf();
			if (neighborTile.isPathTo(dir)) {dir = dir.turnHalf();res.add(dir);}
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
		Point p;

		if (!t.isBuilding())	throw new RuntimeException("The point " + building + " is not a building");
		for (Direction dir: Direction.DIRECTION_LIST)
		{
			p = dir.getNeighbour(building.x, building.y);
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
	 * @return true if the player has started his maiden travel
	 =================================================================*/
	public boolean hasStartedMaidenTravel(String playerName)
	{
		PlayerInfo pi = this.playerInfoList.get(playerName);
//TODO
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);
		return pi.startedMaidenTravel;
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
		int width, height;
		Direction dir;

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
					res[x][y]		= Tile.parseTile(tileFileName);
					dir				= Direction.parse(sc.nextInt());
					res[x][y]		.setDirection(dir);
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
					fw.write("" + this.board[x][y].getTileID()	+ " " + this.board[x][y].getTileDirection().getVal() + "\n");
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
	 * @return Creates the line cards from the corresponding files
	 ==============================================*/
	private void parseStaticGameInformations(int nbrBuildingInLine)
	{
		int line;
		String color;
		File f;
		Scanner sc;

		Data.existingLine			= new LinkedList<Integer>();						// Scab the existing lines and corresponding colors
		Data.existingColors			= new LinkedList<Color>();
		try
		{
			f = new File(lineFile);
			sc = new Scanner(f);
			for (int i=1; i<=maxNbrPlayer; i++)
			{
				Data.existingLine	.add(i);
				line = sc.nextInt();
				if (line != i) {sc.close();throw new Exception();}
				color = sc.next();
				Data.existingColors.add(Util.parseColor(color));
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed line file");}

		Data.existingBuildingInLine	= new LinkedList<String[][]>();						// Scan the existing building in line cards
		try
		{
			f = new File(buildingInLineFile+nbrBuildingInLine);
			sc = new Scanner(f);
			for (int l=0; l<maxNbrPlayer; l++)
			{
				String[][] strTab = new String[maxNbrPlayer][nbrBuildingInLine];
				for (int p=0; p<maxNbrPlayer; p++)
				{
					for (int b=0; b<nbrBuildingInLine; b++) strTab[p][b] = sc.next();
				}
				Data.existingBuildingInLine.add(strTab);
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed building in line file");}
	}
	/**============================================
	 * @return Creates the initial hand from the corresponding file
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
				this.initialHand.add(Tile.parseTile(tileName));
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
	/**===========================================================================
	 * @return the list of the terminus positions corresponding to the given line
	 =============================================================================*/
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
	/**=====================================================================
	 * @return a copy of the playerInfo list where for each player:
	 * If the player is player who asks: all the informations are return
	 * else: only the shared information are shown.  If this player has started his maiden travel, his aim is shown too
	 ========================================================================*/
	private HashMap<String, PlayerInfo> getPlayerCopyOfPlayerInfoList(String playerName)
	{
		HashMap<String, PlayerInfo> res = new HashMap<String, PlayerInfo>();
		PlayerInfo pi, piRes;

		for (String str: this.playerInfoList.keySet())
		{
			pi				= this.playerInfoList.get(str);
			piRes			= new PlayerInfo();							// Shared Information
			piRes.player	= null;
			piRes.playerName= new String(str);
			piRes.line		= pi.line;
			piRes.color		= new Color(pi.color.getRGB());
			piRes.hand		= pi.hand.getClone();
			piRes.terminus	= (new Copier<Point>()).copyList(pi.terminus);
			piRes.history	= (new Copier<LinkedList<Action>>()).copyList(pi.history);
// TODO ajouter les nouveaux attributes de valentin
			if ((str.equals(playerName)) || (this.hasStartedMaidenTravel(str)))		// Private Informations
			{
				piRes.buildingInLine_name		= (new Copier<String>()).copyTab (pi.buildingInLine_name);
				piRes.buildingInLine_position	= (new Copier<Point>()).copyList(pi.buildingInLine_position);
			}
			else
			{
				piRes.buildingInLine_name		= null;
				piRes.buildingInLine_position	= null;
			}
			res.put(str, piRes);
		}
		return res;
	}
	private int getExistingColorIndex(Color color)
	{
		for (int i=0; i<existingColors.size(); i++)
			if (color.equals(existingColors.get(i))) return i;

		throw new RuntimeException("Unknown Color: " + color);
	}
// PB: rend un pointeur sur la structure locale (peut etre changé de l'exterieur)
	public PlayerInfo getPlayerInfo(String playerName)
	{
		return playerInfoList.get(playerName); 
	}

	// --------------------------------------------
	// Player Info class:
	// --------------------------------------------
	public class PlayerInfo implements Serializable
	{
		// Attributes
		private static final long	serialVersionUID = -7495867115345261352L;
		public PlayerInterface		player;
		public String				playerName;
		public Hand					hand;
		public int					line;	// Real value of the line (belongs to [1, 6])
		public Color				color;
		public String[]				buildingInLine_name;
		public LinkedList<Point>	buildingInLine_position;
		public LinkedList<Point>	terminus;
		public ArrayList<LinkedList<Action>>	history; // organized by turns

// TODO: PB de l'init de ces 2 attributes
		public boolean startedMaidenTravel = false;
		public Point tramPosition = new Point();
		public Point startTerminus = new Point();
// TODO ???
		public LinkedList<Point> endTermini = new LinkedList<>();

		// Builder
		public PlayerInfo(PlayerInterface pi, String playerName, Color playerColor)
		{
			Random rnd = new Random();
			int i;

			this.player		= pi;
			this.playerName	= new String(playerName);
			this.history	= new ArrayList<LinkedList<Action>>();
			this.hand		= new Hand(initialHand);
			this.line		= 1 + getExistingColorIndex(playerColor);
			i				= rnd.nextInt(remainingBuildingInLine.size());				// Draw the buildings to go through
			this.color		= new Color(playerColor.getRGB());
			this.buildingInLine_name = remainingBuildingInLine.get(i)[line-1];
			remainingBuildingInLine.remove(i);
			this.buildingInLine_position = getBuildingPosition(buildingInLine_name);	// Init the building line position
			this.terminus= getTerminusPosition(this.line);								// Init the terminus position
		}
		public PlayerInfo(){}
	}
}
