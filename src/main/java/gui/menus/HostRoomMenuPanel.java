package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.GameInterface;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.ComboBox;
import main.java.gui.components.ImagePanel;
import main.java.gui.components.Label;
import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class HostRoomMenuPanel extends MenuPanel {

	// Properties
	
	private Label[] nameLabels = new Label[5];
	private ComboBox[] choiceComboBoxes = new ComboBox[5];
	private ImagePanel[] avaterImagePanels = new ImagePanel[5];
	
	// Constructors
	
	public HostRoomMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupPlayersFields();
		this.setupButtons();
		
		GameInterface game;
		try {
			game = gc.player.getGame();
			System.out.println(game.getLoginInfo(gc.player.getPlayerName()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(700, 500));
    	this.setMenuTitle("New Game", null);

		Label titleLabel = new Label("Waiting room", null);
		titleLabel.setBounds(120, 20, 300, 100);
	    this.add(titleLabel);
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
			this.add(comboBox);
		    this.choiceComboBoxes[i] = comboBox;
	    }
	    
	    ComboBox hostComboBox = this.choiceComboBoxes[0];
	    hostComboBox.setEditable(false);
	    hostComboBox.setSelectedIndex(0);
	}

	private void setupButtons() {
		Button playGameButton = new Button("Play", null);
		playGameButton.addAction(this, "playGame");
		playGameButton.setBounds(new Rectangle(370, 430, 150, 40));
    	this.add(playGameButton);
    	
		Button cancelButton = new Button("Cancel", null);
		cancelButton.addAction(this, "cancelGame");
		cancelButton.setBounds(new Rectangle(180, 430, 150, 40));
    	this.add(cancelButton);
	}
		
	// Actions
	
	public void playGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.showInGamePanel();
	}
	
	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.player = null;
		gc.showWelcomeMenuPanel();
	}
	
	// InterfaceIHM
	
	public void refresh(Data data) {
		super.refresh(data);
		
		int i = 0;
		for (String playerName : data.getPlayerNameList()) {
			Label nameLabel = this.nameLabels[i];
			nameLabel.setText(playerName);
			i++;
		}
	}
	
}
