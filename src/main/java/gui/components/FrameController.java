
package main.java.gui.components;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class FrameController {

    // Properties

    protected Frame frame;
    
    // Constructors

    public FrameController() {
        Frame frame = new Frame("Untitled");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setFrameController(this);
        this.frame = frame;
    }

    // Setters / getters

    public Frame getFrame() {
        return frame;
    }

    public Panel getFrameContentPane() {
    	return (Panel) this.frame.getContentPane();
    }
    
    public void setFrameContentPane(Panel panel) {
    	this.frame.setContentPane(panel);
    	this.frame.revalidate();
    	this.frame.repaint();
    }
    
    public JMenuBar getFrameMenuBar() {
    	return this.frame.getJMenuBar();
    }
    
    public void setFrameMenuBar(JMenuBar menuBar) {
    	this.frame.setJMenuBar(menuBar);
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

    public void toggleFullScreen() {
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

}
