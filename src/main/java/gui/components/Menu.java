package main.java.gui.components;

import javax.swing.JMenu;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class Menu extends JMenu {

	// Constructors
	
	public Menu(String title) {
		super(title);
	}
	
	public Menu(String title, String comment) {
		this(Resources.localizedString(title, comment));
	}
	
}
