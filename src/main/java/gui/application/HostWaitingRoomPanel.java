package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import main.java.gui.components.Button;
import main.java.gui.components.Panel;

public class HostWaitingRoomPanel extends Panel {

	// Properties
	
	private static final long serialVersionUID = 1L;

	// Constructors
	
	HostWaitingRoomPanel() {
		super();
    	this.setLayout(null);
    	this.setSize(new Dimension(700, 500));
    	this.setBackground(Color.white); 
    	this.setupTitle();
		this.setupTextFields();
		this.setupComboBox();
		this.setupButtons();
	}
	
	private void setupTitle() {
		//place 'New Game' at the top center of the panel
	}	

	private void setupTextFields() {
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h2>Waiting room</h2></html>");
		titleLabel.setBounds(120, 20, 300, 100);
	    this.add(titleLabel);
	    
	    JLabel name1Label = new JLabel();
	    name1Label.setText("<html><h3>Name1</h3></html>");
	    name1Label.setBounds(210, 140, 100, 40);
	    this.add(name1Label);	    
	    JLabel name2Label = new JLabel();
	    name2Label.setText("<html><h3>Name2</h3></html>");
	    name2Label.setBounds(210, 190, 100, 40);
	    this.add(name2Label);	    
	    JLabel name3Label = new JLabel();
	    name3Label.setText("<html><h3>Name3</h3></html>");
	    name3Label.setBounds(210, 240, 100, 40);
	    this.add(name3Label);	    
	    JLabel name4Label = new JLabel();
	    name4Label.setText("<html><h3>Name4</h3></html>");
	    name4Label.setBounds(210, 290, 100, 40);
	    this.add(name4Label);	    
	    JLabel name5Label = new JLabel();
	    name5Label.setText("<html><h3>Name5</h3></html>");
	    name5Label.setBounds(210, 340, 100, 40);
	    this.add(name5Label);
	    
	    JLabel player1Label = new JLabel();
	    player1Label.setText("<html><h3>Player</h3></html>");
	    player1Label.setBounds(380, 140, 100, 40);
	    this.add(player1Label);	
	}	
	
	private void setupComboBox() {
		String[] adversary = {"Player",
							  "Computer (easy)",
						  	  "Computer (medium)", 
						  	  "Computer (hard)",						  	  
						  	  "Closed"};
		JComboBox adversaryChooser2 = new JComboBox(adversary);
		adversaryChooser2.setBounds(370, 190, 150, 40);
		adversaryChooser2.setSelectedIndex(1);
		this.add(adversaryChooser2);
		JComboBox adversaryChooser3 = new JComboBox(adversary);
		adversaryChooser3.setBounds(370, 240, 150, 40);
		this.add(adversaryChooser3);
		JComboBox adversaryChooser4 = new JComboBox(adversary);
		adversaryChooser4.setBounds(370, 290, 150, 40);
		this.add(adversaryChooser4);
		JComboBox adversaryChooser5 = new JComboBox(adversary);
		adversaryChooser5.setBounds(370, 340, 150, 40);
		this.add(adversaryChooser5);
	}
	
	private void setupButtons() {
		Button playGameButton = new Button("Play", this, "playGame");
		playGameButton.setBounds(new Rectangle(370, 430, 150, 40));
    	this.add(playGameButton);
    	
		Button cancelButton = new Button("Cancel", this, "cancelGame");
		cancelButton.setBounds(new Rectangle(180, 430, 150, 40));
    	this.add(cancelButton);
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, 30, 700, 30);    
        g.drawLine(0, 0, 0, 500); //left line
        g.drawLine(0, 0, 700, 0); //top line
        g.drawLine(0, 499, 700, 499); //bottom line
        g.drawLine(699, 0, 699, 499); //right line
        g.drawLine(50, 90, 650, 90); //underline 'Waiting room'
        
        g.drawRect(200, 140, 160, 40); //rectangle of name1
        g.drawRect(200, 190, 160, 40); //rectangle of name2
        g.drawRect(200, 240, 160, 40); //rectangle of name3
        g.drawRect(200, 290, 160, 40); //rectangle of name4
        g.drawRect(200, 340, 160, 40); //rectangle of name5
        
        g.drawRect(370, 140, 150, 40);
    }
		
	// Actions
	
	public void cancelGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showWelcomeMenuPanel();
	}
	
	public void playGame() {
		MainFrameController mfc = (MainFrameController)this.getFrameController();
		mfc.showInGamePanel();
	}

}