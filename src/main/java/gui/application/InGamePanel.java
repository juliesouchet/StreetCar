package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotYourTurn;
import main.java.gui.board.MapPanel;
import main.java.gui.chat.ChatPanel;
import main.java.gui.components.Panel;
import main.java.gui.components.TitlePanel;
import main.java.player.PlayerIHM;

public class InGamePanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;
	Panel bigChatPanel;	
	Panel chatPanel;
	Panel centerMapPanel;
	Panel playersSidebarPanel;
	Panel deckAndTurnPanel;
	TitlePanel titlePanel;
	MapPanel mapPanel;
	ArrayList<PlayerPanel> playerPanels = new ArrayList<PlayerPanel>();
	Data data;
	String[] playersTab;

	// Constructors
	
	InGamePanel(GameController gc) {
		super();
    	this.setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(1350, 870));
    	
    	this.data = gc.player.getGameData();
    	
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
		
		this.mapPanel = new MapPanel();
		bigMapPanel.add(this.mapPanel, BorderLayout.CENTER);
		
		mapPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		
		BottomPlayerPanel bottomPlayerPanel = new BottomPlayerPanel();
		centerMapPanel.add(bottomPlayerPanel, BorderLayout.SOUTH);		
	}
	
	private void setupChatPanel() {
    	this.bigChatPanel = new Panel();
    	this.bigChatPanel.setLayout(new BorderLayout());
    	this.bigChatPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));    	    	
    	this.bigChatPanel.setPreferredSize(new Dimension(280, 870));
    	this.bigChatPanel.setBackground(Color.WHITE);
    	this.add(bigChatPanel, BorderLayout.EAST);
    	
    	Panel chatInputPanel = new Panel();
    	chatInputPanel.setBackground(Color.WHITE);
    	chatInputPanel.setPreferredSize(new Dimension(280, 90));
    	chatInputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    	this.bigChatPanel.add(chatInputPanel, BorderLayout.SOUTH);
    	
    	ChatPanel chatTextPanel = new ChatPanel();
    	bigChatPanel.add(chatTextPanel, BorderLayout.CENTER);
    	chatTextPanel.setBackground(Color.WHITE);
    	
    	TitlePanel titlePanel = new TitlePanel("Chat");
    	this.bigChatPanel.add(titlePanel, BorderLayout.NORTH);
	}
	
	private void setupPlayersPanel() { 	
		//this.playersTab = data.getPlayerOrder(); TODO
		int nbPlayers = 4;
		//int nbPlayers = data.getNbrPlayer();
		
    	this.playersSidebarPanel = new Panel();
    	this.playersSidebarPanel.setLayout(null);
    	this.playersSidebarPanel.setPreferredSize(new Dimension(330, nbPlayers*185+30));
    	this.playersSidebarPanel.setBackground(Color.WHITE);
    	this.playersSidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    	
		titlePanel = new TitlePanel("Adversaries");
		titlePanel.setBounds(0, 0, 329, 30);
		playersSidebarPanel.add(titlePanel);
    	
    	int y = 40;
    	System.out.println(nbPlayers);
    	for (int i=0; i<nbPlayers-1; i++) {
    		PlayerPanel playerPanel = new PlayerPanel("gbvdf"); //TODO
    		playerPanels.add (playerPanel);
    		if (i<nbPlayers-1) { //last bar not displayed
    			playerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    		}
    		playerPanel.setBounds(15, y+(185*i), 285, 175);
    		playersSidebarPanel.add(playerPanel);
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
	
	public void validate() {
		try {
			this.getPlayer().validate();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionGameHasNotStarted e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		}
	}
	
	public void beginTrip() {
		System.out.println("THE TERMINUS IS UNDEFINED FOR THE MOMENT");
		PlayerIHM player = this.getPlayer();
		Point terminus = new Point();
		try {
			this.getPlayer().startMaidenTravel(player.getPlayerName(), terminus);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionGameHasNotStarted e) {
			e.printStackTrace();
		}
	}
	
	// Refresh game
	
	public Data getData() {
		return this.data;
	}
	
	public PlayerIHM getPlayer() {
		return ((GameController)(this.getFrameController())).player;
	}
	
	public void refreshGame(PlayerIHM player, Data data) {
		System.out.println("REFRESH GAME");
		this.mapPanel.refreshGame(player, data);
		for (PlayerPanel playerPanel : this.playerPanels) {
			playerPanel.refreshGame(player, data);
		}
		this.data = data;
	}
}
