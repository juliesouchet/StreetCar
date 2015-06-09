package main.java.gui.menus;

import java.awt.Dimension;

import main.java.data.Data;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class MessageMenuPanel extends MenuPanel {

	// Properties

	private Label messageLabel = null;
	private Button button = null;
	private Runnable runnable = null;

	// Constructors

	public MessageMenuPanel(GameController gc) {
		super(gc);

		this.setSize(new Dimension(500, 160));
		this.setMenuTitle("Alert", null);
		
		this.messageLabel = new Label();
		this.messageLabel.setBounds(50, 50, 400, 30);
		this.add(this.messageLabel);
		
		this.button = new Button("OK");
		this.button.setBounds(210, 100, 80, 30);
		this.button.addAction(this, "performButtonAction");
		this.add(this.button);
	}

	// Getters
	
	public String getMessage() {
		return this.messageLabel.getText();
	}
	
	public String getButtonTitle() {
		return this.button.getText();
	}
	// Setters
	
	public void setMessage(String message) {
		this.messageLabel.setText(message);
	}
	
	public void setButtonTitle(String title) {
		this.button.setText(title);
	}
	
	public void setButtonAction(Runnable runnable) {
		this.runnable = runnable;
	}
	
	// Actions
	
	public void performButtonAction() {
		if (this.runnable != null) {
			this.runnable.run();
		}	
	}

	// Abstract methods

	public void refreshMenu(PlayerIHM player, Data data) {}
	
}
