package main.java.automaton.test;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import main.java.automaton.AutomatePlusCourtChemin;
import main.java.automaton.DecisionTable;
import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.player.PlayerAI;
import main.java.player.PlayerIHM;
import main.java.rubbish.InterfaceIHM;
import main.java.util.TraceDebugAutomate;
import test.java.player.DataViewerFrame;






public class PlayerAutomata implements InterfaceIHM
{
	// --------------------------------------------
	// Attributs:
	// --------------------------------------------
	private DataViewerFrame frame;


	private PlayerIHM player = null;
	private String name;
	private int i = 0;
	private int nbCoups = 100;

	private boolean firstRefreshDone = false;
	private boolean secondRefreshDone = false;

	AutomatePlusCourtChemin myAutomaton ;

	// --------------------------------------------
	// Builder:
	// --------------------------------------------
	public PlayerAutomata()
	{
		Scanner sc = new Scanner(System.in);
		String str, name, gameName, ip, iaName;
		boolean create;
		PlayerIHM playerIHM = null;
		PlayerAI playerIA = null;
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
		int nbrBuildingInLine= 2;	/////// Nom par defaut

		try
		{
			playerIHM	= PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine,  create, ip, this);
			playerIHM	.setPlayerColor(color);
			game		= Game.getRemoteGame("127.0.0.1", gameName);
			iaName		= "IA_DUMB_" + ((new Random()).nextDouble());
			playerIA	= new PlayerAI(iaName, false, game, 0, null);
			playerIA	.setPlayerColor(Color.blue);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame(playerIA);
		this.frame.setGameData(playerIHM.getGameData());
		this.frame.setVisible(true);

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
		String gameName, ip;
		boolean create;
		Color color;
		TraceDebugAutomate.decisionTableTrace=true;


		if ( i== 0)	{
			create = true; name = "joueurA"; gameName = "jeu"; color = Color.red; ip = null;
		} else { //if ( i== 1) {
			create = false; name = "joueurB"; gameName = "jeu"; color = Color.blue; ip = "127.0.0.1";
		}


		String boardName = "newOrleans5";	/////// Nom par defaut
		int nbrBuildingInLine= 2;	/////// Nom par defaut


		try{
			player = PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine, create, ip, this);
			//player.setPlayerColor(color);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame();
		this.frame.setGameData(player.getGameData());
		this.frame.setVisible(true);

		if (create)
		{

			try	{
				//=======================================
				TraceDebugAutomate.debugDecisionTableTrace("Lancement de la partie\n");
				player.hostStartGame();
				//=======================================
				TraceDebugAutomate.debugDecisionTableTrace("Partie lancée\n");
			}catch (Exception e)	{e.printStackTrace();}



		}
	}


	// --------------------------------------------
	// Local methods:
	// --------------------------------------------
	public void refresh(Data data)
	{
		DecisionTable maTableDeDecision = null;
		int nbActionsPossibles = 0;
		Action myAction = null;
		
		//=======================================
		TraceDebugAutomate.debugDecisionTableTrace("Refresh called\n");

		if (!firstRefreshDone || !data.isPlayerTurn(name)){
			//=======================================
			TraceDebugAutomate.debugDecisionTableTrace("It's not my turn.\n");
			firstRefreshDone = true;
			return;
		}
		
		ArrayList<Point> mesTerminus = new ArrayList<Point>(5);
		for(int i=0; i<data.getPlayerTerminusPosition(name).length;i++){
			mesTerminus.add(data.getPlayerTerminusPosition(name)[i]);
		}
		
		ArrayList<Point> mesStops = new ArrayList<Point>(5);
		for(int i=0; i<data.getPlayerAimBuildings(name).length;i++){
			mesTerminus.add(data.getPlayerAimBuildings(name)[i]);
		}
		
		
	myAutomaton = new AutomatePlusCourtChemin(data, mesTerminus, mesStops);

	System.out.println(data.getPlayerTerminusPosition(name)[0]);
	System.out.println(data.getPlayerTerminusPosition(name)[1]);
	System.out.println(data.getPlayerTerminusPosition(name)[2]);
	System.out.println(data.getPlayerTerminusPosition(name)[3]);

	System.out.println(data.getPlayerAimBuildings(name)[0]);
	System.out.println(data.getPlayerAimBuildings(name)[1]);
	this.frame.setGameData(data);

	Tile maTile1 = Tile.parseTile("Tile_FFFFZZ2113");
	Tile maTile2 = Tile.parseTile("Tile_FFFFZZ2113");
	Tile maTile3 = Tile.parseTile("Tile_FFFFZZ2113");
	Tile maTile4 = Tile.parseTile("Tile_FFFFZZ2113");
	
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);

	System.out.println("\n\t(1)==============================================\n");


		data.setTile(3, 1, maTile1);
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);
	//System.out.println(myAutomaton);
	System.out.println("\n\t(2)===============================================\n");

	data.setTile(3, 2, maTile2);
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);
	//System.out.println(myAutomaton);
	System.out.println("\n\t(3)===============================================\n");

	data.setTile(3,3, maTile3);
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);
	//System.out.println(myAutomaton);
	System.out.println("\n\t(4)===============================================\n");

	data.setTile(3,4, maTile4);
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);
	//System.out.println(myAutomaton);
	System.out.println("\n\t(5)===============================================\n");

	data.setTile(3,5, maTile3);
	myAutomaton.computeHeuristic();
	myAutomaton.printMatrice(myAutomaton.heuristic);
	System.out.println("Best terminus="+myAutomaton.getBestTerminus());
	
	System.out.println("\n\t(6)===============================================\n");/**/
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

	@Override
	public void refreshMessages(String playerName, String message) {
		// TODO Auto-generated method stub
		
	}
}