package main.java.gui.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class LinePanel extends Panel{
	
	BufferedImage bufferedImage;
	
	public LinePanel() {
		super();
	}
	
	public LinePanel(BufferedImage bufferedImage) {
		this();
		this.bufferedImage = bufferedImage;
	}
	
	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}
	
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bufferedImage != null) {
			g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		
	}
}
