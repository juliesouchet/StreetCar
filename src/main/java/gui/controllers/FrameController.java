
package main.java.gui.controllers;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class FrameController {

    // Properties

    protected JFrame frame;
    protected JMenuBar menuBar;
    protected PanelController panelController;

    // Constructors

    public FrameController() {
        this(null);
    }

    public FrameController(PanelController panelController) {
        this.frame = this.createInitialFrame();
        this.menuBar = this.createInitialMenuBar();
        this.panelController = panelController;
    }

    // Create UI elements

    public JFrame createInitialFrame() {
        JFrame frame = new JFrame("Untitled");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    public JMenuBar createInitialMenuBar() {
        return null;
    }

    // Setters / getters

    public JFrame getFrame() {
        return frame;
    }

    public PanelController getPanelController() {
        return this.panelController;
    }

    public void setPanelController(PanelController panelController) {
        this.panelController = panelController;
    }

    // Show / hide frame

    public void showFrame() {
        this.frame.setVisible(true);
    }

    public void hideFrame() {
        this.frame.setVisible(false);
    }

    public void toggleFullscreenMode() {
        if (!this.frame.isUndecorated()) {
            this.frame.setUndecorated(true);
            this.frame.setSize(this.frame.getToolkit().getScreenSize());
        } else {
            this.frame.setUndecorated(false);
            this.frame.setSize(new Dimension(1100, 800));
        }
        this.frame.setLocationRelativeTo(null);
        this.frame.validate();
    }

}
