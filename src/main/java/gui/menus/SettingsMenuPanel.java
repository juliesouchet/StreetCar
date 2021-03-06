package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.data.Data;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.util.UserDefaults;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class SettingsMenuPanel extends MenuPanel {

	// Constructors
	
	public SettingsMenuPanel(GameController gc) {
		super(gc);
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
		GameController gc = (GameController)this.getFrameController();
		gc.showWelcomeMenuPanel();
	}

	@Override
	public void refreshMenu(PlayerIHM player, Data data) { }

}