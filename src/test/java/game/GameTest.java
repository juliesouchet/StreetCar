package test.java.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Hand;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.player.PlayerAbstract;

import org.junit.Test;

public class GameTest {

	@SuppressWarnings("serial")
	class ValzTestPlayer extends PlayerAbstract
	{
		Hand hand;

		public ValzTestPlayer(boolean isHost, String playerName, Color playerColor, GameInterface game) 
				throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor 
		{
			super(isHost, playerName, playerColor, game);
			hand = new Hand();
		}

		@Override
		public void dealTile(Tile t) throws RemoteException {
			hand.addTile(t);
		}

		@Override
		public void gameHasChanged(Data data) throws RemoteException {
			// TODO Auto-generated method stub

		}
	}

	@Test
	public void gameAndPlayerCreationTest()
	{
		Game game = null;
		try { game = new Game(); } 
		catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e1) { e1.printStackTrace(); }
		assertNotNull(game);

		try {
			new ValzTestPlayer(true, "host", Color.red, game);
			new ValzTestPlayer(false, "guest2", Color.blue, game);
			new ValzTestPlayer(false, "guest3", Color.orange, game);
			new ValzTestPlayer(false, "guest4", Color.green, game);
		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName
				| ExceptionUsedPlayerColor e) {
			e.printStackTrace();
		}

		try {
			assertEquals(game.getData("host").getNbrPlayer(), 4);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void hostStartGameTest() {
		Game game = createBasicGame();
		Data data = game.getData();
		assertNotNull(data);

		try {
			new ValzTestPlayer(true, "host", Color.red, game);
			new ValzTestPlayer(false, "guest2", Color.blue, game);
			new ValzTestPlayer(false, "guest3", Color.orange, game);
			new ValzTestPlayer(false, "guest4", Color.green, game);
		} catch (RemoteException | ExceptionFullParty | ExceptionUsedPlayerName | ExceptionUsedPlayerColor e) { e.printStackTrace(); }

		try 
		{
			game.hostStartGame("guest2");
			fail("host wasnt game creator error");
		}
		catch (ExceptionForbiddenAction e1) { } // this exception should be raised
		catch (RemoteException e) { e.printStackTrace(); }

		try { game.hostStartGame("host"); } 
		catch (RemoteException e) { e.printStackTrace(); }

		String[] playerOrder = data.getPlayerOrder();
		assertNotNull(playerOrder);
		assertEquals(playerOrder.length, 4);


		for(String player : playerOrder)
		{
			LinkedList<Tile> playerHand = data.getPlayerInfo(player).hand.getTiles();
			assertEquals(playerHand.size(), Data.initialHandSize);

			System.out.println(player + "'s hand :");
			for(Tile t : playerHand)
			{
				System.out.println(t.toString());
			}
			System.out.println("");
		}



		//TODO test if each player has same hand as game
	}

	public Game createBasicGame() {
		Game game = null;
		try { game = new Game(); } 
		catch (RemoteException | ExceptionUnknownBoardName | RuntimeException e) { e.printStackTrace(); }
		assertNotNull(game);

		return game;
	}
}
