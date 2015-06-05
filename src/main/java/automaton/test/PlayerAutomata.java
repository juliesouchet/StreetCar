package main.java.automaton.test;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionTable;
import main.java.automaton.Dumbest;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionGameIsOver;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionPlayerIsBlocked;
import main.java.game.ExceptionTooManyActions;
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

	PlayerAutomaton edouard ;

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
		TraceDebugAutomate.decisionTableTrace=true;


		if ( i== 0)	{
			create = true; name = "joueurA"; gameName = "jeu"; color = Color.red; ip = null;
		} else { //if ( i== 1) {
			create = false; name = "joueurB"; gameName = "jeu"; color = Color.blue; ip = "127.0.0.1";
		}


		String boardName = "newOrleans";	/////// Nom par defaut
		int nbrBuildingInLine= 3;	/////// Nom par defaut


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
		DecisionTable monNoeudDedecision = null;
		int nbActionsPossibles = 0;
		Action myAction = null;
		
		//=======================================
		TraceDebugAutomate.debugDecisionTableTrace("Refresh called\n");

		if (!firstRefreshDone){
			firstRefreshDone = true;
			return;
		}

		try {
			nbActionsPossibles = data.getPossibleActions(data.getPlayerTurn(), new DecisionNode(1, 0, "root").getPossibleFollowingActionTable(),false);
		} catch (ExceptionUnknownNodeType e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("nbActionPossibles="+nbActionsPossibles);
		int profondeurExplorable = 150000/nbActionsPossibles;
		System.out.println("Profondeur explorable="+profondeurExplorable);
		try {
			monNoeudDedecision = new DecisionTable(nbActionsPossibles, profondeurExplorable, "joueurA");
		} catch (ExceptionUnknownNodeType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(profondeurExplorable==0){
			edouard = new Dumbest("joueurA");
			myAction = edouard.makeChoice(data);
			try {
				player.doAction("joueurA", myAction);
			} catch (RemoteException | ExceptionGameHasNotStarted
					| ExceptionNotYourTurn | ExceptionForbiddenAction
					| ExceptionTooManyActions | ExceptionPlayerIsBlocked
					| ExceptionGameIsOver e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAction = edouard.makeChoice(data);
			try {
				player.doAction("joueurA", myAction);
			} catch (RemoteException | ExceptionGameHasNotStarted
					| ExceptionNotYourTurn | ExceptionForbiddenAction
					| ExceptionTooManyActions | ExceptionPlayerIsBlocked
					| ExceptionGameIsOver e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		

		}
		
//		try {
//			player.drawTile(2);
//		} catch (RemoteException | ExceptionGameHasNotStarted
//				| ExceptionNotYourTurn | ExceptionNotEnoughTilesInDeck
//				| ExceptionTwoManyTilesToDraw | ExceptionForbiddenAction e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("pop");

		
		this.frame.setGameData(data);
		System.out.println("game setted");
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
