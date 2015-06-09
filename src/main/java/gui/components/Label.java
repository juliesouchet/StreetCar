package main.java.gui.components;

import javax.swing.JLabel;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class Label extends JLabel {

	// Constructors
	public Label() {
		super();
	}
	
	public Label(String text) {
		this();
		this.setText(text);
	}
	
	public Label(String text, String comment) {
		this(Resources.localizedString(text, comment));
	}
	
	// Setters

	public void setText(String text, String comment) {
		this.setText(Resources.localizedString(text, comment));
	}
	
}
