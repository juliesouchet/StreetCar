package main.java.gui.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.gui.components.TextField;
import main.java.gui.util.Constants;
import main.java.gui.util.UserDefaults;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class JoinGameMenuPanel extends MenuPanel {

	// Properties
	
	private TextField playerNameField;
	private TextField gameNameField;
	private TextField addressField;
	
	// Constructors
	
	public JoinGameMenuPanel(GameController gc) {
		super(gc);
    	this.setupPanel();
		this.setupTextFields();
		this.setupButtons();
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(500, 350));
    	this.setMenuTitle("Join Game", null);
	}	
	
	private void setupTextFields() {
		Label titleLabel = new Label("Profile", null);
		titleLabel.setBounds(90, 20, 300, 100);
	    this.add(titleLabel);
	    
	    Label playerNameLabel = new Label("Player name", null);
	    playerNameLabel.setBounds(140, 100, 100, 40);
	    this.add(playerNameLabel);	

	    Label gameNameLabel = new Label("Game name", null);
	    gameNameLabel.setBounds(140, 150, 100, 40);
	    this.add(gameNameLabel);
	    
	    Label adressLabel = new Label("IP adress", null);
	    adressLabel.setBounds(140, 200, 100, 40);
	    this.add(adressLabel);
		
	    UserDefaults ud = UserDefaults.getSharedInstance();
	    String lastPlayerName = ud.getString(Constants.PLAYER_NAME_KEY);
		this.playerNameField = new TextField(lastPlayerName);
		this.playerNameField.setPlaceholder("Player1", null);
		this.playerNameField.setBounds(new Rectangle(215, 105, 150, 30));
		this.add(this.playerNameField);
		
	    String lastGameName = ud.getString(Constants.GAME_NAME_KEY);
		this.gameNameField = new TextField(lastGameName);
		this.gameNameField.setPlaceholder("Game1", null);
		this.gameNameField.setBounds(new Rectangle(215, 155, 150, 30));
		this.add(this.gameNameField);
		
		this.addressField = new TextField("");
		addressField.setBounds(new Rectangle(215, 205, 150, 30));
		addressField.setPlaceholder("ex: 130.190.31.67", null);
		addressField.setEditable(true);
		this.add(this.addressField);
	}
	
	private void setupButtons() {
		Button createGameButton = new Button("Join game", null);
		createGameButton.addAction(this, "joinGame");
		createGameButton.setBounds(270, 280, 150, 40);
    	this.add(createGameButton);
    	
		Button cancelButton = new Button("Cancel", null);
		cancelButton.addAction(this, "cancelGame");
		cancelButton.setBounds(80, 280, 150, 40);
    	this.add(cancelButton);
	}
	
	// Actions
	
	public void joinGame() {
		String playerName = this.playerNameField.getText();
		String gameName = this.gameNameField.getText();
		if ((playerName.isEmpty() || playerName.trim().equals("")) ||
			(gameName.isEmpty() || gameName.trim().equals(""))) {
		     Toolkit.getDefaultToolkit().beep();
			return;
		}
			
		UserDefaults ud = UserDefaults.getSharedInstance();
		ud.setString(Constants.PLAYER_NAME_KEY, playerName);
		ud.setString(Constants.GAME_NAME_KEY, gameName);
		
		GameController gc = this.getGameController();
		try {
			gc.player = PlayerIHM.launchPlayer(playerName,
					                           gameName,
					                           "newOrleans",
					                           2,
					                           Color.RED,
					                           false,
					                           null,
					                           gc);
			gc.showClientWaitingRoomPanel();
		} catch (Exception e)	{
		     Toolkit.getDefaultToolkit().beep();
		     System.out.println("NO HOST");
		}
	}
	
	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.showWelcomeMenuPanel();
	}

}