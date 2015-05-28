package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;

import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.ComboBox;
import main.java.gui.components.ImagePanel;
import main.java.gui.components.Label;
import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class ClientRoomMenuPanel extends MenuPanel {
	
	// Properties
	
	private Label[] nameLabels = new Label[5];
	private ComboBox[] choiceComboBoxes = new ComboBox[5];
	private ImagePanel[] avaterImagePanels = new ImagePanel[5];
		
	// Constructors
	
	public ClientRoomMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupPlayersFields();
		this.setupButtons();
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(700, 500));
    	this.setMenuTitle("Join Game", null);
	}	

	private void setupPlayersFields() {
	    String defaultName = Resources.localizedString("Name", null);
	    String[] adversaryChoices = {
			Resources.localizedString("Player", null),
			Resources.localizedString("Computer (easy)", null),
			Resources.localizedString("Computer (medium)", null),
			Resources.localizedString("Computer (hard)", null),		  	  
			Resources.localizedString("Closed", null)
	    };
	    
	    for (int i = 0, y = 140; i < 5; i++, y += 50) {
	    	ImagePanel imagePanel = new ImagePanel();
	    	imagePanel.setBounds(160, y, 40, 40);
		    this.add(imagePanel);
		    this.avaterImagePanels[i] = imagePanel;
		    
	    	Label nameLabel = new Label(defaultName + " " + i);
		    nameLabel.setBounds(210, y, 100, 40);
		    this.add(nameLabel);
		    this.nameLabels[i] = nameLabel;
		    
			ComboBox comboBox = new ComboBox(adversaryChoices);
			comboBox.setBounds(370, y, 150, 40);
			comboBox.setSelectedIndex(4);
			comboBox.setEditable(false);
			this.add(comboBox);
		    this.choiceComboBoxes[i] = comboBox;
	    }
	}
	
	private void setupButtons() {    	
		Button cancelButton = new Button("Cancel");
		cancelButton.addAction(this, "cancelGame");
		cancelButton.setBounds(new Rectangle(275, 430, 150, 40));
    	this.add(cancelButton);
	}
	
	// Actions
	
	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.showWelcomeMenuPanel();
	}
}
