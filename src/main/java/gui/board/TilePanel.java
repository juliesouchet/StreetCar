package main.java.gui.board;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.TransferHandler;

import main.java.data.Tile;
import main.java.gui.components.Panel;
import main.java.util.Direction;

@SuppressWarnings("serial")
public class TilePanel extends Panel implements Transferable, MouseListener, MouseMotionListener {
	
	// Properties
	
	private Tile tile = null;
	private boolean draggable = false;
	private boolean editable = false;
	private boolean isDragging = false;
	
	// Constructors
	
	public TilePanel() {
		super();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setTransferHandler(new TilePanelDragTransferHandler());
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
		this.repaint();
	}
	
	public boolean isDraggable() {
		return this.draggable;
	}
	
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	// Drawings
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.tile == null) {
			// add image when no tile
		} else {
			Graphics2D g2d = (Graphics2D)g;
			int min = Math.min(this.getWidth(), this.getHeight());
			int x = (this.getWidth() - min) / 2;
			int y = (this.getHeight() - min) / 2;
			TileImage.drawTile(g2d, this.tile, x, y, min);
		}
	}
	
	// MouseListener
	
	public void mouseReleased(MouseEvent e) {
		if (this.isDragging) {
			this.isDragging = false;
			return;
		}
		
		if (this.tile != null && this.editable != false) {
			Direction newDirection = this.tile.getTileDirection().turnLeft();
			this.tile.setDirection(newDirection);
			this.repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	// MouseMotionListener
	
	public void mouseDragged(MouseEvent e) {
		if (this.tile == null || this.draggable == false || this.isDragging == true) {
			return;
		}
		
		BufferedImage image = TileImage.getRotatedImage(this.tile);
		Point offset = new Point(-image.getWidth() / 2, -image.getHeight() / 2);
		
		TransferHandler handler = this.getTransferHandler();
		handler.setDragImage(image);
		handler.setDragImageOffset(offset);
		handler.exportAsDrag(this, e, TransferHandler.COPY);
		
		this.isDragging = true;
		TilePanel.latestDraggedTilePanel = this;
	}

	public void mouseMoved(MouseEvent e) {}
	

	// Transferable
	
	public static TilePanel latestDraggedTilePanel = null; // HACK ;)
	
	public Object getTransferData(DataFlavor flavor) {
		if (this.tile == null) return null;
		
		DataFlavor thisflavor = TilePanel.getDragDataFlavor();
		if (thisflavor != null && flavor.equals(thisflavor)) {
			return this.tile;
		} else {
			return null;
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {null};
		flavors[0] = TilePanel.getDragDataFlavor();
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		DataFlavor[] flavors = {null};
		flavors[0] = TilePanel.getDragDataFlavor();
		for (DataFlavor f : flavors) {
			if (f.equals(flavor)) {
				return true;
			}
		}
		return false;
	}
		
	// Data flavor
	
	private static DataFlavor dragDataFlavor = null;
	
	public static DataFlavor getDragDataFlavor() {
		if (dragDataFlavor == null) {
			dragDataFlavor = new DataFlavor(TilePanel.class, "TilePanel");
		}
		return dragDataFlavor;
	}
}
