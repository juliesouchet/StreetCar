package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class SettingsMenuPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	SettingsMenuPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 450));
    	this.setBackground(Color.white); 
    	this.setupTitle();
		this.setupButtons();
	}
	
	private void setupTitle() {
		//place 'Settings' a the top center of the panel
	}
	
	private void setupButtons() {    	    	
		Button saveAndQuitButton = new Button("Save and quit", this, "saveAndQuitGame");
		saveAndQuitButton.setBounds(new Rectangle(270, 380, 150, 40));
    	this.add(saveAndQuitButton);
    	
		Button quitWithoutSavingButton = new Button("Quit without saving", this, "quitWithoutSaving");
		quitWithoutSavingButton.setBounds(new Rectangle(80, 380, 150, 40));
    	this.add(quitWithoutSavingButton);
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
	
	public void quitWithoutSaving() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}
	
	public void saveAndQuitGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}