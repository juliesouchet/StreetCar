package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;


@SuppressWarnings("serial")
public class MapPanel extends Panel implements MouseListener, ComponentListener {

	// Properties

    private int originX;
    private int originY;
    private int mapWidth;
    private int cellWidth;
    private Data data;
    
    // Constructors
    
	public MapPanel() {
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.addComponentListener(this);

		MapPanelDropTargetListener dropTarget = new MapPanelDropTargetListener(this);
        this.setDropTarget(new DropTarget(this, dropTarget));
	}
	
	// Cells positions
	
	protected void updateMapGeometry() {
		this.mapWidth = (int) (0.96 * Math.min(this.getWidth(), this.getHeight()));
		this.cellWidth = this.mapWidth / this.data.getWidth();
	    this.originX = (this.getWidth() - Math.round(this.mapWidth)) / 2;
	    this.originY = (this.getHeight() - Math.round(this.mapWidth)) / 2;
	    
		this.repaint();
	}
	
	public Point cellPositionForLocation(Point location) {
		if (location.x >= this.originX &&  location.x <= this.originX + this.mapWidth &&
			location.y >= this.originY &&  location.y <= this.originY + this.mapWidth) {
			Point cellPosition = new Point();
			cellPosition.x = (int)((float)(location.x - this.originX) / this.cellWidth);
			cellPosition.y = (int)((float)(location.y - this.originY) / this.cellWidth);
			return cellPosition;
		} else {
			return null;
		}
	}
	
	// Drawings
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.data == null) {
        	System.out.println("data is null");
        	return;
        }
        
        int x = this.originX;
        int y = this.originY;
        Graphics2D g2d = (Graphics2D)g; 
    	Tile[][] board = this.data.getBoard();
    	
		for (int j=0; j < this.data.getHeight(); j++) {
			for (int i=0; i < this.data.getWidth(); i++) {
				TileImage tileImage = new TileImage(board[i][j]);
				tileImage.drawInGraphics(g2d, x, y, this.cellWidth);
				x += this.cellWidth;
			}
			x = this.originX;
			y += this.cellWidth;
		}
    }
	
	// Mouse Listener
	
	public void mouseClicked(MouseEvent e) {
		Point p = this.cellPositionForLocation(e.getPoint());
		if (p != null) {
			System.out.println(p);
		}
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}   
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	

	// ComponentListener
	
	public void componentResized(ComponentEvent e) {
		this.updateMapGeometry();
	}

	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	
	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		this.data = data;
		this.updateMapGeometry();
	}
	
}
