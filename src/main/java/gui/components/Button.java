package main.java.gui.components;

import java.awt.Color;

import javax.swing.JButton;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class Button extends JButton {

	// Constructors
	
	public Button(String title) {
		super(title);
		this.setUI(new StyledButtonUI());
		this.setBackground(new Color(0xFFFFFF));
	}
	
	public Button(String title, String comment) {
		this(Resources.localizedString(title, comment));
	}
	
	// Action performer
	
	public void addAction(Object target, String action) {
		this.addActionListener(new ActionPerformer(target, action));
	}
}
