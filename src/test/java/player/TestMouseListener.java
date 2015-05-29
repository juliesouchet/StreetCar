package test.java.player;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Random;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.player.PlayerInterface;

public class TestMouseListener implements MouseListener
{
	private PlayerInterface player;
	private LinkedList<Tile> tileList;

	public TestMouseListener(PlayerInterface player)
	{
		this.player		= player;
		this.tileList	= new LinkedList<Tile>();

		this.tileList	.add(Tile.parseTile("Tile_FFFFZZ060123"));
		this.tileList	.add(Tile.parseTile("Tile_FFFFZZ2113"));
		this.tileList	.add(Tile.parseTile("Tile_FFFFZZ2003"));
		this.tileList	.add(Tile.parseTile("Tile_FFFFZZ100102"));
		this.tileList	.add(Tile.parseTile("Tile_TFFFZZ06031323"));
		this.tileList	.add(Tile.parseTile("Tile_TFFFZZ06121323"));
		this.tileList	.add(Tile.parseTile("Tile_TFFFZZ040213"));
	}
	
	
	
	

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		Random rnd = new Random();

		while (true)
		{
			try
			{
				Data data = this.player.getGameData();
				int x = 1 + rnd.nextInt(data.getWidth()-1);
				int y = 1 + rnd.nextInt(data.getHeight()-1);
				Tile t = this.tileList.get(rnd.nextInt(this.tileList.size()));
				for (int i=0; i<rnd.nextInt(4); i++)t.turnLeft();
				if (!data.isAcceptableTilePlacement(x, y, t)) throw new Exception();
				this.player.placeTile(t, new Point(x, y));
				return;
			}
			catch(Exception e)	{}
		}
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