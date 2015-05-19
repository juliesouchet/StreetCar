
package main.java.gui.controllers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import main.java.gui.application.MainMenuPanelController;

public class FrameController implements KeyListener {

    // Properties

    protected JFrame frame;
    protected JMenuBar menuBar;
    protected PanelController panelController;
    
    MainMenuPanelController mainMenuPanelController;

    // Constructors

    public FrameController() {
        this(null);
    }

    public FrameController(PanelController panelController) {
        this.frame = this.createInitialFrame();
        this.frame.addKeyListener(this);
        this.menuBar = this.createInitialMenuBar();
        this.panelController = panelController;
    }

    // Create UI elements

    public JFrame createInitialFrame() {
        JFrame frame = new JFrame("Untitled");
        frame.setSize(1100, 800);
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

    public JPanel getFrameContentPane() {
    	return (JPanel) this.frame.getContentPane();
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

    // Full screen mode

    public boolean isInFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getFullScreenWindow() == this.frame;
    }

    public void toggleFullScreenMode() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.getFullScreenWindow() == this.frame) {
            this.frame.dispose();
            this.frame.setUndecorated(false);
            gd.setFullScreenWindow(null);
            this.frame.setVisible(true);

        } else if (gd.isFullScreenSupported()) {
            this.frame.dispose();
            this.frame.setUndecorated(true);
            gd.setFullScreenWindow(this.frame);
            this.frame.setVisible(true);
        }
    }

    // MouseListener

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == 0 && this.isInFullScreen()) {
            this.toggleFullScreenMode();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
