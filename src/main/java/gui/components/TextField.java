package main.java.gui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

import main.java.gui.util.Resources;

@SuppressWarnings("serial")
public class TextField extends JTextField {

	// Properties
	
	protected String placeholder = null;
	
	// Constructors
	
	public TextField() {
		super();
	}
	
	public TextField(String text) {
		this();
		this.setText(text);
	}
	
	public TextField(String text, String comment) {
		super(Resources.localizedString(text, comment));
	}
	
	// Setters / getters
	
	public String getPlaceholder() {
		return this.placeholder;
	}
	
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
		this.repaint();
	}
	
	public void setPlaceholder(String placeholder, String comment) {
		this.setPlaceholder(Resources.localizedString(placeholder, comment));
	}
	
	// Drawings
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.placeholder == null       ||
            this.placeholder.length() == 0 ||
            this.getText().length() > 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        
        int x = this.getInsets().left;
        int y = g.getFontMetrics().getMaxAscent() + getInsets().top;
        g.setColor(this.getDisabledTextColor());
        g.drawString(this.placeholder, x, y);
    }
}
