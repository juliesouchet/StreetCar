package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JLabel;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.AvatarPanel;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

public class PlayerPanel extends Panel{

	private static final long serialVersionUID = 1L;

	String nameOfPlayer = null;
	Color playerColor;
	ArrayList<TilePanel> tilePanels = new ArrayList<TilePanel>();
	ArrayList<TilePanel> buildingsPanels = new ArrayList<TilePanel>();

	int sizeOfCard = 45;
	int spaceBetween = 10;

	private AvatarPanel avatarPanel = null;

	public PlayerPanel(String nameOfPlayer) {		
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(285, 175));
		this.setLayout(null);
		this.nameOfPlayer = nameOfPlayer;
		placePlayerAvatar();
		placePlayerName();
		placePlayerCards();
		placePlayerStations();
	}
	
	protected void placePlayerAvatar() {
		this.avatarPanel = new AvatarPanel();
		avatarPanel.setBounds(10, 5, 45, 45);
		this.add(avatarPanel);
	}
	
	protected void placePlayerName() {
		JLabel nameOfPlayerLabel = new JLabel(nameOfPlayer);
		nameOfPlayerLabel.setBounds(70, 11, 80, 30);
		this.add(nameOfPlayerLabel);
	}
	
	protected void placePlayerCards() {
		for (int i=0; i < 5; i++) {
			TilePanel tilePanel = new TilePanel();
			tilePanel.setBounds(sizeOfCard*i + spaceBetween*(i+1), 60, sizeOfCard, sizeOfCard);
			this.add(tilePanel);
			tilePanels.add(tilePanel);
		}
	}
	
	protected void placePlayerStations() {
		for (int i=0; i<2; i++) {
			TilePanel tilePanel = new TilePanel();
			tilePanel.setBounds(sizeOfCard*i + spaceBetween*(i+1), 115, sizeOfCard, sizeOfCard);
			this.add(tilePanel);
			buildingsPanels.add(tilePanel);
		}
	}

	public void refreshPlayerHandCards(String playerName) {
		PlayerIHM player = StreetCar.player;
		Data data = player.getGameData();
		for (int i=0; i<data.getHandSize(playerName); i++) {
			Tile tile = data.getHandTile(playerName, i);
			TilePanel tilePanel = tilePanels.get(i);
			tilePanel.setTile(tile);
		}
		for (int i=data.getHandSize(playerName); i<5; i++) {
			TilePanel tilePanel = tilePanels.get(i);
			tilePanel.setTile(null);
		}
	}

	public void refreshStationCards(String playerName) {
		PlayerIHM player = StreetCar.player;
		Data data = player.getGameData();
		try {
			if(!player.getPlayerName().equals(playerName)) {
				for (int i=0; i < 2; i++) {
					TilePanel tilePanel = buildingsPanels.get(i);
					tilePanel.setTile(null);
					tilePanel.setBackground(Color.GRAY);
				}
			} else {
				Point[] stationsPositions = data.getPlayerAimBuildings(playerName);
				for (int i=0; i<stationsPositions.length; i++) {
					Point p = stationsPositions[i];
					Tile tile = data.getBoard()[p.x][p.y];
					TilePanel tilePanel = buildingsPanels.get(i);
					tilePanel.setTile(tile);
				}
			}
		} catch (RemoteException e) { e.printStackTrace(); }
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int[] xPoints = {230, 253, 276, 253};
		int[] yPoints = {28, 5, 28, 51};
		g.drawPolygon(xPoints, yPoints, 4);
	}

	// Refresh game

	public void refreshGame(PlayerIHM player, Data data) {
		Color playerColor = data.getPlayerColor(nameOfPlayer);
		avatarPanel.setColor(playerColor);
		refreshPlayerHandCards(nameOfPlayer);
		refreshStationCards(nameOfPlayer);
		if (data.isPlayerTurn(nameOfPlayer)) {
			this.setBackground(new Color(0xC9ECEE));
		} else {
			this.setBackground(new Color(0xFFEDDE));
		}
	}
}
