package test.java.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import main.java.data.Tile;

public class TilePanel extends JComponent {
	private static final long serialVersionUID = -3553177329312479409L;
	final int side = 60;
	Tile tile;
	String tileName;
	int tileNumber;
	boolean isSelected, mouseOver;
	
	public TilePanel(String string, int number) {
		super();
		tile = new Tile(string);
		tileName = string;
		tileNumber = number;
		isSelected = false;
		mouseOver = false;
	}
	
	public void setTile(Tile t) {
		tile = t;
		tileName = t.getTileID();
	}
		
	public void setSelection(boolean b) {
		isSelected = b;
	}	
	
	public void setMouseOver(boolean b) {
		mouseOver = b;
	}
	
	public int getTileNumber() {
		return tileNumber;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public void rotateTile(int orientation) {
		int nbrLeftRotation = tile.getNbrLeftRotation();
		switch (nbrLeftRotation - orientation) {
		case -3 :
			tile.turnRight();
			break;
		case -2 :
			tile.turnHalf();
			break;
		case -1 :
			tile.turnLeft();
			break;
		case 0 :
			return;
		case 1 :
			tile.turnRight();
			break;
		case 2 :
			tile.turnHalf();
			break;
		case 3 :
			tile.turnLeft();
			break;
		default :
			break;
		}
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; 		
		BufferedImage img = null;
		try	{img = ImageIO.read(new File(BoardCreator.tilePath+tileName));}
		catch (IOException e) {e.printStackTrace(); System.exit(0);}
		g2.drawImage(img, 0, 0, side, side, null);
		
		if(isSelected) {
			g2.setPaint(Color.BLACK);
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(0, 0, side, side);
		}
		if(mouseOver) {
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(0, 0, side, side);
		}
	}
}
