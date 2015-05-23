package test.java.util;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.UnknownBoardNameException;
import test.java.player.DataViewerFrame;
import test.java.player.DataViewerFrame.ViewerPanel;


public class BoardCreator implements Runnable {
	static final String boardPath = "src/test/resources/boards/";
	final int padding = 150;
	
	Tile currentTile = null;
	TileGrid tileGrid;
	DataViewerFrame frame;
	Data data = null;


	 public static void main(String[] arguments) {
		 	BoardCreator test = new BoardCreator();
	        SwingUtilities.invokeLater(test);
	 }
	 
	 
	@Override
	public void run() {
		JPanel panelSave;
		JSplitPane verticalPanel, horizontalPanel;
		JButton buttonSave, buttonLoad;
		ViewerPanel board;
		// Lecture du terrain initial
		try {
			data = new Data("Board Creator", "newOrleans", 2);
		} catch (UnknownBoardNameException | RuntimeException e) {
			e.printStackTrace();
		}
		frame = new DataViewerFrame();
		
		// Terrain
		frame.setGameData(data);
		board = frame.getViewerPanel();
		board.addMouseListener(new BoardListener(this));
		
		// Boutons
		panelSave = new JPanel();
		buttonSave = new JButton("Save");
		buttonSave.addMouseListener(new SaveListener(data));
		panelSave.add(buttonSave);
		buttonLoad = new JButton("Load");
		buttonLoad.addMouseListener(new LoadListener(frame));
		panelSave.add(buttonLoad);
		
		// Tuiles � choisir
		tileGrid = new TileGrid();
		
		// Division de la fen�tre
		verticalPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelSave, tileGrid);
		horizontalPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, frame.getViewerPanel(), verticalPanel);
		horizontalPanel.setDividerLocation(14*60);
		horizontalPanel.setResizeWeight(0.5);
		frame.add(horizontalPanel);
		
		// Manipulation � la souris
		tileGrid.addMouseListener(new TileGridListener(this, tileGrid));
		tileGrid.addMouseMotionListener(new TileGridListener(null, tileGrid));		
		frame.addMouseWheelListener(new TileGridListener(null, tileGrid));
		
		// D�marrage de la fen�tre
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(frame.getWidth()+padding, frame.getHeight()));
		frame.setVisible(true);
	}
	
	public void setCurrentTile(Tile tile) {
		currentTile = tile;
	}
	
	public void drawTile(int x,int y) {
		data.setTile(x,y, new Tile(currentTile));
	}
	
	public ViewerPanel getViewerPanel() {
		return frame.getViewerPanel();
	}
	
	public Tile getCurrentTile() {
		return currentTile;
	}

	public Data getData() {
		return data;
	}
/*
	public void repaintTilePanels() {
		for(int i = 0; i < 13; i++) {
			boolean b = tileGrid[i].getTile() == currentTile;
			tileGrid[i].setSelection(b);
			tileGrid[i].repaint();
		}
	}*/
}
