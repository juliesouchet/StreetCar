package test.java.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Hashtable;

import main.java.data.Deck;
import main.java.data.Tile;

import org.junit.Test;

public class DeckTest {

	@Test
	public void testDeck() 
	{
		Deck deck = new Deck();
		assertNotNull(deck);
		assertEquals(101, deck.getNbrRemainingDeckTile());
		
		Hashtable<Tile, Integer> tileList = new Hashtable<Tile, Integer>();
		
		// Tuiles sans arbre
		Tile t = Tile.parseTile("Tile_FFFFZZ2003");
		int nb = 20;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_FFFFZZ2113");
		nb = 21;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_FFFFZZ060123");
		nb = 6;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_FFFFZZ100102");
		nb = 10;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_FFFFZZ100103");
		nb = 10;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_FFFFZZ100203");
		nb = 10;
		tileList.put(t, nb);
		
		// Tuiles � arbre
		t = Tile.parseTile("Tile_TFFFZZ040213");
		nb = 4;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_TFFFZZ02010223");
		nb = 2;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_TFFFZZ02021203");
		nb = 2;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_TFFFZZ06031323");
		nb = 6;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_TFFFZZ06121323");
		nb = 6;
		tileList.put(t, nb);
		
		t = Tile.parseTile("Tile_TFFFZZ0401122303");
		nb = 4;
		tileList.put(t, nb);
		
		// V�rification du nb de tuiles initiales pour chaque type
		for(Tile tmp : tileList.keySet()) {
			assertTrue("La tuile " + tmp.getTileID() + " est mal initialis�e",
						tileList.get(tmp)==deck.getNbrRemainingTile(tmp));
		}
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
		Hashtable<String, Integer> tileList = new Hashtable<String, Integer>();
		Deck d = new Deck();
		for (String str: IDList)
		{
			Tile t = Tile.parseTile(str);
			if (t.isDeckTile()) tileList.put(t.getTileID(), d.getNbrRemainingTile(t));
		}

		Tile t = d.drawTile();
		Integer oldNb = tileList.get(t.getTileID()),
			newNb = d.getNbrRemainingTile(t);
		assertTrue("Tirer la tuile n'a pas d�cr�ment� le nb de tuiles restantes de ce type",
					oldNb ==(newNb+1));
		tileList.put(t.getTileID(), d.getNbrRemainingTile(t));
		
		boolean oneTileEmpty = false;
		while(!oneTileEmpty) {
			t = d.drawTile();
			tileList.put(t.getTileID(), d.getNbrRemainingTile(t));
			for(Integer i : tileList.values()) {
				if (i==0) {
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
		File f			= new File(Deck.stackDirectory);
		String[] IDList	= f.list();
		Hashtable<String, Integer> tileList = new Hashtable<String, Integer>();
		Deck d = new Deck();
		for (String str: IDList)
		{
			Tile t = Tile.parseTile(str);
			if (t.isDeckTile()) tileList.put(t.getTileID(), d.getNbrRemainingTile(t));
		}
		int total = d.getNbrRemainingDeckTile();
		
		while(!d.isEmpty()) {
			Tile t = d.drawTile();
			double probSupposee = ((double)(tileList.get(t.getTileID())-1))/ ((double)(total-1)),
					probCalculee = d.getPickProba(t);
			assertTrue("calcul de proba incorrect",
						 probSupposee == probCalculee);
			tileList.put(t.getTileID(), d.getNbrRemainingTile(t));
			total = d.getNbrRemainingDeckTile();
		}
	}

}
