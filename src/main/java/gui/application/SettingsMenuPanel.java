package main.java.gui.application;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.gui.components.Button;
import main.java.gui.util.UserDefaults;

@SuppressWarnings("serial")
public class SettingsMenuPanel extends MenuPanel {

	// Constructors
	
	SettingsMenuPanel() {
		super();
    	this.setupPanel();
		this.setupButtons();
	}
	
	private void setupPanel() {
		this.setMenuTitle("Settings", null);
    	this.setSize(new Dimension(500, 450));
	}
	
	// Actions
	
	private void setupButtons() {    	    	
		Button quitButton = new Button("Save and quit", null);
		quitButton.addAction(this, "saveAndQuitGame");
		quitButton.setBounds(new Rectangle(175, 380, 150, 40));
		this.add(quitButton);
	}
	
	public void saveAndQuitGame() {
		UserDefaults.getSharedInstance().synchronize();
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}