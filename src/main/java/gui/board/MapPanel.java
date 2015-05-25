package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.gui.components.Panel;


public class MapPanel extends Panel implements MouseListener {

	private static final long serialVersionUID = 1L;
	
	public MapPanel() {
		this.setBackground(Color.WHITE);		
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        float min = Math.min(this.getWidth(), this.getHeight());
        float width = (float) (0.9 * min);
        float widthPerCase = width/12;
        int originX = (this.getWidth() - Math.round(width)) / 2;
        int originY = (this.getHeight() - Math.round(width)) / 2;
        
        for (float i=0; i<11; i++) {    
            g.drawLine(originX, 
            		   originY + Math.round(widthPerCase*i) + Math.round(widthPerCase),
                       originX + Math.round(width),
                       originY + Math.round(widthPerCase*i) + Math.round(widthPerCase)); 
            
            g.drawLine(originX + Math.round(widthPerCase*i) + Math.round(widthPerCase), 
            		   originY, 
            		   originX + Math.round(widthPerCase*i) + Math.round(widthPerCase), 
            		   originY + Math.round(width));
        }
        
        g.drawRect(originX, originY, (int)width+1, (int)width+1);        
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Mouse clicked");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		System.out.println("Mouse entered");
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		System.out.println("Mouse exited");		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("Mouse pressed");		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.println("Mouse released");		
	}    
}
