package test.java.ai;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.java.data.Tile;

public class TilePanel extends JPanel {
	private static final long serialVersionUID = -3553177329312479409L;
	Tile tile;
	String tileName;
	int tileNumber;
	boolean mouseOver;
	
	public TilePanel(String string, int number) {
		super();
		tile = new Tile(string);
		tileName = string;
		tileNumber = number;
		mouseOver = false;
		setSize(60, 60);
	}
	
	public void setTile(Tile t) {
		tile = t;
		tileName = t.getTileID();
	}
	
	public int getTileNumber() {
		return tileNumber;
	}
	
	public void setMouseOver(boolean b) {
		mouseOver = b;
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
		g2.drawImage(img, 0, 0, 60, 60, null);
		
		if(mouseOver) {
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(4));
			g2.drawRect(2, 2, 58, 58);
		}
	}
}
