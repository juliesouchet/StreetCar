package main.java.gui.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.BorderFactory;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionNonInitializedPlayer;
import main.java.game.ExceptionNotEnougthPlayers;
import main.java.gui.application.GameController;
import main.java.gui.components.Button;
import main.java.gui.components.ComboBox;
import main.java.gui.components.ImagePanel;
import main.java.gui.components.Label;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class HostRoomMenuPanel extends MenuPanel {

	// Properties
	
	private ArrayList<Label> nameLabels = new ArrayList<Label>();
	private ArrayList<ComboBox> choiceComboBoxes = new ArrayList<ComboBox>();
	private ArrayList<ImagePanel> avatarImagePanels = new ArrayList<ImagePanel>();
	
	// Constructors
	
	public HostRoomMenuPanel(GameController gc) {
		super(gc);
		this.setupPanel();
		this.setupPlayersFields();
		this.setupButtons();
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
		    this.avatarImagePanels.add(imagePanel);
		    
	    	Label nameLabel = new Label(" " + defaultName);
		    nameLabel.setBounds(210, y, 150, 40);
		    this.add(nameLabel);
		    nameLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		    this.nameLabels.add(nameLabel);
		    
			ComboBox comboBox = new ComboBox(adversaryChoices);
			comboBox.setBounds(370, y, 150, 40);
			this.add(comboBox);
		    this.choiceComboBoxes.add(comboBox);
		    
		    comboBox.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		changePlayerType((ComboBox)e.getSource());
		    	}
		    });
	    }
	    
	    ComboBox hostComboBox = this.choiceComboBoxes.get(0);
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
		try {
			this.getGameController().player.hostStartGame();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionNotEnougthPlayers e) {
			e.printStackTrace();
		} catch (ExceptionNonInitializedPlayer e) {
			e.printStackTrace();
		}
	}
	
	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		gc.player = null;
		gc.showWelcomeMenuPanel();
	}
	
	public void changePlayerType(ComboBox comboBox) {
//////		System.out.println("CHANGE");

		GameController gc = this.getGameController();
		PlayerIHM player = gc.player;
		
		int index = comboBox.getSelectedIndex();
		boolean isClosed = (index == 4);
		boolean isHuman = (index == 0 || index == 4);
		int aiLevel = index;
		String randomName = UUID.randomUUID().toString();
		LoginInfo info = new LoginInfo(isClosed, randomName, false, isHuman, aiLevel);
		
		try {
			index = this.choiceComboBoxes.indexOf(comboBox);
			player.setLoginInfo(index, info);
			System.out.println("ON VIENT DE CHANGER UN LOGIN INFO");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenHostModification e) {
			e.printStackTrace();
		}
	}
	
	// Refresh menu
	
	public void refreshMenu(PlayerIHM player, Data data) {
		if (data == null) {
			System.out.println("DATA VAUT NULL");
			return;
		}
		System.out.println(data.getPlayerNameList());
		for (String playerName : data.getPlayerNameList()) {
			if (data.getPlayerColor(playerName) == null) {
				Color playerColor = data.getRandomUnusedColor();
				data.setPlayerColor(playerName, playerColor);
			}
		}
		
		try {
			LoginInfo[] loginInfos = player.getLoginInfo();
			for (int i = 0; i < 5; i++) {
				
				Label nameLabel = this.nameLabels.get(i);
				ComboBox choiceComboBox = this.choiceComboBoxes.get(i);
				ImagePanel avatarImagePanel = this.avatarImagePanels.get(i);
				
				LoginInfo info = loginInfos[i];
///////				System.out.println(info);
				nameLabel.setText(info.getPlayerName());
				choiceComboBox.setEditable(!info.isHost());
// TODO: erreur: tu ne teste pas si info.isClosed().   Si c isClosed, alors info.getPlayerName() == null
if (!info.isOccupiedCell()) continue;
// TODO: correction bidon, 
				avatarImagePanel.setBackground(data.getPlayerColor(info.getPlayerName()));
				
				System.out.println(i + " " + info);
				if (info.isClosed()) {
					nameLabel.setText(" Connection closed", null);
					choiceComboBox.setSelectedIndex(4);
					
				} if (nameLabel.getText() == null && info.isHuman()) {
					nameLabel.setText(" Waiting connection", null);
					choiceComboBox.setSelectedIndex(0);
					
				} else if (!info.isHuman()) {
					int level = info.getAiLevel();
					choiceComboBox.setSelectedIndex(level);
					nameLabel.setText(" AI", null);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
