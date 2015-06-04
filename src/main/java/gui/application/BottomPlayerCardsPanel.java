package main.java.gui.application;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;
import java.util.ArrayList;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.board.TilePanel;
import main.java.gui.components.AvatarPanel;
import main.java.gui.components.Label;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;

@SuppressWarnings("serial")
public class BottomPlayerCardsPanel extends Panel{
	
	ArrayList<TilePanel> tilePanels = new ArrayList<TilePanel>();
	Color playerColor;
	Label lineNumberLabel;
	int lineNumber;
	Color lineNumberBackgroundColor;
	
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
		PlayerIHM player  = StreetCar.player;
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
		
		int x = 350;
		for (int i=0; i<stationsPositions.length; i++) {
			Point p = stationsPositions[i];
			Tile tile = data.getBoard()[p.x][p.y];
			TilePanel stationPanel = new TilePanel(tile);			
			stationPanel.setBounds(x+(i*60), 80, 50, 50);
			stationPanel.setToolTipText("Create your line, it must be connected to these buildings !" );
			this.add(stationPanel);
		}
		
		lineNumber = data.getPlayerLine(playerName);
		lineNumberLabel = new Label(Integer.toString(lineNumber));
		lineNumberLabel.setBounds(281, 20, 50, 50);
		lineNumberLabel.setToolTipText("Your line number. Create a line between your two terminus !");
		this.add(lineNumberLabel);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Avatar
        /*g.setColor(playerColor);
        g.fillRect(20, 20, 50, 50);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 50, 50);*/
        
        // Line number
		int[] xPoints = {260, 285, 310, 285};
		int[] yPoints = {45, 20, 45, 70};
		switch (lineNumber) {
		case 1:
			lineNumberBackgroundColor = new Color(0xFC3939);			
			g.setColor(lineNumberBackgroundColor);
			break;
		case 2:
			g.setColor(Color.YELLOW);
			break;
		case 3:
			g.setColor(Color.GREEN);
			break;
		case 4:
			lineNumberBackgroundColor = new Color(0x00C4FF);
			g.setColor(lineNumberBackgroundColor);
			break;
		case 5:
			g.setColor(Color.WHITE);
			break;
		case 6:
			lineNumberLabel.setForeground(Color.WHITE);
			g.setColor(Color.BLACK);
			break;
		}
		g.fillPolygon(xPoints, yPoints, 4);
		g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 4);
	}
}
