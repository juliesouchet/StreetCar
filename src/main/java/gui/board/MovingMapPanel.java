package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.java.gui.components.ImagePanel;
import main.java.gui.components.Panel;
import main.java.gui.util.Resources;


@SuppressWarnings("serial")
public class MovingMapPanel extends Panel {

	// Properties
	
	BufferedImage bufferedImage;
	ImagePanel imagePanel;
	
	public MovingMapPanel() {
		super();
		bufferedImage = Resources.imageNamed("background");
		this.imagePanel = new ImagePanel();
		this.add(this.imagePanel);
		this.imagePanel.setImage(bufferedImage);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(25, 201, 29));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if (bufferedImage != null) {
			int x = (this.getWidth() - bufferedImage.getWidth()) / 2;
			int y = (this.getHeight() - bufferedImage.getHeight()) / 2;
			g.drawImage(bufferedImage, x, y, null);
		}
		

		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
	}
}
