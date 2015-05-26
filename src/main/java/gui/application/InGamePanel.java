package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import main.java.gui.board.ChatPanel;
import main.java.gui.board.MapPanel;
import main.java.gui.components.Panel;

public class InGamePanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;
	Panel chatPanel;
	Panel centerMapPanel;
	Panel playersSidebarPanel;
	Panel deckAndTurnPanel;
	TitlePanel titlePanel;
	PlayerPanel[] playersPanel = new PlayerPanel[5];

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
		centerMapPanel = new Panel();
		centerMapPanel.setLayout(new BorderLayout());
		this.add(centerMapPanel, BorderLayout.CENTER);
		
		//deckAndTurnPanel = new Panel();
		//centerMapPanel.add(deckAndTurnPanel, BorderLayout.NORTH);
		
		Panel bigMapPanel = new Panel();
		bigMapPanel.setLayout(new BorderLayout());	
		centerMapPanel.add(bigMapPanel, BorderLayout.CENTER);
		
		Panel eastTestPanel = new Panel();
		Panel westTestPanel = new Panel();
		eastTestPanel.setPreferredSize(new Dimension(18, 0));
		westTestPanel.setPreferredSize(new Dimension(18, 0));
		eastTestPanel.setBackground(Color.WHITE);
		westTestPanel.setBackground(Color.WHITE);
		bigMapPanel.add(eastTestPanel, BorderLayout.EAST);
		bigMapPanel.add(westTestPanel, BorderLayout.WEST);
		
		MapPanel mapPanel = new MapPanel();
		bigMapPanel.add(mapPanel, BorderLayout.CENTER);
		
		mapPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		BottomPlayerPanel bottomPlayerPanel = new BottomPlayerPanel();
		centerMapPanel.add(bottomPlayerPanel, BorderLayout.SOUTH);		
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
    	chatInputPanel.setPreferredSize(new Dimension(280, 90));
    	chatInputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    	chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
    	
    	ChatPanel chatTextPanel = new ChatPanel();
    	chatPanel.add(chatTextPanel, BorderLayout.CENTER);
    	
	}
	
	private void setupPlayersPanel() { 		
		int nbPlayers = 4;
    	this.playersSidebarPanel = new Panel();
    	this.playersSidebarPanel.setLayout(null);
    	this.playersSidebarPanel.setPreferredSize(new Dimension(330, nbPlayers*185+30));
    	this.playersSidebarPanel.setBackground(Color.WHITE);
    	this.playersSidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    	
		titlePanel = new TitlePanel("Adversaries");
		titlePanel.setBounds(10, 0, 298, 30);
		//titlePanel.setBackground(Color.GREEN);
		playersSidebarPanel.add(titlePanel);
    	
    	int y = 40;
    	for (int i=0; i<nbPlayers; i++) {
    		PlayerPanel playerPanel = new PlayerPanel("Name", "Difficulty");
    		playersPanel[i] = playerPanel;
    		if (i<nbPlayers-1) { //last bar not displayed
    			playersPanel[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    		}
    		playersPanel[i].setBounds(15, y+(185*i), 285, 175);
    		playersSidebarPanel.add(playersPanel[i]);
    	}
    	
    	JScrollPane scrollPane = new JScrollPane(playersSidebarPanel);
    	scrollPane.setHorizontalScrollBar(null);
    	scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    	this.add(scrollPane, BorderLayout.WEST);    	
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
	
	// Actions
	
	/*public void quitGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}*/	
}
