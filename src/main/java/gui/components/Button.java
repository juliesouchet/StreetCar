package main.java.gui.components;

import javax.swing.JButton;

import main.java.gui.util.Resources;

public class Button extends JButton {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	public Button(String title) {
		super(Resources.localizedString(title, null));
	}
	
	public Button(String title, Object target, String action) {
		this(title);
		this.addActionListener(new ActionPerformer(target, action));
	}
	
}
