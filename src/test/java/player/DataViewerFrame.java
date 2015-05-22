package test.java.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.java.data.Data;

public class DataViewerFrame extends JFrame {

	// Properties
	private static final long serialVersionUID = 1L;
	ViewerPanel viewerPanel = new ViewerPanel();
	// Constructors
	
	public DataViewerFrame() {
		super();
		this.setSize(new Dimension(800, 800));
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
			
			int x = 0;
			int y = 0;
			for (int i=0; i < this.gameData.getWidth(); i++) {
				for (int j=0; j < this.gameData.getHeight(); j++) {
					x += 40;
					g.setColor(Color.RED);
					g.drawRect(x, y, 40, 40);
				}
				x = 0;
				y += 40;
			}
		}
	}
}
