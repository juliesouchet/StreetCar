package main.java.gui.components;

import java.awt.Container;

import javax.swing.JPanel;

public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Setters / getters

	public FrameController getFrameController() {
		return this.getFrame().getFrameController();
	}
	
	public Frame getFrame() {
		Container parent = this.getParent();
		while (parent != null && !(parent instanceof Frame)) {
			parent = parent.getParent();
		}
		return (Frame)parent;
	}
	
}
