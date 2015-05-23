package main.java.gui.components;

import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import main.java.gui.util.OS;
import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class MenuItem extends JMenuItem {
	
	// Constructors
	
	public MenuItem(String title) {
		super(title);
	}
	
	public MenuItem(String title, String comment) {
		super(Resources.localizedString(title, comment));
	}
	
	// Action performer
	
	public void addAction(Object target, String action) {
		this.addActionListener(new ActionPerformer(target, action));
	}
	
	public void addAction(Object target, String action, int key) {
		this.addAction(target, action);
		if (OS.isMac()) {
			KeyStroke ks = KeyStroke.getKeyStroke(key , ActionEvent.META_MASK);
			this.setAccelerator(ks);
		} else {
			KeyStroke ks = KeyStroke.getKeyStroke(key , ActionEvent.CTRL_MASK);
			this.setAccelerator(ks);
		}
	}
}
