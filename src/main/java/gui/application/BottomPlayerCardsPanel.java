package main.java.gui.application;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.AvatarPanel;
import main.java.gui.components.Label;
import main.java.gui.components.LinePanel;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class BottomPlayerCardsPanel extends Panel{
	
	ArrayList<TilePanel> tilePanels = new ArrayList<TilePanel>();
	Color playerColor;
	Label lineNumberLabel;
	int lineNumber;
	Color lineNumberBackgroundColor;
	String playerName;
	
	public BottomPlayerCardsPanel() {
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		placePlayerName();
		placeAvatar();
		placeTiles();
		placeObjectives();
	}
	
	protected void placePlayerName() {
		PlayerIHM player  = StreetCar.player;
		try {
			String playerName = player.getPlayerName();
			Label playerNameLabel = new Label(playerName);
			playerNameLabel.setBounds(80, 27, 150, 30);
			this.add(playerNameLabel);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void placeAvatar() {
		try {
			PlayerIHM player  = StreetCar.player;
			Data data = player.getGameData();
			String playerName = player.getPlayerName();
			playerColor = data.getPlayerColor(playerName);
			AvatarPanel avatarPanel = new AvatarPanel(playerColor);
			avatarPanel.setBounds(20, 20, 50, 50);
			this.add(avatarPanel);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected void placeTiles() {
		PlayerIHM player = StreetCar.player;
		Data data = player.getGameData();
		try {
			String playerName = player.getPlayerName();
			for (int i = 0; i < 5; i++) {
				Tile tile = data.getHandTile(playerName, i);
				TilePanel tilePanel = new TilePanel(tile);
				tilePanel.setEditable(true);
				tilePanel.setDraggable(true);
				tilePanel.setBounds(20 + 60 * i, 80, 50, 50);
				this.add(tilePanel);
				this.tilePanels.add(tilePanel);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected void placeObjectives() {

		PlayerIHM player  = StreetCar.player;
		Data data = player.getGameData();
		String playerName = null;
		try {
			playerName = StreetCar.player.getPlayerName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Point[] stationsPositions = data.getPlayerAimBuildings(playerName);
		
		int x = 325;
		for (int i=0; i<stationsPositions.length; i++) {
			Point p = stationsPositions[i];
			Tile tile = data.getBoard()[p.x][p.y];
			TilePanel stationPanel = new TilePanel(tile);			
			stationPanel.setBounds(x+(i*60), 80, 50, 50);
			stationPanel.setToolTipText("Crééz votre ligne en passant par ces deux stations !" );
			this.add(stationPanel);
		}
		
		lineNumber = data.getPlayerLine(playerName);
		//lineNumberLabel = new Label(Integer.toString(lineNumber));
		LinePanel linePanel = new LinePanel();
		linePanel.setLineNumber(lineNumber);
		linePanel.setBounds(260, 20, 50, 50);
		linePanel.setToolTipText("Votre numéro de ligne. Crééz votre ligne entre vos deux terminus !");
		this.add(linePanel);
		//lineNumberLabel.setBounds(281, 20, 50, 50);
		//lineNumberLabel.setToolTipText("Your line number. Create a line between your two terminus !");
		//this.add(lineNumberLabel);
	}
	
	public void refreshPlayerHandCards(String playerName, Data data) { 
		for (int i=0; i<data.getHandSize(playerName); i++) {
			Tile tile = data.getHandTile(playerName, i);
			TilePanel tilePanel = this.tilePanels.get(i);
			tilePanel.setTile(tile);
		}
		for (int i=data.getHandSize(playerName); i<5; i++) {
			TilePanel tilePanel = this.tilePanels.get(i);
			tilePanel.setTile(null);	
		}
	}
	
	public void refreshGame(PlayerIHM player, Data data) {
		player = StreetCar.player;
		try {
			playerName = player.getPlayerName();
			refreshPlayerHandCards(playerName, data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// TODO remove below
		System.out.println("allowed speed : " + data.getMaximumSpeed());
	}
}
