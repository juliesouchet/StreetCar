package test.java.gui;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotEnoughTilesInHand;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTooManyActions;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.gui.board.MapPanel;
import main.java.player.PlayerInterface;

public class GuiTester {
	
	static HashMap<String, String> commands;
	
	class ConsolePlayer implements PlayerInterface
	{

		@Override
		public Data getGameData() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPlayerName() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Color getPlayerColor() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isHumanPlayer() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public LoginInfo[] getLoginInfo() throws RemoteException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLoginInfo(int playerToChangeIndex,
				LoginInfo newPlayerInfo) throws RemoteException,
				ExceptionForbiddenAction, ExceptionForbiddenHostModification {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setPlayerColor(Color playerColor) throws RemoteException,
				ExceptionUsedPlayerColor {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onQuitGame(String playerName) throws RemoteException,
				ExceptionForbiddenAction {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void hostStartGame() throws RemoteException,
				ExceptionForbiddenAction, ExceptionNotEnoughPlayers {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void excludePlayer() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void gameHasChanged(Data data) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void placeTile(Tile t, Point position) throws RemoteException,
				ExceptionGameHasNotStarted, ExceptionNotYourTurn,
				ExceptionForbiddenAction, ExceptionTooManyActions {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void replaceTwoTiles(Tile t1, Tile t2, Point p1, Point p2)
				throws RemoteException, ExceptionGameHasNotStarted,
				ExceptionNotYourTurn {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void validate() throws RemoteException,
				ExceptionGameHasNotStarted, ExceptionNotYourTurn,
				ExceptionForbiddenAction {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startMaidenTravel(String playerName, Point terminus)
				throws RemoteException, ExceptionNotYourTurn,
				ExceptionForbiddenAction, ExceptionGameHasNotStarted {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveTram(LinkedList<Point> tramMovement)
				throws RemoteException, ExceptionNotYourTurn,
				ExceptionForbiddenAction, ExceptionGameHasNotStarted {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawTile(int nbrCards) throws RemoteException,
				ExceptionGameHasNotStarted, ExceptionNotYourTurn,
				ExceptionNotEnoughTilesInDeck, ExceptionTwoManyTilesToDraw {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pickTileFromPlayer(String chosenPlayer, Tile tile)
				throws RemoteException, ExceptionGameHasNotStarted,
				ExceptionNotYourTurn, ExceptionTwoManyTilesToDraw,
				ExceptionForbiddenAction, ExceptionNotEnoughTilesInHand {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static void main(String[] args) {
		buildCommandMap();
		Data d = null;
		try { d = new Data("pokpokpok", "newOrleans", 2); } 
		catch (ExceptionUnknownBoardName | RuntimeException e) { e.printStackTrace(); }
		
		System.out.println("pokpokpok");
		MapPanel p = new MapPanel();
		p.setData(d);
		d.addPlayer(new GuiTester().new ConsolePlayer(), "Bob", true, true);
		d.addPlayer(new GuiTester().new ConsolePlayer(), "Fred", false, true);
		d.addPlayer(new GuiTester().new ConsolePlayer(), "George", false, true);
		JFrame frame = new JFrame();
		frame.add(p);
		frame.setSize(700, 700);
		frame.setVisible(true);
		
		p.startMaidenVoyage(new Point(2, 0));
		LinkedList<Point> tramPath = new LinkedList<Point>();
		tramPath.add(new Point(2, 1));
		tramPath.add(new Point(3, 1));
		tramPath.add(new Point(3, 2));
		tramPath.add(new Point(3, 3));
		tramPath.add(new Point(4, 3));
		p.moveTram(tramPath);
		
	}
	
	private static void executeLine(String commandLine)
	{
		String[] tab = commandLine.split("[ ]+");
		LinkedList<String> splitLine= (LinkedList<String>) Arrays.asList(tab);
		String command = splitLine.removeFirst();
		if(!commands.containsKey(command))
		{
			System.out.println(command + " is not a valid command");
		}
	}

	private static void printHelp()
	{
		for(String command : commands.keySet())
		{
			System.out.println(command + " : " + commands.get(command));
		}
	}

	private static void buildCommandMap() {
		commands.put("quit", "exits the application");
		commands.put("help", "prints commands and use of them, type \"help <command>\" for more info on a command");
		commands.put("tile", "creates a tile");
	}
}
