package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	public PlayerPanel(String nameOfPlayer, String difficultyLevel) {		
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(285, 175));
		this.setLayout(null);
		JLabel nameOfPlayerLabel = new JLabel(nameOfPlayer);
		JLabel difficultyLevelLabel = new JLabel(difficultyLevel);

		nameOfPlayerLabel.setBounds(70, 5, 50, 30);
		difficultyLevelLabel.setBounds(70, 25, 50, 30);
		
		this.add(nameOfPlayerLabel);
		this.add(difficultyLevelLabel);		
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(10, 5, 45, 45); //avatar
        
        int sizeOfCard = 45;
        int spaceBetween = 10;
        g.drawRect(sizeOfCard*0 + spaceBetween*1, 60, sizeOfCard, sizeOfCard); //card1
        g.drawRect(sizeOfCard*1 + spaceBetween*2, 60, sizeOfCard, sizeOfCard); //card2
        g.drawRect(sizeOfCard*2 + spaceBetween*3, 60, sizeOfCard, sizeOfCard); //card3
        g.drawRect(sizeOfCard*3 + spaceBetween*4, 60, sizeOfCard, sizeOfCard); //card4
        g.drawRect(sizeOfCard*4 + spaceBetween*5, 60, sizeOfCard, sizeOfCard); //card5
        
        g.drawRect(10, 115, 45, 45); //station1
        g.drawRect(65, 115, 45, 45); //station2
    }
}
