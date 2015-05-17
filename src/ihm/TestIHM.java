package ihm;

import java.util.Scanner;
import player.Main;




public class TestIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------

// --------------------------------------------
// Builder:
// --------------------------------------------
	public TestIHM(Main m)
	{
		Scanner sc = new Scanner(System.in);
		String str, name, gameName, ip;
		boolean create;

		while (true)
		{
			System.out.println("Please enter:");
			System.out.println("\t- \"J\" to join an existing game");
			System.out.println("\t- \"C\" to create a new game");
			System.out.print("\tchoice: ");	str = sc.nextLine();
			if (str.equals("J"))	{create = false;	break;}
			if (str.equals("C"))	{create = true;		break;}
			System.out.println("Unhandeled choice");
		}
		System.out.print("\n\n");
		System.out.print("\t- Player name\t\t:");						name		= sc.next();
		System.out.print("\t- Game name\t\t:");							gameName	= sc.next();
		if(!create){System.out.print("\t- IP of the application\t:");	ip			= sc.next();}
		else															ip			= null;

		try					{m.newGame(name, gameName, create, ip);}
		catch (Exception e)	{e.printStackTrace();}
		sc.close();
	}

// --------------------------------------------
// Local methodes:
// --------------------------------------------
}
