package main.java.gui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class BottomPlayerCardsPanel extends Panel{
	
	TilePanel tilePanel1;
	TilePanel tilePanel2;
	TilePanel tilePanel3;
	TilePanel tilePanel4;
	TilePanel tilePanel5;
	Data data;
	Color playerColor;
	
	public BottomPlayerCardsPanel() {
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		placeAvatar();
		placeTiles();
		placeObjectives();
	}
	
	protected void placeAvatar() {
		try {
			playerColor = data.getPlayerColor(this.getPlayer().getPlayerName());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected void placeTiles() {
		tilePanel1 = new TilePanel();
		tilePanel2 = new TilePanel();
		tilePanel3 = new TilePanel();
		tilePanel4 = new TilePanel();
		tilePanel5 = new TilePanel();
		
		tilePanel1.setBounds(20, 80, 50, 50);
		tilePanel2.setBounds(80, 80, 50, 50);
		tilePanel3.setBounds(140, 80, 50, 50);
		tilePanel4.setBounds(200, 80, 50, 50);
		tilePanel5.setBounds(260, 80, 50, 50);
		
		this.add(tilePanel1);
		this.add(tilePanel2);
		this.add(tilePanel3);
		this.add(tilePanel4);
		this.add(tilePanel5);
	}
	
	protected void placeObjectives() {
		String playerName = null;
		try {
			playerName = this.getPlayer().getPlayerName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Point[] stationsPositions = data.getPlayerAimBuildings(playerName);
		
		int x = 350;
		for (int i=0; i<stationsPositions.length; i++) {
			Point p = stationsPositions[i];
			Tile tile = data.getBoard()[p.x][p.y];
			TilePanel tilePanel = new TilePanel(tile);			
			tilePanel.setBounds(x+(i*60), 80, 50, 50);
			this.add(tilePanel);
		}
	}

	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Avatar
        g.setColor(playerColor);
        g.fillRect(20, 20, 50, 50);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 50, 50);

		//g.drawRect(350, 80, 50, 50); //station1
		//g.drawRect(410, 80, 50, 50); //station2
	}
	
	public Data getData() {
		return this.getPlayer().getGameData();
	}
	
	public PlayerIHM getPlayer() {
		return ((GameController)(this.getFrameController())).player;
	}
}
