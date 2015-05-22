package test.java.player;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Scanner;

import main.java.player.PlayerIHM;






public class TestIHM 
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	
// --------------------------------------------
// Builder:
// --------------------------------------------
	public TestIHM(PlayerIHM player)
	{
		Scanner sc = new Scanner(System.in);
		String str, name, gameName, ip;
		boolean create;

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
		System.out.print("\n\n");
		System.out.print("\t- Player name\t\t :");						name		= sc.next();
		System.out.print("\t- Game name\t\t :");						gameName	= sc.next();
		Color color = askColor(sc);
		if(!create){System.out.print("\t- IP of the application\t:");	ip			= sc.next();}
		else															ip			= null;
String boardName = "newOrleans";	/////// Nom par defaut
int nbrBuildingInLine= 3;	/////// Nom par defaut


		try					{player.launchPlayer(name, gameName, boardName, nbrBuildingInLine,  color, create, ip);}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		DataViewerFrame frame = new DataViewerFrame();
		try						{frame.setGameData(player.getGameData());}
		catch(RemoteException e){e.printStackTrace(); System.exit(0);}
		frame.setVisible(true);

		sc.close();
	}

	private Color askColor(Scanner sc)
	{
		String color;
		while(true)
		{
			System.out.print("\t- Color\t\t\t :"); color = sc.next();
			if		(color.equals("red"))	return Color.red;
			else if	(color.equals("green"))	return Color.green;
			else if	(color.equals("blue"))	return Color.blue;
			else if	(color.equals("cyan"))	return Color.cyan;
			else if	(color.equals("gray"))	return Color.gray;
		}
	}

// --------------------------------------------
// Local methodes:
// --------------------------------------------
}
