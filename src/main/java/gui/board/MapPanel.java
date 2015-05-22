package main.java.gui.board;

import java.awt.Graphics;

import main.java.gui.components.Panel;


public class MapPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = (int) (0.9 * Math.min(this.getWidth(), this.getHeight()));
        int widthPerCase = width/12;
        int originX = (this.getWidth() - width) / 2;
        int originY = (this.getHeight() - width) / 2;
        
        for (int i=0; i<11; i++) {
            g.drawLine(originX, originY+widthPerCase*i+widthPerCase, originX+width, originY+widthPerCase*i+widthPerCase); 
            g.drawLine(originX+widthPerCase*i+widthPerCase, originY, originX+widthPerCase*i+widthPerCase, originY+width);
        }
        
        g.drawRect(originX, originY, width, width);
        
    }
    
}