package test.java.util;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.UnknownBoardNameException;
import test.java.player.DataViewerFrame;


public class BoardCreator implements Runnable {
	Tile currentTile;
	TilePanel[] individualTile;
	DataViewerFrame frame;
	Data data = null;

	static final String tilePath = "src/main/resources/images/";
	static final String[] tileID = {"Tile_FFFFZZ2003", "Tile_FFFFZZ2113", "Tile_FFFFZZ060123",
			"Tile_FFFFZZ100102", "Tile_FFFFZZ100103", "Tile_FFFFZZ100203",
			"Tile_TFFFZZ040213", "Tile_TFFFZZ02010213", "Tile_TFFFZZ02021203",
			"Tile_TFFFZZ06031323", "Tile_TFFFZZ06121323", "Tile_TFFFZZ0401122303",
			"Tile_FFFFZZ99"};

	 public static void main(String[] arguments) {
		 	BoardCreator test = new BoardCreator();
	        SwingUtilities.invokeLater(test);
	 }
	 
	 
	@Override
	public void run() {
		JPanel panelSave, tilesGrid;
		JSplitPane verticalPanel, horizontalPanel;
		JButton buttonSave, buttonLoad;
		try {
			data = new Data("Board Creator", "newOrleans", 2);
		} catch (UnknownBoardNameException | RuntimeException e) {
			e.printStackTrace();
		}
		
		frame = new DataViewerFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setGameData(data);
		
		panelSave = new JPanel();
		buttonSave = new JButton("Save");
		buttonSave.addMouseListener(new SaveListener(data));
		panelSave.add(buttonSave);
		buttonLoad = new JButton("Load");
		buttonLoad.addMouseListener(new LoadListener(frame));
		panelSave.add(buttonLoad);
		
		tilesGrid = new JPanel();
		tilesGrid.setLayout(new GridLayout(7,2));
		individualTile = new TilePanel[13];
		for(int i = 0; i < 13; i++) {
			individualTile[i] = new TilePanel(tileID[i], i);
			individualTile[i].addMouseListener(new TilePanelListener(this, individualTile[i]));
			tilesGrid.add(individualTile[i]);
		}
		
		verticalPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelSave, tilesGrid);
		horizontalPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, frame.getViewerPanel(), verticalPanel);
		horizontalPanel.setDividerLocation(14*60);
		horizontalPanel.setResizeWeight(0.75);
		frame.add(horizontalPanel);
		
		frame.setVisible(true);
	}
	
	public void setCurrentTile(Tile tile) {
		currentTile = tile;
	}
	
	public void setTile(int x,int y) {
		System.out.println("setTile pas encore implémenté");
		//data.setTile(x,y,currentTile);
	}
	
	public Tile getCurrentTile() {
		return currentTile;
	}

	public Data getData() {
		return data;
	}

	public void repaintTilePanels() {
		for(int i = 0; i < 13; i++) {
			boolean b = individualTile[i].getTile() == currentTile;
			individualTile[i].setSelection(b);
			individualTile[i].repaint();
		}
	}
}
