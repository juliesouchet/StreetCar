package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNoPreviousGameToReach;
import main.java.game.ExceptionNotEnoughTilesInDeck;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTwoManyTilesToDraw;
import main.java.gui.components.Button;
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

	Button validateButton;
	Button beginTripButton;
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
		this.buttonsPanel.setPreferredSize(new Dimension(175, 150));

		beginTripButton = new Button("Commencer le voyage", null);
		validateButton = new Button("Valider", null);
		resetButton = new Button("Annuler le dernier coup", null);
		
		beginTripButton.setEnabled(false);
		//validateButton.setEnabled(false);
		resetButton.setEnabled(false);
		
		beginTripButton.addAction(this, "beginTrip");
		validateButton.addAction(this, "validate");
		resetButton.addAction(this, "reset");
		
		
		beginTripButton.setBounds(0, 15, 165, 35);
		validateButton.setBounds(0, 55, 165, 35);
		resetButton.setBounds(0, 95, 165, 35);

		buttonsPanel.add(beginTripButton);
		buttonsPanel.add(validateButton);
		buttonsPanel.add(resetButton);
		this.add(buttonsPanel, BorderLayout.EAST);	
	}
	
	public void validate() {
		System.out.println("validate");
		try {
			//StreetCar.player.drawTile(2);
			StreetCar.player.drawTile(StreetCar.player.getGameData().getPlayerRemainingTilesToDraw(playerName));
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
	
	public void reset() {
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
		System.out.println("REQUESTING TO BEGIN TRIP");
		if(canBeginTrip)
		{
			Point p = player.getGameData().getPlayerTerminusPosition(playerName)[0];
			
		}
		else System.out.println("CANT BEGIN TRIP");
	}
	
	protected void checkBeginTripButton() {
		/* TODO
		if (!canBeginTrip) {
			beginTripButton.setEnabled(false);
		} else {
			beginTripButton.setEnabled(true);
		}
		*/
		beginTripButton.setEnabled(true);
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
		/*super.paintComponent(g);
		g.drawRect(20, 20, 50, 50); //avatar
		
		g.drawRect(20, 80, 50, 50); //card1
		g.drawRect(80, 80, 50, 50); //card2
		g.drawRect(140, 80, 50, 50); //card3
		g.drawRect(200, 80, 50, 50); //card4
		g.drawRect(260, 80, 50, 50); //card5		

		g.drawRect(350, 80, 50, 50); //station1
		g.drawRect(410, 80, 50, 50); //station2*/
	}
	
	public void refreshGame(PlayerIHM player, Data data) {
		this.player = player;
		player = StreetCar.player;
		try {
			playerName = player.getPlayerName();
			canBeginTrip = data.isTrackCompleted(playerName);
			checkBeginTripButton();
			System.out.println("BEGIN TRIP BUTTON : " + canBeginTrip);
			checkValidateButton(data);
			checkResetButton(data);
			if (data.isPlayerTurn(playerName)) {				
				cardsPanel.setBackground(new Color(0xC9ECEE));
				buttonsPanel.setBackground(new Color(0xC9ECEE));
			} else {
				cardsPanel.setBackground(new Color(0xFFEDDE));
				buttonsPanel.setBackground(new Color(0xFFEDDE));
			}
			cardsPanel.refreshGame(player, data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}
}