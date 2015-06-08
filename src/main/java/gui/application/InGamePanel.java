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
	BottomPlayerPanel bottomPlayerPanel;
	TitlePanel titlePanel;
	MapPanel mapPanel;
	ArrayList<PlayerPanel> playerPanels = new ArrayList<PlayerPanel>();
	String[] playersTab;

	// Constructors
	
	InGamePanel(GameController gc) {
		super();
    	this.setLayout(new BorderLayout());
    	this.setPreferredSize(new Dimension(1350, 870));
    	//this.setBackground(new Color(0xFFEDDE));
    	this.setupGameMapPanel();
    	this.setupChatPanel();
    	this.setupPlayersPanel();
	}
	
	private void setupGameMapPanel() {	
		centerMapPanel = new Panel();
		centerMapPanel.setLayout(new BorderLayout());
		this.add(centerMapPanel, BorderLayout.CENTER);
		
		/*this.deckAndRoundPanel = new Panel();
		this.deckAndRoundPanel.setLayout(null);
		this.deckAndRoundPanel.setPreferredSize(new Dimension(100, 0));
		centerMapPanel.add(this.deckAndRoundPanel, BorderLayout.EAST);*/
		
		/*Panel eastTestPanel = new Panel();
		Panel westTestPanel = new Panel();
		eastTestPanel.setPreferredSize(new Dimension(18, 0));
		westTestPanel.setPreferredSize(new Dimension(18, 0));
		eastTestPanel.setBackground(Color.WHITE);
		westTestPanel.setBackground(Color.WHITE);
		bigMapPanel.add(eastTestPanel, BorderLayout.EAST);
		bigMapPanel.add(westTestPanel, BorderLayout.WEST);*/
		
		this.mapPanel = new MapPanel();
		centerMapPanel.add(this.mapPanel, BorderLayout.CENTER);
		
		bottomPlayerPanel = new BottomPlayerPanel();
		centerMapPanel.add(bottomPlayerPanel, BorderLayout.SOUTH);		
		bottomPlayerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
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
		Data data = StreetCar.player.getGameData();
		this.playersTab = data.getPlayerOrder();
		int nbPlayers = data.getNbrPlayer();
		String playerName = null;
		try {
			playerName = StreetCar.player.getPlayerName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
    	this.playersSidebarPanel = new Panel();
    	this.playersSidebarPanel.setLayout(null);
    	this.playersSidebarPanel.setPreferredSize(new Dimension(330, (nbPlayers-1)*185+30));
    	this.playersSidebarPanel.setBackground(new Color(0xFFEDDE));
    	this.playersSidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
    	
		titlePanel = new TitlePanel("Adversaries");
		titlePanel.setBounds(0, 0, 329, 30);
		playersSidebarPanel.add(titlePanel);  	
    	
    	System.out.println("NUMBER OF PLAYERS: " + nbPlayers);
    	
    	// catch the index of bottom player
    	int bottomPlayerIndex = 0;
    	for (int i=0; i<nbPlayers; i++) {
    		if (playersTab[i].equals(playerName)) {
				bottomPlayerIndex = i;
				break;
			}
    	}
    	
    	
    	System.out.println("PLAYERS ORDER: ");
    	for (int i=0; i<nbPlayers; i++) {
    		System.out.println(i+1 + ". " + playersTab[i]);
    	}
    	
    	int y = 40;
    	int j = 0;
    	for (int i = bottomPlayerIndex+1; i < nbPlayers; i++) {
    		System.out.println("* indice i = " + i);
    		PlayerPanel playerPanel = new PlayerPanel(playersTab[i]);
       		playerPanels.add (playerPanel);
       		if (i<nbPlayers-1) { //last bar not displayed
       			playerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
      		}
       		playerPanel.setBounds(15, y+(185*j), 285, 175);
       		playersSidebarPanel.add(playerPanel);
       		j++;
   		}
    	
   		for (int i = 0; i < bottomPlayerIndex; i++) {
    		System.out.println("- indice i = " + i);
   			PlayerPanel playerPanel = new PlayerPanel(playersTab[i]);
  	     	playerPanels.add (playerPanel);
        	if (i<nbPlayers-1) { //last bar not displayed
        		playerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        	}
        	playerPanel.setBounds(15, y+(185*j), 285, 175);
        	playersSidebarPanel.add(playerPanel);
        	j++;
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
	
	public void beginTrip() {
		System.out.println("THE TERMINUS IS UNDEFINED FOR THE MOMENT");
		PlayerIHM player = StreetCar.player;
		Point terminus = new Point();
//TODO
/*		try {
			player.startMaidenTravel(player.getPlayerName(), terminus);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionGameHasNotStarted e) {
			e.printStackTrace();
		} catch (ExceptionUncompletedPath e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/	}
	
	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		System.out.println("REFRESH GAME");
		this.mapPanel.refreshGame(player, data);
		for (PlayerPanel playerPanel : this.playerPanels) {
			playerPanel.refreshGame(player, data);
		}
		this.bottomPlayerPanel.refreshGame(player, data);
	}
}
