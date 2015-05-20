package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class InGamePanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	InGamePanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(1100, 800));
    	this.setBackground(Color.white); 
    	this.setupButtons();
	}
	
	private void setupButtons() {
		Button createGameButton = new Button("Quit game", this, "quitGame");
		createGameButton.setBounds(new Rectangle(475, 380, 150, 40));
    	this.add(createGameButton);
	}
	
	// Actions
	
	public void quitGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}