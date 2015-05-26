package test.java.player;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.GameInterface;

public class DataViewerFrame extends JFrame {

	// Properties
	public static final int frameWidth	= 800;
	public static final int frameHeight	= 800;
	public static final int	paddingWidth= 50;
	public static final int	paddingHeight= 50;

	private static final long serialVersionUID = 1L;
	private int tileWidth;
	private int tileHeight;
	ViewerPanel viewerPanel = new ViewerPanel();

	// Constructors

	public DataViewerFrame(GameInterface game, String playerName) {
		super();
		this.setSize(new Dimension(frameWidth+paddingWidth,frameHeight+paddingHeight));
		this.add(this.viewerPanel);
		this.viewerPanel.addMouseListener(new TestMouseListener(game, playerName, paddingWidth, paddingHeight, tileWidth, tileHeight));
	}

	// Setters / getters
	
	public Data getGameData() {
		return this.viewerPanel.gameData;
	}
	
	public void setGameData(Data data) {
		this.tileWidth	= (getWidth() - paddingWidth) / data.getWidth();
		this.tileHeight	= (getHeight()- paddingHeight)/ data.getHeight();
		this.viewerPanel.gameData = data;
	}
	
	public ViewerPanel getViewerPanel() {
		return viewerPanel;
	}
	
	// ViewerPanel
	public class ViewerPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
		private Data gameData = null;
		
		protected void paintComponent(Graphics g) {
			super.paintComponents(g);

			if (this.gameData == null) {
				return;
			}
			String cst = "src/main/resources/images/tiles/";
			BufferedImage img = null;
			String tileName;
			Tile[][] board = this.gameData.getBoard();
			int x = tileWidth/2;
			int y = tileHeight/2;

			for (int j=0; j < this.gameData.getHeight(); j++)
			{
				for (int i=0; i < this.gameData.getWidth(); i++)
				{
					tileName = cst + board[i][j].getTileID();
					try {img = ImageIO.read(new File(tileName));}
					catch (IOException e) {e.printStackTrace(); System.exit(0);}
					AffineTransformOp transform = getRotation(board[i][j], img, tileWidth, tileHeight);
					g.drawImage(transform.filter(img,null), x, y, null);
					x += tileWidth;
				}
				x = tileWidth/2;
				y += tileHeight;
			}
		}
	}
	
	private AffineTransformOp getRotation(Tile tile, BufferedImage img, int tileWidth, int tileHeight) {
		int leftRotations = tile.getNbrLeftRotation();
		double rotationRequired = Math.toRadians(leftRotations*270);
		double locationX = tileWidth / 2;
		double locationY = tileHeight / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		double xScale = (double)tileWidth/(double)img.getWidth();
		double yScale = (double)tileHeight/(double)img.getHeight();
		tx.concatenate(AffineTransform.getScaleInstance(xScale, yScale));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
}
