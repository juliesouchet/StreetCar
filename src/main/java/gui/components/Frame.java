package main.java.gui.components;

import javax.swing.JFrame;

public class Frame extends JFrame {

	// Properties
	
	private static final long serialVersionUID = 1L;

	protected FrameController frameController = null;
	
	// Constructors
	
	public Frame(String title) {
		super(title);
		this.setContentPane(new Panel());
	}
	
	// Setters / getters
	
	public FrameController getFrameController() {
		return this.frameController;
	}
	
	public void setFrameController(FrameController fc) {
		this.frameController = fc;
	}
	
}
