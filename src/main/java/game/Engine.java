package main.java.game;

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
	private Object						lock;
	private LinkedList<EngineAction>	actionList;
	private EngineAction				toExecute;
	private Data						data;
	private int                         currentPlayerIndex;

	// --------------------------------------------
	// Builder:
	// --------------------------------------------
	public Engine(Object lock, Data data)
	{
		this.lock		= lock;
		this.actionList	= new LinkedList<EngineAction>();
		this.data = data;
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
			synchronized (lock)													// Wait for a new action
			{
				while (this.actionList.isEmpty())
				{
					try								{lock.wait();}
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
					thisClass	= this.getClass();
					method		= thisClass.getDeclaredMethod(ea.function);		// Execute the corresponding action
					method		.invoke(this);
				}
				catch (Exception e){e.printStackTrace(); return;}
			}
		}
	}
	/**===================================================================
	 * @return add an event to the engine action queue
	 ====================================================================*/
	public void addAction(String playerName, Data data, String function)
	{
		synchronized (lock)
		{
			EngineAction ea = new EngineAction(playerName, data, function);
			this.actionList.add(ea);
		}
	}

	// --------------------------------------------
	// Private methods:
	// TODO: declare all the private methods as synchronized
	// --------------------------------------------

	public void hostStartGame() throws RemoteException
	{
		data.randomizePlayerOrder();
		currentPlayerIndex = 0;
		for(String player : data.getPlayerOrder())
		{
			PlayerInterface remotePlayer = data.getPlayer(player);
			remotePlayer.dealLineCard(data.getPlayerInfo(player).line);
			remotePlayer.dealRouteCard(data.getPlayerInfo(player).buildingInLine_name);
		}
	}

	public boolean onQuitGame(String playerName) throws RemoteException 
	{
		// TODO
		return false;
	}
	public Data getData(String playerName) throws RemoteException
	{
		return data;
	}

	public void	undoAttempt(String playerName) throws RemoteException
	{
		// TODO
	}

	public void	undoTurn(String playerName) throws RemoteException
	{
		// TODO
	}

	public void	placeTile(String playerName, Tile t, Point position) throws RemoteException
	{
		// TODO
	}

	public void	moveTram(String playerName, Point dest) throws RemoteException
	{
		// TODO
	}

	public void	pickTileFromBox(String playerName) throws RemoteException
	{
		// TODO
	}

	public void	pickTileFromPlayer(String playerName, String chosenPlayer, Tile car) throws RemoteException
	{
		// TODO
	}

	public void	replaceTwoTiles(String playerName, Tile t1, Tile t2, Point p1, Point p2) throws RemoteException
	{
		// TODO
	}

	public void	startMaidenTravel(String playerName, Point terminus) throws RemoteException
	{
		// TODO
	}

	public void	Validate(String playerName) throws RemoteException
	{
		// TODO
	}


	// --------------------------------------------
	// Private class:
	// --------------------------------------------
	private class EngineAction
	{
		// Attributes
		public String			playerName;
		public Data				data;
		public String			function;

		// Builder
		public EngineAction (String playerName, Data data, String function)
		{
			this.playerName		= playerName;
			this.data			= data;
			this.function		= function;
		}
	}


	public Data getData() {
		return data;
	}
	
	public void addPlayer(PlayerInterface player, boolean isHost) throws RemoteException, ExceptionFullParty
	{
		data.addPlayer(player, player.getPlayerName(), isHost);
	}
}