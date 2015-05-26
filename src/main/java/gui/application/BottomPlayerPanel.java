package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BottomPlayerPanel extends JPanel {

	// Properties
	
	private static final long serialVersionUID = 1L;
	
	JPanel bottomPlayerPanel = new JPanel();
	
	// Constructors
	
	public BottomPlayerPanel() {
		bottomPlayerPanel.setPreferredSize(new Dimension(300, 150));
		bottomPlayerPanel.setBackground(Color.WHITE);
		bottomPlayerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
