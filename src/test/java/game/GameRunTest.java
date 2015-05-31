package test.java.game;

import main.java.data.Data;



/**============================================================
 * Remote Application 
 * URL: rmi://ip:port/gameName
 * @author kassuskley
 ==============================================================*/



public class GameRunTest implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private Data	data;

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void run()
	{
		Object lock = new Object();

		synchronized (lock)
		{
			try
			{
				lock.wait(5000);
				String playerName = null;
				for (String str: this.data.getPlayerNameList()) playerName = str;
				System.out.println("yeuve: " + playerName);
				System.out.println("nuebo: " + this.data.getRemotePlayer(playerName).getPlayerName());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}