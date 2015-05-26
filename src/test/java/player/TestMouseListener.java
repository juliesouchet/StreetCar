package test.java.player;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.data.Tile;
import main.java.game.GameInterface;

public class TestMouseListener implements MouseListener
{
	private GameInterface game;
	private String	playerName;
	private int x0;
	private int y0;
	private int	tileWidth;
	private int tileHeight;

	public TestMouseListener(GameInterface game, String playerName, int x0, int y0, int tileWidth, int tileHeight)
	{
		this.game		= game;
		this.playerName	= playerName;
		this.x0			= x0;
		this.y0			= y0;
		this.tileHeight	= tileHeight;
		this.tileWidth	= tileWidth;
	}
	
	
	
	

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		int x = (arg0.getX() - x0) / tileWidth;
		int y = (arg0.getX() - y0) / tileHeight;
		Tile t = Tile.parseTile("Tile_FFFFZZ060123");
		
		try					{this.game.placeTile(playerName, t, new Point(x, y));}
		catch(Exception e)	{e.printStackTrace(); System.exit(0);}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}