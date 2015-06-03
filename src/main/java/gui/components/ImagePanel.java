package main.java.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;

@SuppressWarnings("serial")
public class ImagePanel extends Panel{

	// Properties
	
    private BufferedImage image;

    // Constructors
    
    public ImagePanel() {
       this(null);
       this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    }
    
    public ImagePanel(BufferedImage image) {
    	super();
    	this.image = image;
    }
    
    // Setters / getter
    
    public BufferedImage getImage() {
    	return this.image;
    }
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    	this.repaint();
    }
    
    // Drawings

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image != null) {
        	int x = (this.getWidth() - this.image.getWidth()) / 2;
        	int y = (this.getHeight() - this.image.getHeight()) / 2;
        	g.drawImage(this.image, x, y, null);
        }
    }

}
