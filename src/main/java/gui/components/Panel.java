package main.java.gui.components;

import java.awt.Container;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel {

	// Setters / getters

	public FrameController getFrameController() {
		return this.getFrame().getFrameController();
	}
	
	public Frame getFrame() {
		Container parent = this.getParent();
		if(parent == null) return null;
		while (!(parent instanceof Frame)) {
			parent = parent.getParent();
		}
		return (Frame)parent;
	}
	
}
