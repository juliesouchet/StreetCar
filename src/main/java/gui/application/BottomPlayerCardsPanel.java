package main.java.gui.application;

import java.awt.Color;
import java.awt.Graphics;

import main.java.gui.board.TilePanel;
import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class BottomPlayerCardsPanel extends Panel{
	
	TilePanel tilePanel1;
	TilePanel tilePanel2;
	TilePanel tilePanel3;
	TilePanel tilePanel4;
	TilePanel tilePanel5;
	
	public BottomPlayerCardsPanel() {
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		placeTiles();
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
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(20, 20, 50, 50); //avatar	

		g.drawRect(350, 80, 50, 50); //station1
		g.drawRect(410, 80, 50, 50); //station2
	}
}
