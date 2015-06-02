package test.java.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import main.java.data.Deck;

import org.junit.Test;

public class DeckTest {

	@Test
	public void testDeck() 
	{
		Deck deck = new Deck();
		assertEquals(101, deck.getNbrRemainingDeckTile());
		assertNotNull(deck);
	}

	@Test
	public void testGetNbrRemainingTile() {
		Deck deck = new Deck();
		assertEquals(101, deck.getNbrRemainingDeckTile());
		deck.drawTile();
		assertEquals(100, deck.getNbrRemainingDeckTile());
		for(int i = 0; i < 100; i++)
		{
			deck.drawTile();
		}
		assertEquals(0, deck.getNbrRemainingDeckTile());
	}

	@Test
	public void testGetPlayerClone() {
		Deck deck = new Deck();
		Deck clone = deck.getPlayerClone();
		assertEquals(deck.getNbrRemainingDeckTile(), clone.getNbrRemainingDeckTile());
		deck.drawTile();
		assertEquals(deck.getNbrRemainingDeckTile(), deck.getPlayerClone().getNbrRemainingDeckTile());
	}

	@Test
	public void testIsEmpty() {
		Deck deck = new Deck();
		assertEquals(false, deck.isEmpty());
		for(int i = 0; i < 101; i++)
		{
			deck.drawTile();
		}
		assertEquals(true, deck.isEmpty());
	}

	@Test
	public void testGetNbrRemainingDeckTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testDrawTile() {
		Deck d = new Deck();
	}

	@Test
	public void testGetPickProba() {
		fail("Not yet implemented");
	}

}
