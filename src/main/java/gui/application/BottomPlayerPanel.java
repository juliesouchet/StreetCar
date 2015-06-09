package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNoPreviousGameToReach;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.gui.components.Button;
import main.java.gui.components.Label;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class BottomPlayerPanel extends Panel {

	// Properties

	BottomPlayerCardsPanel cardsPanel;
	Panel buttonsPanel;
	boolean canBeginTrip = false;
	String playerName;
	int numberOfCardPlayed;
	PlayerIHM player;
	Label canBeginText;

	Button validateButton;
	Button showMyTripButton;
	Button resetButton;
	Button stopShowingMyTripButton;
	Button resetMyTripButton;
	
	// Constructors

	public BottomPlayerPanel() {
		setupBottomPanel();
		setupBottomPlayerCardsPanel();
		setupBottomPlayerButtonsPanel();
	}
	
	protected void setupBottomPanel() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(0, 150));
		//this.setBackground(Color.WHITE);
	}
	
	protected void setupBottomPlayerCardsPanel() {		
		this.cardsPanel = new BottomPlayerCardsPanel();
		this.add(cardsPanel, BorderLayout.CENTER);
	}
	
	protected void setupBottomPlayerButtonsPanel() {		
		this.buttonsPanel = new Panel();
		this.buttonsPanel.setLayout(null);
		this.buttonsPanel.setBackground(Color.WHITE);
		this.buttonsPanel.setPreferredSize(new Dimension(300, 150));

		validateButton = new Button("Valider", null);
		resetButton = new Button("Annuler le dernier coup", null);
		showMyTripButton = new Button("Montrer mon chemin", null);
		stopShowingMyTripButton = new Button("Arrêter de montrer mon chemin", null);
		resetMyTripButton = new Button("Recommencer mon chemin", null);
		
		validateButton.setEnabled(false);
		resetButton.setEnabled(false);
		
		validateButton.addAction(this, "validateAction");
		resetButton.addAction(this, "resetAction");
		showMyTripButton.addAction(this, "showMyTripAction");
		stopShowingMyTripButton.addAction(this, "stopShowingMyTripAction");
		resetMyTripButton.addAction(this, "resetMyTripAction");
		
		validateButton.setBounds(0, 55, 280, 35);
		resetButton.setBounds(0, 95, 280, 35);
		showMyTripButton.setBounds(0, 15, 280, 35);
		stopShowingMyTripButton.setBounds(0, 15, 280, 35);
		resetMyTripButton.setBounds(0, 15, 280, 35);
		
		buttonsPanel.add(validateButton);
		buttonsPanel.add(resetButton);
		buttonsPanel.add(showMyTripButton);

		/*canBeginText = new Label("");
		canBeginText.setFont(new Font("Serif", Font.PLAIN, 10));
		canBeginText.setBorder(new LineBorder(Color.BLACK));
		canBeginText.setBounds(0, 15, 200, 35);
		buttonsPanel.add(canBeginText);*/
		
		// TODO display canBeginText
		//canBeginText = new Label("Vous ne pouvez pas commencer votre voyage");
		//canBeginText.setBorder(new LineBorder(Color.BLACK));
		//canBeginText.setBounds(0, 15, 200, 35);
		
		//canBeginText.setFont(new Font("Serif", Font.PLAIN, 10));
		//canBeginText.setFont(getFont());
		
		this.add(buttonsPanel, BorderLayout.EAST);	
	}
	
	public void validateAction() {
		try {
			int nbTiles = StreetCar.player.getGameData().getPlayerRemainingTilesToDraw(playerName);
			if (nbTiles > 0) StreetCar.player.drawTile(nbTiles);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (ExceptionGameHasNotStarted e1) {
			e1.printStackTrace();
		} catch (ExceptionNotYourTurn e1) {
			e1.printStackTrace();
		} catch (ExceptionNotEnoughTilesInDeck e1) {
			e1.printStackTrace();
		} catch (ExceptionTwoManyTilesToDraw e1) {
			e1.printStackTrace();
		} catch (ExceptionForbiddenAction e1) {
			e1.printStackTrace();
		}
		try {
			StreetCar.player.validate();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionGameHasNotStarted e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		}
	}
	
	public void resetAction() {
		try {
			player.rollBack();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionNoPreviousGameToReach e) {
			e.printStackTrace();
		}
	}
	
	public void showMyTripAction() {
		// TODO passe en mode montrer mon chemin
		showMyTripButton.setVisible(false);
		buttonsPanel.add(stopShowingMyTripButton);
		stopShowingMyTripButton.setVisible(true);
	}
	
	public void stopShowingMyTripAction() {
		// TODO passe en mode ne plus montrer mon chemin
		stopShowingMyTripButton.setVisible(false);		
		buttonsPanel.add(showMyTripButton);
		showMyTripButton.setVisible(true);
	}
	
	public void checkInTripButton(Data data) {
		if (data.hasStartedMaidenTravel(playerName)) {
			System.out.println("ON EST EN MAIDEN TRAVEL");
			stopShowingMyTripButton.setVisible(false);
			showMyTripButton.setVisible(false);
			buttonsPanel.add(resetMyTripButton);
			resetMyTripButton.setVisible(true);
		}
	}
	
	public void resetMyTripAction() {
		// TODO recommencer le maiden travel
	}
	
	/*protected void checkIfCanBeginTrip() {
		if (!canBeginTrip) {
			return;
		}
		
		Data data = StreetCar.player.getGameData();
		if (data.getTramPosition(playerName) == null) {
			canBeginText.setText("Vous pouvez commencer votre voyage !");
		} else {
			canBeginText.setText("Vitesse max = " + data.getMaximumSpeed());
		}
	}*/
	
	protected void checkValidateButton(Data data) {
		boolean enabled = false;
		try {
			if (data.getPlayerTurn().equals(playerName)) {
				enabled = !data.hasRemainingAction(this.playerName);
			}
		} catch (Exception e) {
			enabled = false;
		}
		validateButton.setEnabled(enabled);
	}
	
	protected void checkResetButton(Data data) {
		if  (data.getPlayerTurn().equals(playerName)) {
			resetButton.setEnabled(!data.isStartOfTurn(playerName));
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	private int refreshCount = 0;	
	public void refreshGame(PlayerIHM player, Data data) {
		this.refreshCount ++;
		this.player = player;
		player = StreetCar.player;
		try {
			playerName = player.getPlayerName();
			canBeginTrip = data.isTrackCompleted(playerName);
			if (refreshCount > 1) {
				checkValidateButton(data);
				checkResetButton(data);
				checkInTripButton(data);
				cardsPanel.refreshGame(player, data);
			}
			if (data.isPlayerTurn(playerName)) {				
				cardsPanel.setBackground(new Color(0xC9ECEE));
				buttonsPanel.setBackground(new Color(0xC9ECEE));
			} else {
				cardsPanel.setBackground(new Color(0xFFEDDE));
				buttonsPanel.setBackground(new Color(0xFFEDDE));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}