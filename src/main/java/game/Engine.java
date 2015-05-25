package main.java.game;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Data;
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
		Data	data		= this.toExecute.data;
		String	playerName	= this.toExecute.playerName;
		Data	privateData;
		PlayerInterface pi;

		data.randomizePlayerOrder();
		for (String name: data.getPlayerNameList())
		{
			pi			= data.getPlayer(name);
			privateData	= data.getClone(name);
			pi.gameHasChanged(privateData);
		}
	}
	
//	@Override
//	public void hostStartGame(String playerName) throws RemoteException, ExceptionTooFewPlayers, ExceptionTooManyPlayers, ExceptionOnlyHostCanStartGame {
//		Set<String> dataPlayers = data.getPlayerNameList();
//		int nbOfPlayers = dataPlayers.size();
//		
//		data.randomizePlayerOrder();
//		for(int i = 0; i < Data.initialHandSize; i++)
//		{
//			for(String player : data.getPlayerOrder())
//			{
//				dealATileTo(player);
//			}
//		}
//	}
//
//	private void dealATileTo(String player) throws RemoteException {
//		Tile t = data.getDeck().drawTile();
//
//		PlayerInfo dataPlayer = data.getPlayerInfo(player);
//		dataPlayer.hand.addTile(t);
//		
//		PlayerInterface remotePlayer = data.getPlayer(player);
//		remotePlayer.dealTile(t);
//	}

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
}