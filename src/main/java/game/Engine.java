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

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Engine(Object lock)
	{
		this.lock		= lock;
		this.actionList	= new LinkedList<EngineAction>();
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
	public void addAction(String playerName, Data data, String function, Point position, Tile tile)
	{
		synchronized (lock)
		{
			EngineAction ea = new EngineAction(playerName, data, function, position, tile);
			this.actionList.add(ea);
		}
	}

// --------------------------------------------
// Private methods:
// TODO: declare all the private methods as synchronized
// --------------------------------------------
	public void hostStartGame() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;

		data.hostStartGame(playerName);
		this.notifyAllPlayers(data);
	}
	public void placeTile() throws RemoteException
	{
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;
		Point	position	= this.toExecute.position;
		Tile	tile		= this.toExecute.tile;
//TODO pas fini
		data.setTile(position.x, position.y, tile);
		data.skipTurn();
		this.notifyAllPlayers(data);
	}

// --------------------------------------------
// Private class:
// --------------------------------------------
	private void notifyAllPlayers(Data data) throws RemoteException
	{
		PlayerInterface pi;
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