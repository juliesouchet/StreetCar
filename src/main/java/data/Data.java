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

import main.java.automaton.CoupleActionIndex;
import main.java.data.Tile.Path;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionUnknownBoardName;
import main.java.player.PlayerInterface;
import main.java.util.CloneableInterface;
import main.java.util.Copier;
import main.java.util.Direction;
import main.java.util.Util;




// TODO: quand on annule une action faire this.winner = null;
// TODO: dire a l'ihm: apres chaque refresh, verifier si getWinner != null et si isGameBlocked


public class Data implements Serializable
{
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
	public static final int				maxPossibleAction		= 6000;
	public static final int				maxNbrTramPath			= 4 ^ maxSpeed;
	public static final int				maxGameRound			= 150;

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
	public int							round;
	private int							maxPlayerSpeed;
	private String[]					playerOrder;
	private String						host;
	private String						winner;

	private Path[]						tmpPathTab		= Tile.initPathTab();				// Optimization attribute
	private PathFinder					pathFinder		= new PathFinder();
	private PathFinderMulti				pathFinderMulti	= new PathFinderMulti();
	private Point[][]					pathMatrix		= initPossibleTramPathMatrix();

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
		for (int j=minLine; j<=maxLine; j++)	this.remainingLine.add(j);

		this.parseStaticGameInformations(nbrBuildingInLine);							// Init the existing buildings, lines (and corresponding colors)
	}
	private Data(){}
	/**==============================================================
	 *  Returns a deep copy of the current data, as seen by the player (the hidden informations are not given)
	 ================================================================ */
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
		res.deck					= this.deck.getClone();
		res.playerInfoList			= getCopyOfPlayerInfoList(playerName);
		res.round					= this.round;
		res.winner					= (this.winner == null) ? null : new String(this.winner);
		res.maxPlayerSpeed			= this.maxPlayerSpeed;
		res.playerOrder				= (this.playerOrder == null)? null : cpS.copyTab(this.playerOrder);
		res.host					= (this.host == null)		? null : new String(this.host);

		return res;
	}
	public void setData(Data data)
	{
// TODO: ATTENTION: ne surtout pas clonner l'historique (req.history = null; res.ptrHistory ) -1)
		Copier<String> 		cpS		= new Copier<String>();

		this.gameName				= new String(data.gameName);
		this.nbrBuildingInLine		= data.nbrBuildingInLine;
		this.setBoard(data.board);
		this.deck.setDeck(data.deck);
		this.playerInfoList			= getCopyOfPlayerInfoList(null);
		this.round					= data.round;
		this.maxPlayerSpeed			= data.maxPlayerSpeed;
		this.playerOrder			= (data.playerOrder == null)? null : cpS.copyTab(data.playerOrder);
		this.host					= (data.host == null)		? null : new String(data.host);
		this.winner					= (data.winner == null)		? null : new String(data.winner);
	}

	/**=======================================================
	 * @return the previous game data.  The 
	 =========================================================*/
	public void getPreviousDataAndRollBack()
	{
		PlayerInfo	pi			= this.playerInfoList.get(this.getPlayerTurn());
		HistoryCell	hc			= pi.getLastActionHistory();
		String		playerName	= this.getPlayerTurn();
		int x, y;
//TODO traiter la pioche
//TODO   FINIIIIIIIIIIIIIR
		if (hc != null)																										// Case player has done an action this round
		{
			if (hc.action2 != null)																							//		Case undo round second game
			{
				if (hc.action2.isBUILD_SIMPLE())
				{
					x = hc.action2.positionTile2.x;
					y = hc.action2.positionTile2.y;
					if (hc.oldTile2 != null)	{this.board[x][y] = hc.oldTile2; pi.hand.remove(hc.oldTile2);}				//			Case: game was a tile improve
					else						 this.board[x][y] = this.getNewEmptyTile();
					pi.hand.add(hc.action2.tile1);
				}
				else throw new RuntimeException("????");
				hc.action2	= null;
				hc.oldTile2	= null;
			}
			else																											//		Case undo round first game
			{
				if (hc.action1.isBUILD_SIMPLE() || hc.action1.isBUILD_AND_START_TRIP_NEXT_TURN())
				{
					x = hc.action1.positionTile1.x;
					y = hc.action1.positionTile1.y;
					if (hc.oldTile1 != null)	{this.board[x][y] = hc.oldTile1; pi.hand.remove(hc.oldTile1);}				//			Case: game was a tile improve
					else						 this.board[x][y] = this.getNewEmptyTile();									//			Case: game was a simple tile put
					pi.hand.add(hc.action1.tile1);
				}
				else if (hc.action1.isBUILD_DOUBLE())
				{
					x = hc.action1.positionTile1.x;
					y = hc.action1.positionTile1.y;
					if (hc.oldTile1 != null)	{this.board[x][y] = hc.oldTile1;; pi.hand.remove(hc.oldTile1);}				//			Case: game was a tile improve
					else						 this.board[x][y] = this.getNewEmptyTile();									//			Case: game was a simple tile put
					pi.hand.add(hc.action1.tile1);
					x = hc.action1.positionTile2.x;
					y = hc.action1.positionTile2.y;
					if (hc.oldTile2 != null)	{this.board[x][y] = hc.oldTile2; pi.hand.remove(hc.oldTile2);}				//			Case: game was a tile improve
					else						 this.board[x][y] = this.getNewEmptyTile();									//			Case: game was a simple tile put
					pi.hand.add(hc.action1.tile2);
				}
				else if (hc.action1.isMOVE())
				{
					// TODO check avec travel
				}
				else throw new RuntimeException("????");
				hc.action1	= null;
				hc.oldTile1	= null;
				hc.oldTile2	= null;
			}
		}
		else																													// Case player ha
		{
			this.playerInfoList.get(playerName).undoRound();
			this.round --;
			this.getPreviousDataAndRollBack();
		}
		throw new RuntimeException("Pas fini");
	}

// --------------------------------------------
// Setter:
// --------------------------------------------
	/**===================================================================
	 * @return gathers all the possible action setters on this data
	 =====================================================================*/
	public void doAction(String playerName, Action action)
	{
		if (action.isMOVE())
		{
			if (action.startTerminus != null)
// TODO: avoir avec travel pour set terminus point
			this.setTramPosition(playerName, action.tramwayMovement[action.ptrTramwayMovement]); //TODO ptrTramwaymovement ou -1 ?
			return;
		}
		if (action.isBUILD_SIMPLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
			return;
		}
		if (action.isTWO_BUILD_SIMPLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
			this.placeTile(playerName, action.positionTile2.x, action.positionTile2.y, action.tile2);
			return;
		}
		if (action.isBUILD_DOUBLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1, action.positionTile2.x, action.positionTile2.y, action.tile2);
			return;
		}
		if (action.isBUILD_AND_START_TRIP_NEXT_TURN())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
			return;
		}
	}
	
	
	
	public void setPreviousTramPosition(String playerName, Point newPosition) { playerInfoList.get(playerName).previousTramPosition = newPosition; }
	public void setMaximumSpeed(int newMaxSpeed) { this.maxPlayerSpeed = newMaxSpeed; }

	/**================================================
	 * @return Add a player to the present game
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
	 * @return Set the player's color.  Every color matches an unique line.
	 * This function initialize the player's line and the player's buildings
	 ================================================================*/
	public void setPlayerColor(String playerName, Color playerColor)
	{
		PlayerInfo pi = this.playerInfoList.get(playerName);

		if (!this.remainingColors.contains(playerColor)) throw new RuntimeException("Used color");

		this.remainingColors.remove(playerColor);
		this.remainingColors.add(pi.color);
		pi.color = new Color(playerColor.getRGB());
	}
	/**================================================
	 * @return Remove a player from the present game
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
	 * @return Start the game: </br>
	 * Check whether all the parameters have been set
	 * Pick the player order (random)
	 ==================================================*/
	public void hostStartGame(String host)
	{
		if (!this.isGameReadyToStart())	throw new RuntimeException("The game definition is not complete"); 
		if (!this.host.equals(host))throw new RuntimeException("The starting host does not correspond the Data known host");

		int size = this.getNbrPlayer();

		this.round			= -1;
		this.winner			= null;
		this.playerOrder	= new String[size];				// Init playerOrder
		this.playerOrder[0]	= this.getHost();
		int i = 1;
		for (String str: this.playerInfoList.keySet())
		{
			if (str.equals(host)) continue;
			this.playerOrder[i] = str;
			i ++;
		}
		this.skipTurn();									// Update the player History
	}
	/**===================================================
	 * @return Makes the game forward to the next player's turn
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
	 * @return Places the given tile on the board.</br>
	 * If the board had an non empty tile, the old tile is put in the player's hand.</br>
	 * The tile is removed from the player's hand.</br>
	 =====================================================*/
	public void	placeTile(String playerName, int x, int y, Tile t)
	{
		PlayerInfo	pi		= this.playerInfoList.get(playerName);
		Hand		hand	= pi.hand;
		Tile		oldT	= null;
		Tile		oldTH	= this.board[x][y];

		if (!this.board[x][y].isEmpty()) oldT = this.board[x][y];
		this.board[x][y] = t;
		hand.remove(t);																			// Remove the tile from the player's hand
		if (oldT != null) hand.add(oldT);														// Change the current tile
		Point building = this.isBuildingAround(x, y);
		if (building != null)																	// Case put stop next to building
		{
			if (this.isStopNextToBuilding(building) == null) this.board[x][y].setStop(true);
		}
		pi.getLastActionHistory().addLastAction(Action.newBuildSimpleAction(x, y, t), oldTH, null);	// Update player's history
	}
	/**===================================================
	 * @return Places the two given tiles on the board.</br>
	 * If the board had an non empty tile, the old tile is put in the player's hand.</br>
	 * The tiles are  removed from the player's hand.
	 =====================================================*/
	public void	placeTile(String playerName, int x1, int y1, Tile t1, int x2, int y2, Tile t2)
	{
// TODO
		throw new RuntimeException("Not implemented yet");
	}
	/**===================================================
	 * @return Draw a tile from the deck.  This tile is put in the player's hand
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
	 * @return Draw a tile from a player's hand.  This tile is put in the player's hand
	 =====================================================*/
	public void pickTileFromPlayer(String playerName, String chosenPlayerName, Tile tile)
	{
		Hand src = this.playerInfoList.get(playerName).hand;
		Hand dst = this.playerInfoList.get(chosenPlayerName).hand;

		dst.remove(tile);
		src.add(tile);
	}
	/**==================================================
	 * @return The player declares the start of his maiden travel 
	 ====================================================*/
	public void startMaidenTravel(String playerName)
	{
		playerInfoList.get(playerName).startedMaidenTravel = true;
// TODO: ajouter une action a l'historique du joueur
// TODO: ajouter une action a l'historique du jeu
	}
	/**================================================
	 * @return The player moves his streetcar
	 ==================================================*/
	public void setTramPosition(String playerName, Point newPosition)
	{
		PlayerInfo pi = playerInfoList.get(playerName);

		pi.tramPosition = newPosition;
		if (newPosition.equals(pi.endTerminus[0]))	this.winner = new String(playerName);
		if (newPosition.equals(pi.endTerminus[1]))	this.winner = new String(playerName);
// TODO: ajouter une action a l'historique du joueur
// TODO: ajouter une action a l'historique du jeu
	}
	/**==========================================================================
	 * @return The player chooses the destination of his maiden travel (the opposite terminus from his starting terminus)
	 * @param playerName : the player
	 * @param dest : the two points corresponding to the ending terminus
	 =============================================================================*/
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
	/**=============================================================
	 *  @return  the current position of this player's streetcar 
	 * (or null if he hasn't started yet his maiden travel)
	 * ============================================================= */
	public Point getTramPosition(String playerName)
	{
		return new Point(playerInfoList.get(playerName).tramPosition);
	}
	/**=============================================================
	 * @return true if this terminus belongs to that player
	 * ============================================================= */
	public boolean	isPlayerTerminus(String playerName, Point terminus)
	{
		Point[] terminusTab = playerInfoList.get(playerName).terminus;
		for (Point p: terminusTab) if (p.equals(terminus)) return true;
		return false;
	}

// --------------------------------------------
// Getter relative to players:
// --------------------------------------------
	public PlayerInterface		getRemotePlayer(String playerName)				{return this.playerInfoList.get(playerName).player;}
	public int					getPlayerLine(String playerName)				{return this.playerInfoList.get(playerName).line;}
	public Color				getPlayerColor(String playerName)				{return this.playerInfoList.get(playerName).color;}
	public boolean				isHost(String playerName)						{return this.host.equals(playerName);}
	public boolean				isPlayerLogged(String name)						{return this.playerInfoList.containsKey(name);}
	public int					getHandSize(String playerName)					{return this.playerInfoList.get(playerName).hand.getSize();}
	public Tile					getHandTile(String playerName, int tileIndex)	{return this.playerInfoList.get(playerName).hand.get(tileIndex);}
	public boolean				isInPlayerHand(String playerName, Tile t)		{return this.playerInfoList.get(playerName).hand.isInHand(t);}
	public boolean				isUsedPlayerName(String playerName)				{return this.playerInfoList.keySet().contains(playerName);}
	public boolean				hasDoneRoundFirstAction(String playerName)		{return this.playerInfoList.get(playerName).hasDoneFirstRoundAction();}
	public Point[]				getPlayerTerminusPosition(String playerName)	{return this.playerInfoList.get(playerName).terminus;}
	public Point[]				getPlayerAimBuildings(String playerName)		{return this.playerInfoList.get(playerName).buildingInLine_position;}
	public int					getPlayerRemainingTilesToDraw(String playerName){return (Hand.maxHandSize - this.playerInfoList.get(playerName).hand.getSize());}
	public boolean				hasStartedMaidenTravel(String playerName)		{return this.playerInfoList.get(playerName).startedMaidenTravel;}
	public Point				getPreviousTramPosition(String playerName)		{return playerInfoList.get(playerName).previousTramPosition; }
	/**======================================================
	 * @return true if this player still has actions to do in his turn
	 ======================================================== */
	public boolean hasRemainingAction(String playerName)
	{
		if (!this.isPlayerTurn(playerName))	throw new RuntimeException("Not player's turn: " + playerName);
		HistoryCell lastActions = this.playerInfoList.get(playerName).getLastActionHistory();

		return lastActions.hasRemainingAction();
	}
	/**======================================================
	 *  @return true if this player is at the start of his turn (and he hasn't done anything yet)
	 ======================================================== */
	public boolean isStartOfTurn(String playerName)
	{
		if(!isPlayerTurn(playerName)) return false;
		HistoryCell lastActions = this.playerInfoList.get(playerName).getLastActionHistory();
		return lastActions.isEmpty();
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
	public int					getNbrRemainingDeckTile()						{return this.deck.getNbrRemainingDeckTile();} // ajouté par Julie
	public String				getGameName()									{return new String(this.gameName);}
	public Set<String>			getPlayerNameList()								{return this.playerInfoList.keySet();}
	public Tile[][]				getBoard()										{return new Copier<Tile>().copyMatrix(this.board);}
	public Tile					getTile  (int x, int y)							{return this.board[x][y].getClone();}
	public Tile					getTile(Point p)								{return getTile(p.x, p.y);}
	public int					getWidth()										{return this.board.length;}
	public int					getHeight()										{return this.board[0].length;}
	public int					nbrBuildingInLine()								{return this.nbrBuildingInLine;}
	public LinkedList<Point>	getShortestPath(Point p0, Point p1)				{return this.pathFinder.getPath(this, p0, p1);}
	public boolean				pathExistsBetween(Point p1, Point p2)			{return getShortestPath(p1, p2) != null;}
	public int					getNbrPlayer()									{return this.playerInfoList.size();}
	public int					getMaximumSpeed()								{return this.maxPlayerSpeed;}
	public int					getRound()										{return this.round;}
	public String				getHost()										{return (this.host == null) ? null : new String(this.host);}
	public String[]				getPlayerOrder()								{return (new Copier<String>()).copyTab(playerOrder);}
	public String				getPlayerTurn()									{return this.playerOrder[this.round%this.playerOrder.length];}
	public boolean				isUsedColor(Color c)							{return this.remainingColors.contains(c);}
	public boolean				isPlayerTurn(String playerName)					{return (this.playerOrder == null) ? false : (this.getPlayerTurn().equals(playerName));}
	public boolean				isEmptyDeck()									{return this.deck.isEmpty();}
	public boolean				isEnougthTileInDeck(int nbrTile)				{return (this.deck.getNbrRemainingDeckTile() >= nbrTile);}
	public boolean				isGameReadyToStart()							{return (this.playerInfoList.size() >= minNbrPlayer);}
	public boolean				isGameStarted()									{return this.playerOrder != null;}
	public LinkedList<Color>	getRemainingColors()							{return ((new Copier<Color>()).copyList(this.remainingColors));}
	public String				getWinner()										{return this.winner;}
	/**=======================================================================
	 * @return true if the player has no possible game, considering his hand, the deck and his travel
	 ========================================================================= */
	public boolean isGameBlocked(String playerName)
	{
		if ((!isEmptyDeck()) && (this.getHandSize(playerName) > 0))				return false;
		if (hasStartedMaidenTravel(playerName))									return false;

		Tile[] rotations = new Tile[4];
		for(int j = 0; j < 4; j++)	rotations[j] = new Tile();

		for (int i=0; i<this.getHandSize(playerName); i++)
		{
			Tile t = this.getHandTile(playerName, i);
			int nbrRotations = t.getUniqueRotationList(rotations);
			for (int r=0; r<nbrRotations; r++)
			{
				for (int x=1; x<this.getWidth()-1; x++)
				{
					for (int y=1; y<this.getHeight()-1; y++)
					{
						if (this.isAcceptableTilePlacement(x, y, rotations[r]))	return false;
					}
				}
			}
		}
		return true;
	}
	public boolean isWithinnBoard(int x, int y)
	{
		if ((x < 0) || (x >= getWidth()))	return false;
		if ((y < 0) || (y >= getHeight()))	return false;
		return true;
	}
	/**=================================================
	 *  @return true if (x,y) is on the edge of the board 
	 * (meaning, the one-square wide band around the board with the termini)
	 =================================================== */
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
		int additionalPathSize = oldT.isReplaceable(t, tmpPathTab);

		if (additionalPathSize == -1)	return false;													// Check whether t contains the old t (remove Tile and Rule C)
		if (this.isOnEdge(x, y))		return false;

		Tile nt = Tile.specialNonRealTileConstructor(tmpPathTab, additionalPathSize, t);
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
	/**===============================================================
	 * @return if the deposit of the two tiles together on the board at the position (x1, y1), (x2, y2) is possible
	 =================================================================*/
	public boolean isAcceptableTilePlacement(int x, int y, Tile tile1, int x2, int y2, Tile tile2)
	{
// TODO Auto-generated method stub
		if (x > -10) throw new RuntimeException("Not implemented yet");
		return false;
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
			if (!this.isWithinnBoard(p.x, p.y))	continue;
			if (this.board[p.x][p.y].isStop())	return p;
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
			if (!this.isWithinnBoard(neighbor.x, neighbor.y))	continue;
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
	/**=============================================================
	 * @return the number of different actions that may be realized at this step of the game.</br>
	 * This actions are added to the input tab.</br>
	 * The input tab size must be maxPossibleAction (or  higher).  Each one of its cells must have been initialized
	 ===============================================================*/
	public int getPossibleActions(String playerName, CoupleActionIndex[] resTab, boolean writeActionsInTab)
	{
		if (!this.isPlayerTurn(playerName))	throw new RuntimeException("Not the player turn: " + playerName);
		Tile[] tmpRotation1		= new Tile[4];											// Init optimization parameters
		Tile[] tmpRotation2		= new Tile[4];
		for (int i=0; i<4; i++)
		{
			tmpRotation1[i]= this.board[0][0].getClone();
			tmpRotation2[i]= this.board[0][0].getClone();
		}

		int res = 0, nbrRotation1, nbrRotation2, nbrPath;
		Point lastTramPosition		= this.playerInfoList.get(playerName).previousTramPosition;
		Point currentTramPosition	= this.playerInfoList.get(playerName).tramPosition;
		Point startTerminus			= this.getPlayerTerminusPosition(playerName)[0];
		Tile t, oldT1;

		if ((this.hasStartedMaidenTravel(playerName)) || (this.isTrackCompleted(playerName)))			// Case: can move tram
		{
// TODO s'arreter au stop
System.out.println("iciiiii");
			if (startTerminus == null)	startTerminus = this.getPlayerTerminusPosition(playerName)[0];	//		Case Can start maiden travel
			else						startTerminus = null;
			for (int l = 1; l<=this.maxPlayerSpeed; l++)
			{
				nbrPath = this.pathFinderMulti.getAllFixedLengthPath(this, currentTramPosition, l, this.pathMatrix);
				for (int i=0; i<nbrPath; i++)
				{
					if (this.pathMatrix[i][1].equals(lastTramPosition)) continue;
					if(writeActionsInTab){
						resTab[res].getAction().setMoveAction(startTerminus, this.pathMatrix[i], l);
						resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
					}
					res ++;
				}
			}
			return res;
		}
																										// Case is building
		for (int h1 = 0; h1<this.getHandSize(playerName); h1++)											//		For each player's hand tile
		{
			t				= this.getHandTile(playerName, h1);
			nbrRotation1	= t.getUniqueRotationList(tmpRotation1);
			for (int r1=0; r1<nbrRotation1; r1++)														//		For each first tile rotation
			{
				for (int x1=1; x1<this.getWidth()-1; x1++)												//		For each board cell
				{
					for (int y1=1; y1<this.getHeight()-1; y1++)
					{
						if (!this.isAcceptableTilePlacement(x1, y1, tmpRotation1[r1]))	continue;		//		Case player may start maiden travel next turn
						oldT1 = this.board[x1][y1];
						this.board[x1][y1] = tmpRotation1[r1];
						if (this.isTrackCompleted(playerName))			//TODO************ulysse non************** Peut etre evite en ajoutant un coup inutile
						{
System.out.println("iciiiii, trackCompleted");
							if(writeActionsInTab){
								resTab[res].getAction().setSimpleBuildingAndStartTripNextTurnAction(x1, y1, tmpRotation1[r1]);
								resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
							}
							res ++;
						}
						else if (this.getHandSize(playerName) == 1)	;									//		Case no second hand tile (!!!!!! Ne pas retirer le ';'  )
						else
						{
//System.out.println("iciiiii, general");
							for (int h2 = h1+1; h2<this.getHandSize(playerName); h2++)					//		For each second player's hand tile
							{
								for (int x2=1; x2<this.getWidth()-1; x2++)								//		For each board cell
								{
									for (int y2=1; y2<this.getHeight()-1; y2++)
									{
										t				= this.getHandTile(playerName, h2);
										nbrRotation2	= t.getUniqueRotationList(tmpRotation2);
										for (int r2=0; r2<nbrRotation2; r2++)							//		For each second tile rotation
										{
											if (!this.isAcceptableTilePlacement(x2, y2, tmpRotation2[r2])) continue;
											if(writeActionsInTab){
												resTab[res].getAction().setTwoSimpleBuildingAction(x1, y1, tmpRotation1[r1], x2, y2, tmpRotation2[r2]);
												resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
											}
											res ++;
										}
									}
								}
							}
						}
						this.board[x1][y1] = oldT1;
					}
				}
			}
		}

		return res;
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
	private Point[][] initPossibleTramPathMatrix()
	{
		Point[][] res = new Point[Data.maxNbrTramPath][Data.maxSpeed];

		for (int x=0; x<Data.maxNbrTramPath; x++)
		{
			for (int y=0; y<Data.maxSpeed; y++)
			{
				res[x][y] = new Point();
			}
		}
		return res;
	}
	private void setBoard(Tile[][] board)
	{
		if (this.board.length		!= board.length)		throw new RuntimeException("Diffrent boards");
		if (this.board[0].length	!= board[0].length)		throw new RuntimeException("Diffrent boards");

		for (int x=0; x<this.getWidth(); x++)
		{
			for (int y=0; y<this.getHeight(); y++)
			{
				this.board[x][y].copy(board[x][y]);
			}
		}
	}
	/**=====================================================================
	 * @return a copy of the playerInfo list where for each player:</br>
	 * If the player is playerName: all the informations are return</br>
	 * else: only the shared information are shown.  If this player has started his maiden travel, his aim is shown too</br>
	 * If playerName is null, all the informations are returned</br>
	 ========================================================================*/
	private HashMap<String, PlayerInfo> getCopyOfPlayerInfoList(String playerName)
	{
		HashMap<String, PlayerInfo> res = new HashMap<String, PlayerInfo>();
		Copier<Point> cpP = new Copier<Point>();

		PlayerInfo pi, piRes;

		for (String str: this.playerInfoList.keySet())
		{
			pi							= this.playerInfoList.get(str);
			piRes						= new PlayerInfo();													// Shared Information
			piRes.player				= pi.player;
			piRes.isHuman				= pi.isHuman;
			piRes.line					= pi.line;
			piRes.color					= (pi.color == null) ? null : new Color(pi.color.getRGB());
			piRes.hand					= pi.hand.getClone();
			piRes.terminus				= (pi.terminus == null) ? null : cpP.copyTab(pi.terminus);
			piRes.history				= (new Copier<HistoryCell>()).copyList(pi.history);
			piRes.startedMaidenTravel	= pi.startedMaidenTravel;
			piRes.tramPosition			= (pi.tramPosition == null) ? null : new Point(pi.tramPosition);
			piRes.endTerminus			= (pi.endTerminus == null) ? null : cpP.copyTab(pi.endTerminus);
			piRes.previousTramPosition	= (pi.previousTramPosition == null) ? null : new Point(pi.previousTramPosition);

			if ((playerName == null) || (str.equals(playerName)) || (this.hasStartedMaidenTravel(str)))		// Private Informations
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
	public Tile getNewEmptyTile(){return this.board[0][0].getClone();}

// --------------------------------------------
// Player Info class:
// --------------------------------------------
	private class PlayerInfo implements Serializable
	{
		// Attributes
		private static final long		serialVersionUID = -7495867115345261352L;
		public PlayerInterface			player;
		public boolean					isHuman;
		public Hand						hand;
		public int						line;											// Real value of the line (belongs to [1, 6])
		public Color					color;
		public String[]					buildingInLine_name;
		public Point[]					buildingInLine_position;
		public String[][]				remainingBuildingInLineSave;
		public Point[]					terminus;										// Complete player's terminus list
		public boolean					startedMaidenTravel		= false;				// Data relative to the travel
		public Point					tramPosition			= null;
		public Point[]					endTerminus				= new Point[2];
		public Point					previousTramPosition	= null;
		public LinkedList<HistoryCell>	history;							// organized by turns

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
			remainingColors.remove(i);
			i 							= rnd.nextInt(remainingLine.size());
			this.line					= remainingLine.get(i);
			remainingLine.remove(i);
			i 							= rnd.nextInt(remainingBuildingInLine.size());
			this.remainingBuildingInLineSave = remainingBuildingInLine.get(i);
			this.buildingInLine_name	= this.remainingBuildingInLineSave[this.line-1];
			remainingBuildingInLine		.remove(i);
			this.buildingInLine_position= getBuildingPosition(this.buildingInLine_name);				// Init the building line position
			this.terminus				= getTerminusPosition(this.line);								// Init the terminus position
			this.history				= new LinkedList<HistoryCell>();
			this.endTerminus[0]			= new Point();
			this.endTerminus[1]			= new Point();
		}

		// Getter
		public HistoryCell getLastActionHistory()
		{
			if (this.history.isEmpty())	return null;
			else						return this.history.getLast();
		}
		public boolean hasDoneFirstRoundAction()
		{
			if (this.history.size() <= round)	return false;
			else								return (!this.getLastActionHistory().isEmpty());
		}
		public void newRound()	{this.history.addLast(new HistoryCell());}
		public void undoRound() {this.history.removeLast();}
	}

// --------------------------------------------
// Player Info class:
// --------------------------------------------
	private class HistoryCell implements Serializable, CloneableInterface<HistoryCell>
	{
		// Attributes
		private static final long serialVersionUID = 2412756799747914486L;
		public Action	action1		= null;
		public Action	action2		= null;
		public Tile		oldTile1	= null;
		public Tile		oldTile2	= null;;

		// Builder
		private HistoryCell(){}

		// Local methods
		public HistoryCell getClone()
		{
			HistoryCell res = new HistoryCell();
			if (this.action1 != null)
			{
				res.action1	= new Action();
				res.action1.copy(this.action1);
			}
			if (this.action2 != null)
			{
				res.action2	= new Action();
				res.action2.copy(this.action2);
			}
			if (this.oldTile1 != null)
			{
				res.oldTile1 = new Tile();
				res.oldTile1.copy(this.oldTile1);
			}
			if (this.oldTile2 != null)
			{
				res.oldTile2 = new Tile();
				res.oldTile2.copy(this.oldTile2);
			}

			return res;
		}
		public boolean isEmpty()		{return (this.action1 == null);}
		public boolean hasRemainingAction()
		{
			if (this.action1 == null)			return true;
			if (this.action1.isTwoStepAction())	return false;
			if (this.action2 == null)			return true;
			else								return false;
		}
		public void addLastAction(Action a, Tile oldTile1, Tile oldTile2)
		{
			if (action1 == null)	this.action1 = a;
			else
			{
				if (this.action1.isTwoStepAction())	throw new RuntimeException();
				this.action2 = a;
			}
			this.oldTile1 = oldTile1;
			this.oldTile2 = oldTile2;
		}
	}
}
