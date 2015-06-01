package main.java.gui.board;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import main.java.gui.util.Resources;
import main.java.util.Direction;

public class TileImage {

	// Properties
	
	static Hashtable<String, BufferedImage> tilesHashTable = new Hashtable<String, BufferedImage>();
	String tileID;
	BufferedImage bufferedImage;
	Direction rotation;
	
	// Constructors
	
	public TileImage() {
		
	}
	
	// Actions
	
	public BufferedImage getImage(String tileID) {		
		if (bufferedImage != null) {
			return bufferedImage;
		} else {
			this.bufferedImage = Resources.imageNamed(tileID);
			if (bufferedImage != null) { 
				tilesHashTable.put(tileID, bufferedImage);
				return bufferedImage;
			}
		}
		return bufferedImage;
	}	
	
	public Direction getRotation() {
		return this.rotation;
	}
	
	public String getTileID() {
		return this.tileID;
	}
	
}

