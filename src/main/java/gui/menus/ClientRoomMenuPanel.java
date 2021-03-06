package main.java.gui.menus;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.gui.application.GameController;
import main.java.gui.components.AvatarPanel;
import main.java.gui.components.Button;
import main.java.gui.components.ComboBox;
import main.java.gui.components.Label;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class ClientRoomMenuPanel extends MenuPanel {
	
	// Properties
	
	private ArrayList<Label> nameLabels = new ArrayList<Label>();
	private ArrayList<ComboBox> choiceComboBoxes = new ArrayList<ComboBox>();
	private ArrayList<AvatarPanel> avatarImagePanels = new ArrayList<AvatarPanel>();
			
	// Constructors
	
	public ClientRoomMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupPlayersFields();
		this.setupButtons();
	}
	
	private void setupPanel() {
    	this.setSize(new Dimension(700, 500));
    	this.setMenuTitle("Rejoindre une partie", null);
	}	

	private void setupPlayersFields() {
	    String defaultName = Resources.localizedString("Nom", null);
	    String[] adversaryChoices = {
			Resources.localizedString("Joueur", null),
			Resources.localizedString("Ordinateur (facile)", null),
			Resources.localizedString("Ordinateur (moyen)", null),
			Resources.localizedString("Ordinateur (difficile)", null),		  	  
			Resources.localizedString("Fermé", null)
	    };
	    
	    for (int i = 0, y = 140; i < 5; i++, y += 50) {
	    	AvatarPanel imagePanel = new AvatarPanel();
	    	imagePanel.setBounds(160, y, 40, 40);
		    this.add(imagePanel);
		    this.avatarImagePanels.add(imagePanel);
		    
	    	Label nameLabel = new Label(defaultName + " " + i);
		    nameLabel.setBounds(210, y, 100, 40);
		    this.add(nameLabel);
		    this.nameLabels.add(nameLabel);
		    
			ComboBox comboBox = new ComboBox(adversaryChoices);
			comboBox.setBounds(370, y, 150, 40);
			comboBox.setSelectedIndex(4);
			comboBox.setEditable(false);
			this.add(comboBox);
		    this.choiceComboBoxes.add(comboBox);
	    }
	}
	
	private void setupButtons() {    	
		Button cancelButton = new Button("Quitter");
		cancelButton.addAction(this, "cancelGame");
		cancelButton.setBounds(new Rectangle(275, 430, 150, 40));
    	this.add(cancelButton);
	}
	
	// Actions
	
	public void cancelGame() {
		this.getGameController().stopGame();
		this.getGameController().showWelcomeMenuPanel();
	}
	
	// Refresh menu

	public void refreshMenu(PlayerIHM player, Data data) {
		
		if (data.isGameStarted()) {
			GameController gc = this.getGameController();
			gc.showInGamePanel();
			return;
		}
		
		try {
			LoginInfo[] loginInfos = player.getLoginInfo();
			for (int i = 0; i < 5; i++) {
				LoginInfo info = loginInfos[i];
				Label nameLabel = this.nameLabels.get(i);
				ComboBox choiceComboBox = this.choiceComboBoxes.get(i);
				AvatarPanel avatarImagePanel = this.avatarImagePanels.get(i);
				String playerName = info.getPlayerName();
				if (playerName == null) continue;
				
				nameLabel.setText(playerName);
				choiceComboBox.setEditable(!info.isHost());
				avatarImagePanel.setColor(data.getPlayerColor(playerName));
				
				if (info.isClosed()) {
					nameLabel.setText("Connexion fermée", null);
					choiceComboBox.setSelectedIndex(4);
					
				} if (nameLabel.getText() == null && info.isHuman()) {
					nameLabel.setText("En attente de connexion", null);
					choiceComboBox.setSelectedIndex(0);
					
				} else if (!info.isHuman()) {
					int level = info.getAiLevel();
					choiceComboBox.setSelectedIndex(level);
					nameLabel.setText("IA", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
