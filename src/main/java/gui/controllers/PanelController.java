
package main.java.gui.controllers;

import java.awt.Dimension;

import javax.swing.JPanel;

public class PanelController {

    // Properties

    protected JPanel panel;
    FrameController frameController;

    // Constructors

    public PanelController() {
        this(new JPanel());
    }

    public PanelController(JPanel panel) {
        this.panel = this.createInitialPanel();
    }
    
    public JPanel createInitialPanel() {
    	JPanel panel = new JPanel();
    	panel.setSize(new Dimension(500, 500));
    	return panel;
    }

    // Setters / getters

    public JPanel getPanel() {
        return this.panel;
    }

}
