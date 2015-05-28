package main.java.game;

import java.awt.Color;
import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Data.PlayerInfo;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.player.PlayerInterface;



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
	public static final String	gameMessageHeader		= "Street Car application: ";
	public final static int		applicationPort			= 5000;
	public final static String	applicationProtocol		= "rmi";

	private Data		data;
	private LoginInfo[]	loggedPlayerTable;
	private Engine		engine;
	private Thread		engineThread;
	private Object		engineLock;

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
		this.engineLock			= new Object();
		this.engine				= new Engine(this.engineLock);
		this.engineThread		= new Thread(this.engine);
		this.engineThread		.start();

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
	public Data getData(String playerName) throws RemoteException
	{
		return this.data.getClone(playerName);
	}
	public void onJoinGame(PlayerInterface player, boolean isHost) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		if (this.data.getNbrPlayer() >= Data.maxNbrPlayer)
		{
			System.out.println("\n===========================================================");
			System.out.println(gameMessageHeader + "join request from player : \"" + player.getPlayerName() + "\"");
			System.out.println(gameMessageHeader + "Refusing player, party is currently full.");
			System.out.println("===========================================================\n");
			throw new ExceptionFullParty();
		}
		else if (this.data.containsPlayer(player.getPlayerName()))
		{
			System.out.println("\n===========================================================");
			System.out.println(gameMessageHeader + "join request from player : \"" + player.getPlayerName() + "\"");
			System.out.println(gameMessageHeader + "Refusing player, name already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerName();
		}
		else if (this.usedColor(player.getColor()))
		{
			System.out.println("\n===========================================================");
			System.out.println(gameMessageHeader + "join request from player : \"" + player.getPlayerName() + "\"");
			System.out.println(gameMessageHeader + "Refusing player, color \"" + player.getColor() + "\"  already taken.");
			System.out.println("===========================================================\n");
			throw new ExceptionUsedPlayerColor();
		}
		else
		{
			this.data.addPlayer(player, player.getPlayerName(), isHost);
			System.out.println("\n===========================================================");
			System.out.println(Game.gameMessageHeader + "join request from player : \"" + player.getPlayerName() + "\"");
			System.out.println(Game.gameMessageHeader + "accepted player");
			System.out.println(Game.gameMessageHeader + "NbrPlayer: " + this.data.getNbrPlayer());
			System.out.println("===========================================================\n");
		}
	}
	public boolean onQuitGame(String playerName) throws RemoteException
	{
		String resS = null;
		boolean res= false;

		for (String name: this.data.getPlayerNameList())
		{
			if (name.equals(playerName))
			{
				this.data.removePlayer(name);
				resS= "player logged out";
				res = true;
				break;
			}
		}
		if (resS == null)	{resS = "player not found in the local list";	res = false;}

		System.out.println("\n===========================================================");
		System.out.println(gameMessageHeader + "quitGame");
		System.out.println(gameMessageHeader + "logout result : " + resS);
		System.out.println(gameMessageHeader + "playerName    : " + playerName);
		System.out.println("===========================================================\n");
		return res;
	}
	public void hostStartGame(String playerName) throws RemoteException
	{
		if (!this.data.getHost().equals(playerName))	throw new ExceptionForbiddenAction();

		this.engine.addAction(playerName, this.data, "hostStartGame", null, null, null);
		notifyEngine();
	}
// Version simple pour tester l'ia
//TODO Remplacer par public void placeTile(String playerName, int indexInHand, Point position, Direction rotation)
	public void placeTile(String playerName, Tile t, Point position)throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
		if (!this.data.isAcceptableTilePlacement(position.x, position.y, t))	throw new ExceptionForbiddenAction();

		this.engine.addAction(playerName, this.data, "placeTile", position, t, null);
		notifyEngine();
	}
	private void notifyEngine() {
		synchronized(this.engineLock)
		{
			try					{this.engineLock.notify();}
			catch(Exception e)	{e.printStackTrace(); System.exit(0);}
		}
	}
	
	public void  moveTram (String playerName, LinkedList<Point> tramMovement) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		PlayerInfo dataPlayer = data.getPlayerInfo(playerName);
		if(!dataPlayer.startedMaidenTravel) throw new ExceptionForbiddenAction();
		// TODO test if tram can reach (both regarding existing path and allowed number of movement)
		// TODO test if tram must stop at a stopping 
		// TODO maybe more tests?
		// TODO verify stoped at a stop sign
		// TODO instead of a Point, pass a list of Points (easyer to check)
		// TODO check that tram isn't goint backwards
		// TODO check if tramMovement is linked to current tram position
		// TODO check if each tramMovement is linked together
		// TODO check if args are null
		// TODO check if null (for method)
		// TODO check if maidenTravel has started (for every method)

		
		this.engine.addAction(playerName, this.data, "moveTram", null, null, tramMovement);
		notifyEngine();
	}
	
	public void	startMaidenTravel (String playerName, Point terminus) throws RemoteException, ExceptionNotYourTurn, ExceptionForbiddenAction, ExceptionGameHasNotStarted
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();

		PlayerInfo dataPlayer = data.getPlayerInfo(playerName);
		if(!dataPlayer.terminus.contains(terminus)) throw new ExceptionForbiddenAction();
		
		// TODO if(dataPlayer.numberOfTilesPlacedThisTurn > 0) throw new ExceptionForbiddenAction();
		// TODO if(dataPlayer.numberOfTilesDrawnThisTurn > 0) throw new ExceptionForbiddenAction();
		
		if(dataPlayer.startedMaidenTravel) throw new ExceptionForbiddenAction();
		
		dataPlayer.startedMaidenTravel = true;
		dataPlayer.tramPosition = (Point) terminus.clone(); // TODO should I clone everything?

		this.engine.addAction(playerName, this.data, "startMaidenTravel", null, null, null);
		notifyEngine();
	}
	
// TODO Version simple pour tester l'ia
	public Tile drawCard(String playerName, int nbrCards) throws RemoteException, ExceptionGameHasNotStarted, ExceptionNotYourTurn
	{
		if (!this.data.isGameStarted())											throw new ExceptionGameHasNotStarted();
		if (!this.data.isPlayerTurn(playerName))								throw new ExceptionNotYourTurn();
// Rajouter d'autres exceptions
		return this.data.drawCard();
	}
// TODO public LoginInfo[] getLoginInfo()
// TODO public void getLoginInfo(String playerName, int indexInLogTable, LogInfo li)



// --------------------------------------------
// Private methods:
// --------------------------------------------
	private boolean usedColor(Color c) throws RemoteException
	{
		PlayerInterface p;

		for (String name: this.data.getPlayerNameList())
		{
			p = this.data.getPlayer(name);
			if (p.getColor().equals(c))	return true;
		}
		return false;
	}
}