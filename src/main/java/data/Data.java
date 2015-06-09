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
import main.java.game.ExceptionTramwayExceededArrival;
import main.java.game.ExceptionTramwayJumpCell;
import main.java.game.ExceptionTrtamwayDoesNotStop;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionWrongTramwayPath;
import main.java.game.ExceptionWrongTramwayStart;
import main.java.game.ExceptionWrongTramwayStartTerminus;
import main.java.player.PlayerInterface;
import main.java.util.CloneableInterface;
import main.java.util.Copier;
import main.java.util.Direction;
import main.java.util.Util;







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
	public static final int				maxNbrTileToDraw		= 2;
	public static final int				minSpeed				= 0;
	public static final int				maxSpeed				= 10;
	public static final int				initialSpeed			= 1;
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
		this.maxPlayerSpeed		= initialSpeed;
		this.winner				= null;
		this.remainingLine		= new LinkedList<Integer>();
		for (int j=minLine; j<=maxLine; j++)	this.remainingLine.add(j);

		this.parseStaticGameInformations(nbrBuildingInLine);							// Init the existing buildings, lines (and corresponding colors)
	}
	private Data(){}
	/**==============================================================
	 *  Returns a deep copy of the current data, as seen by the player (the hidden informations are not given)</br>
	 *  If playerName = null, shows everything
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
		res.maxPlayerSpeed			= this.maxPlayerSpeed;
		res.playerOrder				= (this.playerOrder == null)? null : cpS.copyTab(this.playerOrder);
		res.host					= (this.host == null)		? null : new String(this.host);
		res.winner					= (this.winner == null) ? null : new String(this.winner);

		return res;
	}

// --------------------------------------------
// Setter:
// --------------------------------------------
	/**=======================================================
	 * @return the previous game data.</b>
	 * If the current player has done an action this turn, his action is undone.</b>
	 * If the player has done no action this turn, the last player's action is undone.</br>
	 =========================================================*/
	public void rollBack()
	{
		PlayerInfo pi			= this.playerInfoList.get(this.getPlayerTurn());
		HistoryCell	hc			= pi.getLastActionHistory();
		String		playerName	= this.getPlayerTurn();
/*
System.out.println("Action 1: " + hc.action1);
System.out.println("Action 2: " + hc.action2);
System.out.println("Drawn 1: " + hc.drawnTile1);
System.out.println("Drawn 2: " + hc.drawnTile2);
System.out.println("oldTile 1: " + hc.oldTile1);
System.out.println("oldTile 2: " + hc.oldTile2);
System.out.println("Hand: " + pi.hand);
System.out.println("\n----------------------------------------\n");
*/
		if ((hc != null) && ((hc.action1 != null) || (hc.action2 != null)))							// Case player has done an action this round
		{
			if (hc.action2 != null)																	//		Case undo round second game
			{
if (!hc.action2.isBUILD_SIMPLE()) throw new RuntimeException("ffffff");
				this.undoSecondGameInThisRound(hc, pi);
				hc.action2				= null;
				hc.oldTile2				= null;
				hc.drawnTile2			= null;
				hc.drawnFromPlayerHand2	= null;
			}
			else																					//		Case undo round first game
			{
				if (hc.action1.isBUILD_SIMPLE() || hc.action1.isBUILD_AND_START_TRIP_NEXT_TURN())	//			Case simple game
				{
					this.undoFirstSimpleGameInThisRound(hc, pi);
				}
				else if (hc.action1.isBUILD_DOUBLE() || hc.action1.isTWO_BUILD_SIMPLE())			//			Case double game
				{
					this.undoFirstDoubleGameInThisRound(hc, pi);
				}
				else if (hc.action1.isMOVE())
				{
					this.undoFirstTravelGameInThisRound(hc, pi);
					this.winner = null;
				}
				else throw new RuntimeException("????");
				hc.action1				= null;
				hc.action2				= null;
				hc.oldTile1				= null;
				hc.oldTile2				= null;
				hc.drawnTile1			= null;
				hc.drawnTile2			= null;
				hc.drawnFromPlayerHand1	= null;
				hc.drawnFromPlayerHand2	= null;
			}
		}
		else																						// Case player hah done no action this round
		{
if (this.round == 0) throw new RuntimeException("Round == 0");
			this.playerInfoList.get(playerName).undoRound();
			this.round --;
			this.rollBack();																		//		Undo the last player's round
		}
	}
	/**===================================================================
	 * @return gathers all the possible action setters on this data
	 =====================================================================*/
	public void doAction(String playerName, Action action)
	{
//System.out.println("Data.doAction player: " + playerName + ",   Action: " + action);
		if (action.isMOVE())
		{
			this.setTramPosition(playerName, action.tramwayMovement, action.tramwayMovementSize, action.startTerminus);
		}
		else if (action.isBUILD_SIMPLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
		}
		else if (action.isTWO_BUILD_SIMPLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
			this.placeTile(playerName, action.positionTile2.x, action.positionTile2.y, action.tile2);
		}
		else if (action.isBUILD_DOUBLE())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1, action.positionTile2.x, action.positionTile2.y, action.tile2);
		}
		else if (action.isBUILD_AND_START_TRIP_NEXT_TURN())
		{
			this.placeTile(playerName, action.positionTile1.x, action.positionTile1.y, action.tile1);
		}
		else throw new RuntimeException("Unknown action type");
//System.out.println("After hand : " + playerInfoList.get(playerName).hand);
	}

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

		if (!this.board[x][y].isEmpty()) oldT = this.board[x][y].getClone();
		this.board[x][y] = t;
		hand.remove(t);																			// Remove the tile from the player's hand
		if (oldT != null) hand.add(oldT);														// Change the current tile
		Point building = this.isBuildingAround(x, y);
		if (building != null)																	// Case put stop next to building
		{
			if (this.isStopNextToBuilding(building) == null) this.board[x][y].setStop(true);
		}
		Action		a	= Action.newBuildSimpleAction(x, y, t);
		HistoryCell hc	= pi.getLastActionHistory();
		if		(hc.action1 == null)	{hc.action1 = a; hc.oldTile1 = oldTH;}
		else if	(hc.action2 == null)	{hc.action2 = a; hc.oldTile2 = oldTH;}
		else	throw new RuntimeException("You have already done two actions");
	}
	/**===================================================
	 * @return Places the two given tiles on the board.</br>
	 * If the board had an non empty tile, the old tile is put in the player's hand.</br>
	 * The tiles are  removed from the player's hand.</br>
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
		if (nbrCards == 0) return;
		PlayerInfo	pi	= this.playerInfoList.get(playerName);
		Hand		hand= pi.hand;
		HistoryCell	hc	= pi.getLastActionHistory();
		Tile t;

		switch(nbrCards)
		{
			case 1:
				t = this.deck.drawTile();
				hand.add(t);
				if		(hc.drawnTile1 == null)	{hc.drawnTile1 = t; hc.drawnFromPlayerHand1 = null;}
				else if (hc.drawnTile2 == null)	{hc.drawnTile2 = t; hc.drawnFromPlayerHand2 = null;}
				else							throw new RuntimeException("You already have drawn two tiles");
				break;
			case 2:
				t = this.deck.drawTile();
				hand.add(t);
				if		(hc.drawnTile1 == null)	{hc.drawnTile1 = t; hc.drawnFromPlayerHand1 = null;}
				else							throw new RuntimeException("You already have drawn two tiles");
				t = this.deck.drawTile();
				hand.add(t);
				if		(hc.drawnTile2 == null)	{hc.drawnTile2 = t; hc.drawnFromPlayerHand2 = null;}
				else							throw new RuntimeException("You already have drawn two tiles");
				break;
			default: throw new RuntimeException("Wrong number of tiles to draw: " + nbrCards);
		}
	}
	/**===================================================
	 * @return Draw a tile from a player's hand.  This tile is put in the player's hand
	 =====================================================*/
	public void pickTileFromPlayer(String playerName, String chosenPlayerName, Tile tile)
	{
		Hand		src = this.playerInfoList.get(playerName).hand;
		Hand		dst = this.playerInfoList.get(chosenPlayerName).hand;
		HistoryCell	hc	= this.playerInfoList.get(playerName).getLastActionHistory();

		if		(hc.drawnTile1 == null)	{hc.drawnTile1 = tile; hc.drawnFromPlayerHand1 = chosenPlayerName;}
		else if (hc.drawnTile2 == null)	{hc.drawnTile2 = tile; hc.drawnFromPlayerHand2 = chosenPlayerName;}
		else							throw new RuntimeException("You already have drawn two tiles");
		dst.remove(tile);
		src.add(tile);
	}
	/**==================================================
	 * @return The player declares the START of his maiden travel
	 ====================================================*/
	public void startMaidenTravel(String playerName, Point startTerminus)
	{
		playerInfoList.get(playerName).startMaidenTravel(startTerminus);
	}
	/**==================================================
	 * @return The player declares the end of his maiden travel to find a new path or build a new one
	 ====================================================*/
	public void stopMaidenTravel(String playerName)
	{
		playerInfoList.get(playerName).stopMaidenTravel();
	}
	/**================================================
	 * @return The player moves his streetcar.</br>
	 * If the parameter startTerminus != null, the player start his maiden travel.</br>
	 ==================================================*/
	public void setTramPosition(String playerName, Point[] tramPath, int tramPathSize, Point startTerminus)
	{
		PlayerInfo pi = playerInfoList.get(playerName);
		HistoryCell hc	= pi.getLastActionHistory();
		Point tramP		= new Point(pi.tramPosition);
		Point tramPP	= new Point(pi.previousTramPosition);


		
		
		
		if (hc.action1 != null)														throw new RuntimeException("You have already done an action");
		if ((this.hasStartedMaidenTravel(playerName)) && (startTerminus != null))	throw new RuntimeException("You have already started maiden travel");

		if (!pi.hasStartedMaidenTravel()) pi.startMaidenTravel(startTerminus);

		if		(tramPathSize == 0)	throw new RuntimeException("Empty path");
		else if (tramPathSize == 1)
		{
			if (!tramPath[0].equals(pi.tramPosition))	throw new RuntimeException("Wrong starting point");
			else return;
		}
		else
		{
			pi.previousTramPosition.x			= tramPath[tramPathSize-2].x;
			pi.previousTramPosition.y			= tramPath[tramPathSize-2].y;
		}
		pi.tramPosition.x					= tramPath[tramPathSize-1].x;
		pi.tramPosition.y					= tramPath[tramPathSize-1].y;

//System.out.println("Data.SetTramPosition " + playerName + " from " + pi.previousTramPosition + " to " + pi.tramPosition + " (data.setTramPosition)");

		if (pi.tramPosition.equals(pi.endTerminus[0]))	this.winner = playerName;
		if (pi.tramPosition.equals(pi.endTerminus[1]))	this.winner = playerName;
		this.maxPlayerSpeed = tramPathSize;
		if(this.maxPlayerSpeed >= maxSpeed) this.maxPlayerSpeed = maxSpeed;

		Action a						= Action.newMoveAction(tramPath, tramPathSize, startTerminus);
		hc.action1						= a;
		hc.previousTramPosition			= tramP;
		hc.previousPreviousTramPosition	= tramPP;
//TODO a enlever
int ps = this.maxPlayerSpeed;
int nbrPath = this.pathFinderMulti.getAllFixedLengthPath(this, pi.previousTramPosition, pi.tramPosition, ps, this.pathMatrix);
System.out.println("*******");
System.out.println("Nbr path: " + nbrPath);
System.out.println("from: " + pi.tramPosition);
System.out.println("length: " + ps);
for (int i=0; i<nbrPath; i++)
{
	System.out.println("Path: " + i);
	for (int j=0; j<ps; j++) System.out.println("\t- " + this.pathMatrix[i][j]);
}
	}

// --------------------------------------------
// Getter relative to travel:
// --------------------------------------------
	public boolean hasStartedMaidenTravel(String playerName) {return this.playerInfoList.get(playerName).hasStartedMaidenTravel();}
	/**=============================================================
	 *  @return  the current position of this player's streetcar 
	 * (or null if he hasn't started yet his maiden travel)
	 * ============================================================= */
	public Point getTramPosition(String playerName)
	{
		PlayerInfo pi = playerInfoList.get(playerName);

		if (!this.hasStartedMaidenTravel(playerName)) return null;
		return new Point(pi.tramPosition);
	}
	public Point getPreviousTramPosition(String playerName)
	{
		PlayerInfo pi = playerInfoList.get(playerName);

		if (!this.hasStartedMaidenTravel(playerName)) return null;
		return new Point(pi.previousTramPosition);
	}
	public Point[] getPlayerEndTerminus(String playerName)
	{
		PlayerInfo pi = playerInfoList.get(playerName);

		if (!this.hasStartedMaidenTravel(playerName)) return null;
		return (new Copier<Point>()).copyTab(pi.endTerminus);
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
	public Tile					getHandTile(String playerName, int tileIndex)	{return this.playerInfoList.get(playerName).hand.get(tileIndex).getClone();}
	public boolean				isInPlayerHand(String playerName, Tile t)		{return this.playerInfoList.get(playerName).hand.isInHand(t);}
	public boolean				isUsedPlayerName(String playerName)				{return this.playerInfoList.keySet().contains(playerName);}
	public boolean				hasDoneRoundFirstAction(String playerName)		{return ((this.playerInfoList.get(playerName).getLastActionHistory() != null) && (!this.playerInfoList.get(playerName).getLastActionHistory().isEmpty()));}  // TODO a corriger(done mais je laisse le todo au cas ou ...)
	public Point[]				getPlayerTerminusPosition(String playerName)	{return (new Copier<Point>()).copyTab(playerInfoList.get(playerName).terminus);}
	public Point[]				getPlayerAimBuildings(String playerName)		{return this.playerInfoList.get(playerName).buildingInLine_position;}
	public int					getPlayerRemainingTilesToDraw(String playerName){return Math.min(Data.maxNbrTileToDraw,(Hand.maxHandSize - this.playerInfoList.get(playerName).hand.getSize()));}
	/**=============================================================
	 * @return true if this terminus belongs to that player
	 * ============================================================= */
	public boolean	isPlayerTerminus(String playerName, Point terminus)
	{
		Point[] terminusTab = playerInfoList.get(playerName).terminus;
		for (Point p: terminusTab) if (p.equals(terminus)) return true;
		return false;
	}
	/**======================================================
	 * @return true if this player still has actions to do in his turn
	 ======================================================== */
	public boolean hasRemainingAction(String playerName)
	{
		if (!this.isPlayerTurn(playerName))	throw new RuntimeException("Not player's turn: " + playerName);
		HistoryCell lastActions = this.playerInfoList.get(playerName).getLastActionHistory();

		return lastActions == null || lastActions.hasRemainingAction();
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
		LinkedList<Point> res = this.getCompletedTrack(playerName);
//System.out.println("\n************************\nTrack completed: " + (res != null));
		return (res != null);
	}
	/**===============================================================
	 * @return the complete player's track path between the 2 terminus and through all the buildings)</br>
	 =================================================================*/
	public LinkedList<Point> getCompletedTrack(String playerName)
	{
		PlayerInfo	pi		= this.playerInfoList.get(playerName);
		Point[]		stop	= new Point[this.nbrBuildingInLine];
		Point p0, p1, building, pp;
		LinkedList<Point> arretPath	= new LinkedList<Point>();
		LinkedList<Point> res		= new LinkedList<Point>();
		LinkedList<Point> tmpRes	= null;

		for (int i=0; i<this.nbrBuildingInLine; i++)				// Look for stop around buildings
		{
			building	= pi.buildingInLine_position[i];
			stop[i]		= this.isStopNextToBuilding(building);
			if (stop[i] == null) return null;
		}
		for (int[] buildingCombinaison: this.getCombinaison())
		{
			res.clear();
			arretPath.clear();
			arretPath.add(pi.terminus[0]);
			for (int i=0; i<buildingCombinaison.length; i++) arretPath.add(stop[buildingCombinaison[i]]);
			arretPath.add(pi.terminus[3]);
			p0 = arretPath.get(0);
			pp = initPreviousTerminusPosition(p0);
			for (int i=1; i<arretPath.size(); i++)
			{
				p1		= arretPath.get(i);
				tmpRes	= this.getShortestPath(pp, p0, p1);
//System.out.println("\n**********************\nTrack betweew " + p0 + "   and " + p1 + "\n" + tmpRes);
				if (tmpRes == null) break;
				if(i>1)		tmpRes.removeFirst();
				res.addAll(tmpRes);
				p0 = p1;
				pp = tmpRes.get(tmpRes.size()-2);
			}
			if (tmpRes != null) return res;
		}
		return null;
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
public LinkedList<Point>	getShortestPath(Point pOld, Point p, Point pNext){return this.pathFinder.getPath(this, pOld, p, pNext);}
public boolean				pathExistsBetween(Point pOld, Point p, Point pNext){return getShortestPath(pOld, p, pNext) != null;}
public boolean				simplePathExistsBetween(Point pOld, Point p, Point pNext){return this.pathFinder.isSimplePath(this, pOld, p, pNext);}

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
	 * @return true if the player has no possible move, considering his hand, the deck and his travel</br>
	 ========================================================================= */
	public boolean isGameBlocked(String playerName)
	{
		PlayerInfo pi = this.playerInfoList.get(playerName);
		LinkedList<Point> tramNeighbor;

		if (hasStartedMaidenTravel(playerName))															// Case has started maiden travel
		{
			tramNeighbor = this.getAccessibleNeighborsPositions(pi.tramPosition.x, pi.tramPosition.y);

			Point p0;
			switch (tramNeighbor.size())
			{
				case 0: throw new RuntimeException("???");
				case 1:
					p0 = tramNeighbor.get(0);
					if (p0.equals(pi.previousTramPosition)) return true;
					else throw new RuntimeException("???");
				default: return false;
			}
		}
/***********************
		if (hasStartedMaidenTravel(playerName))															// Case has started maiden travel
		{
			for (Direction dir: Direction.DIRECTION_LIST)
			{
				tramNeighbor = this.getAccessibleNeighborsPositions(pi.tramPosition.x, pi.tramPosition.y, dir);
				for (Point neighbor: tramNeighbor)
				{
					if (neighbor.equals(pi.previousTramPosition)) continue;
					return false;
				}
			}
			return true;
		}
********************/
		else																								// Case is building
		{
			if ((isEmptyDeck()) && (this.getHandSize(playerName) == 0)) return true;

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
							if (this.isAcceptableTilePlacement(x, y, rotations[r])) return false;
						}
					}
				}
			}
			return true;
		}
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
	 * @return the list of the neighbor coordinates that can be acceded from the (x,y) cell.</br>
	 ==============================================================*/
	public LinkedList<Point> getAccessibleNeighborsPositions(int x, int y)
	{
		LinkedList<Point>		res = new LinkedList<Point>();
		int	ad	= board[x][y].getAccessibleDirections();				// List of the reachable directions

		for (Direction d: Direction.DIRECTION_LIST)						// For each accesible position
		{
			if (!d.isDirectionInList(ad)) continue;
			Point next = d.getNeighbour(x, y);
//			if (isWithinnBoard(next.x, next.y))	res.add(next);
			if ((next.x < 0) || (next.y < 0))	continue;
			if (next.x >= this.board.length)	continue;
			if (next.y >= this.board[0].length)	continue;
			res.add(next);
		}
		return res;
	}
	/**============================================================
	 * @return the list of the neighbor coordinates that can be acceded from the (x,y) cell and from the given (x,y) direction.</br>
	 ==============================================================*/
/*****	public LinkedList<Point> getAccessibleNeighborsPositions(int x, int y, Direction initialDir)
	{
		LinkedList<Point>		res = new LinkedList<Point>();
		int	ad	= board[x][y].getAccessibleDirections(initialDir);				// List of the reachable directions from the given direction

		for (Direction d: Direction.DIRECTION_LIST)								// For each accesible position
		{
			if (!d.isDirectionInList(ad)) continue;
			Point next = d.getNeighbour(x, y);
			if (isWithinnBoard(next.x, next.y))	res.add(next);
		}
		return res;
	}
************/
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
/*	public LinkedList<Point> getConnectedNeighborPositions(int x, int y)
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
*/
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
	 * The actions are added to the input tab.</br>
	 * The input tab size must be maxPossibleAction (or  higher).  Each one of its cells must have been initialized</br>
	 * @return the number of different actions that may be realized at this step of the game.
	 ===============================================================*/
	public int getPossibleActions(String playerName, CoupleActionIndex[] resTab, boolean writeActionsInTab)
	{
//TODO		if (!this.isPlayerTurn(playerName))	throw new RuntimeException("Not the player turn: " + playerName);

		Tile[] tmpRotation1		= new Tile[4];																			// Init optimization parameters
		Tile[] tmpRotation2		= new Tile[4];
		PlayerInfo pi = this.playerInfoList.get(playerName);
		for (int i=0; i<4; i++)
		{
			tmpRotation1[i]= Tile.getNewEmptyTile();
			tmpRotation2[i]= Tile.getNewEmptyTile();
		}

		int res = 0, nbrRotation1, nbrRotation2, nbrPath;
		boolean hashStartedTravel	= this.hasStartedMaidenTravel(playerName);
		Point startTerminus			= pi.terminus[0];
		Tile t, oldT1;

		if ((this.hasStartedMaidenTravel(playerName)) || (this.isTrackCompleted(playerName)))							// Case: can move tramway
		{
			if (!hashStartedTravel)	{startTerminus = pi.terminus[0];this.startMaidenTravel(playerName, startTerminus);}	//		Case Can start maiden travel
			else					startTerminus = null;
			Point previousTramPosition	= pi.previousTramPosition;
			Point currentTramPosition	= pi.tramPosition;
			for (int l = 1; l<=this.maxPlayerSpeed; l++)
			{
				nbrPath = this.pathFinderMulti.getAllFixedLengthPath(this, previousTramPosition, currentTramPosition, l, this.pathMatrix);
				for (int i=0; i<nbrPath; i++)
				{
					try					{this.checkTramPath(playerName, this.pathMatrix[i], l, null);}
					catch (Exception e)	{continue;}

System.out.println("Possible move actions :");
System.out.println("\tPrevious position = (" + previousTramPosition.x + "," + previousTramPosition.y + "), Current position = (" + currentTramPosition.x + "," + currentTramPosition.y + "), Length = " + l);
System.out.print("\t("+this.pathMatrix[i][0].x+","+this.pathMatrix[i][0].y+")");
for (int z =1; z<=l; z++)
{
System.out.print("->("+this.pathMatrix[i][z].x+","+this.pathMatrix[i][z].y+")");
}
System.out.println("\n");

					if (this.pathMatrix[i][1].equals(previousTramPosition)) continue;// TODO a enlever

					
					if(writeActionsInTab)
					{
						resTab[res].getAction().setMoveAction(startTerminus, this.pathMatrix[i], l);
						resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
					}
					res ++;
				}
			}
			if (!hashStartedTravel)this.stopMaidenTravel(playerName);
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
							if(writeActionsInTab)
							{
								resTab[res].getAction().setSimpleBuildingAndStartTripNextTurnAction(x1, y1, tmpRotation1[r1]);
								resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
							}
							res ++;
						}
						else if (this.getHandSize(playerName) == 1)	;									//		Case no second hand tile (!!!!!! Ne pas retirer le ';'  )
						else
						{
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
											if(writeActionsInTab)
											{
												resTab[res].getAction().setTwoSimpleBuildingAction(x1, y1, tmpRotation1[r1], x2, y2, tmpRotation2[r2]);
												resTab[res].setIndex(CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
											}
											res ++;
											
											// TODO s'arranger pour que le tableau soit plus grand
											if(writeActionsInTab && res >= resTab.length) { 
												this.board[x1][y1] = oldT1;
												return res;
											}
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
	/**
	 * A commenter
	 * @param playerName
	 * @param tramPath
	 * @param tramPathSize
	 * @param startTerminus
	 * @throws ExceptionTramwayExceededArrival
	 * @throws ExceptionWrongTramwayStart
	 * @throws ExceptionWrongTramwayStartTerminus
	 * @throws ExceptionTramwayJumpCell
	 * @throws ExceptionWrongTramwayPath
	 * @throws ExceptionTrtamwayDoesNotStop
	 */
	public void checkTramPath(String playerName, Point[] tramPath, int tramPathSize, Point startTerminus) throws ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionWrongTramwayPath, ExceptionTrtamwayDoesNotStop
	{
		boolean hasStartedTravel = this.hasStartedMaidenTravel(playerName);
		if (hasStartedTravel)
		{
			int winner	= -1;
			int stop	= -1;
			Point tramPosition = this.getTramPosition(playerName);
			Point p0, p1, p2;
			Point[] endTerminus =  this.getPlayerTerminusPosition(playerName);

			if (!tramPath[0].equals(tramPosition))				throw new ExceptionWrongTramwayStart();

			p0 = this.getPreviousTramPosition(playerName);
			p1 = tramPosition;
			for (int i=1; i<tramPathSize; i++)
			{
				p2 = tramPath[i];
				if (winner != -1)								throw new ExceptionTramwayExceededArrival();
				if (stop	!= -1)								throw new ExceptionTrtamwayDoesNotStop();
				if (!this.pathExistsBetween(p0, p1, p2))		throw new ExceptionWrongTramwayPath();
				if (Util.manhathanDistance(p1, p2) != 1)		throw new ExceptionTramwayJumpCell();
				if (p2.equals(endTerminus[0]))		winner = i;
				if (p2.equals(endTerminus[1]))		winner = i;
				if (this.getTile(p2).isStop())		stop = i;
				p0 = p1;
				p1 = p2;
			}
			return;
		}
		else
		{
			Point[]	terminus = this.getPlayerTerminusPosition(playerName);
			int i;
			
			for (i=0; i<4; i++) if (startTerminus.equals(terminus[i])) break;
			if (i == 4) throw new ExceptionWrongTramwayStartTerminus();
			this.startMaidenTravel(playerName, startTerminus);
			try
			{
				this.checkTramPath(playerName, tramPath, tramPathSize, startTerminus);
			}
			catch(	ExceptionTramwayExceededArrival		|
					ExceptionWrongTramwayStart			|
					ExceptionWrongTramwayStartTerminus	|
					ExceptionTramwayJumpCell			|
					ExceptionWrongTramwayPath			|
					ExceptionTrtamwayDoesNotStop 		e)
			{
				this.stopMaidenTravel(playerName);
				throw e;
			}
			this.stopMaidenTravel(playerName);
		}
	}

// --------------------------------------------
// Private Roll back methods:
// --------------------------------------------
	private void undoLastDraw(HistoryCell hc, PlayerInfo pi)
	{
		Hand hand;
		if (hc.drawnTile2 != null)
		{
			pi.hand.remove(hc.drawnTile2);
			if (hc.drawnFromPlayerHand2 == null)	this.deck.undrawTile(hc.drawnTile2);
			else
			{
				hand = this.playerInfoList.get(hc.drawnFromPlayerHand2).hand;
				hand.add(hc.drawnTile2);
			}
			hc.drawnTile2			= null;
			hc.drawnFromPlayerHand2	= null;
		}
		else if (hc.drawnTile1 != null)
		{
			pi.hand.remove(hc.drawnTile1);
			if (hc.drawnFromPlayerHand1 == null)	this.deck.undrawTile(hc.drawnTile1);
			else
			{
				hand = this.playerInfoList.get(hc.drawnFromPlayerHand1).hand;
				hand.add(hc.drawnTile1);
			}
			hc.drawnTile1			= null;
			hc.drawnFromPlayerHand1	= null;
		}
///		else throw new RuntimeException("No draw to undo");
	}
	private void undoSecondGameInThisRound(HistoryCell hc, PlayerInfo pi)
	{

		if (!hc.action2.isBUILD_SIMPLE())throw new RuntimeException("????");

		int x = hc.action2.positionTile1.x;
		int y = hc.action2.positionTile1.y;

		if (!hc.oldTile2.isEmpty())														//			Case: game was a tile improve
		{
			this.board[x][y] = hc.oldTile2;
			pi.hand.remove(hc.oldTile2);
		}
		else																			//			Case: game was a simple tile put
		{
			this.board[x][y] = Tile.getNewEmptyTile();
			this.undoLastDraw(hc, pi);
		}
//		if (!pi.hand.isFull()) pi.hand.add(hc.action2.tile1);
		pi.hand.add(hc.action2.tile1.getClone());
	}
	private void undoFirstSimpleGameInThisRound(HistoryCell hc, PlayerInfo pi)
	{
		int x, y;

		x = hc.action1.positionTile1.x;
		y = hc.action1.positionTile1.y;
		if (!hc.oldTile1.isEmpty())														//			Case: game was a tile improve
		{
			this.board[x][y] = hc.oldTile1;
			pi.hand.remove(hc.oldTile1);
		}
		else																			//			Case: game was a simple tile put
		{
			this.board[x][y] = Tile.getNewEmptyTile();
			this.undoLastDraw(hc, pi);
		}
//		if (!pi.hand.isFull()) pi.hand.add(hc.action1.tile1);
		pi.hand.add(hc.action1.tile1.getClone());
	}
	private void undoFirstDoubleGameInThisRound(HistoryCell hc, PlayerInfo pi)
	{
		int x, y;

		x = hc.action1.positionTile2.x;
		y = hc.action1.positionTile2.y;
		if (!hc.oldTile2.isEmpty())														//			Case: game was a tile improve
		{
			this.board[x][y] = hc.oldTile2;
			pi.hand.remove(hc.oldTile2);
		}
		else																			//			Case: game was a simple tile put
		{
			this.board[x][y] = Tile.getNewEmptyTile();
			this.undoLastDraw(hc, pi);
		}
//		if (!pi.hand.isFull()) pi.hand.add(hc.action1.tile2);
		pi.hand.add(hc.action1.tile2.getClone());

		x = hc.action1.positionTile1.x;
		y = hc.action1.positionTile1.y;
		if (!hc.oldTile1.isEmpty())														//			Case: game was a tile improve
		{
			this.board[x][y] = hc.oldTile1;
			pi.hand.remove(hc.oldTile1);
		}
		else
		{
			this.board[x][y] = Tile.getNewEmptyTile();									//			Case: game was a simple tile put
			this.undoLastDraw(hc, pi);
		}
//		if (!pi.hand.isFull()) pi.hand.add(hc.action1.tile1);
		pi.hand.add(hc.action1.tile1.getClone());
	}
	private void undoFirstTravelGameInThisRound(HistoryCell hc, PlayerInfo pi)
	{
//System.out.println("pi.tramPosition        : " + pi.tramPosition);
//System.out.println("pi.previousTramPosition: " + pi.previousTramPosition);
		pi.tramPosition.x			= hc.previousTramPosition.x;
		pi.tramPosition.y			= hc.previousTramPosition.y;
		if (hc.action1.startTerminus != null) this.stopMaidenTravel(this.getPlayerTurn());
		else
		{
			pi.previousTramPosition.x			= hc.previousPreviousTramPosition.x;
			pi.previousTramPosition.y			= hc.previousPreviousTramPosition.y;
			hc.previousTramPosition.x			= -1;
			hc.previousTramPosition.y			= -1;
			hc.previousPreviousTramPosition.x	= -1;
			hc.previousPreviousTramPosition.y	= -1;
		}
//System.out.println("pi.tramPosition        : " + pi.tramPosition);
//System.out.println("pi.previousTramPosition: " + pi.previousTramPosition);
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
			pi									= this.playerInfoList.get(str);
			piRes								= new PlayerInfo();													// Shared Information
			piRes.player						= pi.player;
			piRes.isHuman						= pi.isHuman;
			piRes.line							= pi.line;
			piRes.color							= (pi.color == null) ? null : new Color(pi.color.getRGB());
			piRes.hand							= pi.hand.getClone();
			piRes.terminus						= (pi.terminus == null) ? null : cpP.copyTab(pi.terminus);
			piRes.history						= (new Copier<HistoryCell>()).copyList(pi.history);
			piRes.startTerminus					= (pi.startTerminus == null)		? null : new Point(pi.startTerminus);
			piRes.endTerminus					= (pi.endTerminus == null)			? null : cpP.copyTab(pi.endTerminus);
			piRes.tramPosition					= (pi.tramPosition == null)			? null : new Point(pi.tramPosition);
			piRes.previousTramPosition			= (pi.previousTramPosition == null) ? null : new Point(pi.previousTramPosition);

// TODO			if ((playerName == null) || (str.equals(playerName)) || (this.hasStartedMaidenTravel(str)))		// Private Informations
// TODO			{
				piRes.buildingInLine_name		= (new Copier<String>()).copyTab (pi.buildingInLine_name);
				piRes.buildingInLine_position	= (new Copier<Point>()).copyTab(pi.buildingInLine_position);
// TODO			}
/*			else
			{
				piRes.buildingInLine_name		= null;
				piRes.buildingInLine_position	= null;
			}
*/			res.put(str, piRes);
		}
		return res;
	}
	private Point initPreviousTerminusPosition(Point terminus)
	{
		Point neighbor;
		int dirList = board[terminus.x][terminus.y].getAccessibleDirections();

		for (Direction dir: Direction.DIRECTION_LIST)
		{
			if (!dir.isDirectionInList(dirList)) continue;
			neighbor = dir.getNeighbour(terminus.x, terminus.y);
			if (!board[neighbor.x][neighbor.y].isTerminus()) continue;
			return neighbor;
		}
		throw new RuntimeException("???");
	}

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
		public Point					startTerminus					= null;			// Data relative to the travel
		public Point[]					endTerminus						= null;
		public Point					tramPosition					= null;
		public Point					previousTramPosition			= null;
		public LinkedList<HistoryCell>	history;										// organized by turns

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
			this.startTerminus			= new Point(-1, -1);
			this.endTerminus			= new Point[2];
			this.endTerminus[0]			= new Point(-1, -1);
			this.endTerminus[1]			= new Point(-1, -1);
			this.tramPosition			= new Point(-1, -1);
			this.previousTramPosition	= new Point(-1, -1);

			if(playerName.equals("cheater"))
			{
				line = 1;
				buildingInLine_name[0] = "L";
				buildingInLine_name[1] = "H";
				buildingInLine_position[0] = new Point(6, 4);
				buildingInLine_position[1] = new Point(11, 4);
				terminus[0] = new Point(0, 6);
				terminus[1] = new Point(0, 7);
				terminus[2] = new Point(13, 2);
				terminus[3] = new Point(13, 3);
			}/*
			if(playerName.equals("AI Level 2")) //TODO à enlever
			{
				line = 1;
				buildingInLine_name[0] = "L";
				buildingInLine_name[1] = "H";
				buildingInLine_position[0] = new Point(6, 4);
				buildingInLine_position[1] = new Point(11, 4);
				terminus[0] = new Point(0, 6);
				terminus[1] = new Point(0, 7);
				terminus[2] = new Point(13, 2);
				terminus[3] = new Point(13, 3);
			}*/
		}

		// Getter
		public HistoryCell getLastActionHistory()
		{
			if (this.history.isEmpty())	return null;
			else						return this.history.getLast();
		}

		// Setter
		public void newRound()	{this.history.addLast(new HistoryCell());}
		public void undoRound() {this.history.removeLast();}
		public void startMaidenTravel(Point startTerminus)
		{
			this.startTerminus.x		= startTerminus.x;
			this.startTerminus.y		= startTerminus.y;
			int i = 0;
			for (Point p: this.terminus)
			{
				if (Util.manhathanDistance(startTerminus, p) <= 1) continue;
				this.endTerminus[i].x = p.x;
				this.endTerminus[i].y = p.y;
				i ++;
			}
			this.tramPosition.x			= startTerminus.x;
			this.tramPosition.y			= startTerminus.y;

			this.previousTramPosition	= initPreviousTerminusPosition(startTerminus);
		}
		public boolean hasStartedMaidenTravel(){return (!this.startTerminus.equals(new Point(-1, -1)));}
		public void stopMaidenTravel()
		{
			this.startTerminus.x				= -1;
			this.startTerminus.y				= -1;
			this.endTerminus[0].x				= -1;
			this.endTerminus[0].y				= -1;
			this.endTerminus[1].x				= -1;
			this.endTerminus[1].y				= -1;
			this.tramPosition.x					= -1;
			this.tramPosition.y					= -1;
			this.previousTramPosition.x			= -1;
			this.previousTramPosition.y			= -1;
		}

	}

// --------------------------------------------
// History cell class:
// --------------------------------------------
	private class HistoryCell implements Serializable, CloneableInterface<HistoryCell>
	{
		// Attributes
		private static final long serialVersionUID = 2412756799747914486L;
		public Action	action1							= null;
		public Action	action2							= null;
		public Tile		oldTile1						= null;
		public Tile		oldTile2						= null;
		public Tile		drawnTile1						= null;
		public Tile		drawnTile2						= null;
		public String	drawnFromPlayerHand1			= null;
		public String	drawnFromPlayerHand2			= null;
		public Point	previousTramPosition			= null;
		public Point	previousPreviousTramPosition	= null;

		// Builder
		private HistoryCell(){}

		// Local methods
		public HistoryCell getClone()
		{
			HistoryCell res = new HistoryCell();

			if (this.action1						!= null)		{res.action1	= new Action();	res.action1		.copy(this.action1);}
			if (this.action2						!= null)		{res.action2	= new Action();	res.action2		.copy(this.action2);}
			if (this.oldTile1						!= null)		{res.oldTile1	= new Tile();	res.oldTile1	.copy(this.oldTile1);}
			if (this.oldTile2						!= null)		{res.oldTile2	= new Tile();	res.oldTile2	.copy(this.oldTile2);}
			if (this.drawnTile1						!= null)		{res.drawnTile1	= new Tile();	res.drawnTile1	.copy(this.drawnTile1);}
			if (this.drawnTile2						!= null)		{res.drawnTile2	= new Tile();	res.drawnTile2	.copy(this.drawnTile2);}
			if (this.drawnFromPlayerHand1			!= null)		{res.drawnFromPlayerHand1			= new String(this.drawnFromPlayerHand1);}
			if (this.drawnFromPlayerHand2			!= null)		{res.drawnFromPlayerHand2			= new String(this.drawnFromPlayerHand2);}
			if (this.previousTramPosition			!= null)		{res.previousTramPosition			= new Point(this.previousTramPosition);}
			if (this.previousPreviousTramPosition	!= null)		{res.previousPreviousTramPosition	= new Point(this.previousPreviousTramPosition);}
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
		public String toString()
		{
			return "Action 1: " + this.action1 + "\nAction 2: " + this.action2; 
		}
	}
	
	
	
	
//TODO
//TODO to remove:
private final int[][] combinaison2 = {{0, 1}, {1, 0}};
private final int[][] combinaison3 = {{0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 1, 0}, {2, 0, 1}};
private int[][] getCombinaison()
{
	switch(this.nbrBuildingInLine)
	{
		case 2: return combinaison2;
		case 3: return combinaison3;
		default: throw new RuntimeException("???");
	}
}
}
