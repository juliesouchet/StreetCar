
package main.java.gui.controllers;

import javax.swing.JPanel;

public class PanelController {

    // Properties

    protected JPanel panel;

    // Constructors

    public PanelController() {
        this(new JPanel());
    }

    public PanelController(JPanel panel) {
        this.panel = panel;
    }

    // Setters / getters

    public JPanel getPanel() {
        return this.panel;
    }

}
