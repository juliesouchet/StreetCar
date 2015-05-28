package main.java.gui.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;

import main.java.data.Data;
import main.java.gui.application.GameController;
import main.java.gui.components.Panel;
import main.java.gui.util.Resources;
import main.java.rubbish.InterfaceIHM;

@SuppressWarnings("serial")
public class MenuPanel extends Panel implements InterfaceIHM {

	// Properties
	
	protected String menuTitle;
	protected GameController gc;
	
	// Constructors
	
	public MenuPanel(GameController gameController) {
		super();
		
		this.gc = gameController;
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 450));
    	this.setBackground(Color.white);
    	this.setBorder(BorderFactory.createLineBorder(Color.black)); 
	}

	// Getters
	
	public String getMenuTitle() {
		return this.menuTitle;
	}
	
	public GameController getGameController() {
		return this.gc;
	}
	
	// Setters
	
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
        
        String title = this.getMenuTitle();
        if (title != null) {
        	FontRenderContext frc = new FontRenderContext(null, true, true);
            Rectangle2D rect = this.getFont().getStringBounds(title, frc);
            
            int barHeight = 30;
            int stringOriginX = (int) (((this.getWidth() - rect.getWidth()) / 2));
            g.setColor(Color.BLACK); 
            g.drawLine(0, barHeight, this.getWidth(), barHeight);
            g.drawString(this.getMenuTitle(), stringOriginX, 20);
        }
    }

	// Interface IHM
	
	public void refresh(Data data) {}

	public void excludePlayer() {}
	
}
