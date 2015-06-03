package main.java.gui.board;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import main.java.data.Tile;
import main.java.gui.util.Resources;
import main.java.util.Direction;

public class TileImage {

	// Static
	
	static private Hashtable<String, BufferedImage> tilesHashTable = null;
	
	static {
		tilesHashTable = new Hashtable<String, BufferedImage>();
	}
	
	// Properties
	
	private String tileID;
	private BufferedImage tileImage;
	private Direction rotation;
	
	// Constructors
	
	public TileImage(Tile tile) {
		this.tileID = tile.getTileID();
		this.rotation = tile.getTileDirection();
	}
	
	// Getters
	
	public BufferedImage getImage() {		
		if (this.tileImage != null) {
			return this.tileImage;
		}

		this.tileImage = tilesHashTable.get(this.tileID);
		if (this.tileImage != null) {
			return this.tileImage;
		}
		
		this.tileImage = Resources.imageNamed(this.tileID);
		if (this.tileImage != null) { 
			tilesHashTable.put(this.tileID, this.tileImage);
			return this.tileImage;
		}
		return this.tileImage;
	}	
	
	public Direction getRotation() {
		return this.rotation;
	}
	
	public String getTileID() {
		return this.tileID;
	}
	
	// Drawings
	
	public void drawInGraphics(Graphics2D g2d, int x, int y, int width) {
		BufferedImage image = this.getImage();
		
		AffineTransform at = new AffineTransform();
		at.translate(x + width / 2, y + width / 2);
		at.rotate(Math.toRadians(this.getRotation().getVal()*90));
		at.scale((double)width / (double)image.getWidth(),
				 (double)width / (double)image.getHeight());

		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		
		g2d.drawImage(image, at, null);
	}
	
}
