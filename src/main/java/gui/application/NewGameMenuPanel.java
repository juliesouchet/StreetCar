package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class NewGameMenuPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	NewGameMenuPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 450));
    	this.setBackground(Color.black); 
		this.setupButtons();
	}
	
	private void setupButtons() {
		Button otherButton = new Button("Other", this, "other");
		otherButton.setBounds(new Rectangle(150, 70, 200, 50));
    	this.add(otherButton);
	}
	
	// Actions
	
	public void other() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}