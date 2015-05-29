package test.java.player;

import java.awt.Color;
import java.util.Random;

import main.java.data.Data;
import main.java.game.Game;
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
		GameInterface game;

		try
		{
			playerIHM	= PlayerIHM.launchPlayer("riyane", "jeux", "newOrleans", 3,  Color.red, true, null, this);
			game		= Game.getRemoteGame("127.0.0.1", "jeux");
			new PlayerIA(iaName, Color.BLACK, game, 0, null);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame(playerIHM);
		this.frame.setGameData(playerIHM.getGameData());
		this.frame.setVisible(true);

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
		if (this.frame != null) this.frame.setGameData(data);
	}
	public void excludePlayer()
	{
		System.out.println("------------------------------------");
		System.out.println("excludePlayer");
		System.out.println("Not managed yet");
	}
}
