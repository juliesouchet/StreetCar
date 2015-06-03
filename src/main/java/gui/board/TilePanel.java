package main.java.gui.board;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import main.java.data.Tile;
import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class TilePanel extends Panel implements Transferable {
	
	// Properties
	
	Tile tile;
	TileImage tileImage;
	
	// Constructors
	
	public TilePanel() {
		super();
		this.setTransferHandler(new TilePanelDragTransferHandler());
		this.addMouseListener(new TilePanelDragMouseListener());
	}
	
	public TilePanel(Tile tile) {
		this();
		this.setTile(tile);
	}
	
	// Getters / setters
	
	public Tile getTile() {
		return this.tile;
	}
	
	public void setTile(Tile tile) {
		this.tile = tile;
		if (tile == null) {
			this.tileImage = new TileImage(tile);
			this.getTransferHandler().setDragImage(tileImage.getImage());
			this.revalidate();
		} else {
			this.tileImage = null;
			this.getTransferHandler().setDragImage(null);
			this.revalidate();
		}
	}
	
	// Drawings
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		if (this.tileImage == null) return;
		int min = Math.min(this.getWidth(), this.getHeight());
		this.tileImage.drawInGraphics(g2d, 0, 0, min);
	}
	
	// Transferable
	
	public Object getTransferData(DataFlavor flavor) {
		DataFlavor thisFlavor = null;

		try {
			thisFlavor = TilePanel.getDragDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		if (this.tile != null  && thisFlavor != null && flavor.equals(thisFlavor)) {
			return this.tile;
		}
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {null};
		try {
			flavors[0] = TilePanel.getDragDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		DataFlavor[] flavors = {null};
		try {
			flavors[0] = TilePanel.getDragDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return false;
		}

		for (DataFlavor f : flavors) {
			if (f.equals(flavor)) {
				return true;
			}
		}
		return false;
	}
		
	// Data flavor
	
	private static DataFlavor dragDataFlavor = null;
	
	public static DataFlavor getDragDataFlavor() throws Exception {
		if (dragDataFlavor == null) {
			dragDataFlavor = new DataFlavor(TilePanel.class, "TilePanel");
		}
		return dragDataFlavor;
	}
}
