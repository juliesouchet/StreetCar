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

	private TextField nameField;
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
	    
	    Label nameLabel = new Label("Name", null);
	    nameLabel.setBounds(140, 115, 100, 40);
	    this.add(nameLabel);	
	    
	    Label adressLabel = new Label("IP adress", null);
	    adressLabel.setBounds(140, 164, 100, 40);
	    this.add(adressLabel);
		
	    UserDefaults ud = UserDefaults.getSharedInstance();
	    String lastName = ud.getString(Constants.PLAYER_NAME_KEY);
		this.nameField = new TextField(lastName);
		this.nameField.setPlaceholder("Player1", null);
		this.nameField.setBounds(new Rectangle(215, 120, 150, 30));
		this.add(this.nameField);
		
		this.addressField = new TextField(IP.getIpAdressFromInet());
		this.addressField.setBounds(new Rectangle(215, 170, 150, 30));
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
		ud.setString(Constants.PLAYER_NAME_KEY, this.nameField.getText());
		
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showHostWaitingRoomPanel();
	}
	
	public void cancelGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}
}
