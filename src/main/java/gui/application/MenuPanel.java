package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import main.java.gui.components.Panel;
import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class MenuPanel extends Panel {

	// Properties
	
	protected String menuTitle;
	
	// Constructors
	
	public MenuPanel() {
		super();
		
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 450));
    	this.setBackground(Color.white); 
	}

	// Setters / getters
	
	public String getMenuTitle() {
		return this.menuTitle;
	}
	
	public void setMenuTitle(String title) {
		this.menuTitle = title;
		this.repaint();
	}
	
	public void setMenuTitle(String title, String comment) {
		this.setMenuTitle(Resources.localizedString(title, comment));
	}
	
	// Drawings
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int w = this.getWidth();
        int h = this.getHeight();
        g.setColor(Color.BLACK);  
        g.drawLine(0, 0, 0, h);
        g.drawLine(0, 0, w, 0);
        g.drawLine(0, h, w, h);
        g.drawLine(w, 0, w, h);
        
        String title = this.getMenuTitle();
        if (title != null) {
        	FontRenderContext frc = new FontRenderContext(null, true, true);
            Rectangle2D rect = this.getFont().getStringBounds(title, frc);
            
            int barHeight = 30;
            int stringOriginX = (int) (((this.getWidth() - rect.getWidth()) / 2));
            g.drawLine(0, barHeight, w, barHeight);
            g.drawString(this.getMenuTitle(), stringOriginX, 20);
        }
    }
	
}
