package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import main.java.gui.board.MapPanel;
import main.java.gui.components.Panel;

public class InGamePanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;
	Panel chatPanel;
	Panel gameMapPanel;
	Panel playersSidebarPanel;
	Panel[] playersPanel = new Panel[5];

	// Constructors
	
	InGamePanel() {
		super();
    	this.setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(1350, 870));
    	
    	this.setupGameMapPanel();
    	this.setupChatPanel();
    	this.setupPlayersPanel();
	}
	
	private void setupGameMapPanel() {	
		this.setBackground(Color.WHITE);
		MapPanel mapPanel = new MapPanel();
		this.add(mapPanel, BorderLayout.CENTER);
	}
	
	private void setupChatPanel() {
    	this.chatPanel = new Panel();
    	chatPanel.setLayout(new BorderLayout());
    	chatPanel.setPreferredSize(new Dimension(280, 870));
    	chatPanel.setBackground(Color.WHITE);
    	chatPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
    	this.add(chatPanel, BorderLayout.EAST);
    	
    	Panel chatInputPanel = new Panel();
    	chatInputPanel.setBackground(Color.WHITE);
    	chatInputPanel.setPreferredSize(new Dimension(280, 130));
    	chatInputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    	chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
    	
    	Panel chatTextPanel = new Panel();
    	chatTextPanel.setBackground(Color.WHITE);
    	chatPanel.add(chatTextPanel, BorderLayout.CENTER);
    	
	}
	
	private void setupPlayersPanel() { 
		int nbPlayers = 5;
    	this.playersSidebarPanel = new Panel();
    	playersSidebarPanel.setLayout(null);
    	playersSidebarPanel.setPreferredSize(new Dimension(330, nbPlayers*160+35));
    	playersSidebarPanel.setBackground(Color.WHITE);
    	playersSidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    	
    	int y = 45;
    	for (int i=0; i<5; i++) {
    		Panel panel = new Panel();
    		playersPanel[i] = panel;
    		playersPanel[i].setPreferredSize(new Dimension(280, 150));
    		playersPanel[i].setBounds(20, y+(160*i), 280, 150);
    		playersPanel[i].setBackground(Color.GREEN);
    		playersSidebarPanel.add(playersPanel[i]);
    	}
    	
    	JScrollPane scrollPane = new JScrollPane(playersSidebarPanel);
    	scrollPane.setHorizontalScrollBar(null);
    	this.add(scrollPane, BorderLayout.WEST);
    	
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(10, 10, 200, 200);
    }
	
	// Actions
	
	/*public void quitGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}*/
	
}