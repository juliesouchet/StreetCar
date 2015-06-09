package main.java.game;

import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.player.PlayerInterface;






public class EngineChat implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Thread						engineThread;
	private Object						engineLock;
	private LinkedList<EngineMessage>	actionList;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public EngineChat()
	{
		this.actionList		= new LinkedList<EngineMessage>();
		this.engineLock		= new Object();
		this.engineThread	= new Thread(this);
		this.engineThread	.start();
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public synchronized void addAction(Data data, String playerName, String message)
	{
		this.actionList.add(new EngineMessage(data, playerName, message));
		synchronized(this.engineLock)
		{
			try					{this.engineLock.notify();}
			catch(Exception e)	{e.printStackTrace(); System.exit(0);}
		}
	}

	public void run()
	{
		LinkedList<EngineMessage> actionList;

		while(true)
		{
			synchronized (this.engineLock)													// Wait for a new action
			{
				while (this.actionList.isEmpty())
				{
					try								{engineLock.wait();}
					catch (InterruptedException e)	{e.printStackTrace();}
				}
				actionList = new LinkedList<EngineMessage> (this.actionList);
				this.actionList.clear();
			}
			for (EngineMessage em:actionList)
			{
				try							{this.sendMessage(em);}
				catch (RemoteException e)	{e.printStackTrace(); return;}
			}
		}
	}
	private synchronized void sendMessage(EngineMessage em) throws RemoteException
	{
		PlayerInterface pi;

		for (String playerName: em.data.getPlayerNameList())
		{
			pi = em.data.getRemotePlayer(playerName);
			pi.newChatMessage(em.playerName, em.message);
		}
	}

// --------------------------------------------
// Private class:
// --------------------------------------------
	class EngineMessage
	{
		// Attributes
		public Data		data;
		public String	playerName;
		public String	message;

		// Builder
		public EngineMessage(Data data, String playerName, String message)
		{
			this.data		= data;
			this.playerName = playerName;
			this.message	= message;
		}
	}
}