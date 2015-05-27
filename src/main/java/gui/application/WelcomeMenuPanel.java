package main.java.gui.application;

import java.awt.Rectangle;

import main.java.gui.components.Button;

@SuppressWarnings("serial")
public class MainMenuPanel extends MenuPanel {

	// Constructors
	
	MainMenuPanel() {
		super();
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
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showNewGamePanel();
	}

	public void joinGameAction() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showJoinGamePanel();
	}

	public void settingsAction() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showSettingsPanel();
	}

	public void gameRulesAction() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showRulesPanel();
	}

	public void quitAction() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.quitGame();
	}
	
}
