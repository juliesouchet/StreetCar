package main.java.automaton.test;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

import main.java.automaton.Dumbest;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionEndGame;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotEnougthTileInDeck;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTooManyActions;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.player.PlayerAI;
import main.java.player.PlayerIHM;
import main.java.rubbish.InterfaceIHM;
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
		int nbrBuildingInLine= 3;	/////// Nom par defaut

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

		if ( i== 0)	{
			create = true; name = "joueurA"; gameName = "jeu"; color = Color.red; ip = null;
		} else { //if ( i== 1) {
			create = false; name = "joueurB"; gameName = "jeu"; color = Color.blue; ip = "127.0.0.1";
		}


		String boardName = "newOrleans";	/////// Nom par defaut
		int nbrBuildingInLine= 3;	/////// Nom par defaut


		try{
			player = PlayerIHM.launchPlayer(name, gameName, boardName, nbrBuildingInLine, create, ip, this);
			player.setPlayerColor(color);
		}
		catch (Exception e)	{e.printStackTrace(); System.exit(0);}

		// Game data viewer
		this.frame = new DataViewerFrame();
		this.frame.setGameData(player.getGameData());
		this.frame.setVisible(true);

		if (create)
		{

			try	{
				player.hostStartGame();
			}catch (Exception e)	{e.printStackTrace();}

			
			
			//player.getGameData().drawCard(name,1);
			//refresh(player.getGameData());
			//System.out.println("============================\n");

		}
	}	


	// --------------------------------------------
	// Local methods:
	// --------------------------------------------
	public void refresh(Data data)
	{
		PlayerAutomaton edouard = new Dumbest(name);
		edouard.setName(name);
//		boolean win = false;
		/*System.out.println("------------------------------------");
		System.out.println("Refresh");
		System.out.println("\t Host\t: "	+ data.getHost());
		System.out.println("\t Round\t: "	+ data.getRound());*/
		if (this.frame!=null && (i <= nbCoups)){
			i++;
			if(player.getGameData().isTrackCompleted(name)) {
//				System.out.println("Chemin completé (tour " + i + ")");
//				win = true;
				this.frame.setGameData(data);
			}else{
				Point[] objectifsTerminus = null;
				Point[] objectifsArrets = null;
				objectifsTerminus = player.getGameData().getPlayerTerminusPosition(name);
				objectifsArrets=player.getGameData().getPlayerAimBuildings(name);
//				System.out.println("Objectifs : " + objectifs);
//				if(!win) System.out.println("Chemin non complété");
				
//				System.out.println(" TOUR " + (i+1));
// TODO: --Riyane:
// g pas voulu coriger l'erreur
// c une modif pour ne plus faire de new dans votre automate (je c que c pas claire, on en reparle)
//				System.out.println("Main :" + player.getGameData().getHand(name));
//				System.out.println();
				Action choix_de_edouard = null;
				try {
					choix_de_edouard = edouard.makeChoice(player.getGameData());
				} catch (ExceptionUnknownNodeType e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					player.placeTile(choix_de_edouard.tile1 ,choix_de_edouard.positionTile1);
					player.drawTile(1);
					if(i%2 == 0)	player.validate();
				} catch (RemoteException | ExceptionGameHasNotStarted
						| ExceptionNotYourTurn | ExceptionForbiddenAction
						| ExceptionTooManyActions | ExceptionNotEnougthTileInDeck | ExceptionTwoManyTilesToDraw e) {
					e.printStackTrace();
				} catch (ExceptionEndGame e) {
					//e.printStackTrace();
					System.out.println(e.getWinner() + " wins !!");
				}
			
	
				this.frame.setGameData(data);
			}
		}
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
