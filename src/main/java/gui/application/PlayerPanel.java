package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlayerPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public PlayerPanel() {
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(280, 150));
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(10, 5, 50, 50); //avatar
        g.drawRect(10, 65, 30, 30); //card1
        g.drawRect(50, 65, 30, 30); //card2
        g.drawRect(90, 65, 30, 30); //card3
        g.drawRect(130, 65, 30, 30); //card4
        g.drawRect(170, 65, 30, 30); //card5
        g.drawRect(10, 105, 30, 30); //station1
        g.drawRect(50, 105, 30, 30); //station2
    }
}
