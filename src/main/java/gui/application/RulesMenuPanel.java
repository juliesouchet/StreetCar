package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class RulesMenuPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	RulesMenuPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(700, 500));
    	this.setBackground(Color.white); 
    	this.setupTitle();
		this.setupButtons();
	}
	
	private void setupTitle() {
		//place 'Game Rules' a the top center of the panel
	}
	
	private void setupButtons() {    	
		Button cancelButton = new Button("Back to main menu", this, "leaveGame");
		cancelButton.setBounds(new Rectangle(275, 430, 150, 40));
    	this.add(cancelButton);
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 30, 700, 30);    
        g.drawLine(0, 0, 0, 500); //left line
        g.drawLine(0, 0, 700, 0); //top line
        g.drawLine(0, 499, 700, 499); //bottom line
        g.drawLine(699, 0, 699, 500); //right line
    }

	
	// Actions
	
	public void leaveGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}