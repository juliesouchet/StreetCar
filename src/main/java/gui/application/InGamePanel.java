package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.gui.board.MapPanel;
import main.java.gui.chat.Chat;
import main.java.gui.chat.ChatPanel;
import main.java.gui.components.Button;
import main.java.gui.components.Panel;
import main.java.gui.components.TitlePanel;
import main.java.player.PlayerIHM;

public class InGamePanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;
	Panel bigChatPanel;	
	ChatPanel chatTextPanel;
	Panel centerMapPanel;
	Panel playersSidebarPanel;
	BottomPlayerPanel bottomPlayerPanel;
	TitlePanel titlePanel;
	MapPanel mapPanel;
	ArrayList<PlayerPanel> playerPanels = new ArrayList<PlayerPanel>();
	String[] playersTab;
	JTextArea inputArea;
	Chat chat = new Chat();

	// Constructors
	
	InGamePanel(GameController gc) {
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
		
		this.mapPanel = new MapPanel();
		centerMapPanel.add(this.mapPanel, BorderLayout.CENTER);
		
		bottomPlayerPanel = new BottomPlayerPanel();
		centerMapPanel.add(bottomPlayerPanel, BorderLayout.SOUTH);		
		bottomPlayerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
	}
	
	private void setupChatPanel() {
		int humanAdversariesCount = 0;
		try {
			for (LoginInfo info : StreetCar.player.getLoginInfo()) {
				if (info.isHuman()) humanAdversariesCount++;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (humanAdversariesCount <= 1) return;
		
    	this.bigChatPanel = new Panel();
    	this.bigChatPanel.setLayout(new BorderLayout());
    	this.bigChatPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));    	    	
    	this.bigChatPanel.setPreferredSize(new Dimension(280, 870));
    	this.bigChatPanel.setBackground(Color.WHITE);
    	this.add(bigChatPanel, BorderLayout.EAST);
    	
    	Panel chatInputPanel = new Panel();
    	chatInputPanel.setLayout(null);
    	chatInputPanel.setBackground(Color.RED);
    	chatInputPanel.setPreferredSize(new Dimension(280, 90));
    	chatInputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    	chatInputPanel.setBackground(new Color(0xFFEDDE));
    	this.bigChatPanel.add(chatInputPanel, BorderLayout.SOUTH);
    	
    	inputArea = new JTextArea();
    	inputArea.setBounds(5, 5, 185, 80);
    	inputArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
    	inputArea.setLineWrap(true);
    	inputArea.setMargin(new Insets(10,10,10,10));
    	chatInputPanel.add(inputArea);

    	Button inputButton = new Button("Envoyer", null);
    	inputButton.setBounds(195, 5, 80, 80);
    	inputButton.addAction(this, "sendMessage");
    	chatInputPanel.add(inputButton);
    
		this.chat = new Chat();
    	chatTextPanel = new ChatPanel();
    	bigChatPanel.add(chatTextPanel, BorderLayout.CENTER);
    	chatTextPanel.setChat(this.chat);
    	try {
			this.chatTextPanel.setSenderColor(StreetCar.player.getPlayerColor());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
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
    	
		titlePanel = new TitlePanel("Adversaires");
		titlePanel.setBounds(0, 0, 329, 30);
		playersSidebarPanel.add(titlePanel);
    	
    	// catch the index of bottom player
    	int bottomPlayerIndex = 0;
    	for (int i=0; i<nbPlayers; i++) {
    		if (playersTab[i].equals(playerName)) {
				bottomPlayerIndex = i;
				break;
			}
    	}
    	
    	int y = 40;
    	int j = 0;
    	for (int i = bottomPlayerIndex+1; i < nbPlayers; i++) {
    		PlayerPanel playerPanel = new PlayerPanel(playersTab[i], mapPanel);
       		playerPanels.add (playerPanel);
       		if (i<nbPlayers-1) { //last bar not displayed
       			playerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
      		}
       		playerPanel.setBounds(15, y+(185*j), 285, 175);
       		playersSidebarPanel.add(playerPanel);
       		j++;
   		}
    	
   		for (int i = 0; i < bottomPlayerIndex; i++) {
   			PlayerPanel playerPanel = new PlayerPanel(playersTab[i], mapPanel);
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
	
	
	public void sendMessage() {
		String text = this.inputArea.getText();
		if (text == null || text.trim().equals("")) {
			return;
		}
		this.inputArea.setText("");
		try {
			StreetCar.player.sendChatMessage(text);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		this.mapPanel.refreshGame(player, data);
		for (PlayerPanel playerPanel : this.playerPanels) {
			playerPanel.refreshGame(player, data);
		}
		this.bottomPlayerPanel.refreshGame(player, data);
	}
	
	public void refreshMessages(String playerName, String message) {
		Color senderColor = StreetCar.player.getGameData().getPlayerColor(playerName);
		this.chat.addMessage(senderColor, message);
	}
}
