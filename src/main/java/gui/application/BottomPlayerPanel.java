package main.java.gui.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class BottomPlayerPanel extends Panel {

	// Properties

	Panel cardsPanel;
	Panel buttonsPanel;
	
	Button validateButton;
	Button beginTripButton;
	
	// Constructors

	public BottomPlayerPanel() {
		setupBottomPanel();
		setupBottomPlayerCardsPanel();
		setupBottomPlayerButtonsPanel();
	}
	
	protected void setupBottomPanel() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(0, 150));
		this.setBackground(Color.WHITE);
		//this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
	}
	
	protected void setupBottomPlayerCardsPanel() {		
		this.cardsPanel = new Panel();
		this.cardsPanel.setBackground(Color.WHITE);
		//this.cardsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
		this.add(cardsPanel, BorderLayout.CENTER);
	}
	
	protected void setupBottomPlayerButtonsPanel() {		
		this.buttonsPanel = new Panel();
		this.buttonsPanel.setLayout(null);
		this.buttonsPanel.setBackground(Color.WHITE);
		this.buttonsPanel.setPreferredSize(new Dimension(150, 150));

		beginTripButton = new Button("Begin trip", null);
		validateButton = new Button("Validate", null);

		beginTripButton.setBounds(10, 25, 130, 40);
		validateButton.setBounds(10, 85, 130, 40);

		buttonsPanel.add(beginTripButton);
		buttonsPanel.add(validateButton);
		this.add(buttonsPanel, BorderLayout.EAST);	
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(20, 20, 50, 50); //avatar
		
		g.drawRect(20, 80, 50, 50); //card1
		g.drawRect(80, 80, 50, 50); //card2
		g.drawRect(140, 80, 50, 50); //card3
		g.drawRect(200, 80, 50, 50); //card4
		g.drawRect(260, 80, 50, 50); //card5		

		g.drawRect(350, 80, 50, 50); //station1
		g.drawRect(410, 80, 50, 50); //station2
	}
}