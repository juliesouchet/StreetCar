package main.java.gui.application;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.gui.components.TextField;
import main.java.gui.util.Constants;
import main.java.gui.util.IP;
import main.java.gui.util.UserDefaults;

@SuppressWarnings("serial")
public class NewGameMenuPanel extends MenuPanel {

	// Properties

	private TextField gameNameField;
	private TextField playerNameField;
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
	    
	    Label gameNameLabel = new Label("Game name", null);
	    gameNameLabel.setBounds(140, 100, 100, 40);
	    this.add(gameNameLabel);	
	    
	    Label playerNameLabel = new Label("Player name", null);
	    playerNameLabel.setBounds(140, 150, 100, 40);
	    this.add(playerNameLabel);	
	    
	    Label adressLabel = new Label("IP adress", null);
	    adressLabel.setBounds(140, 200, 100, 40);
	    this.add(adressLabel);
		
	    UserDefaults ud = UserDefaults.getSharedInstance();
	    String lastGameName = ud.getString(Constants.GAME_NAME_KEY);
		this.gameNameField = new TextField(lastGameName);
		this.gameNameField.setPlaceholder("Game1", null);
		this.gameNameField.setBounds(new Rectangle(215, 105, 150, 30));
		this.add(this.gameNameField);
	    
	    String lastPlayerName = ud.getString(Constants.PLAYER_NAME_KEY);
		this.playerNameField = new TextField(lastPlayerName);
		this.playerNameField.setPlaceholder("Player1", null);
		this.playerNameField.setBounds(new Rectangle(215, 155, 150, 30));
		this.add(this.playerNameField);
		
		this.addressField = new TextField(IP.getIpAdressFromInet());
		this.addressField.setBounds(new Rectangle(215, 205, 150, 30));
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
		UserDefaults ud = UserDefaults.getSharedInstance();
		ud.setString(Constants.PLAYER_NAME_KEY, this.playerNameField.getText());
		ud.setString(Constants.GAME_NAME_KEY, this.gameNameField.getText());
		
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showHostWaitingRoomPanel();
	}
	
	public void cancelGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}
}
