package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTextField;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class JoinGameMenuPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	public JoinGameMenuPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(500, 350));
    	this.setBackground(Color.white); 
    	this.setupTitle();
		this.setupTextFields();
		this.setupButtons();
	}
	
	private void setupTitle() {
		//place 'Join game' a the top center of the panel
	}	
	
	private void setupTextFields() {
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h2>Profile</h2></html>");
		titleLabel.setBounds(90, 20, 300, 100);
	    this.add(titleLabel);
	    
	    JLabel nameLabel = new JLabel();
	    nameLabel.setText("<html><h3>Name</h3></html>");
	    nameLabel.setBounds(140, 115, 100, 40);
	    this.add(nameLabel);	
	    
	    JLabel adressLabel = new JLabel();
	    adressLabel.setText("<html><h3>IP adress</h3></html>");
	    adressLabel.setBounds(140, 164, 100, 40);
	    this.add(adressLabel);
		
		JTextField nameTextField = new JTextField(20);
		nameTextField.setBounds(new Rectangle(215, 120, 150, 30));
		this.add(nameTextField);
		JTextField adressTextField = new JTextField(20);
		adressTextField.setBounds(new Rectangle(215, 170, 150, 30));
		this.add(adressTextField);
	}
	
	private void setupButtons() {
		Button createGameButton = new Button("Join game", this, "joinGame");
		createGameButton.setBounds(new Rectangle(270, 280, 150, 40));
    	this.add(createGameButton);
    	
		Button cancelButton = new Button("Cancel", this, "cancelGame");
		cancelButton.setBounds(new Rectangle(80, 280, 150, 40));
    	this.add(cancelButton);
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 30, 500, 30);    
        g.drawLine(0, 0, 0, 350); //left line
        g.drawLine(0, 0, 500, 0); //top line
        g.drawLine(0, 349, 500, 349); //bottom line
        g.drawLine(499, 0, 499, 350); //right line
        g.drawLine(50, 90, 450, 90); //underline 'Profile'
    }
	
	// Actions
	
	public void joinGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showClientWaitingRoomPanel();
	}
	
	public void cancelGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}

}