package main.java.gui.components;

import javax.swing.JLabel;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class Label extends JLabel {

	// Constructors
	
	public Label(String text) {
		super(text);
	}
	
	public Label(String text, String comment) {
		this(Resources.localizedString(text, comment));
	}
	
}
