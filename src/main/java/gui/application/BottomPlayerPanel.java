package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;

import javax.swing.border.LineBorder;

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
	//Button beginTripButton;
	Button resetButton;
	
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
		this.buttonsPanel.setPreferredSize(new Dimension(205, 150));

		validateButton = new Button("Valider", null);
		resetButton = new Button("Annuler le dernier coup", null);
		
		resetButton.setEnabled(false);
		
		validateButton.addAction(this, "validateAction");
		resetButton.addAction(this, "resetAction");
		
		
		//beginTripButton.setBounds(0, 15, 200, 35);
		validateButton.setBounds(0, 55, 200, 35);
		resetButton.setBounds(0, 95, 200, 35);

		buttonsPanel.add(validateButton);
		buttonsPanel.add(resetButton);
		
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
	
	// TODO delete this?
	public void beginTrip() {
		if(canBeginTrip)
		{
			Point p = player.getGameData().getPlayerTerminusPosition(playerName)[0];
			
		}
	}
	
	protected void checkIfCanBeginTrip() {
		if (!canBeginTrip) {
			return;
		}
		
		Data data = StreetCar.player.getGameData();
		if (data.getTramPosition(playerName) == null) {
			canBeginText = new Label("Vous pouvez commencer votre voyage !");
			canBeginText.setFont(new Font("Serif", Font.PLAIN, 10));
			canBeginText.setBorder(new LineBorder(Color.BLACK));
			canBeginText.setBounds(0, 15, 200, 35);
			buttonsPanel.add(canBeginText);
		} else {
			canBeginText = new Label("Vitesse max = " + data.getMaximumSpeed());
		}
	}
	
	protected void checkValidateButton(Data data) {
		Boolean enabled;
		try {
			enabled = !data.hasRemainingAction(this.playerName);
		} catch (Exception e) {
			enabled = false;
		}
		validateButton.setEnabled(enabled);
	}
	
	protected void checkResetButton(Data data) {
		/*numberOfCardPlayed = 5 - data.getHandSize(playerName);
		if (numberOfCardPlayed != 0) {
			resetButton.setEnabled(true);
		} else {
			resetButton.setEnabled(false);
		}*/
		
		resetButton.setEnabled(!data.isStartOfTurn(playerName));
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
				checkIfCanBeginTrip();
				checkValidateButton(data);
				checkResetButton(data);
				cardsPanel.refreshGame(player, data);
			}
			if (data.isPlayerTurn(playerName)) {				
				cardsPanel.setBackground(new Color(0xC9ECEE));
				buttonsPanel.setBackground(new Color(0xC9ECEE));
			} else {
				cardsPanel.setBackground(new Color(0xFFEDDE));
				buttonsPanel.setBackground(new Color(0xFFEDDE));
			}

			//beginTripButton.setEnabled(data.isTrackCompleted(playerName));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}