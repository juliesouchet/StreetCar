package main.java.gui.board;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import main.java.data.Tile;
import main.java.gui.util.Resources;

public class TileImage {

	// Private
	
	static private Hashtable<String, BufferedImage> tilesHashTable = null;
	
	static {
		tilesHashTable = new Hashtable<String, BufferedImage>();
	}
	
	// Public
	
	public static BufferedImage getImage(Tile tile) {
		String tileID = tile.getTileID();
		BufferedImage tileImage = null;
		
		tileImage = tilesHashTable.get(tileID);
		if (tileImage != null) {
			return tileImage;
		}
		
		tileImage = Resources.imageNamed(tileID);
		if (tileImage != null) { 
			tilesHashTable.put(tileID, tileImage);
			return tileImage;
		}
		
		return tileImage;
	}
	
	public static BufferedImage getRotatedImage(Tile tile) {
		BufferedImage image = TileImage.getImage(tile);
		
		AffineTransform at = new AffineTransform();
		at.translate(image.getWidth() / 2, image.getHeight() / 2);
		at.rotate(Math.toRadians(tile.getTileDirection().getVal()*90));
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	    return op.filter(image, null);
	}
	
	public static void drawTile(Graphics2D g2d, Tile tile, int x, int y, int width) {
		BufferedImage image = TileImage.getImage(tile);
		
		AffineTransform at = new AffineTransform();
		at.translate(x + width / 2, y + width / 2);
		at.rotate(Math.toRadians(tile.getTileDirection().getVal()*90));
		at.scale((double)width / (double)image.getWidth(),
				 (double)width / (double)image.getHeight());
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		g2d.drawImage(image, at, null);
		
		if (tile.isStop()) {
			int stopWidth = (int) (0.5 * width);
			g2d.setColor(Color.RED);
			g2d.fillOval(x + (width-stopWidth)/2, y + (width-stopWidth)/2, stopWidth, stopWidth);
			
			g2d.setColor(Color.WHITE);
	        FontMetrics fm = g2d.getFontMetrics();
	        Rectangle2D r = fm.getStringBounds("S", g2d);
	        int textX = (width - (int) r.getWidth()) / 2;
	        int textY = (width - (int) r.getHeight()) / 2 + fm.getAscent();
	        g2d.drawString("S", x+textX, y+textY);
		}
	}
	
}
