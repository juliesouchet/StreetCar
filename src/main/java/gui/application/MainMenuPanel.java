package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class MainMenuPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	MainMenuPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 450));
    	this.setBackground(Color.white); 
    	this.setupTitle();
		this.setupButtons();	
	}
	
	private void setupTitle() {
		//place 'welcome' at the top center of the panel
	}
	
	private void setupButtons() {
		Button newGameButton = new Button("New Game", this, "newGameAction");     	
    	Button joinGameButton = new Button("Join Game", this, "joinGameAction");
    	Button settingsButton = new Button("Settings", this, "settingsAction");
    	Button gameRulesButton = new Button("Rules", this, "gameRulesAction");
    	Button quitButton = new Button("Quit",this, "quitAction");  

    	newGameButton.setBounds(new Rectangle(150, 70, 200, 50));
    	joinGameButton.setBounds(new Rectangle(150, 140, 200, 50));
    	settingsButton.setBounds(new Rectangle(150, 210, 200, 50));
    	gameRulesButton.setBounds(new Rectangle(150, 280, 200, 50));
    	quitButton.setBounds(new Rectangle(150, 350, 200, 50));
    	
    	this.add(newGameButton);
    	this.add(joinGameButton);
    	this.add(settingsButton);
    	this.add(gameRulesButton);
    	this.add(quitButton);    	
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 30, 500, 30);    
        g.drawLine(0, 0, 0, 450); //left line
        g.drawLine(0, 0, 500, 0); //top line
        g.drawLine(0, 449, 500, 449); //bottom line
        g.drawLine(499, 0, 499, 450); //right line
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
