package test.java.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import main.java.data.Tile;

public class TileGrid extends JComponent {
	/*========================
	 *		Attributes
	 ========================*/
	private static final long serialVersionUID = -3553177329312479409L;
	static final String tilePath = "src/main/resources/images/tiles/";
	static final String[] tileID = {"Tile_FFFFZZ2003", "Tile_FFFFZZ2113", "Tile_FFFFZZ060123",
			"Tile_FFFFZZ100102", "Tile_FFFFZZ100103", "Tile_FFFFZZ100203",
			"Tile_TFFFZZ040213", "Tile_TFFFZZ02010213", "Tile_TFFFZZ02021203",
			"Tile_TFFFZZ06031323", "Tile_TFFFZZ06121323", "Tile_TFFFZZ0401122303",
			"Tile_FFFFZZ99"};
	
	Tile[] grid;
	Integer selected, mouseOver;
	boolean slowRotate; // to slow the tile rotation by mouse wheel
	
	/*========================
	 *		Builder
	 ========================*/
	public TileGrid() {
		super();
		grid = new Tile[13];
		for(int i = 0 ; i < 13; i++) {
			grid[i] = new Tile(tileID[i]);
		}
		selected = mouseOver = null;
		slowRotate = true;
		setSize(140, 490);
	}
	
	/*========================
	 *		Setters
	 ========================*/
	public void setTile(int i, Tile t) {
		grid[i] = t;
	}
		
	public void setSelection(int i) {
		selected = i;
	}	
	
	public void setMouseOver(Integer i) {
		mouseOver = i;
	}
	
	public void rotate(int rotation) {
		if(slowRotate) {
			switch (rotation) {
			case -3 :
				turnRight();
				break;
			case -2 :
				turnHalf();
				break;
			case -1 :
				turnLeft();
				break;
			case 0 :
				return;
			case 1 :
				turnRight();
				break;
			case 2 :
				turnHalf();
				break;
			case 3 :
				turnLeft();
				break;
			default :
				break;
			}
		}
		slowRotate = !slowRotate;
	}
	
	/*========================
	 *		Getter
	 ========================*/
	public Tile getSelected() {
		if (selected == null) return null;
		else return grid[selected];
	}
	
	/*========================
	 *		Painting
	 ========================*/
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		int side = 9*getWidth()/20;
		int padding = getWidth()/20;
		int x;
		int y = padding;
		int i = 0;
		for(int j = 0 ; j < 7; j++) {
			x = padding;
			for (int k = 0; k < 2; k++) {
				if(i==13) break;
				BufferedImage img = null;
				try	{img = ImageIO.read(new File(tilePath+grid[i].getTileID()));}
				catch (IOException e) {e.printStackTrace(); throw new RuntimeException("sdfqsd");}
				AffineTransformOp transform = rotation(img, side);
				g2.drawImage(transform.filter(img,null), x, y, null);
				
				if(selected != null && i == selected) {
					g2.setPaint(Color.BLACK);
					g2.drawRect(x, y, side, side);
				}
				if(mouseOver != null && i == mouseOver) {
					g2.setPaint(Color.WHITE);
					g2.drawRect(x, y, side, side);
				}			
				x += side+padding;
				i++;
			}
			y += side+padding;	
		}	
	}
	
	

	/*=====================
	 *	Private methods
	 =====================*/
	private void turnLeft() {
		for(int i = 0 ; i < 13; i++) {
			grid[i].turnLeft();
		}
	}

	private void turnHalf() {
		for(int i = 0 ; i < 13; i++) {
			grid[i].turnHalf();
		}
	}

	private void turnRight() {
		for(int i = 0 ; i < 13; i++) {
			grid[i].turnRight();
		}
	}
	
	private AffineTransformOp rotation(BufferedImage img, int side) {
		int leftRotations = grid[0].getNbrLeftRotation();
		double rotationRequired = Math.toRadians(leftRotations*270);
		double locationX = side / 2;
		double locationY = side / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		double xScale = (double)side/(double)img.getWidth();
		double yScale = (double)side/(double)img.getHeight();
		tx.concatenate(AffineTransform.getScaleInstance(xScale, yScale));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}

}
