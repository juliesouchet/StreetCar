package main.java.data;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
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
			public static final int maxCardinal = 6000;
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
		 * @return
		 * L'ensemble des actions possible pour le joueur spécifié (c'est son tour de jouer)
		 */
		public possibleActionsSet getPossibleActions(String playerName){
return null;
}
///////// BUGS
/*************			possibleActionsSet result = new possibleActionsSet(possibleActionsSet.maxCardinal);
			PlayerInfo pi = playerInfoList.get(playerName);
			Hand hand = pi.hand;
			Tile t1, t2, oldT;
			Direction[] uniqueDirTab1, uniqueDirTab2;
			Point origin;
			LinkedList<Point> neighbors;
			
			// Building
			if(!pi.startedMaidenTravel) {
				for (int i = 0; i < hand.getSize(); i++) {					// first tile from the hand
					t1 = hand.get(i);
					uniqueDirTab1 = t1.getUniqueDirectionTab();
					for (int d1 = 0; d1 < t1.getUniqueDirectionPtr(); d1++) {	// each rotation of the 1st tile (minus the duplicates)
						t1.setDirection(uniqueDirTab1[d1]);
						
						for (int j = i; j < hand.getSize(); j++) {			// second tile from the hand
							t2 = hand.get(j);
							uniqueDirTab2 = t2.getUniqueDirectionTab();
							for (int d2 = 0; d2 < t2.getUniqueDirectionPtr(); d2++) {// each rotation of the 2nd tile (minus the duplicates)
								t2.setDirection(uniqueDirTab2[d2]);						
							
								for (int x = 0; x < board.length; x++) {			// each square of the board
									for (int y = 0; y < board[x].length; y++) {
										
										if(isAcceptableTilePlacement(x, y, t1)) { // simple build t1
											result.acceptablesActions[result.cardinal] = Action.newBuildSimpleAction(new Point(x,y), t1);
											result.cardinal++;
										}
										// TODO si la première pose est un échange, tester les poses de la tuile récupérée
										oldT = getTile(x, y);
										setTile(x, y, t1);
										for (Point p : getAccessibleNeighborsPositions(x, y)) {	// double build t1 & neighbors (t2)
											if(isAcceptableTilePlacement(p.x, p.y, t2)) {
												result.acceptablesActions[result.cardinal] = Action.newBuildDoubleAction(new Point(x,y), t1, p, t2);
												result.cardinal++;
											}
										}
										setTile(x, y, oldT);
										
									}
								}
								
							}
						}
						
					}
				}
			}
			
			// Traveling
			else {	// pi.startedMaidenTravel
				origin = pi.tramPosition;
				neighbors = getAccessibleNeighborsPositions(origin.x, origin.y);
				// TODO interdire les retours en arrière
				for (Point inter : neighbors) {
					if(distance(inter, origin) <= maxPlayerSpeed || getTile(inter).isStop()) {
						Point[] destination = {inter};
						result.acceptablesActions[result.cardinal] = Action.newMoveAction(destination);
						result.cardinal++;
					}
					neighbors.addAll(getAccessibleNeighborsPositions(inter.x, inter.y));
				}
			}
			return result; //TODO implémenter la méthode
		}
		// END ulysse'swish list
****************************/
		/**
		 * @param p1
		 * @param p2
		 * @return distance de manhattan entre p1 et p2
		 */
		@SuppressWarnings("unused")
		private int distance(Point p1, Point p2) {
			return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
		}
	
	
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private static final long			serialVersionUID		= -2740586808331187527L;
	public static String				boardDirectory			= "src/main/resources/boards/";
	public static final	String			lineFile				= "src/main/resources/line/lineDescription";
	public static final	String			buildingInLineFile		= "src/main/resources/line/buildingInLineDescription_";
	public static final int				minNbrPlayer			= 1; //TODO modifie par ulysse pour permettre tests basiques des automates. remettre a 2
	public static final int				maxNbrPlayer			= 5;
	public static final int				minLine					= 1;
	public static final int				maxLine					= 6;
	public static final int				minNbrBuildingInLine	= 2;
	public static final int				maxNbrBuildingInLine	= 3;
	public static final int				minSpeed				= 1;
	public static final int				maxSpeed				= 10;

	private static int[]				existingLine;
	private static String[][][]			existingBuildingInLine;
	private static Color[]				existingColors;

	LinkedList<Integer>					remainingLine;
	private LinkedList<String[][]>		remainingBuildingInLine;
	private LinkedList<Color>			remainingColors;

	private String						gameName;
	private int							nbrBuildingInLine;
	private Tile[][]					board;
	private Deck						deck;
	private HashMap<String, PlayerInfo>	playerInfoList;
	private int							round;
	private int							maxPlayerSpeed;
	private String[]					playerOrder;
	private String						host;
	private String						winner;

	private Path[]						additionalPath = Tile.initPathTab();// Optimization attribute

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**
	 * Creates the game data
	 * @param gameName : the name of this game (as given by the host)
	 * @param boardName : the initial board
	 * @param nbrBuildingInLine : the amount of obligatory stops
	 * @throws ExceptionUnknownBoardName
	 * @throws RuntimeException
	 */
	public Data(String gameName, String boardName, int nbrBuildingInLine) throws ExceptionUnknownBoardName, RuntimeException
	{
		File f = new File(boardDirectory + boardName);
		Scanner sc;

		if ((nbrBuildingInLine > maxNbrBuildingInLine) || 
			(nbrBuildingInLine < minNbrBuildingInLine))	throw new RuntimeException("Unknown nbr building in a line");

		this.gameName			= new String(gameName);
		this.nbrBuildingInLine	= nbrBuildingInLine;
		try						{sc = new Scanner(f);}
		catch(Exception e)		{e.printStackTrace(); throw new ExceptionUnknownBoardName();}
		this.board				= scanBoardFromFile(sc);
		sc.close();
		this.deck				= new Deck();
		this.playerInfoList		= new HashMap<String, PlayerInfo>();
		this.maxPlayerSpeed		= minSpeed;
		this.winner				= null;
		this.remainingLine		= new LinkedList<Integer>();
		for (int j=minLine; j<=maxLine; j++) remainingLine.add(j);

		this.parseStaticGameInformations(nbrBuildingInLine);							// Init the existing buildings, lines (and corresponding colors)
	}
	private Data(){}
	/** Returns a copy of the current data, as seen by the player (the hidden informations are not given) */
	public Data getClone(String playerName)
	{
		Data res = new Data();
		Copier<Tile> 		cpT		= new Copier<Tile>();
		Copier<Color> 		cpC		= new Copier<Color>();
		Copier<String> 		cpS		= new Copier<String>();

		res.remainingBuildingInLine	= null;
		res.remainingLine			= null;
		res.remainingColors			= cpC.copyList(remainingColors);

		res.gameName				= new String(this.gameName);
		res.nbrBuildingInLine		= this.nbrBuildingInLine;
		res.board					= cpT.copyMatrix(this.board);
		res.deck					= this.deck.getPlayerClone();
		res.playerInfoList			= getCopyOfPlayerInfoList(playerName);
		res.round					= this.round;
		res.winner					= (this.winner == null) ? null : new String(this.winner);
		res.maxPlayerSpeed			= this.maxPlayerSpeed;
		res.playerOrder				= (this.playerOrder == null)? null : cpS.copyTab(this.playerOrder);
		res.host					= (this.host == null)		? null : new String(this.host);
res.playerOrder	= (playerOrder==null) ? null : cpS.copyTab(playerOrder);
		return res;
	}

// --------------------------------------------
// Setter:
// --------------------------------------------
	
	public void setPreviousTramPosition(String playerName, Point newPosition) { playerInfoList.get(playerName).previousTramPosition = newPosition; }
	
	public void setMaximumSpeed(int newMaxSpeed) { this.maxPlayerSpeed = newMaxSpeed; } // TODO rename this method
	
	/**================================================
	 * Add a player to the present game
	 ==================================================*/
	public void addPlayer(PlayerInterface p, String playerName, boolean isHost, boolean isHuman) throws ExceptionFullParty
	{
		if (this.playerInfoList.size() >= maxNbrPlayer)	throw new ExceptionFullParty();
		if ((isHost) && (this.host != null))			throw new ExceptionHostAlreadyExists();

		PlayerInfo pi = new PlayerInfo(p, isHuman, playerName);
		this.playerInfoList.put(playerName, pi);
		if (isHost) this.host = new String(playerName);
	}
	/**==============================================================
	 * Set the player's color.  Every color matches an unique line.
	 * This function initialize the player's line and the player's buildings
	 ================================================================*/
	public void setPlayerColor(String playerName, Color playerColor)
	{
		PlayerInfo pi = this.playerInfoList.get(playerName);

		if (!this.remainingColors.contains(playerColor)) throw new RuntimeException("Used color");

		pi.color = new Color(playerColor.getRGB());
		this.remainingColors.add(pi.color);
		this.remainingColors.remove(playerColor);
	}
	/**================================================
	 * Remove a player from the present game
	 ==================================================*/
	public void removePlayer(String playerName)
	{
		PlayerInfo pi = this.playerInfoList.get(playerName);
		if (pi == null) throw new RuntimeException("Unknown player: " + playerName);

		this.remainingLine				.add(pi.line);
		this.remainingColors			.add(pi.color);
		this.remainingBuildingInLine	.add(pi.remainingBuildingInLineSave);
		this.playerInfoList.remove(playerName);
	}
	/**================================================
	 * Start the game:
	 * Check whether all the parameters have been set
	 * Pick the player order (random)
	 ==================================================*/
	public void hostStartGame(String host)
	{
		if (!this.isGameReadyToStart())	throw new RuntimeException("The game definition is not complete"); 
		if (!this.host.equals(host))throw new RuntimeException("The starting host does not correspond the Data known host");

// TODO: Enlever les LinkedList
		LinkedList<String> players = new LinkedList<String> (this.getPlayerNameList());
		Random rnd = new Random();
		int i, size = players.size();

		this.round			= -1;
		this.winner			= null;
		this.playerOrder	= new String[size];				// Init playerOrder
		for (int s=size-1; s>=0; s--)
		{
			i = rnd.nextInt(s+1);
			playerOrder[s] = players.get(i);
			players.remove(i);
		}

		this.skipTurn();
	}
	/**===================================================
	 * Makes the game forward to the next player's turn
	 =====================================================*/
	public void skipTurn()
	{
		this.round ++;
		String playerName = this.getPlayerTurn();
		this.playerInfoList.get(playerName).newRound();
	}
////////////////TODO
// TODO toremove
	public void setTile(int x, int y, Tile t)
	{
		if (this.isGameStarted()) throw new RuntimeException("This methode is kept for the IA tests...");
		this.board[x][y] = t;
	}
	/**===================================================
	 * Places the given tile on the board.  If the board had an non empty tile, the old tile is put in the player's hand.
	 * The tile is removed from the player's hand.
	 =====================================================*/
	public void	placeTile(String playerName, int x, int y, Tile t)
	{
		PlayerInfo	pi		= this.playerInfoList.get(playerName);
		Hand		hand	= pi.hand;
		Tile		oldT	= null;

		if (!this.board[x][y].isEmpty()) oldT = this.board[x][y];
		this.board[x][y] = t;
		hand.remove(t);												// Remove the tile from the player's hand
		if (oldT != null) hand.add(oldT);							// Change the current tile
		Point building = this.isBuildingAround(x, y);
		if (building != null)										// Case put stop next to building
		{
			if (this.isStopNextToBuilding(building) == null)
				this.board[x][y].setStop(true);
		}
// TODO: Enlever les LinkedList
		LinkedList<Action> history = pi.getLastActionHistory();
		history.addLast(Action.newBuildSimpleAction(x, y, t));		// Update player's history
	}
	/**===================================================
	 * Draw a tile from the deck.  This tile is put in the player's hand
	 =====================================================*/
	public void drawTile(String playerName, int nbrCards)
	{
		Hand hand = this.playerInfoList.get(playerName).hand;
		Tile t;

		for (int i=0; i<nbrCards; i++)
		{
			t = this.deck.drawTile();
			hand.add(t);
		}
	}
	/**===================================================
	 * Draw a tile from a player's hand.  This tile is put in the player's hand
	 =====================================================*/
	public void pickTileFromPlayer(String playerName, String chosenPlayerName, Tile tile)
	{
		Hand src = this.playerInfoList.get(playerName).hand;
		Hand dst = this.playerInfoList.get(chosenPlayerName).hand;

		dst.remove(tile);
		src.add(tile);
	}
	/**
	 * The player declares the start of his maiden travel 
	 */
	public void startMaidenTravel(String playerName)
	{
		playerInfoList.get(playerName).startedMaidenTravel = true;
	}
	/**
	 * The player moves his streetcar
	 * @param playerName
	 * @param newPosition
	 */
	public void setTramPosition(String playerName, Point newPosition)
	{
		PlayerInfo pi = playerInfoList.get(playerName);

		pi.tramPosition = newPosition;
		if (newPosition.equals(pi.endTerminus[0]))	this.winner = new String(playerName);
		if (newPosition.equals(pi.endTerminus[1]))	this.winner = new String(playerName);
	}
// TODO: Enlever les LinkedList
	/**
	 * The player chooses the destination of his maiden travel (the opposite terminus from his starting terminus)
	 * @param playerName : the player
	 * @param dest : the two points corresponding to the ending terminus
	 */
	public void	setDestinationTerminus(String playerName, Point[] dest)
	{
		Point[] tab = playerInfoList.get(playerName).endTerminus;
		for (int i=0; i<tab.length; i++)
		{
			tab[i].x = dest[i].x;
			tab[i].y = dest[i].y;
		}
	}

// --------------------------------------------
// Getter relative to travel:
// --------------------------------------------
	/** Returns the current position of this player's streetcar 
	 * (or null if he hasn't started yet his maiden travel) */
	public Point	getTramPosition(String playerName)							{return new Point(playerInfoList.get(playerName).tramPosition);}
	/**
	 * Returns true if this terminus belongs to that player
	 */
	public boolean	isPlayerTerminus(String playerName, Point terminus)
	{
		Point[] terminusTab = playerInfoList.get(playerName).terminus;
		for (Point p: terminusTab) if (p.equals(terminus)) return true;
		return false;
	}

// --------------------------------------------
// Getter relative to players:
// --------------------------------------------
	/** Returns the player with this name */
	public PlayerInterface		getRemotePlayer(String playerName)				{return this.playerInfoList.get(playerName).player;}
	/** Returns the number of this player's line */
	public int					getPlayerLine(String playerName)				{return this.playerInfoList.get(playerName).line;}
	/** Returns this player's color */
	public Color				getPlayerColor(String playerName)				{return this.playerInfoList.get(playerName).color;}
	/** Return true if this player is hosting the game */
	public boolean				isHost(String playerName)						{return this.host.equals(playerName);}
	/** Returns true if there is a player with this name among the players of this game */
	public boolean				isPlayerLogged(String name)						{return this.playerInfoList.containsKey(name);}
	public boolean				hasDoneFirstAction(String name)					{return this.playerOrder[0].equals(name);}
	/** Returns the number of tile in this player's hand */
	public int					getHandSize(String playerName)					{return this.playerInfoList.get(playerName).hand.getSize();}
	/** Returns the tileIndex-nth tile in this player's hand */
	public Tile					getHandTile(String playerName, int tileIndex)	{return this.playerInfoList.get(playerName).hand.get(tileIndex);}
	/** Returns true if this tile is in the player's hand */
	public boolean				isInPlayerHand(String playerName, Tile t)		{return this.playerInfoList.get(playerName).hand.isInHand(t);}
	/** Returns true if this player's name is already used by another */
	public boolean				isUsedPlayerName(String playerName)				{return this.playerInfoList.keySet().contains(playerName);}
	/** Returns the four points of this player's termini (two per terminus) */
	public Point[]				getPlayerTerminusPosition(String playerName)	{return this.playerInfoList.get(playerName).terminus;}
	/** Returns the buildings that this player must "visit" during his maiden travel. 
	 * He must pass through stops that are next to the buildings (one each). */
	public Point[]				getPlayerAimBuildings(String playerName)		{return this.playerInfoList.get(playerName).buildingInLine_position;}
	/** Returns the number of cards to draw at the end of this player's turn */
	public int					getPlayerRemainingTilesToDraw(String playerName){return (Hand.maxHandSize - this.playerInfoList.get(playerName).hand.getSize());}
	/** Returns true if the player has started his maiden travel 
	 * (his track is completed and his streetcar is on the board) */
	public boolean				hasStartedMaidenTravel(String playerName)		{return this.playerInfoList.get(playerName).startedMaidenTravel;}
	/** Returns the previous position of this player's streetcar (one square backwards) */
	public Point				getPreviousTramPosition(String playerName)		{return playerInfoList.get(playerName).previousTramPosition; }
// TODO: Enlever les LinkedList
	/** Returns true if this player still has actions to do in his turn */
	public boolean				hasRemainingAction(String playerName)
	{
		if (!this.isPlayerTurn(playerName))	throw new RuntimeException("Not player's turn: " + playerName);
		LinkedList<Action> lastActions = this.playerInfoList.get(playerName).getLastActionHistory();

		if		(lastActions.size() == 0) return true;
		else if (lastActions.size() == 1)
		{
			Action a = lastActions.getFirst();
			return (a.isSimpleConstructing());
		}
		else if (lastActions.size() == 2) return false;
		else	throw new RuntimeException("Player history malformed: cell size = " + lastActions.size());
	}
// TODO: Enlever les LinkedList
	/** Returns true if this player is at the start of his turn (and he hasn't done anything yet) */
	public boolean isStartOfTurn(String playerName)
	{
		if(!isPlayerTurn(playerName)) return false;
		LinkedList<Action> lastActions = this.playerInfoList.get(playerName).getLastActionHistory();
		return lastActions.size() == 0;
	}
	/**===============================================================
	 * @return true if the player's track is completed (path between the 2 terminus and through all the buildings)
	 =================================================================*/
	public boolean isTrackCompleted(String playerName)
	{
		int			size	= 2 + this.nbrBuildingInLine;
		Point[]		path	= new Point[size];
		PlayerInfo	pi		= this.playerInfoList.get(playerName);
		Point p0, p1, building;

		path[0] = pi.terminus[0];
		path[1] = pi.terminus[3];

		for (int i=0; i<this.nbrBuildingInLine; i++)				// Look for stop around buildings
		{
			building	= pi.buildingInLine_position[i];
			path[i+2]	= this.isStopNextToBuilding(building);
			if (path[i+2] == null) return false;
		}
		p0 = path[0];
		for (int i=1; i<path.length; i++)
		{
			p1 = path[i];
			if (this.getShortestPath(p0, p1) == null)	return false;
			p0 = p1;
		}
		return true;
	}

// --------------------------------------------
// Getter relative to game:
// --------------------------------------------
	/** Returns true if all the tiles have been played but no player can win */
	public boolean				isGameBlocked() {
		if(!isEmptyDeck())					return false;
		Set<String> playerNameList = getPlayerNameList();
		for(String playerName : playerNameList) {
			if(getHandSize(playerName)>0 || hasStartedMaidenTravel(playerName))	return false;
		}
		return true;
	}
	/** Returns the name of the winner, null if the game is in progress */
	public String				getWinner()										{return this.winner;}
	/** Returns the total number of remaining tiles in the deck */
	public int					getNbrRemainingDeckTile()						{return this.deck.getNbrRemainingDeckTile();} // ajouté par Julie
	/** Returns the game name (the one given by the host at the creation) */
	public String				getGameName()									{return new String(this.gameName);}
	/** Returns the names of the players in the game (host + joiners) */
	public Set<String>			getPlayerNameList()								{return this.playerInfoList.keySet();}
	/** Returns a copy of the board */
	public Tile[][]				getBoard()										{return new Copier<Tile>().copyMatrix(this.board);}
	/**
	 * @param x
	 * @param y
	 * @return a copy of the tile at position (x,y)
	 */
	public Tile					getTile  (int x, int y)							{return this.board[x][y].getClone();}
//TODO: peut etre pour l'ia 	public Tile					getTileIA(int x, int y)							{return this.board[x][y];}
	/**
	 * @param p
	 * @return a copy of the tile at position p
	 */
	public Tile					getTile(Point p)								{return getTile(p.x, p.y);}
	/**
	 * @return the width of the board
	 */
	public int					getWidth()										{return this.board.length;}
	/**
	 * @return the height of the board
	 */
	public int					getHeight()										{return this.board[0].length;}
	/**
	 * @return the number of necessary stops in the game
	 */
	public int					nbrBuildingInLine()								{return this.nbrBuildingInLine;}
	/**
	 * Calculates the shortest path from p0 to p1 using tiles already on the board
	 * @param p0
	 * @param p1
	 * @return the path if it exists, else null
	 */
	public LinkedList<Point>	getShortestPath(Point p0, Point p1)				{return PathFinder.getPath(this, p0, p1);}
	/**
	 * @param p1
	 * @param p2
	 * @return true if there is a path between p1 and p2 using tiles already on the board, else false
	 */
	public boolean				pathExistsBetween(Point p1, Point p2)			{return getShortestPath(p1, p2) != null;}
	/**
	 * @return the number of players in the game (host included)
	 */
	public int					getNbrPlayer()									{return this.playerInfoList.size();}
	/**
	 * The maximum number of squares that a streetcar can go in one turn. 
	 * This value goes up by 1 each time a streetcar moves, up to 10. 
	 * If a streetcar stops before the limit, then the maximum speed becomes the traveled distance + 1.
	 * @return the maximum speed
	 */
	public int					getMaximumSpeed()								{return this.maxPlayerSpeed;}
	/** The number of the current round */
	public int					getRound()										{return this.round;}
	/** The host's player name */
	public String				getHost()										{return (this.host == null) ? null : new String(this.host);}
	/** The playing order of the players (chosen randomly at the start of the game) */
	public String[]				getPlayerOrder()								{return (new Copier<String>()).copyTab(playerOrder);}
	/** The player that is currently playing */
	public String				getPlayerTurn()									{return this.playerOrder[this.round%this.playerOrder.length];}
	/**
	 * @param c
	 * @return true if this color is already used by another player */
	public boolean				isUsedColor(Color c)							{return this.remainingColors.contains(c);}
	/**
	 * @param playerName
	 * @return true if it is this player's turn to play
	 */
	public boolean				isPlayerTurn(String playerName)					{return (this.playerOrder == null) ? false : (this.getPlayerTurn().equals(playerName));}
	/** Returns true if the deck is empty (there are no more tile to draw) */
	public boolean				isEmptyDeck()									{return this.deck.isEmpty();}
	/**
	 * @param nbrTile
	 * @return true if there is at least nbrTile tiles remaining in the deck
	 */
	public boolean				isEnougthTileInDeck(int nbrTile)				{return (this.deck.getNbrRemainingDeckTile() >= nbrTile);}
	/** Returns true if there are enough players to start */
	public boolean				isGameReadyToStart()							{return (this.playerInfoList.size() >= minNbrPlayer);}
	/** Returns true if the host has launched the game */
	public boolean				isGameStarted()									{return this.playerOrder != null;}
	/** Returns true if all the human players are initialized (?) */
	public boolean				isAllHumanPlayersInitialized()
	{
		for (String str: this.playerInfoList.keySet())
		{
			PlayerInfo pi = this.playerInfoList.get(str);
			if (!pi.isHuman)			continue;
			if (!pi.isInitialized())	return false;
		}
		return true;
	}
	/** Returns true if (x,y) is within the limits of the board */
	public boolean isWithinnBoard(int x, int y)
	{
		if ((x < 0) || (x >= getWidth()))	return false;
		if ((y < 0) || (y >= getHeight()))	return false;
		return true;
	}
	/** Returns true if (x,y) is on the edge of the board 
	 * (meaning, the one-square wide band around the board with the termini) */
	public boolean	isOnEdge(int x, int y)
	{
		if ((x == 0) || (x == getWidth()-1))	return true;
		if ((y == 0) || (y == getHeight()-1))	return true;
		return false;
	}
	/**===============================================================
	 * @return if the deposit of the tile t on the board at the position (x, y) is possible
	 =================================================================*/
	public boolean isAcceptableTilePlacement(int x, int y, Tile t)
	{
		Tile oldT = this.board[x][y];
		int additionalPathSize = oldT.isReplaceable(t, additionalPath);

		if (additionalPathSize == -1)	return false;													// Check whether t contains the old t (remove Tile and Rule C)
		if (this.isOnEdge(x, y))		return false;

		Tile nt = Tile.specialNonRealTileConstructor(additionalPath, additionalPathSize, t);
		int accessibleDirection = nt.getAccessibleDirections();
		for (Direction d: Direction.DIRECTION_LIST)														// Check whether the new tile is suitable with the (x,y) neighborhood
		{
			if (!d.isDirectionInList(accessibleDirection)) continue;
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
	 * @return the list of the neighbor coordinates that can be acceded from the (x,y) cell
	 ==============================================================*/
	public LinkedList<Point> getAccessibleNeighborsPositions(int x, int y)
	{
		LinkedList<Point>		res = new LinkedList<Point>();
		int	ad	= board[x][y].getAccessibleDirections();				// List of the reachable directions

		for (Direction d: Direction.DIRECTION_LIST)						// For each accesible position
		{
			if (!d.isDirectionInList(ad)) continue;
			Point next = d.getNeighbour(x, y);
			if (isWithinnBoard(next.x, next.y))	res.add(next);
		}
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor tiles that can be acceded from the (x,y) cell
	 ==============================================================*/
	public LinkedList<Tile> getAccessibleNeighborsTiles(int x, int y)
	{
		LinkedList<Tile>		res = new LinkedList<Tile>();
		int	ad	= board[x][y].getAccessibleDirections();				// List of the reachable directions

		for (Direction d: Direction.DIRECTION_LIST)						// For each accesible position
		{
			if (!d.isDirectionInList(ad))	continue;
			Point next = d.getNeighbour(x, y);
			if (isWithinnBoard(next.x, next.y))	res.add(board[next.x][next.y].getClone());
		}
		return res;
	}
	/**============================================================
	 * @returns the list of directions d such as the neighbor d has a path to the current tile (x,y)
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
	 * @return the position of the stop next to the building, or null if no stop is found in the building neighborhood
	 * @throws runtimeException if the parameter is not a building
	 ===========================================================================*/
	public Point isStopNextToBuilding(Point building)
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
	/**=========================================================================
	 * @return the position of the building next to the given position, or null if no building is found in the neighborhood
	 ===========================================================================*/
	public Point isBuildingAround(int x, int y)
	{
		Tile neighborT;
		Point neighbor;

		for (Direction dir: Direction.DIRECTION_LIST)
		{
			neighbor = dir.getNeighbour(x, y);
			if (this.isWithinnBoard(neighbor.x, neighbor.y))	continue;
			neighborT = this.board[neighbor.x][neighbor.y];
			if (neighborT.isBuilding())	return neighbor;
		}
		return null;
	}
	/**===============================================================
	 * @return the list of the neighbors connected to the current tile
	 * ((x,y) has a path to the neighbor and the neighbor has a path to (x,y))
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
	/**============================================
	 * @return Create the board from a file
	 ==============================================*/
	public Tile[][] scanBoardFromFile(Scanner sc)
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
			sc.close();
		}
		catch (Exception e){e.printStackTrace();throw new RuntimeException("Malformed board file");}

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
	public Color getRandomUnusedColor()
	{
		int i = (new Random()).nextInt(this.remainingColors.size());
		return this.remainingColors.get(i);
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

		Data.existingLine	= new int[maxLine];						// Scan the existing lines and corresponding colors
		Data.existingColors	= new Color[maxLine];
		this.remainingColors= new LinkedList<Color>();
		try
		{
			f = new File(lineFile);
			sc = new Scanner(f);
			for (int i=1; i<=maxLine; i++)
			{
				Data.existingLine[i-1] = i;
				line = sc.nextInt();
				if (line != i) {sc.close();throw new Exception();}
				color = sc.next();
				Data.existingColors[i-1]	= Util.parseColor(color);
				this.remainingColors		.add(Util.parseColor(color));
			}
			sc.close();
		}
		catch (Exception e){e.printStackTrace(); throw new RuntimeException("Malformed line file");}

		Data.existingBuildingInLine	= new String[maxLine][][];			// Scan the existing building in line cards
		this.remainingBuildingInLine= new LinkedList<String[][]>();
		try
		{
			f = new File(buildingInLineFile+nbrBuildingInLine);
			sc = new Scanner(f);
			for (int l=0; l<maxLine; l++)
			{
				String[][] strTab = new String[maxLine][nbrBuildingInLine];
				for (int p=0; p<maxLine; p++)
				{
					for (int b=0; b<nbrBuildingInLine; b++) strTab[p][b] = sc.next();
				}
				Data.existingBuildingInLine[l] = strTab;
				this.remainingBuildingInLine.addLast(strTab);
			}
			sc.close();
		}
		catch (Exception e){throw new RuntimeException("Malformed building in line file");}
	}
	/**============================================
	 * @return the position of the building which name are given
	 ==============================================*/
	private Point[] getBuildingPosition(String[] buildingNameTab)
	{
		Point[] res = new Point[this.nbrBuildingInLine];
		int ptrRes = 0;
		String s0;

		for (String s: buildingNameTab)
		{
			for (int x=0; x<this.getWidth(); x++)
			{
				for (int y=0; y<this.getWidth(); y++)
				{
					s0 = this.board[x][y].getBuildingName();
					if ((s0 != null) && (s0.equals(s)))	{res[ptrRes] = new Point(x, y); ptrRes ++;}
				}
			}
		}
		if (ptrRes != buildingNameTab.length) throw new RuntimeException("Missing buildings on board");
		return res;
	}
	/**===========================================================================
	 * @return the list of the terminus positions corresponding to the given line
	 =============================================================================*/
	private Point[] getTerminusPosition(int line)
	{
		Point[] res = new Point[4];
		int w = this.getWidth()-1;
		int h = this.getHeight()-1;
		int i0, ptrRes = 0;

		for (int x=0; x<w; x++)
		{
			i0 = this.board[x][0].getTerminusName();
			if (i0 == line) {res[ptrRes] = new Point(x, 0); ptrRes ++;}
		}
		for (int x=0; x<w; x++)
		{
			i0 = this.board[x][h].getTerminusName();
			if (i0 == line) {res[ptrRes] = new Point(x, h); ptrRes ++;}
		}
		if (ptrRes == 4) return res;
		for (int y=0; y<h; y++)
		{
			i0 = this.board[0][y].getTerminusName();
			if (i0 == line) {res[ptrRes] = new Point(0, y); ptrRes ++;}
		}
		for (int y=0; y<h; y++)
		{
			i0 = this.board[w][y].getTerminusName();
			if (i0 == line) {res[ptrRes] = new Point(w, y); ptrRes ++;}
		}

		if (ptrRes != 4) throw new RuntimeException("Wrong terminus for line " + line + ": ");
		return res;
	}
	/**=====================================================================
	 * @return a copy of the playerInfo list where for each player:
	 * If the player is player who asks: all the informations are return
	 * else: only the shared information are shown.  If this player has started his maiden travel, his aim is shown too
	 ========================================================================*/
	private HashMap<String, PlayerInfo> getCopyOfPlayerInfoList(String playerName)
	{
		HashMap<String, PlayerInfo> res = new HashMap<String, PlayerInfo>();
		Copier<Point> cpP = new Copier<Point>();

		PlayerInfo pi, piRes;

		for (String str: this.playerInfoList.keySet())
		{
			pi				= this.playerInfoList.get(str);
			piRes			= new PlayerInfo();										// Shared Information
			if (!pi.isInitialized()) {res.put(str, piRes); continue;}
			piRes.player				= null;
			piRes.isHuman				= pi.isHuman;
			piRes.line					= pi.line;
			piRes.color					= (pi.color == null) ? null : new Color(pi.color.getRGB());
			piRes.hand					= pi.hand.getClone();
			piRes.terminus				= (pi.terminus == null) ? null : cpP.copyTab(pi.terminus);
			piRes.history				= (new Copier<LinkedList<Action>>()).copyList(pi.history);
			piRes.startedMaidenTravel	= pi.startedMaidenTravel;
			piRes.tramPosition			= (pi.tramPosition == null) ? null : new Point(pi.tramPosition);
			piRes.endTerminus			= (pi.endTerminus == null) ? null : cpP.copyTab(pi.endTerminus);
			piRes.previousTramPosition	= (pi.previousTramPosition == null) ? null : new Point(pi.previousTramPosition);

			if ((str.equals(playerName)) || (this.hasStartedMaidenTravel(str)))		// Private Informations
			{
				piRes.buildingInLine_name		= (new Copier<String>()).copyTab (pi.buildingInLine_name);
				piRes.buildingInLine_position	= (new Copier<Point>()).copyTab(pi.buildingInLine_position);
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

// --------------------------------------------
// Player Info class:
// --------------------------------------------
	private class PlayerInfo implements Serializable
	{
		// Attributes
		private static final long	serialVersionUID = -7495867115345261352L;
		public PlayerInterface		player;
		public boolean				isHuman;
		public Hand					hand;
		public int					line;											// Real value of the line (belongs to [1, 6])
		public Color				color;
		public String[]				buildingInLine_name;
		public Point[]				buildingInLine_position;
		public Point[]				terminus;										// Complete player's terminus list
//TODO: ulysse Ne plus stocker les action mais les data
		public LinkedList<LinkedList<Action>>	history;							// organized by turns
		public boolean				startedMaidenTravel		= false;				// Data relative to the travel
		public Point				tramPosition			= null;
		public Point[]				endTerminus				= new Point[2];
		public Point				previousTramPosition	= null;
public String[][]remainingBuildingInLineSave;

		// Builder
		private PlayerInfo(){}
		public PlayerInfo(PlayerInterface pi, boolean isHuman, String playerName)
		{
			Random rnd = new Random();
			int i;
			this.player					= pi;
			this.isHuman				= isHuman;
			this.hand					= Hand.initialHand.getClone();
			i 							= rnd.nextInt(remainingColors.size());
			this.color					= remainingColors.get(i);
			i 							= rnd.nextInt(remainingLine.size());
			this.line					= remainingLine.get(i);
			remainingLine.remove(i);
			i 							= rnd.nextInt(remainingBuildingInLine.size());
			this.remainingBuildingInLineSave = remainingBuildingInLine.get(i);
			this.buildingInLine_name	= this.remainingBuildingInLineSave[this.line-1];
			remainingBuildingInLine		.remove(i);
			this.buildingInLine_position= getBuildingPosition(this.buildingInLine_name);				// Init the building line position
			this.terminus				= getTerminusPosition(this.line);								// Init the terminus position
			this.history				= new LinkedList<LinkedList<Action>>();
			this.endTerminus[0]			= new Point();
			this.endTerminus[1]			= new Point();
		}

		// Getter
		public LinkedList<Action> getLastActionHistory()
		{
			if (this.history.isEmpty()) return null;
			else						return this.history.getLast();
		}
		public boolean isInitialized() 	{return this.line > -1;}
		public void newRound()			{this.history.addLast(new LinkedList<Action>());}
	}
}
