package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

public class PlayerPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	String nameOfPlayer = null;
	Data data;
	Color playerColor;
	
	public PlayerPanel(String nameOfPlayer) {		
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(285, 175));
		this.setLayout(null);
		this.nameOfPlayer = nameOfPlayer;
		JLabel nameOfPlayerLabel = new JLabel(nameOfPlayer);
		nameOfPlayerLabel.setBounds(70, 5, 80, 30);
		this.add(nameOfPlayerLabel);
		
	}
	
	public void setPlayerHandCards(String playerName) {
		for (int i=0; i<data.getHandSize(playerName); i++) {
			Tile tile = data.getHandTile(playerName, i);
			TilePanel tilePanel = new TilePanel(tile);
			
	        int sizeOfCard = 45;
	        int spaceBetween = 10;
			tilePanel.setBounds(sizeOfCard*i + spaceBetween*(i+1), 60, sizeOfCard, sizeOfCard);
			this.add(tilePanel);
		}
	}
	
	public void setStationCards(String playerName) {
		Point[] stationsPositions = data.getPlayerAimBuildings(playerName);
		for (int i=0; i<stationsPositions.length; i++) {
			Point p = stationsPositions[i];
			Tile tile = data.getBoard()[p.x][p.y];
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
        g.setColor(playerColor);
        g.fillRect(10, 5, 45, 45);
        g.setColor(Color.BLACK);
        g.drawRect(10, 5, 45, 45);
        
        //g.drawRect(230, 5, 46, 46); //rectangle for diamond
        //g.drawLine(230, 28, 253, 5); //line1 for diamond
        //g.drawLine(253, 5, 276, 28); //line2 for diamond
        //g.drawLine(276, 28, 253, 51); //line3 for diamond
        //g.drawLine(253, 51, 230, 28); //line4 for diamond
        
        int[] xPoints = {230, 253, 276, 253};
        int[] yPoints = {28, 5, 28, 51};
        g.drawPolygon(xPoints, yPoints, 4);
    }

	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		this.data = data;
		if (data == null) {
			playerColor = data.getPlayerColor(nameOfPlayer);
			setPlayerHandCards(this.nameOfPlayer);
		}
	}
}
