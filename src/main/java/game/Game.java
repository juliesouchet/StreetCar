package main.java.game;

import java.awt.Color;
import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
	public final static int				applicationPort			= 5000;
	public final static String			applicationProtocol		= "rmi";

	private Data						data;
	private LoginInfo[]					loggedPlayerTable;
	private Engine						engine;
	private HashMap<String, PlayerAI>	aiList;

// --------------------------------------------
// Builder:
// --------------------------------------------
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
		this.aiList				= new HashMap<String, PlayerAI>();

		System.out.println("\n===========================================================");
		System.out.println(gameMessageHeader + "URL = " + url);
		System.out.println(gameMessageHeader + "ready");
		System.out.println(gameMessageHeader + "Start waiting for connexion request");
		System.out.println("===========================================================\n");
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
	public synchronized Data getData(String playerName) throws RemoteException
	{
		return this.data.getClone(playerName);
	}
	/**==============================================
	 * @return the table used by the waiting room
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

		String	oldPlayerName		= this.loggedPlayerTable[playerToChangeIndex].getPlayerName();
		boolean	oldPlayerIsOccupied	= this.loggedPlayerTable[playerToChangeIndex].isOccupiedCell();
		boolean	newPlayerIsHuman	= newPlayerInfo.isHuman();

		this.loggedPlayerTable[playerToChangeIndex] = newPlayerInfo.getClone();
		this.loggedPlayerTable[playerToChangeIndex].setFreeCell();
		if (oldPlayerIsOccupied)														// Case exclude old player
		{
			this.engine.addAction(this.data, "excludePlayer", oldPlayerName);
			this.aiList.remove(oldPlayerName);
		}
		if (!newPlayerIsHuman)															// Case create AI player
		{
			PlayerAI newPlayer = null;
			try					{newPlayer = new PlayerAI(newPlayerInfo.getPlayerName(), false, getUnusedColor(), this, newPlayerInfo.getAiLevel(), null);}
			catch (Exception e)	{e.printStackTrace(); System.exit(0);}
			Thread t = new Thread(playerName);
			this.aiList.put(playerName, newPlayer);
			t.start();
		}
	}
	/**================================================
	 * @return Makes a player join the game
	 * ================================================*/
	public synchronized void onJoinGame(PlayerInterface player, boolean isHost, int iaLevel) throws RemoteException, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionGameHasAlreadyStarted
	{
		String	playerName	= player.getPlayerName();
		Color	playerColor	= player.getPlayerColor();
		boolean	isHuman		= player.isHumanPlayer();
		int		playerIndex = getFreeAndMatchingLoginInTableIndex(isHost, isHuman, iaLevel);

		if (this.data.getNbrPlayer() >= Data.maxNbrPlayer)	throw new ExceptionFullParty();
		if (playerIndex == -1)								throw new ExceptionNoCorrespondingPlayerExcpected();
		if (this.data.isPlayerLogged(playerName))			throw new ExceptionUsedPlayerName();
		if (this.usedColor(playerColor))					throw new ExceptionUsedPlayerColor();
		if (this.data.isGameStarted())						throw new ExceptionGameHasAlreadyStarted();
		if ((isHost) && (this.data.getHost() != null))		throw new ExceptionHostAlreadyExists();

		this.engine.onJoinGame(this.data, player, playerName, playerColor, isHost);
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
		boolean isHost			= this.data.istHost(playerName);
		boolean gameHasStarted	= this.data.isGameStarted();

		if (!this.data.isPlayerLogged(playerName))	throw new ExceptionForbiddenAction();
		if (playerIndex == -1)						throw new ExceptionForbiddenAction();

		this.loggedPlayerTable[playerIndex] = LoginInfo.initialLoginTable[playerIndex].getClone();
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
	 * Make the kame begins
	 ================================================*/
	public synchronized void hostStartGame(String playerName) throws RemoteException, ExceptionForbiddenAction, ExceptionNotEnougthPlayers
	{
		if (!this.data.getHost().equals(playerName))	throw new ExceptionForbiddenAction();
		if (!this.data.isGameReadyToStart())					throw new ExceptionNotEnougthPlayers();

		for (LoginInfo li: this.loggedPlayerTable) li.setIsClosed(true);
		this.engine.addAction(this.data, "hostStartGame", playerName);
	}
	/**=============================================================================
	 * Places a tile from the player's hand on the bord.
	 * The guiven tile must bellong to the player's hand
	 * The guiven tile is removed from the player's hand
	 ===============================================================================*/
	public synchronized void placeTile(String playerName, Tile t, Point position)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionTooManyActions
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position.x, position.y, t))	throw new ExceptionForbiddenAction();
		if (!this.data.isInPlayerHand(playerName, t))							throw new ExceptionForbiddenAction();
		if (!this.data.hasRemainingAction(playerName))							throw new ExceptionTooManyActions();

		this.engine.addAction(playerName, this.data, "placeTile", position, t, null, -1);
	}
	public synchronized void replaceTwoTiles (String playerName, Tile t1, Tile t2, Point p1, Point p2) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		if (!this.data.isGameStarted())	throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName)) throw new ExceptionNotYourTurn();
		
		EngineAction ea = engine.new EngineAction(playerName, data, "replaceTwoTiles");

		ea.tile = t1;
		ea.secondTile = t2;
		ea.position = p1;
		ea.secondPosition = p2;

		this.engine.addAction(ea);
		// TODO check all possible things
	}
	public synchronized void validate(String playerName) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionForbiddenAction
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		if(!data.hasStartedMaidenTravel(playerName))
		{
			if(data.getHandSize(playerName) < 5) throw new ExceptionForbiddenAction();
			if(data.hasRemainingAction(playerName)) throw new ExceptionForbiddenAction();
		}

		this.engine.addAction(playerName, data, "validate", null, null, null, -1);
	}
	public synchronized void startMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		if(!data.isPlayerTerminus(playerName, terminus)) throw new ExceptionForbiddenAction();
		if(!data.isStartOfTurn(playerName)) throw new ExceptionForbiddenAction();
		if(data.hasStartedMaidenTravel(playerName)) throw new ExceptionForbiddenAction();
		
		EngineAction ea = engine.new EngineAction(playerName, data, "startMaidenTravel");
		ea.position = terminus;

		this.engine.addAction(ea);
	}
	public synchronized void moveTram (String playerName, LinkedList<Point> tramMovement) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		if(!data.hasStartedMaidenTravel(playerName)) throw new ExceptionForbiddenAction();
		if(!data.isStartOfTurn(playerName)) throw new ExceptionForbiddenAction();
		if(tramMovement.size() > data.getMaximumSpeed()) throw new ExceptionForbiddenAction();
		if(tramMovement.size() < Data.minSpeed) throw new ExceptionForbiddenAction();
		if(tramMovement.size() > Data.maxSpeed) throw new ExceptionForbiddenAction();

		Point currentPosition = data.getTramPosition(playerName);
		
		if(!data.pathExistsBetween(currentPosition, tramMovement.getLast())) throw new ExceptionForbiddenAction();	
		if(data.getPreviousTramPosition(playerName).equals(tramMovement.getFirst())) throw new ExceptionForbiddenAction();
		
		Iterator<Point> tramPathIterator = tramMovement.iterator();
		Point previousPosition = null, nextPosition;
		while(tramPathIterator.hasNext())
		{
			nextPosition = tramPathIterator.next();
			if(previousPosition != null)
			{
				if(previousPosition.equals(nextPosition)) throw new ExceptionForbiddenAction();
			}
			if(!data.getAccessibleNeighborsPositions(currentPosition.x, currentPosition.y).contains(nextPosition))  throw new ExceptionForbiddenAction();
			if(data.getTile(currentPosition.x, currentPosition.y).isStop())
			{
				if(tramPathIterator.hasNext()) throw new ExceptionForbiddenAction();
			}
			
			previousPosition = currentPosition;
			currentPosition = nextPosition;
		}
		
		// TODO check if maidenTravel has started (for every method)

		EngineAction ea = engine.new EngineAction(playerName, data, "moveTram");
		ea.tramMovement = tramMovement;
		
		this.engine.addAction(ea);
	}

	/**=============================================================================
	 * Draw a card from the deck.  Put this drawn card in the player's hand
	 ===============================================================================*/
	public synchronized void drawTile(String playerName, int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionNotEnougthTileInDeck, ExceptionTwoManyTilesToDraw
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isEnougthTileInDeck(nbrCards))							throw new ExceptionNotEnougthTileInDeck();
		if (nbrCards > this.data.getPlayerRemainingTilesToDraw(playerName))		throw new ExceptionTwoManyTilesToDraw();

		this.engine.addAction(playerName, this.data, "drawTile", null, null, null, nbrCards);
	}
	/**=============================================================================
	 * Draw a card from a player's hand.  Put this drawn card in the player's hand
	 ===============================================================================*/
	public synchronized void pickTileFromPlayer (String playerName, String chosenPlayerName, Tile tile) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw, ExceptionForbiddenAction, ExceptionNotEnougthTileInHand
	{
		if (!this.data.isGameStarted())										throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))							throw new ExceptionNotYourTurn();
		if (this.data.getPlayerRemainingTilesToDraw(playerName) == 0)		throw new ExceptionTwoManyTilesToDraw();
		if (!this.data.hasStartedMaidenTravel(chosenPlayerName))			throw new ExceptionForbiddenAction();
		if (this.data.getHandSize(chosenPlayerName) == 0)					throw new ExceptionNotEnougthTileInHand();
		if (playerName.equals(chosenPlayerName))							throw new ExceptionForbiddenAction();

		this.engine.addAction(data, "pickTileFromPlayer", playerName, chosenPlayerName, tile);
	}

// --------------------------------------------
// Private methods:
// --------------------------------------------
	private boolean usedColor(Color c) throws RemoteException
	{
		PlayerInterface p;

		for (String name: this.data.getPlayerNameList())
		{
			p = this.data.getRemotePlayer(name);
			if (p.getPlayerColor().equals(c))	return true;
		}
		return false;
	}
// TODO crer dans data un ensemble final de couleurs utilisables
	private Color getUnusedColor()
	{
		Random rnd = new Random();
		return new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}
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
			if (li.isHost() != isHost)							continue;
			if (li.isHuman()!= isHuman)							continue;
			if (!li.isHuman() && (li.getAiLevel() != aiLevel))	continue;
			return i;
		}
		return -1;
	}
}