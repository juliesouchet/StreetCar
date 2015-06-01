package main.java.game;

import java.awt.Color;
import java.awt.Point;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.player.PlayerInterface;





public class Engine implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Thread						engineThread;
	private Object						engineLock;
	private LinkedList<EngineAction>	actionList;
	private EngineAction				toExecute;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Engine()
	{
		this.actionList		= new LinkedList<EngineAction>();
		this.engineLock		= new Object();
		this.engineThread	= new Thread(this);
		this.engineThread	.start();
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void run()
	{
		LinkedList<EngineAction> actionList;
		Class<? extends Engine> thisClass;
		Method method;

		while(true)
		{
			synchronized (this.engineLock)													// Wait for a new action
			{
				while (this.actionList.isEmpty())
				{
					try								{engineLock.wait();}
					catch (InterruptedException e)	{e.printStackTrace();}
				}
				actionList = new LinkedList<EngineAction> (this.actionList);
				this.actionList.clear();
			}
			for (EngineAction ea:actionList)
			{
				this.toExecute = ea;
				try
				{
					synchronized(this.toExecute)
					{
						thisClass	= this.getClass();
						method		= thisClass.getDeclaredMethod(ea.function);		// Execute the corresponding action
						method		.invoke(this);
					}
				}
				catch (Exception e){e.printStackTrace(); return;}
			}
		}
	}
	/** @return add an event to the engine action queue*/
	public void addAction(Data data, String function)
	{
		addAction(new EngineAction(null, data, function, null, null, null, -1, null, null, null));
	}
	/** @return add an event to the engine action queue*/
	public void addAction(Data data, String function, String playerName)
	{
		addAction(new EngineAction(playerName, data, function, null, null, null, -1, null, null, null));
	}
	/** @return add an event to the engine action queue*/
	public void addAction(Data data, String function, String playerName, String chosenPlayerName, Tile t)
	{
		addAction(new EngineAction(playerName, data, function, null, t, null, -1, null, null, chosenPlayerName));
	}
	/** @return add an event to the engine action queue*/
	public void addAction(Data data, String function, String playerName, Color playerColor, boolean isHost)
	{
		addAction(new EngineAction(playerName, data, function, null, null, null, -1, playerColor, isHost, null));
	}
	/** @return add an event to the engine action queue*/
	public void addAction(String playerName, Data data, String function, Point position, Tile tile, LinkedList<Point> tramMovement, int nbrCardsToDraw)
	{
		addAction(new EngineAction(playerName, data, function, position, tile, tramMovement, nbrCardsToDraw, null, false, null));
	}
	/** @return add an event to the engine action queue*/
	public void addAction(EngineAction ea)
	{
		synchronized (engineLock)
		{
			this.actionList.add(ea);
			synchronized(this.engineLock)
			{
				try					{this.engineLock.notify();}
				catch(Exception e)	{e.printStackTrace(); System.exit(0);}
			}
		}
	}

// --------------------------------------------
// Public methods:
// This functions are executed by the caller's thread
// --------------------------------------------
	public void onJoinGame(Data data, PlayerInterface player, String playerName, boolean isHost) throws RemoteException
	{
		PlayerInterface pi;

		data.addPlayer(player, playerName, isHost);
		for (String name: data.getPlayerNameList())
		{
			pi = data.getRemotePlayer(name);
			pi.gameHasChanged(data.getClone(name));
		}
	}
	public void onQuitGame(Data data, String playerName) throws RemoteException
	{
		PlayerInterface pi;

		if ((data.isGameStarted()) || (data.istHost(playerName)))
		{
			for (String name: data.getPlayerNameList()) data.getRemotePlayer(name).excludePlayer();
			this.engineThread.interrupt();
		}
		else
		{
			data.removePlayer(playerName);
			for (String name: data.getPlayerNameList())
			{
				pi = data.getRemotePlayer(name);
				if (name.equals(playerName))	pi.excludePlayer();
				else							pi.gameHasChanged(data.getClone(name));
			}
		}
	}

// --------------------------------------------
// Private methods:
// Declare all the private methods as synchronized
// --------------------------------------------
	@SuppressWarnings("unused")
	private synchronized void excludePlayer() throws RemoteException
	{
		PlayerInterface pi;
		Data privateData;
		Data data = this.toExecute.data;

		for (String name: data.getPlayerNameList())
		{
			pi = data.getRemotePlayer(name);
			if (name.equals(this.toExecute.playerName)) pi.excludePlayer();
			else
			{
				privateData	= data.getClone(name);
				pi.gameHasChanged(privateData);
			}
		}
	}
	@SuppressWarnings("unused")
	private synchronized void hostStartGame() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;

		data.hostStartGame(playerName);
		this.notifyAllPlayers();
	}
	@SuppressWarnings("unused")
	private synchronized void placeTile() throws RemoteException
	{
		String	playerName	= this.toExecute.playerName;
		Data	data		= this.toExecute.data;
		Point	position	= this.toExecute.position;
		Tile	tile		= this.toExecute.tile;

		data.placeTile(playerName, position.x, position.y, tile);
		this.notifyPlayer(playerName);
	}

	@SuppressWarnings("unused")
	private synchronized void replaceTwoTiles () throws RemoteException
	{
// TODO replace by notifyPlayer()
		this.notifyAllPlayers();
	}

	@SuppressWarnings("unused")
	private synchronized void validate() throws RemoteException
	{
		this.toExecute.data.skipTurn();
		this.notifyAllPlayers();

	}
	@SuppressWarnings("unused")
	private synchronized void startMaidenTravel()
	{
		Data	data		= this.toExecute.data;
		String playerName = toExecute.playerName;
		Point chosenTerminus = toExecute.position;
		//PlayerInfo dataPlayer = data.getPlayerInfo(playerName);

		data.startMaidenTravel(playerName);
		data.setTramPosition(playerName, chosenTerminus);

		Point[] destination = new Point[2];
		int i = 0;
		for(Point p : data.getPlayerTerminusPosition(playerName))
		{
			if(p.distance(chosenTerminus) > 1)
			{
				destination[i] = ((Point) p.clone());
				i ++;
			}
		}

		data.setDestinationTerminus(playerName, destination);

//		for(String name : data.getPlayerOrder())
//		{
//			PlayerInterface remotePlayer = data.getPlayer(name);
//			remotePlayer.playerStartedMaidenJourney(playerName, terminus);
//			remotePlayer.reveaPlayerlLine(playerName, dataPlayer.line);
//			//remotePlayer.revealPlayerRoute(playerName, dataPlayer.route); // TODO
//		}
	}
	@SuppressWarnings("unused")
	private synchronized void moveTram() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String playerName = toExecute.playerName;
		LinkedList<Point> tramMovement = toExecute.tramMovement;
		data.setTramPosition(playerName, tramMovement.getLast());

		Point[] terminus = data.getPlayerTerminusPosition(playerName);
		Point position = data.getTramPosition(playerName);
		for (int i=0; i<terminus.length; i++)
		{
			if(terminus[i].equals(position))
			{
				System.out.println("STOP EVERYTHING!!!!!! \n" + playerName + " has won");
			}
		}
		this.notifyPlayer(playerName);
		//		for(String name : data.getPlayerOrder())
		//		{
		//			PlayerInterface remotePlayer = data.getPlayer(name);
		//			remotePlayer.playerHasMovedTram(playerName, dest);
		//
		//			if(dataPlayer.tramPosition.equals(dataPlayer.endTerminus))
		//			{
		//				remotePlayer.announceVictoriousPlayer(playerName);
		//				// TODO check if stopped by all stop points
		//			}
		//		}
	}
	@SuppressWarnings("unused")
	private synchronized void drawTile() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;
		int		nbrCards	= this.toExecute.nbrCardsToDraw;

		data.drawTile(playerName, nbrCards);
		this.notifyPlayer(playerName);
	}
	@SuppressWarnings("unused")
	private synchronized void pickTileFromPlayer () throws RemoteException
	{
		Data	data			= this.toExecute.data;
		String	playerName		= this.toExecute.playerName;
		String	chosenPlayerName= this.toExecute.chosenPlayer;
		Tile	tile			= this.toExecute.tile;

		data.pickTileFromPlayer(playerName, chosenPlayerName, tile);
		this.notifyPlayer(playerName);
	}

	private synchronized void notifyPlayer(String playerName) throws RemoteException
	{
		PlayerInterface pi;
		Data data = this.toExecute.data;
		Data privateData;

		pi			= data.getRemotePlayer(playerName);
		privateData	= data.getClone(playerName);
		pi.gameHasChanged(privateData);
	}
	
	private synchronized void notifyAllPlayers() throws RemoteException
	{
		Data data = this.toExecute.data;
		for (String name: data.getPlayerNameList()) this.notifyPlayer(name);
	}

// --------------------------------------------
// Private class:
// --------------------------------------------
	class EngineAction
	{
		// Attributes
		public PlayerInterface	player;
		public String			playerName;
		public Color			playerColor;
		public Data				data;
		public String			function;
		public Point			position;
		public Point			secondPosition;
		public Tile				tile;
		public Tile				secondTile;
		public LinkedList<Point>tramMovement;
		public Integer			nbrCardsToDraw; // TODO val discuss this with riyane
		public String 			chosenPlayer;
		public Boolean			isHost;

		// Builder
		public EngineAction (String playerName, Data data, String function, Point position, Tile tile, LinkedList<Point> tramMovement, Integer nbrCardsToDraw, Color playerColor, Boolean isHost, String chosenPlayer)
		{
			this.playerName		= playerName;
			this.data			= data;
			this.function		= function;
			this.position		= position;
			this.tile			= tile;
			this.tramMovement	= tramMovement;
			this.nbrCardsToDraw	= nbrCardsToDraw;
			this.playerColor	= playerColor;
			this.chosenPlayer	= chosenPlayer;
			this.isHost			= isHost;
		}
		public EngineAction(String playerName, Data data, String Function)
		{
			this(playerName, data, Function, null, null, null, -1, null, false, null);
		}
	}
}
