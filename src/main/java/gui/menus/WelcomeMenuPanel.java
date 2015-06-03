package main.java.gui.menus;

import java.awt.Rectangle;

import main.java.data.Data;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class WelcomeMenuPanel extends MenuPanel {

	// Constructors
	
	public WelcomeMenuPanel(GameController gc) {
		super(gc);
    	this.setupPanel();
		this.setupButtons();
	}
	
	private void setupPanel() {
		this.setMenuTitle("Main Menu", null);
	}
	
	private void setupButtons() {
		Button newGameButton = new Button("New Game");
		newGameButton.addAction(this, "newGameAction");
    	newGameButton.setBounds(new Rectangle(150, 70, 200, 50));
    	this.add(newGameButton);
    	
    	Button joinGameButton = new Button("Join Game", null);
    	joinGameButton.addAction(this, "joinGameAction");
    	joinGameButton.setBounds(new Rectangle(150, 140, 200, 50));
    	this.add(joinGameButton);
    	
    	Button settingsButton = new Button("Settings", null);
    	settingsButton.addAction(this, "settingsAction");
    	settingsButton.setBounds(new Rectangle(150, 210, 200, 50));
    	this.add(settingsButton);
    	
    	Button gameRulesButton = new Button("Rules", null);
    	gameRulesButton.addAction(this, "gameRulesAction");
    	gameRulesButton.setBounds(new Rectangle(150, 280, 200, 50));
    	this.add(gameRulesButton);
    	
    	Button quitButton = new Button("Quit", null);  
    	quitButton.addAction(this, "quitAction");
    	quitButton.setBounds(new Rectangle(150, 350, 200, 50));
    	this.add(quitButton);    	
	}

	// Actions
	
	public void newGameAction() {
		GameController gc = (GameController)this.getFrameController();
		gc.showNewGamePanel();
	}

	public void joinGameAction() {
		GameController gc = (GameController)this.getFrameController();
		gc.showJoinGamePanel();
	}

	public void settingsAction() {
		GameController gc = (GameController)this.getFrameController();
		gc.showSettingsPanel();
	}

	public void gameRulesAction() {
		GameController gc = (GameController)this.getFrameController();
		gc.showRulesPanel();
	}

	public void quitAction() {
		GameController gc = (GameController)this.getFrameController();
		gc.quitGame();
	}

	@Override
	public void refreshMenu(PlayerIHM player, Data data) { }
	
}
