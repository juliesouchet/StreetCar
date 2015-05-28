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
import main.java.gui.util.IP;
import main.java.gui.util.UserDefaults;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class NewGameMenuPanel extends MenuPanel {

	// Properties

	private TextField playerNameField;
	private TextField gameNameField;
	private TextField addressField;
	
	// Constructors
	
	public NewGameMenuPanel() {
		super();
		this.setupPanel();
		this.setupTextFields();
		this.setupButtons();
	}
	
	public void setupPanel() {
		this.setMenuTitle("New Game", null);
    	this.setSize(new Dimension(500, 350));
	}
	
	private void setupTextFields() {
		Label titleLabel = new Label("Profile", null);
		titleLabel.setBounds(90, 20, 300, 100);
	    this.add(titleLabel);
	    
	    Label playerNameLabel = new Label("Player Name", null);
	    playerNameLabel.setBounds(140, 100, 100, 40);
	    this.add(playerNameLabel);	
	    
	    Label gameNameLabel = new Label("Game Name", null);
	    gameNameLabel.setBounds(140, 150, 100, 40);
	    this.add(gameNameLabel);	
	    
	    Label adressLabel = new Label("IP adress", null);
	    adressLabel.setBounds(140, 200, 100, 40);
	    this.add(adressLabel);
		
	    UserDefaults ud = UserDefaults.getSharedInstance();
	    String lastPlayerName = ud.getString(Constants.PLAYER_NAME_KEY);
		this.playerNameField = new TextField(lastPlayerName);
		this.playerNameField.setPlaceholder("Player1", null);
		this.playerNameField.setBounds(new Rectangle(230, 105, 150, 30));
		this.add(this.playerNameField);

	    String lastGameName = ud.getString(Constants.GAME_NAME_KEY);
		this.gameNameField = new TextField(lastGameName);
		this.gameNameField.setPlaceholder("Game1", null);
		this.gameNameField.setBounds(new Rectangle(230, 155, 150, 30));
		this.add(this.gameNameField);
		
		this.addressField = new TextField(IP.getIpAdressFromInet());
		this.addressField.setBounds(new Rectangle(230, 205, 150, 30));
		this.addressField.setEditable(false);
		this.add(this.addressField);
	}
	
	private void setupButtons() {
		Button createGameButton = new Button("Create game", null);
		createGameButton.addAction(this, "createGame");
		createGameButton.setBounds(new Rectangle(270, 280, 150, 40));
    	this.add(createGameButton);
    	
		Button cancelButton = new Button("Cancel", null);
		cancelButton.addAction(this, "cancelGame");
		cancelButton.setBounds(new Rectangle(80, 280, 150, 40));
    	this.add(cancelButton);
	}
	
	// Actions
	
	public void createGame() {
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
		
		GameController gc = (GameController)this.getFrameController();
		try {
			gc.player = PlayerIHM.launchPlayer(playerName,
					                           gameName,
					                           "newOrleans",
					                           2,
					                           Color.BLACK,
					                           true,
					                           null,
					                           gc);
			gc.showHostWaitingRoomPanel();
		} catch (Exception e)	{
			e.printStackTrace(); 
			System.exit(0);
		}
	}
	
	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.showWelcomeMenuPanel();
	}
}