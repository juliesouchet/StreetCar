package test.java.game;

import main.java.data.Deck;
import main.java.data.Tile;

public class DeckTest {
	
	private static int nbOfCards = 20;
	
	public static void main(String[] args) 
	{
		Deck d = new Deck();
		for(int i = 0; i < nbOfCards; i++)
		{
			Tile t = d.drawTile();
			System.out.println(t.toString());
		}
	}
}
