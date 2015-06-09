package main.java.game;

import java.awt.Color;
import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Random;

import main.java.data.Data;
import main.java.data.Hand;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.player.PlayerAI;
import main.java.player.PlayerInterface;
import main.java.util.Copier;
import main.java.util.Util;

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
	public static int					applicationPort			= 5000;
	public final static String			applicationProtocol		= "rmi";
	public final static String			AiDefaultName			= "AI Level ";

	protected Data						data;
	protected LoginInfo[]				loggedPlayerTable;
	protected Engine					engine;
	protected EngineChat				engineChat;
	protected HashMap<String, Thread>	aiList;

// --------------------------------------------
// Builder:
// --------------------------------------------
// TODO to remove after test
public String	getTestHostName()	{return this.data.getHost();}
public Data		getTestData()		{return this.data;}
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
			url = getRemoteURL(appIP, gameName);
			java.rmi.registry.LocateRegistry.createRegistry(applicationPort);
			Naming.rebind(url, this);
		}
		catch (Exception e) {e.printStackTrace(); throw new RemoteException();}

		this.data				= new Data(gameName, boardName, nbrBuildingInLine);		// Init application
		this.loggedPlayerTable	= LoginInfo.getInitialLoggedPlayerTable();
		this.engine				= new Engine();
		this.engineChat			= new EngineChat();
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
////	System.setSecurityManager(new RMISecurityManager());
		try
		{
			String url = getRemoteURL(appIP, gameName);
			return (GameInterface) Naming.lookup(url);
		}
		catch (Exception e) {e.printStackTrace(); throw new RemoteException();}
	}

	private static String getRemoteURL(String appIP, String gameName) throws UnsupportedEncodingException {
		String encodedIP = URLEncoder.encode(appIP, "UTF-8");
		String encodedGameName = URLEncoder.encode(gameName, "UTF-8");
		return applicationProtocol + "://" + encodedIP + ":" + applicationPort + "/" + encodedGameName;
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
		else notifyAll = true;
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
		this.engine.addAction(data, "excludePlayer", data.getRemotePlayer(playerName));
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
	 ===============================================================================*/
	public synchronized void placeTile(String playerName, Tile t, Point position)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position.x, position.y, t))	throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, t))							throw new ExceptionForbiddenAction();
		if (!this.data.hasRemainingAction(playerName))							throw new ExceptionTooManyActions();
		if (this.data.getWinner() != null)										throw new ExceptionGameIsOver();
		if (this.data.isGameBlocked(playerName))								throw new ExceptionPlayerIsBlocked();
		if (data.hasStartedMaidenTravel(playerName))							throw new ExceptionForbiddenAction();

		this.engine.addAction(this.data, "placeTile", playerName, position, t);
	}
	/**=============================================================================
	 * Switch at once two tiles from the player's hand with two tiles on the board.
	 * The tiles from the board are placed in the player's hand.
	 ===============================================================================*/
	public synchronized void replaceTwoTiles (String playerName, Tile tile1, Tile tile2, Point position1, Point position2) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions, ExceptionPlayerIsBlocked, ExceptionGameIsOver
	{
		if (!this.data.isGameStarted())												throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))									throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position1.x, position1.y, tile1,
												 position2.x, position1.y, tile2))	throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, tile1))							throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, tile2))							throw new ExceptionForbiddenAction();
		if (!this.data.isStartOfTurn(playerName))									throw new ExceptionTooManyActions();
		if (data.getWinner() != null)												throw new ExceptionPlayerIsBlocked();
		if (data.isGameBlocked(playerName))											throw new ExceptionGameIsOver();
		if(data.hasStartedMaidenTravel(playerName))	throw new ExceptionForbiddenAction();

		this.engine.addAction(this.data, "replaceTwoTiles", playerName, position1, tile1, position2, tile2);
	}
	/**=============================================================================
	 * 	Signals the end of this player's turn
	 =============================================================================*/
	public synchronized void validate(String playerName) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		if (!this.data.isGameStarted())								throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))					throw new ExceptionNotYourTurn();

		if(!data.hasStartedMaidenTravel(playerName))
		{
			if(data.getHandSize(playerName) < Hand.maxHandSize &&
			   data.getNbrRemainingDeckTile() > 0)					throw new ExceptionForbiddenAction();
			if(data.hasRemainingAction(playerName))					throw new ExceptionForbiddenAction();
		}

		this.engine.addAction(this.data, "validate", playerName);
	}
	public void rollBack(String playerName) throws RemoteException, ExceptionForbiddenAction, ExceptionNotYourTurn, ExceptionNoPreviousGameToReach
	{
// TODO a decommenter apres les test
//		if (!data.getPlayerTurn().equals(playerName))							throw new ExceptionNotYourTurn();
//		if (!data.hasDoneRoundFirstAction(playerName))							throw new ExceptionNoPreviousGameToReach();

		this.data.rollBack();
		this.engine.addAction(data, "notifyAllPlayers");
	}
	/**=============================================================================
	 * 	Signals the end of this player's maiden travel.
	 =============================================================================*/
	public synchronized void stopMaidenTravel (String playerName) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		if(!this.data.isGameStarted())						throw new ExceptionGameHasNotStarted();
		if(!this.data.isPlayerTurn(playerName))				throw new ExceptionNotYourTurn();
		if(!this.data.hasStartedMaidenTravel(playerName))	throw new ExceptionForbiddenAction();

		this.engine.addAction(this.data, "stopMaidenTravel", playerName);
	}
	/**=============================================================================
	 * @return Moves the player's streetcar following the tramMovement.</br>
	 * It must use a pre-existing track on the board, cannot go backwards,
	 * and cannot be longer than the maximum allowed speed.</br>
	 * @param tramPath must contain the actual tram position, if the travel has already started.  Otherwise, it must contain one of the player's terminus.</br>
	 * @param startTerminus is non null when the player start his maiden travel.  Other wise, this parameter may be null</br>
	 =============================================================================*/
	public synchronized void moveTram (String playerName, Point[] tramPath, int tramPathSize, Point startTerminus) throws ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionMissingStartTerminus, ExceptionWrongPlayerTerminus, ExceptionWrongTramwayPath, ExceptionWrongTramwaySpeed, ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionTrtamwayDoesNotStop
	{
		if (!this.data.isGameStarted())												throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))									throw new ExceptionNotYourTurn();
		if (!this.data.isStartOfTurn(playerName))									throw new ExceptionForbiddenAction();
		if (tramPathSize > data.getMaximumSpeed()+1)								throw new ExceptionWrongTramwaySpeed();
		if (tramPathSize < Data.minSpeed+1)											throw new ExceptionWrongTramwaySpeed();
		if (tramPathSize > Data.maxSpeed+1)											throw new ExceptionWrongTramwaySpeed();

		boolean hasStartedTravel = this.data.hasStartedMaidenTravel(playerName);
		if (!hasStartedTravel)
		{
			if (startTerminus == null)												throw new ExceptionMissingStartTerminus();
			if (!startTerminus.equals(tramPath[0]))									throw new ExceptionWrongTramwayPath();
			if (!data.isPlayerTerminus(playerName, startTerminus))					throw new ExceptionWrongPlayerTerminus();
			if (!data.isTrackCompleted(playerName))									throw new ExceptionForbiddenAction();
		}
		this.checkTramPath(playerName, tramPath, tramPathSize, startTerminus);
		this.engine.addAction(data, "moveTram", playerName, tramPath, tramPathSize, startTerminus);
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
	public synchronized void sendChatMessage(String playerName, String message)	throws RemoteException
	{
		this.engineChat.addAction(this.data, playerName, message);
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
	private void checkTramPath(String playerName, Point[] tramPath, int tramPathSize, Point startTerminus) throws ExceptionTramwayExceededArrival, ExceptionWrongTramwayStart, ExceptionWrongTramwayStartTerminus, ExceptionTramwayJumpCell, ExceptionWrongTramwayPath, ExceptionTrtamwayDoesNotStop
	{
		boolean hasStartedTravel = this.data.hasStartedMaidenTravel(playerName);
		if (hasStartedTravel)
		{
			int winner	= -1;
			int stop	= -1;
			Point tramPosition = this.data.getTramPosition(playerName);
			Point p0, p1, p2;
			Point[] endTerminus =  this.data.getPlayerTerminusPosition(playerName);

			if (!tramPath[0].equals(tramPosition))				throw new ExceptionWrongTramwayStart();

			p0 = this.data.getPreviousTramPosition(playerName);
			p1 = tramPosition;
			for (int i=1; i<tramPathSize; i++)
			{
				p2 = tramPath[i];
				if (winner != -1)								throw new ExceptionTramwayExceededArrival();
				if (stop	!= -1)								throw new ExceptionTrtamwayDoesNotStop();
//TODO				if (!data.pathExistsBetween(p0, p1, p2))		throw new ExceptionWrongTramwayPath();			//TODO a corriger
				if (Util.manhathanDistance(p1, p2) != 1)		throw new ExceptionTramwayJumpCell();
				if (p2.equals(endTerminus[0]))		winner = i;
				if (p2.equals(endTerminus[1]))		winner = i;
				if (this.data.getTile(p2).isStop())	stop = i;
				p0 = p1;
				p1 = p2;
			}
			return;
		}
		else
		{
System.out.println("Start maiden travel " + playerName + " (game.checkTramPath)");
			Point[]	terminus = data.getPlayerTerminusPosition(playerName);
			int i;
			
			for (i=0; i<4; i++) if (startTerminus.equals(terminus[i])) break;
			if (i == 4) throw new ExceptionWrongTramwayStartTerminus();
			this.data.startMaidenTravel(playerName, startTerminus);
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
				this.data.stopMaidenTravel(playerName);
				throw e;
			}
			this.data.stopMaidenTravel(playerName);
		}
	}
}