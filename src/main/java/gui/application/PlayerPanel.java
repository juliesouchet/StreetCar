package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TileImage;
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
		//JLabel difficultyLevelLabel = new JLabel(difficultyLevel);

		nameOfPlayerLabel.setBounds(70, 5, 80, 30);
		//difficultyLevelLabel.setBounds(70, 25, 80, 30);
		
		this.add(nameOfPlayerLabel);
		//this.add(difficultyLevelLabel);
		
		//playerColor = data.getColor(); //TODO
		
		//
	}
	
	public void setPlayerHandCards(String playerName) {
		ArrayList<Tile> playerHand = new ArrayList<Tile>();
		for (int i=0; i<data.getHandSize(playerName); i++){
			
			playerHand.add(data.getHandTile(playerName, i));
			TileImage tileImage = new TileImage(playerHand.get(i));
			tileImage.getImage();
		}
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //avatar
        g.setColor(playerColor);
        g.fillRect(10, 5, 45, 45);
        g.setColor(Color.BLACK);
        g.drawRect(10, 5, 45, 45);
        
        int sizeOfCard = 45;
        int spaceBetween = 10;
        g.drawRect(sizeOfCard*0 + spaceBetween*1, 60, sizeOfCard, sizeOfCard); //card1
        g.drawRect(sizeOfCard*1 + spaceBetween*2, 60, sizeOfCard, sizeOfCard); //card2
        g.drawRect(sizeOfCard*2 + spaceBetween*3, 60, sizeOfCard, sizeOfCard); //card3
        g.drawRect(sizeOfCard*3 + spaceBetween*4, 60, sizeOfCard, sizeOfCard); //card4
        g.drawRect(sizeOfCard*4 + spaceBetween*5, 60, sizeOfCard, sizeOfCard); //card5
        
        //g.drawRect(230, 5, 46, 46); //rectangle for diamond
        //g.drawLine(230, 28, 253, 5); //line1 for diamond
        //g.drawLine(253, 5, 276, 28); //line2 for diamond
        //g.drawLine(276, 28, 253, 51); //line3 for diamond
        //g.drawLine(253, 51, 230, 28); //line4 for diamond
        
        int[] xPoints = {230, 253, 276, 253};
        int[] yPoints = {28, 5, 28, 51};
        g.drawPolygon(xPoints, yPoints, 4);
        
        g.drawRect(10, 115, 45, 45); //station1
        g.drawRect(65, 115, 45, 45); //station2
    }

	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		this.data = data;
		setPlayerHandCards(this.nameOfPlayer);
	}
}
