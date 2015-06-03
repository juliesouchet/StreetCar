package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

public class PlayerPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	String nameOfPlayer = null;
	Color playerColor;
	ArrayList<TilePanel> tilePanels = new ArrayList<TilePanel>();
	
	public PlayerPanel(String nameOfPlayer) {		
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(285, 175));
		this.setLayout(null);
		this.nameOfPlayer = nameOfPlayer;
		JLabel nameOfPlayerLabel = new JLabel(nameOfPlayer);
		nameOfPlayerLabel.setBounds(70, 5, 80, 30);
		this.add(nameOfPlayerLabel);
		setPlayerHandCards();
	}
	
	public void setPlayerHandCards() {
		for (int i=0; i < 5; i++) {
			TilePanel tilePanel = new TilePanel();
	        int sizeOfCard = 45;
	        int spaceBetween = 10;
			tilePanel.setBounds(sizeOfCard*i + spaceBetween*(i+1), 60, sizeOfCard, sizeOfCard);
			this.add(tilePanel);
		}
	}
	
	public void refreshPlayerHandCards(String playerName) {
		PlayerIHM player = StreetCar.player;
		Data data = player.getGameData();
		System.out.println("player name = " + playerName);
		System.out.println(data.getHandSize(playerName));
		for (int i=0; i<data.getHandSize(playerName); i++) {
			Tile tile = data.getHandTile(playerName, i);
			TilePanel tilePanel = tilePanels.get(i);
			tilePanel.setTile(tile);
		}
	}
	
	public void setStationCards(String playerName) {
		Point[] stationsPositions = StreetCar.player.getGameData().getPlayerAimBuildings(playerName);
		for (int i=0; i<stationsPositions.length; i++) {
			Point p = stationsPositions[i];
			Tile tile = StreetCar.player.getGameData().getBoard()[p.x][p.y];
			TilePanel tilePanel = new TilePanel(tile);
			
	        int sizeOfCard = 45;
	        int spaceBetween = 10;
			tilePanel.setBounds(sizeOfCard*i + spaceBetween*(i+1), 115, sizeOfCard, sizeOfCard);
			this.add(tilePanel);
		}
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //avatar
        //g.setColor(playerColor); TODO valz
        g.setColor(Color.YELLOW);
        g.fillRect(10, 5, 45, 45);
        g.setColor(Color.BLACK);
        g.drawRect(10, 5, 45, 45);
        
        int[] xPoints = {230, 253, 276, 253};
        int[] yPoints = {28, 5, 28, 51};
        g.drawPolygon(xPoints, yPoints, 4);
    }

	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		if (data == null) {
			playerColor = data.getPlayerColor(nameOfPlayer);
			refreshPlayerHandCards(nameOfPlayer);
			setStationCards(nameOfPlayer);
		}
	}
}
