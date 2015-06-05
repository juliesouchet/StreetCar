package test.java.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import main.java.data.Deck;
import main.java.data.Tile;

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
		Deck clone = deck.getClone();
		assertEquals(deck.getNbrRemainingDeckTile(), clone.getNbrRemainingDeckTile());
		deck.drawTile();
		assertEquals(deck.getNbrRemainingDeckTile(), deck.getClone().getNbrRemainingDeckTile());
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
		Deck deck = new Deck();
		assertTrue("Pas 101 tuiles au d�part",
					deck.getNbrRemainingDeckTile()==101);
		deck.drawTile();
		assertTrue("Retirer une tuile n'a pas diminu� le nb de tuiles restantes",
					deck.getNbrRemainingDeckTile()==100);		
	}

	@Test
	public void testDrawTile() {
		File f			= new File(Deck.stackDirectory);
		String[] IDList	= f.list();
		Tile[] tileList = new Tile[12];
		int i = 0;
		for (String str: IDList)
		{
			Tile t = Tile.parseTile(str);
			if (t.isDeckTile()) {
				tileList[i] = t;
				i++;
			}
		}
		Deck d = new Deck();
		int[] nb = new int[12];
		for(int j = 0; j < 12; j++) {
			nb[j] = d.getNbrRemainingTile(tileList[j]);
		}
		// TODO c'est pas normal qu'on deborde : a corriger
		Tile t = d.drawTile();
		i = 0;
		while(i<12 && !t.getTileID().equals(IDList[i])) i++;
		if(i==12) throw new RuntimeException(t.getTileID()+" pas dans la liste ?");
		assertTrue("Tirer la tuile n'a pas d�cr�ment� le nb de tuiles restantes de ce type",
					nb[i]==(d.getNbrRemainingTile(t)+1));
		nb[i]--;
		
		boolean oneTileEmpty = false;
		while(!oneTileEmpty) {
			t = d.drawTile();
			i = 0;
			while(i<12 && !t.getTileID().equals(IDList[i])) i++;
			if(i==12) throw new RuntimeException(t.getTileID()+" pas dans la liste ?");
			nb[i]--;
			for(int j = 0; j < 12; j++) {
				if(nb[j]==0) {
					oneTileEmpty = true;
					break;
				}
			}
		}
		
		while(!d.isEmpty()) {
			Tile tmp = d.drawTile();
			assertFalse("On a tir� une tuile qui �tait cens� �tre �puis�e", tmp.equals(t));
		}
	}

	@Test
	public void testGetPickProba() {
		fail("Not yet implemented");
	}

}
