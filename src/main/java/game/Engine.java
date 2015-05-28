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
			synchronized (engineLock)													// Wait for a new action
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
	public void addAction(String playerName, Data data, String function, Point position, Tile tile)
	{
		synchronized (engineLock)
		{
			EngineAction ea = new EngineAction(playerName, data, function, position, tile);
			this.actionList.add(ea);
			synchronized(this.engineLock)
			{
				try					{this.engineLock.notify();}
				catch(Exception e)	{e.printStackTrace(); System.exit(0);}
			}
		}
	}

// --------------------------------------------
// Private methods:
// Declare all the private methods as synchronized
// --------------------------------------------
	@SuppressWarnings("unused")
	private void onJoinGame() throws RemoteException
	{
		this.notifyAllPlayers();
	}
	@SuppressWarnings("unused")
	private void onQuitGame() throws RemoteException
	{
// TODO
		this.notifyAllPlayers();
	}
	@SuppressWarnings("unused")
	private void hostStartGame() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;

		data.hostStartGame(playerName);
		this.notifyAllPlayers();
	}
	@SuppressWarnings("unused")
	private void placeTile() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		Point	position	= this.toExecute.position;
		Tile	tile		= this.toExecute.tile;

		data.setTile(position.x, position.y, tile);
		data.skipTurn();
		this.notifyAllPlayers();
	}
	@SuppressWarnings("unused")
	private void excludePlayer() throws RemoteException
	{
		PlayerInterface pi;
		Data privateData;

		for (String name: this.toExecute.data.getPlayerNameList())
		{
			pi = this.toExecute.data.getPlayer(name);
			if (name.equals(this.toExecute.playerName)) pi.excludePlayer();
			else
			{
				privateData	= this.toExecute.data.getClone(name);
				pi.gameHasChanged(privateData);
			}
		}
	}
	private void notifyAllPlayers() throws RemoteException
	{
		PlayerInterface pi;
		Data data = this.toExecute.data;
		Data privateData;

		for (String name: data.getPlayerNameList())
		{
			pi			= data.getPlayer(name);
			privateData	= data.getClone(name);
			pi.gameHasChanged(privateData);
		}		
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
		public Point			position;
		public Tile				tile;

		// Builder
		public EngineAction (String playerName, Data data, String function, Point position, Tile tile)
		{
			this.playerName		= playerName;
			this.data			= data;
			this.function		= function;
			this.position		= position;
			this.tile			= tile;
		}
	}
}