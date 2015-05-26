package test.java.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Hand;
import main.java.data.Tile;
import main.java.game.Engine;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.player.PlayerAbstract;

import org.junit.Test;

public class GameTest {

	@SuppressWarnings("serial")
	class ValzTestPlayer extends PlayerAbstract
	{
		Hand hand;
		Engine engine;
		private int line;
		private String[] route;

		public ValzTestPlayer(boolean isHost, String playerName, Color playerColor, Engine engine) 
				throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor 
		{
			super(isHost, playerName, playerColor, createBasicGame());
			hand = new Hand(Data.getInitialHandConfiguration());
			this.engine = engine;
		}

		@Override
		public void dealTile(Tile t) throws RemoteException {
			hand.addTile(t);
		}

		@Override
		public void gameHasChanged(Data data) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isHumanPlayer() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void dealLineCard(int cardNumber) throws RemoteException {
			line = cardNumber;
		}

		@Override
		public void dealRouteCard(String[] route) throws RemoteException {
			this.route = route;
		}

		@Override
		public void tileHasBeenPlaced(String playerName, Tile t, Point position)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void exchangeTile(String playerName, Tile t, Point p)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void receiveTileFromPlayer(String chosenPlayer, Tile t)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void placeStop(Point p) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void revealLine(String playerName) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void revealRoute(String playerName) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		public Tile getHandTile(int index) {
			return hand.get(index);
		}

		public int getLine() {
			return line;
		}

		public String[] getRoute() {
			return route;
		}
		
	}

	//	@Test
	//	public void gameAndPlayerCreationTest()
	//	{
	//		Game game = null;
	//		try { game = new Game(); } 
	//		catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e1) { e1.printStackTrace(); }
	//		assertNotNull(game);
	//
	//		try {
	//			new ValzTestPlayer(true, "host", Color.red, game);
	//			new ValzTestPlayer(false, "guest2", Color.blue, game);
	//			new ValzTestPlayer(false, "guest3", Color.orange, game);
	//			new ValzTestPlayer(false, "guest4", Color.green, game);
	//		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName
	//				| ExceptionUsedPlayerColor e) {
	//			e.printStackTrace();
	//		}
	//
	//		try {
	//			assertEquals(game.getData("host").getNbrPlayer(), 4);
	//		} catch (RemoteException e) {
	//			e.printStackTrace();
	//		}
	//	}

	@Test
	public void gameAndPlayerCreationTest()
	{
		Data data = null;
		try { data = new Data("StreetCar", "newOrleans", 3); } 
		catch (Exception e) { e.printStackTrace(); }
		assertNotNull(data);
		Engine engine = new Engine(null, data);
		assertNotNull(engine);

		fillWithPlayers(engine, new HashMap<String, GameTest.ValzTestPlayer>());

		assertEquals(engine.getData().getNbrPlayer(), 4);
	}

	@Test
	public void hostStartGameTest() {
		Engine engine = createBasicEngine();
		Data data = engine.getData();

		HashMap<String, ValzTestPlayer> guestPlayers = new HashMap<String, ValzTestPlayer>();
		fillWithPlayers(engine, guestPlayers);

		try { engine.hostStartGame(); } 
		catch (RemoteException e) { e.printStackTrace(); }

		String[] playerOrder = data.getPlayerOrder();
		assertNotNull(playerOrder);
		assertEquals(playerOrder.length, 4);


		for(String playerName : playerOrder)
		{
			LinkedList<Tile> playerHand = data.getPlayerInfo(playerName).hand.getTiles();
			assertEquals(playerHand.size(), Data.initialHandSize);

			System.out.println(playerName + "'s hand :");
			for(int i = 0; i < playerHand.size(); i++)
			{
				Tile t = playerHand.get(i);
				System.out.println(t.toString());
				assertEquals(t.toString(), ((ValzTestPlayer)guestPlayers.get(playerName)).getHandTile(i).toString());
			}
			System.out.println("");
		}
		
		for(String player : playerOrder)
		{
			int line = data.getPlayerInfo(player).line;
			String[] buildingNames = data.getPlayerInfo(player).buildingInLine_name;
			
			ValzTestPlayer guestPlayer = guestPlayers.get(player);
			assertEquals(line, guestPlayer.getLine());
			if(!Arrays.equals(buildingNames, guestPlayer.getRoute())) fail(" difference between player and data");
			
			System.out.println(player + "'s line : " + line);
			System.out.println(player + "'s route : " + Arrays.toString(buildingNames));
			System.out.println("");
		}
		
		
		//TODO test if each player has same hand as game
	}

	private void fillWithPlayers(Engine engine, HashMap<String, ValzTestPlayer> players) {
		try {
			players.put("host", new ValzTestPlayer(true, "host", Color.red, engine));
			players.put("guest2", new ValzTestPlayer(false, "guest2", Color.blue, engine));
			players.put("guest3", new ValzTestPlayer(false, "guest3", Color.orange, engine));
			players.put("guest4", new ValzTestPlayer(false, "guest4", Color.green, engine));
			
			for(ValzTestPlayer player : players.values())
			{
				boolean isHost = player.getPlayerName().equals("host");
				engine.addPlayer(player, isHost);
			}
		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName | ExceptionUsedPlayerColor e) { e.printStackTrace(); }
	}

	private Game createBasicGame() {
		Game game = null;
		try { game = new Game(); } 
		catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) { e.printStackTrace(); }
		assertNotNull(game);

		return game;
	}

	private Engine createBasicEngine()
	{
		Data data = null;
		try { data = new Data("StreetCar", "newOrleans", 3); } 
		catch (Exception e) { e.printStackTrace(); }
		assertNotNull(data);

		Engine engine = new Engine(null, data);
		assertNotNull(engine);
		return engine;
	}
}
