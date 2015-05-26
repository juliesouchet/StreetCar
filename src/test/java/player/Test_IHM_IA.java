package test.java.player;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Random;

import main.java.data.Data;
import main.java.game.GameInterface;
import main.java.player.PlayerIA;
import main.java.player.PlayerIHM;
import main.java.rubbish.InterfaceIHM;






public class Test_IHM_IA implements InterfaceIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private DataViewerFrame frame;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Test_IHM_IA()
	{
		String iaName = "IA_DUMB_" + ((new Random()).nextInt());
		PlayerIHM playerIHM = null;
		GameInterface game = null;

		try
		{
			playerIHM	= PlayerIHM.launchPlayer("riyane", "jeux", "newOrleans", 3,  Color.red, true, null, this);
			game		= playerIHM.getGame();
			new PlayerIA(iaName, Color.BLACK, game, 0, null);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		try
		{
			this.frame = new DataViewerFrame(game, "riyane");
			this.frame.setGameData(playerIHM.getGameData());
		}
		catch(RemoteException e){e.printStackTrace(); System.exit(0);}
		frame.setVisible(true);

		try
		{
			playerIHM.hostStartGame();
		}
		catch (Exception e)	{e.printStackTrace();}
	}

// --------------------------------------------
// Local methods:
// --------------------------------------------
	public void refresh(Data data)
	{
		System.out.println("------------------------------------");
		System.out.println("Refresh");
		System.out.println("\t Host\t: "	+ data.getHost());
		System.out.println("\t Round\t: "	+ data.getRound());
		this.frame.setGameData(data);
	}
}