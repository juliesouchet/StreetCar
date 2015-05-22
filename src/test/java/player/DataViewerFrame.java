package test.java.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.java.data.Data;
import main.java.data.Tile;

public class DataViewerFrame extends JFrame {

	// Properties
	public static final int frameWidth	= 800;
	public static final int frameHeight	= 800;
	public static final int	paddingWidth= 50;
	public static final int	paddingHeight= 50;

	private static final long serialVersionUID = 1L;
	ViewerPanel viewerPanel = new ViewerPanel();

	// Constructors

	public DataViewerFrame() {
		super();
		this.setSize(new Dimension(frameWidth, frameHeight));
		this.setContentPane(this.viewerPanel);
	}

	// Setters / getters
	
	public Data getGameData() {
		return this.viewerPanel.gameData;
	}
	
	public void setGameData(Data data) {
		this.viewerPanel.gameData = data;
	}
	
	// ViewerPanel
	
	private class ViewerPanel extends JPanel {
	
		private static final long serialVersionUID = 1L;
		private Data gameData = null;

		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			
			if (this.gameData == null) {
				return;
			}
			String cst = "src/main/resources/images/";
			BufferedImage img = null;
			String tileName;
			Tile[][] board = this.gameData.getBoard();
			int tileWidth	= (frameWidth - paddingWidth) / this.gameData.getWidth();
			int tileHeight	= (frameHeight- paddingHeight)/ this.gameData.getHeight();
			int x = 0;
			int y = 0;

			for (int j=0; j < this.gameData.getHeight(); j++)
			{
				for (int i=0; i < this.gameData.getWidth(); i++)
				{
					g.setColor(Color.RED);
					g.drawRect(x, y, tileWidth, tileHeight);
					tileName = cst + board[i][j].getTileID();
					try {img = ImageIO.read(new File(tileName));}
					catch (IOException e) {e.printStackTrace(); System.exit(0);}
					g.drawImage(img, x, y, tileWidth, tileHeight, null);
					x += tileWidth;
				}
				x = 0;
				y += tileHeight;
			}
		}
	}
}
