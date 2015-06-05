package main.java.gui.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.BorderFactory;

import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionForbiddenHostModification;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.gui.application.GameController;
import main.java.gui.application.StreetCar;
import main.java.gui.components.AvatarPanel;
import main.java.gui.components.Button;
import main.java.gui.components.ComboBox;
import main.java.gui.components.Label;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;
import main.java.util.Debug;

@SuppressWarnings("serial")
public class HostRoomMenuPanel extends MenuPanel {

	// Properties

	private ArrayList<Label> nameLabels = new ArrayList<Label>();
	private ArrayList<ComboBox> choiceComboBoxes = new ArrayList<ComboBox>();
	private ArrayList<AvatarPanel> avatarImagePanels = new ArrayList<AvatarPanel>();

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
			AvatarPanel imagePanel = new AvatarPanel();
			imagePanel.setBounds(160, y, 40, 40);
			this.add(imagePanel);
			this.avatarImagePanels.add(imagePanel);

			Label nameLabel = new Label(" " + defaultName);
			nameLabel.setBounds(210, y, 150, 40);
			nameLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			this.add(nameLabel);
			this.nameLabels.add(nameLabel);

			ComboBox comboBox = new ComboBox(adversaryChoices);
			comboBox.setBounds(370, y, 150, 40);
			comboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
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
		GameController gc = this.getGameController();
		try {
			StreetCar.player.hostStartGame();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionNotEnoughPlayers e) {
			e.printStackTrace();
		}
	}

	public void cancelGame() {
		GameController gc = (GameController)this.getFrameController();
		try {
			StreetCar.player.onQuitGame(StreetCar.player.getPlayerName());
		} catch (RemoteException | ExceptionForbiddenAction e) { e.printStackTrace(); }
		gc.showWelcomeMenuPanel();
	}

	public void changePlayerType(ComboBox comboBox) {
		PlayerIHM player = StreetCar.player;

		int index = comboBox.getSelectedIndex();
		boolean isClosed = (index == 4);
		boolean isHuman = (index == 0 || index == 4);
		int aiLevel = index;
		LoginInfo info = new LoginInfo(isClosed, null, false, isHuman, aiLevel);

		try {
			index = this.choiceComboBoxes.indexOf(comboBox);
			player.setLoginInfo(index, info);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenHostModification e) {
			e.printStackTrace();
		}
	}

	private static int nbOfTimesCalled = 0;
	public void refreshMenu(PlayerIHM player, Data data) {
		Debug.printTrace("REFRESH MENU " + ++nbOfTimesCalled);
		try {
			LoginInfo[] loginInfos = player.getLoginInfo();
			for (int i=0; i<loginInfos.length; i++) {
				LoginInfo info = loginInfos[i];

				if(info.isClosed()) closeCell(i);
				else if(info.isOccupiedCell()) showInfoInCell(i, info, data.getPlayerColor(info.getPlayerName()));
				else openCell(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showInfoInCell(int cellIndex, LoginInfo info, Color playerColor) 
	{
		String playerName = info.getPlayerName();
		if(!info.isHuman())
		{
			if(info.getAiLevel() == 1) playerName += " - EASY";
			else if(info.getAiLevel() == 2) playerName += " - MEDIUM";
			else if(info.getAiLevel() == 3) playerName += " - HARD";
		}
		nameLabels.get(cellIndex).setText(playerName);
		choiceComboBoxes.get(cellIndex).setEditable(!info.isHost());
		avatarImagePanels.get(cellIndex).setColor(playerColor);
	}

	private void openCell(int cellIndex) 
	{
		nameLabels.get(cellIndex).setText(" Waiting for connection", null);
		choiceComboBoxes.get(cellIndex).setSelectedIndex(0);
		avatarImagePanels.get(cellIndex).setBackground(Color.gray);
	}

	private void closeCell(int cellIndex) 
	{
		nameLabels.get(cellIndex).setText(" Connection closed", null);
		choiceComboBoxes.get(cellIndex).setSelectedIndex(4);
		avatarImagePanels.get(cellIndex).setBackground(Color.gray);
	}

}
