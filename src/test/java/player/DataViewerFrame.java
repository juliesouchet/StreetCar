package test.java.player;

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
		this.setSize(new Dimension(frameWidth+paddingWidth,frameHeight+paddingHeight));
		this.add(this.viewerPanel);
	}

	// Setters / getters
	
	public Data getGameData() {
		return this.viewerPanel.gameData;
	}
	
	public void setGameData(Data data) {
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
			String cst = "src/main/resources/images/";
			BufferedImage img = null;
			String tileName;
			Tile[][] board = this.gameData.getBoard();
			int tileWidth	= (getWidth() - paddingWidth) / this.gameData.getWidth();
			int tileHeight	= (getHeight()- paddingHeight)/ this.gameData.getHeight();
			int x = tileWidth/2;
			int y = tileHeight/2;

			for (int j=0; j < this.gameData.getHeight(); j++)
			{
				for (int i=0; i < this.gameData.getWidth(); i++)
				{
					tileName = cst + board[i][j].getTileID();
					try {img = ImageIO.read(new File(tileName));}
					catch (IOException e) {e.printStackTrace(); System.exit(0);}
					g.drawImage(img, x, y, tileWidth, tileHeight, null);
					x += tileWidth;
				}
				x = tileWidth/2;
				y += tileHeight;
			}
		}
	}
}
