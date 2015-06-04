package main.java.game;

import java.awt.Color;
import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.Engine.EngineAction;
import main.java.player.PlayerAI;
import main.java.player.PlayerInterface;
import main.java.util.Copier;

/**============================================================
 * Remote Application 
 * URL: rmi://ip:port/gameName
 * @author kassuskley
 ==============================================================*/



@SuppressWarnings("serial")
public class Game extends UnicastRemoteObject implements GameInterface, Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final String			gameMessageHeader		= "Street Car application: ";
	public static int					applicationPort			= 5001;
	public final static String			applicationProtocol		= "rmi";
	public final static String			AiDefaultName			= "AI Level ";

	protected Data						data;
	protected LoginInfo[]				loggedPlayerTable;
	protected Engine					engine;
	protected HashMap<String, Thread>	aiList;

// --------------------------------------------
// Builder:
// --------------------------------------------
// TODO a corriger
public String getHostName(){return this.data.getHost();}
	/**=======================================================================
	 * @return Creates a local application that can be called as a local object
	 * @throws RemoteException			: network trouble	(caught by the IHM)
	 * @throws ExceptionUnknownBoardName: 					(caught by the IHM)
	 * @throws RuntimeException 		: 
	 =========================================================================*/
	public Game(String gameName, String appIP, String boardName, int nbrBuildingInLine) throws RemoteException, ExceptionUnknownBoardName, RuntimeException
	{
		super();
		String url = null;

		try																				// Create the player's remote reference
		{
			url = applicationProtocol + "://" + appIP + ":" + applicationPort + "/" + gameName;
			java.rmi.registry.LocateRegistry.createRegistry(applicationPort);
			Naming.rebind(url, this);
		}
		catch (MalformedURLException e) {e.printStackTrace(); System.exit(0);}

		this.data				= new Data(gameName, boardName, nbrBuildingInLine);		// Init application
		this.loggedPlayerTable	= LoginInfo.getInitialLoggedPlayerTable();
		this.engine				= new Engine();
		this.aiList				= new HashMap<String, Thread>();

		System.out.println("\n===========================================================");
		System.out.println(gameMessageHeader + "URL = " + url);
		System.out.println(gameMessageHeader + "ready");
		System.out.println(gameMessageHeader + "Start waiting for connexion request");
		System.out.println("===========================================================\n");

		for (LoginInfo li:this.loggedPlayerTable)										// Creates the initial AiPlayer's thread
		{
			if (li.isClosed() || li.isHuman()) continue;
			this.launchAIPlayer(li);
		}

	}
	/**=======================================================================
	 * @return Creates a remote application cloned to the real application at the given ip
	 * @throws NotBoundException 		: The web host is not configured	(throw RuntimeException)
	 * @throws RemoteException 			: The web host is offline			(catched by IHM)
	 * @throws auther java.rmi.Exception: NetworkError						(catched by IHM)
	 =========================================================================*/
	public static GameInterface getRemoteGame(String appIP, String gameName) throws RemoteException, NotBoundException
	{
		String url = applicationProtocol + "://" + appIP + ":" + applicationPort + "/" + gameName;

////	System.setSecurityManager(new RMISecurityManager());
		try
		{
			return (GameInterface) Naming.lookup(url);
		}
		catch (MalformedURLException e) {e.printStackTrace(); System.exit(0);}
		return null;
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void run()
	{
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// Must be declared in the interface "RemoteApplicationInterface"
// --------------------------------------------
	/**==============================================
	 * @return a copy of the data of the game
	 ================================================*/
	public synchronized Data getData(String playerName) throws RemoteException
	{
		return this.data.getClone(playerName);
	}
	/**==============================================
	 * @return a copy of the table used by the waiting room
	 ================================================*/
	public synchronized LoginInfo[] getLoginInfo(String playerName) throws RemoteException
	{
		Copier<LoginInfo> cp = new Copier<LoginInfo>();

		return cp.copyTab(loggedPlayerTable);
	}
	/**==============================================
	 * @return Modifies an entry of the waiting room table
	 * Only permitted if the player using this function is the host
	 ================================================*/
	public synchronized void setLoginInfo(String playerName, int playerToChangeIndex, LoginInfo newPlayerInfo) throws RemoteException, ExceptionForbiddenAction, ExceptionForbiddenHostModification
	{
		if (!this.data.getHost().equals(playerName))	throw new ExceptionForbiddenAction();
		if (playerToChangeIndex == 0)					throw new ExceptionForbiddenHostModification();

		if (this.loggedPlayerTable[playerToChangeIndex].equals(newPlayerInfo))
		{
System.out.println("Game.setLoginInfo: no change to do");
			return;
		}

		String	oldPlayerName		= this.loggedPlayerTable[playerToChangeIndex].getPlayerName();
		boolean	oldPlayerIsOccupied	= this.loggedPlayerTable[playerToChangeIndex].isOccupiedCell();
		boolean	oldPlayerIsHuman	= this.loggedPlayerTable[playerToChangeIndex].isHuman();
		boolean	newPlayerIsHuman	= newPlayerInfo.isHuman();
		boolean notifyAll = false;

		this.loggedPlayerTable[playerToChangeIndex] = newPlayerInfo.getClone();
		this.loggedPlayerTable[playerToChangeIndex].setFreeCell();
		if (oldPlayerIsOccupied)														// Case exclude old player
		{
			this.data.removePlayer(oldPlayerName);
			if ((oldPlayerIsHuman) && (oldPlayerName != null))
			{
				PlayerInterface	oldPlayer	= this.data.getRemotePlayer(oldPlayerName);
				this.engine.addAction(this.data, "excludePlayer", oldPlayer);
			}
			else
			{
				this.aiList.get(oldPlayerName).interrupt();
				this.aiList.remove(oldPlayerName);
			}
			notifyAll = true;
		}
		if (!newPlayerIsHuman)															// Case create AI player
		{
			this.launchAIPlayer(newPlayerInfo);
			notifyAll = false;
		}
		if (notifyAll) this.engine.addAction(data, "notifyAllPlayers");
	}
	/**================================================
	 * @return Makes a player join the game
	 * ================================================*/
	public synchronized void setPlayerColor(String playerName, Color playerColor) throws RemoteException, ExceptionUsedPlayerColor
	{
		if (this.data.isUsedColor(playerColor))		throw new ExceptionUsedPlayerColor();

		this.data.setPlayerColor(playerName, playerColor);
	}
	/**================================================
	 * @return Makes a player join the game
	 * ================================================*/
	public synchronized void onJoinGame(PlayerInterface player, boolean isHuman, boolean isHost, int iaLevel) throws RemoteException, ExceptionUsedPlayerName, ExceptionGameHasAlreadyStarted
	{
		String	playerName	= player.getPlayerName();
		int		playerIndex = getFreeAndMatchingLoginInTableIndex(isHost, isHuman, iaLevel);

		if (this.data.getNbrPlayer() >= Data.maxNbrPlayer)	throw new ExceptionFullParty();
		if (playerIndex == -1)								throw new ExceptionNoCorrespondingPlayerExpected();
		if (this.data.isPlayerLogged(playerName))			throw new ExceptionUsedPlayerName();
		if (this.data.isGameStarted())						throw new ExceptionGameHasAlreadyStarted();
		if ((isHost) && (this.data.getHost() != null))		throw new ExceptionHostAlreadyExists();

		data.addPlayer(player, playerName, isHost, isHuman);
		this.engine.addAction(data, "notifyAllPlayers");
		this.loggedPlayerTable[playerIndex] = new LoginInfo(false, playerName, isHost, isHuman, iaLevel);
		System.out.println("\n===========================================================");
		System.out.println(Game.gameMessageHeader + "join request from player : \"" + playerName + "\"");
		System.out.println(Game.gameMessageHeader + "accepted player");
		System.out.println(Game.gameMessageHeader + "Number of player: " + this.data.getNbrPlayer());
		System.out.println("===========================================================\n");
	}
	/**==============================================
	 * @return Makes a player leave the game
	 * If the player is the host, or the game has started, the current thread exits
	 * ================================================*/
	public synchronized void onQuitGame(String playerName) throws RemoteException, ExceptionForbiddenAction
	{
		int playerIndex			= getPlayerInLogInfoTable(playerName);
		boolean isHost			= this.data.isHost(playerName);
		boolean gameHasStarted	= this.data.isGameStarted();

		if (!this.data.isPlayerLogged(playerName))	throw new ExceptionForbiddenAction();
		if (playerIndex == -1)						throw new ExceptionForbiddenAction();

		this.loggedPlayerTable[playerIndex] = LoginInfo.getInitialLoggedPlayerTableCell(playerIndex);
		this.engine.onQuitGame(this.data, playerName);
		System.out.println("\n===========================================================");
		System.out.println(gameMessageHeader + "quitGame");
		System.out.println(gameMessageHeader + "logout result : player logged out");
		System.out.println(gameMessageHeader + "playerName    : " + playerName);
		System.out.println("===========================================================\n");
		if (gameHasStarted || isHost)	System.exit(0);
	}

	/**==============================================
	 * Init the game parameters.
	 * Make the game begins
	 ================================================*/
	public synchronized void hostStartGame(String playerName) throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnoughPlayers
	{
		if (!this.data.getHost().equals(playerName))	throw new ExceptionForbiddenAction();
		if (!this.data.isGameReadyToStart())			throw new ExceptionNotEnoughPlayers();

		for (LoginInfo li: this.loggedPlayerTable) li.setIsClosed(true);
		this.engine.addAction(this.data, "hostStartGame", playerName);
	}
	/**=============================================================================
	 * Places a tile from the player's hand on the board.
	 * The given tile must belong to the player's hand
	 * The given tile is removed from the player's hand
	 ===============================================================================
	 * @throws ExceptionPlayerIsBlocked 
	 * @throws ExceptionGameIsOver */
	public synchronized void placeTile(String playerName, Tile t, Point position)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position.x, position.y, t))	throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, t))							throw new ExceptionForbiddenAction();
		if (!this.data.hasRemainingAction(playerName))							throw new ExceptionTooManyActions();
		if (data.getWinner() != null)											throw new ExceptionPlayerIsBlocked();
		if (data.isGameBlocked(playerName))										throw new ExceptionGameIsOver();
		if (data.hasStartedMaidenTravel(playerName))							throw new ExceptionForbiddenAction();

		this.engine.addAction(this.data, "placeTile", playerName, position, t);
	}
	/**=============================================================================
	 * Switch at once two tiles from the player's hand with two tiles on the board.
	 * The tiles from the board are placed in the player's hand.
	 ===============================================================================*/
	public synchronized void replaceTwoTiles (String playerName, Tile tile1, Tile tile2, Point position1, Point position2) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
//TODO
/****		if (!this.data.isGameStarted())				throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))	throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position1.x, position1.y, t1,
												 position2.x, position1.y, ))	throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, t))							throw new ExceptionForbiddenAction();
		if (!this.data.hasRemainingAction(playerName))							throw new ExceptionTooManyActions();
		if (data.getWinner() != null)											throw new ExceptionPlayerIsBlocked();
		if (data.isGameBlocked(playerName))										throw new ExceptionGameIsOver();
		if(data.hasStartedMaidenTravel(playerName))	throw new ExceptionForbiddenAction();
*****/
		this.engine.addAction(this.data, "placeTile", playerName, position1, tile1, position2, tile2);
// TODO check all possible things
	}
	/**=============================================================================
	 * 	Signals the end of this player's turn
	 =============================================================================*/
	public synchronized void validate(String playerName) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		if(!data.hasStartedMaidenTravel(playerName))
		{
			if(data.getHandSize(playerName) < 5 && data.getNbrRemainingDeckTile() > 0) throw new ExceptionForbiddenAction();
			if(data.hasRemainingAction(playerName)) throw new ExceptionForbiddenAction();
		}

		this.engine.addAction(this.data, "validate", playerName);
	}
	/**=============================================================================
	 * 	Signals the start of this player's maiden travel. 
	 *  There must be a path from one terminus to another, passing next to all of
	 *  this player's buildings. 
	 *  @param terminus : the starting point
	 *  @param playerName 
	 =============================================================================*/
	public synchronized void startMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted, ExceptionUncompletedPath
	{
		if(!this.data.isGameStarted())						throw new ExceptionGameHasNotStarted();
		if(!this.data.isPlayerTurn(playerName))				throw new ExceptionNotYourTurn();
		if(!data.isPlayerTerminus(playerName, terminus))	throw new ExceptionForbiddenAction();
		if(!data.isStartOfTurn(playerName))					throw new ExceptionForbiddenAction();
		if(data.hasStartedMaidenTravel(playerName))			throw new ExceptionForbiddenAction();
		if(!data.isTrackCompleted(playerName))				throw new ExceptionUncompletedPath();

		this.engine.addAction(this.data, "startMaidenTravel", playerName, terminus, null);
	}
	/**=============================================================================
	 * Moves the player's streetcar following the tramMovement. 
	 * It must use a pre-existing track on the board, cannot go backwards, 
	 * and cannot be longer than the maximum allowed speed.
	 =============================================================================*/
	public synchronized void moveTram (String playerName, Point[] tramMovement, int ptrTramwayMovement) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		if (!this.data.isGameStarted())						throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))			throw new ExceptionNotYourTurn();

		if(!data.hasStartedMaidenTravel(playerName))		throw new ExceptionForbiddenAction();
		if(!data.isStartOfTurn(playerName))					throw new ExceptionForbiddenAction();
		if(ptrTramwayMovement >= data.getMaximumSpeed())	throw new ExceptionForbiddenAction();
		if(ptrTramwayMovement < Data.minSpeed-1)			throw new ExceptionForbiddenAction();
		if(ptrTramwayMovement >= Data.maxSpeed)				throw new ExceptionForbiddenAction();

		Point currentPosition = data.getTramPosition(playerName);
		Point nextPosition;

		if(!data.pathExistsBetween(currentPosition, tramMovement[ptrTramwayMovement])) throw new ExceptionForbiddenAction();
		if(data.getPreviousTramPosition(playerName).equals(tramMovement[0])) throw new ExceptionForbiddenAction();

		Point previousPosition = null;
		for (int ptr=0; ptr<= ptrTramwayMovement; ptr++)
		{
			nextPosition = tramMovement[ptr];
			if(previousPosition != null)
			{
				if(previousPosition.equals(nextPosition)) throw new ExceptionForbiddenAction();
			}
			if(!data.getAccessibleNeighborsPositions(currentPosition.x, currentPosition.y).contains(nextPosition))  throw new ExceptionForbiddenAction();
			if(data.getTile(currentPosition.x, currentPosition.y).isStop())
			{
				if(ptr != ptrTramwayMovement) throw new ExceptionForbiddenAction();
			}
			previousPosition = currentPosition;
			currentPosition = nextPosition;
		}

		EngineAction ea = engine.new EngineAction(playerName, data, "moveTram");
		ea.tramMovement = tramMovement;

		this.engine.addAction(ea);
	}

	/**=============================================================================
	 * Draw a card from the deck.  Put this drawn card in the player's hand
	 ===============================================================================*/
	public synchronized void drawTile(String playerName, int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionNotEnoughTilesInDeck, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isEnougthTileInDeck(nbrCards))							throw new ExceptionNotEnoughTilesInDeck();
		if (nbrCards > this.data.getPlayerRemainingTilesToDraw(playerName))		throw new ExceptionTwoManyTilesToDraw();
		if (data.hasStartedMaidenTravel(playerName))							throw new ExceptionForbiddenAction();

		this.engine.addAction(this.data, "drawTile", playerName, nbrCards);
	}
	/**=============================================================================
	 * Draw a card from a player's hand.  Put this drawn card in the player's hand
	 ===============================================================================*/
	public synchronized void pickTileFromPlayer (String playerName, String chosenPlayerName, Tile tile) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand
	{
		if (!this.data.isGameStarted())										throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))							throw new ExceptionNotYourTurn();
		if (this.data.getPlayerRemainingTilesToDraw(playerName) == 0)		throw new ExceptionTwoManyTilesToDraw();
		if (!this.data.hasStartedMaidenTravel(chosenPlayerName))			throw new ExceptionForbiddenAction();
		if (this.data.getHandSize(chosenPlayerName) == 0)					throw new ExceptionNotEnoughTilesInHand();
		if (playerName.equals(chosenPlayerName))							throw new ExceptionForbiddenAction();
		if(data.hasStartedMaidenTravel(playerName))							throw new ExceptionForbiddenAction();

		this.engine.addAction(data, "pickTileFromPlayer", playerName, chosenPlayerName, tile);
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private int getPlayerInLogInfoTable(String playerName)
	{
		LoginInfo li;

		for (int i=0; i<loggedPlayerTable.length; i++)
		{
			li = loggedPlayerTable[i];
			if (playerName.equals(li.getPlayerName())) return i;
		}
		return -1;
	}
	/**================================================================
	 * @return the index of the first free login cell in the table
	 *  that matches the given player.  If no cell is found, -1 is returned
	 ===================================================================*/
	private int getFreeAndMatchingLoginInTableIndex(boolean isHost, boolean isHuman, int aiLevel)
	{
		LoginInfo li;

		for (int i=0; i<this.loggedPlayerTable.length; i++)
		{
			li = this.loggedPlayerTable[i];
			if (li.isClosed())									continue;
			if (li.isOccupiedCell())							continue;
			if (li.isHost() != isHost)							continue;
			if (li.isHuman()!= isHuman)							continue;
			if (!li.isHuman() && (li.getAiLevel() != aiLevel))	continue;
			return i;
		}
		return -1;
	}
	/**=====================================================================
	 * Create a new AI player corresponding to the given informations.
	 * The new player name is picked following to the existing names.
	 * The new player's thread is kept in this.aiList
	 =======================================================================*/
	private void launchAIPlayer(LoginInfo newPlayerInfo)
	{
		PlayerAI	newPlayer	= null;
		String		playerName	= AiDefaultName + newPlayerInfo.getAiLevel();
		String		nameAdd		= "", str;
		Random		rnd			= new Random();

		while(true)
		{
			if (nameAdd.equals(""))	str = playerName;
			else					str = "_" + nameAdd;
			if (this.data.isUsedPlayerName(str)) nameAdd += rnd.nextInt(10);
			else break;
		}

		if (!nameAdd.equals(""))	playerName += ("_" + nameAdd);
		try					{newPlayer = new PlayerAI(playerName, newPlayerInfo.isHost(), this, newPlayerInfo.getAiLevel(), null);}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}
		Thread t = new Thread(newPlayer);
		this.aiList.put(playerName, t);
		t.start();
	}
}