package main.java.gui.components;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class ComboBox extends Panel implements ActionListener, MouseListener, ComponentListener {

	// Properties
	
	  private JPopupMenu popupMenu = new JPopupMenu();
	  private ArrayList<ActionPerformer> performers;
	  private Label label;
	  private int selectedIndex = 0;
	  private boolean editable = true;
	
	// Constructors
	
	public ComboBox() {
		this.performers = new ArrayList<ActionPerformer>();
		this.label = new Label("");
		this.label.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(this.label);
		this.addMouseListener(this);
	}
	  
	public ComboBox(String[] strings) {
		this();
		this.setStrings(strings);
	}
	
	// Setters / getters
	
	public String[] getStrings() {
		int count = this.getComponents().length;
		String[] strings = new String[count];
		 for (int i = 0; i < count; i++) {
			 MenuItem item = (MenuItem)this.getComponent(i);
			 strings[i] = item.getText();
		 }
		 return strings;
	}
	
	public void setStrings(String[] strings) {
		this.popupMenu.removeAll();
		 for (String string : strings) {
			 MenuItem item = new MenuItem(string);
			 item.addActionListener(this);
			 this.popupMenu.add(item);
		 }
		 this.selectedIndex = 0;
		 this.label.setText(strings[0]);
	}
	
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	
	public void setSelectedIndex(int index) {
		this.selectedIndex = index;
		MenuItem selectedItem = (MenuItem)this.popupMenu.getComponent(index);
		this.label.setText(selectedItem.getText());
	}
	
	public boolean getEditable() {
		return this.editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	// Actions
	
	public void actionPerformed(ActionEvent e) {
		MenuItem menuItem = (MenuItem)e.getSource();
		this.selectedIndex = this.popupMenu.getComponentIndex(menuItem);
		for (ActionPerformer performer : this.performers) {
			performer.actionPerformed(null);
		}
	}

	public void addAction(Object target, String action) {
		this.performers.add(new ActionPerformer(target, action));
	}
	
	// MouseListener

	public void mouseClicked(MouseEvent e) {
		if (this.editable) {
			this.popupMenu.show(this, e.getX(), e.getY());
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	// Component listener
	
	public void componentResized(ComponentEvent e) {
		this.label.setBounds(0, 0, this.getWidth(), this.getHeight());
	}

	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {};
	public void componentHidden(ComponentEvent e) {}
	
	// Drawings
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
}
