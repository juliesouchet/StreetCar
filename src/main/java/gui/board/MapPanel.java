package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.gui.components.Panel;


public class MapPanel extends Panel implements MouseListener {

	// Properties
	
	private static final long serialVersionUID = 1L;
	
    float min;
    float width;
    float widthPerCase;
    int originX;
    int originY;
    Point caseofGrid = new Point();
    
    // Constructors
    
	public MapPanel() {
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
	}
	
	//
	
	protected void changeGlobalValues() {
		this.min = Math.min(this.getWidth(), this.getHeight());
		this.width = (float) (0.96 * min);
		this.widthPerCase = width/14;
	    this.originX = (this.getWidth() - Math.round(width)) / 2;
	    this.originY = (this.getHeight() - Math.round(width)) / 2;
	}
	
	// Paint Component
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        changeGlobalValues();
        
        for (float i=0; i<13; i++) {
        g.drawLine(originX,
        		   originY + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originX + Math.round(width),
        		   originY + Math.round(widthPerCase*i) + Math.round(widthPerCase));
        
        g.drawLine(originX + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originY,
        		   originX + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originY + Math.round(width));
        
        g.drawRect(originX, originY, (int)width+1, (int)width+1);
        }      
    }

	
	// Mouse Action
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() >= originX && 
			e.getY() >= originY && 
			e.getX() <= originX + width && 
			e.getY() <= originY + width) {
				int indexX = (int)((float)(e.getX() - originX) / widthPerCase);
				int indexY = (int)((float)(e.getY() - originY) / widthPerCase);	
				System.out.println("(" + indexX + "," + indexY + ")");
				this.caseofGrid.x = indexX;
				this.caseofGrid.y = indexY;				
			}		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// not needed
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// not needed
	}   

	@Override
	public void mouseEntered(MouseEvent e) {
		// not needed
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not needed
	}
}
