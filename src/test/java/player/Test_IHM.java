package test.java.player;

import java.util.Scanner;

import main.java.data.Data;
import main.java.player.PlayerIHM;
import main.java.rubbish.InterfaceIHM;





public class Test_IHM implements InterfaceIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private DataViewerFrame frame;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public Test_IHM()
	{
		Scanner sc = new Scanner(System.in);
		String boardName;
		int nbrBuildingInLine;
		String str, name, gameName, ip;
		boolean create = true;
		PlayerIHM playerIHM = null;

		while (true)
		{
			System.out.println("Please enter:");
			System.out.println("\t- \"J\" to join an existing game");
			System.out.println("\t- \"C\" to create a new game");
			System.out.print("\tchoice : ");str = sc.nextLine();
			if (str.equals("J"))	{create = false;	break;}
			if (str.equals("C"))	{create = true;		break;}
			System.out.println("Unhandeled choice");
		}


if (!create)
{
		System.out.print("\n\n");
		System.out.print("\t- Player name\t\t :");						name		= sc.next();
		System.out.print("\t- Game name\t\t :");						gameName	= sc.next();
		if(!create){System.out.print("\t- IP of the application\t:");	ip			= sc.next();}
		else															ip			= null;
		boardName = "newOrleans";	/////// Nom par defaut
		nbrBuildingInLine= 3;			/////// Nom par defaut
}
else
{
name = "Riyane";
gameName = "jeux";
ip = null;
boardName = "newOrleans";	/////// Nom par defaut
nbrBuildingInLine= 3;			/////// Nom par defaut
}
		try
		{
			playerIHM	= PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine,  create, ip, this);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame(playerIHM);
		this.frame.setGameData(playerIHM.getGameData());
		this.frame.setVisible(true);
		new ControlFrame(playerIHM);

		if (create)
		{
			str = "";
/*			while (!str.equals("start"))
			{
				System.out.print("\n\n\tEnter \"start\" to start the game: ");
				str = sc.next();
			}
*/
			try
			{
				playerIHM.hostStartGame();
			}
			catch (Exception e)	{e.printStackTrace();}
		}
		sc.close();
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
		if (data.isGameStarted())		System.out.println("\t Turn\t: "	+ data.getPlayerTurn());
		if (this.frame != null) this.frame.setGameData(data);
	}
	public void excludePlayer()
	{
		System.out.println("------------------------------------");
		System.out.println("excludePlayer");
		System.out.println("Not managed yet");
	}

// --------------------------------------------
// Private Local methods:
// --------------------------------------------

	@Override
	public void refreshMessages(String playerName, String message) {
		// TODO Auto-generated method stub
		
	}
}