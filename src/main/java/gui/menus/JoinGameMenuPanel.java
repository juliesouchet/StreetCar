	package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.gui.application.GameController;
import main.java.gui.application.StreetCar;
import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.gui.components.TextField;
import main.java.gui.util.Constants;
import main.java.gui.util.UserDefaults;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class JoinGameMenuPanel extends MenuPanel implements DocumentListener{

	// Properties
	
	private TextField playerNameField;
	private TextField gameNameField;
	private TextField addressField;
	
	Button joinGameButton;
	
	String gameAddress;	
	
	// Constructors
	
	public JoinGameMenuPanel(GameController gc) {
		super(gc);
    	this.setupPanel();
		this.setupTextFields();
		this.setupButtons();
		updateJoinGameButton();
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(500, 350));
    	this.setMenuTitle("Rejoindre une partie", null);
	}	
	
	private void setupTextFields() {
		Label titleLabel = new Label("Profil", null);
		titleLabel.setBounds(90, 20, 300, 100);
	    this.add(titleLabel);
	    
	    Label playerNameLabel = new Label("Nom du joueur", null);
	    playerNameLabel.setBounds(120, 100, 100, 40);
	    this.add(playerNameLabel);	

	    Label gameNameLabel = new Label("Nom de la partie", null);
	    gameNameLabel.setBounds(120, 150, 100, 40);
	    this.add(gameNameLabel);
	    
	    Label adressLabel = new Label("Adresse IP", null);
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
				
		this.addressField = new TextField("");
		gameAddress = addressField.toString();
		addressField.setPlaceholder("ex: 130.190.31.67", null);
		addressField.setBounds(new Rectangle(230, 205, 150, 30));
		this.addressField.getDocument().addDocumentListener(this);
		this.add(this.addressField);
	}
	
	private void setupButtons() {
		joinGameButton = new Button("Rejoindre la partie", null);
		joinGameButton.addAction(this, "joinGame");
		joinGameButton.setBounds(270, 280, 150, 40);
    	this.add(joinGameButton);
    	
		Button cancelButton = new Button("Annuler", null);
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
			StreetCar.player = PlayerIHM.launchPlayer(playerName, 
												   gameName,
												   "newOrleans",
												    2,
												    false,
												    addressField.getText(),
												    gc);
			gc.showClientWaitingRoomPanel();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionFullParty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionHostAlreadyExists e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionUsedPlayerName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionUsedPlayerColor e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionUnknownBoardName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionGameHasAlreadyStarted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public void cancelGame() {
		this.getGameController().showWelcomeMenuPanel();
	}
	
	protected void updateJoinGameButton() {
		String playerName = this.playerNameField.getText();
		String gameName = this.gameNameField.getText();
		String gameAddress = this.addressField.getText();
		if ((playerName.isEmpty() || playerName.trim().equals("")) ||
			(gameName.isEmpty() || gameName.trim().equals(""))	||
			(gameAddress.isEmpty() || gameAddress.trim().equals(""))) {
			joinGameButton.setEnabled(false);
		} else {
			joinGameButton.setEnabled(true);
		}
	}
	
	// Document listener
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// Not needed
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateJoinGameButton();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateJoinGameButton();
	}

	@Override
	public void refreshMenu(PlayerIHM player, Data data) { }

}