package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.gui.components.TextField;
import main.java.gui.util.Constants;
import main.java.gui.util.IP;
import main.java.gui.util.UserDefaults;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class NewGameMenuPanel extends MenuPanel implements DocumentListener{

	// Properties

	private TextField playerNameField;
	private TextField gameNameField;
	private TextField addressField;
	
	private Button createGameButton;
	
	// Constructors
	
	public NewGameMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupTextFields();
		this.setupButtons();
		updateCreateGameButton();
	}
	
	// Setup
	
	public void setupPanel() {
		this.setMenuTitle("New Game", null);
    	this.setSize(new Dimension(500, 350));
	}
	
	private void setupTextFields() {
		Label titleLabel = new Label("Profile", null);
		titleLabel.setBounds(90, 20, 300, 100);
	    this.add(titleLabel);
	    
	    Label playerNameLabel = new Label("Player Name", null);
	    playerNameLabel.setBounds(120, 100, 100, 40);
	    this.add(playerNameLabel);	
	    
	    Label gameNameLabel = new Label("Game Name", null);
	    gameNameLabel.setBounds(120, 150, 100, 40);
	    this.add(gameNameLabel);	
	    
	    Label adressLabel = new Label("IP adress", null);
	    adressLabel.setBounds(120, 200, 100, 40);
	    this.add(adressLabel);
		
	    UserDefaults ud = UserDefaults.getSharedInstance();
	    String lastPlayerName = ud.getString(Constants.PLAYER_NAME_KEY);
		this.playerNameField = new TextField(lastPlayerName);
		this.playerNameField.setPlaceholder("Player1", null);
		this.playerNameField.setBounds(new Rectangle(230, 105, 150, 30));
		this.playerNameField.getDocument().addDocumentListener(this);
		this.add(this.playerNameField);

	    String lastGameName = ud.getString(Constants.GAME_NAME_KEY);
		this.gameNameField = new TextField(lastGameName);
		this.gameNameField.setPlaceholder("Game1", null);
		this.gameNameField.setBounds(new Rectangle(230, 155, 150, 30));
		this.gameNameField.getDocument().addDocumentListener(this);
		this.add(this.gameNameField);
		
		this.addressField = new TextField(IP.getIpAdressFromInet());
		this.addressField.setBounds(new Rectangle(230, 205, 150, 30));
		this.addressField.setEditable(false);
		this.add(this.addressField);
	}
	
	private void setupButtons() {
		createGameButton = new Button("Create game", null);
		createGameButton.addAction(this, "createGame");
		createGameButton.setBounds(new Rectangle(270, 280, 150, 40));
		createGameButton.setEnabled(false);
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
		
		GameController gc = this.getGameController();
		try {
			/*
			gc.player = PlayerIHM.launchPlayer(playerName,
					                           gameName,
					                           "newOrleans",
					                           2,
					                           Color.BLACK,
					                           true,
					                           null,
					                           gc);
			*/
			gc.player = PlayerIHM.launchPlayer(playerName, gameName, "newOrleans", 2, true, "127.0.0.1" /*addressField.getText()*/, gc);
			gc.showHostWaitingRoomPanel();
		} catch (Exception e)	{
			e.printStackTrace(); 
			System.exit(0);
		}
	}
	
	public void cancelGame() {
		GameController gc = this.getGameController();
		gc.showWelcomeMenuPanel();
	}

	protected void updateCreateGameButton() {
		String playerName = this.playerNameField.getText();
		String gameName = this.gameNameField.getText();
		if ((playerName.isEmpty() || playerName.trim().equals("")) ||
			    (gameName.isEmpty() || gameName.trim().equals("")))	{
			createGameButton.setEnabled(false);
		} else {
			createGameButton.setEnabled(true);
		}
	}
	
	// Document listener
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// Not needed
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateCreateGameButton();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateCreateGameButton();
	}
}
