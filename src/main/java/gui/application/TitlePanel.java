package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;

import main.java.gui.components.Label;
import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class TitlePanel extends Panel{

	// Properties
	
	Label titleLabel;
	
	// Constructors
	
	public TitlePanel(String title) {
		titleLabel = new Label(title);
		this.setPreferredSize(new Dimension(298, 30));
		this.setBackground(Color.WHITE);
		
		Font font = titleLabel.getFont();
		Font newFont = new Font(font.getFontName(), font.getStyle(), 17);
		titleLabel.setFont(newFont);
		
		this.add(titleLabel);
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }	
}
