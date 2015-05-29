package test.java.player;

import java.awt.Color;
import java.util.Random;

import main.java.data.Data;
import main.java.game.Game;
import main.java.player.PlayerIA;
import main.java.rubbish.InterfaceIHM;




public class Test_IA_Riyane implements InterfaceIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private DataViewerFrame frame;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Test_IA_Riyane()
	{
		PlayerIA playerIA	= null;
		Game	game		= null;
		String	ip			= "127.0.0.1";
		String	gameName	= "jeux";
		String 	iaName		= "IA_DUMB_" + ((new Random()).nextInt());

		try
		{
			game		= new Game(gameName, ip, "newOrleans", 3);
			Thread t	= new Thread(game);
			t.start();
			playerIA	= new PlayerIA(iaName, Color.BLACK, game, 0, this);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer

		this.frame = new DataViewerFrame(playerIA);
		this.frame.setGameData(playerIA.getGameData());
		frame.setVisible(true);

		try
		{
			playerIA.hostStartGame();
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
