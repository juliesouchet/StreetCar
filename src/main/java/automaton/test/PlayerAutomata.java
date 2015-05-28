package main.java.automaton.test;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.GameInterface;
import main.java.player.PlayerIA;
import main.java.player.PlayerIHM;
import main.java.rubbish.InterfaceIHM;
import test.java.player.DataViewerFrame;






public class PlayerAutomata implements InterfaceIHM
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	private DataViewerFrame frame;

// --------------------------------------------
// Builder:
// --------------------------------------------
	public PlayerAutomata()
	{
		Scanner sc = new Scanner(System.in);
		String str, name, gameName, ip, iaName;
		boolean create;
		PlayerIHM playerIHM = null;
		PlayerIA playerIA = null;
		GameInterface game = null;

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

		try
		{
			playerIHM	= PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine,  color, create, ip, this);
			game		= playerIHM.getGame();
			iaName		= "IA_DUMB_" + ((new Random()).nextDouble());
			playerIA	= new PlayerIA(iaName, Color.BLACK, game, 0, null);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		try
		{
			this.frame = new DataViewerFrame(game, name);
			this.frame.setGameData(playerIHM.getGameData());
		}
		catch(RemoteException e){e.printStackTrace(); System.exit(0);}
		frame.setVisible(true);

		if (create)
		{
			str = "";
			while (!str.equals("start"))
			{
				System.out.print("\n\n\tEnter \"start\" to start the game: ");
				str = sc.next();
			}
			try
			{
				playerIHM.hostStartGame();
			}
			catch (Exception e)	{e.printStackTrace();}
		}
		sc.close();
	}

	/** Autre constructeur, l'argument sert juste a surcharger le constructeur avec des valeurs par default
	*	Si i=0 => C joueurA jeu red
	*	si i=1 => J joueurB jeu blue 127.0.0.1
	*
	*/
	public PlayerAutomata(int i)
	{
		PlayerIHM player = null;
		String name, gameName, ip;
		boolean create, win = false;
		Color color;
		
		if ( i== 0)	{
			create = true; name = "joueurA"; gameName = "jeu"; color = Color.red; ip = null;
		} else { //if ( i== 1) {
			create = false; name = "joueurB"; gameName = "jeu"; color = Color.blue; ip = "127.0.0.1";
		}


		String boardName = "newOrleans";	/////// Nom par defaut
		int nbrBuildingInLine= 3;	/////// Nom par defaut


		try{
			player = PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine, color, create, ip, this);
			}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame();
		try	{this.frame.setGameData(player.getGameData());}
		catch(RemoteException e){e.printStackTrace(); System.exit(0);}
		frame.setVisible(true);

		if (create)
		{
            PlayerAutomaton edouard = new Dumbest();
			try	{
				player.hostStartGame();
				for (int j=0; j<100; j++){
					Action choix_de_edouard = edouard.makeChoice(player.getGameData());
					player.getGame().placeTile(name, choix_de_edouard.tile1 ,choix_de_edouard.positionTile1);
					player.getGameData().getHand(name).add(player.getGame().drawCard(name, 1));
					this.frame.setGameData(player.getGameData());
					if(player.getGameData().isTrackCompleted(name) && !win) {
						System.out.println("Chemin completé (tour " + j + ")");
						win = true;
						break;
					}
				}
			}catch (Exception e)	{e.printStackTrace();}
			
			LinkedList<Point> objectifs = null;
			try {
				objectifs = player.getGameData().getTerminus(name);
				objectifs.addAll(player.getGameData().getBuildings(name));
			} catch (RemoteException e) {e.printStackTrace();}
			System.out.println("Objectifs : " + objectifs);
			if(!win) System.out.println("Chemin non complété");
		}
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

// --------------------------------------------
// Private Local methods:
// --------------------------------------------
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

	@Override
	public void excludePlayer() {
		// TODO Auto-generated method stub
		
	}
}
