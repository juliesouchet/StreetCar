package test.java.ai;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.java.data.Data;
import main.java.game.UnknownBoardNameException;
import test.java.player.DataViewerFrame;


public class BoardCreator implements Runnable {
	int currentTile;

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
		DataViewerFrame frame; 
		JPanel panelSave, panelTiles;
		TilePanel[] individualTile; 
		JSplitPane verticalPanel, horizontalPanel;
		JButton buttonSave;
		Data data = null;
		try {
			data = new Data("testAffichagePlateau", "newOrleans", 2);
		} catch (UnknownBoardNameException | RuntimeException e) {
			e.printStackTrace();
		}
		
		frame = new DataViewerFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setGameData(data);
		
		panelSave = new JPanel();
		buttonSave = new JButton("Save");
		buttonSave.addMouseListener(new SaveListener());
		panelSave.add(buttonSave);
		
		panelTiles = new JPanel();
		panelTiles.setLayout(new GridLayout(7,2));
		individualTile = new TilePanel[13];
		for(int i = 0; i < 13; i++) {
			individualTile[i] = new TilePanel(tileID[i], i);
			individualTile[i].addMouseListener(new TilePanelListener(this, individualTile[i]));
			panelTiles.add(individualTile[i]);
		}
		
		verticalPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelSave, panelTiles);
		horizontalPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, frame.getViewerPanel(), verticalPanel);
		horizontalPanel.setDividerLocation(14*60);
		horizontalPanel.setResizeWeight(0.75);
		frame.add(horizontalPanel);
		
		frame.setVisible(true);
	}
	
	public void setCurrentTile(int tileNb) {
		currentTile = tileNb;
	}
}
