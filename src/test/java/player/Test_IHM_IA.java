package test.java.player;

import java.util.Random;

import main.java.data.Data;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.player.PlayerAI;
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
		String iaName = "IA_DUMB_" + ((new Random()).nextInt(7));
		PlayerIHM playerIHM = null;
//		PlayerAI playerAI = null;
		GameInterface game;

		try
		{
			playerIHM	= PlayerIHM.launchPlayer("riyane", "jeux", "newOrleans", 3,  true, null, this);
			game		= Game.getRemoteGame("127.0.0.1", "jeux");
			new PlayerAI(iaName, false, game, 1, null);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame(playerIHM);
		this.frame.setGameData(playerIHM.getGameData());
		this.frame.setVisible(true);
		new ControlFrame(playerIHM);

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
		if (this.frame != null) this.frame.setGameData(data);
	}
	public void excludePlayer()
	{
		System.out.println("------------------------------------");
		System.out.println("excludePlayer");
		System.out.println("Not managed yet");
	}

	@Override
	public void refreshMessages(String playerName, String message) {
		
	}
}
