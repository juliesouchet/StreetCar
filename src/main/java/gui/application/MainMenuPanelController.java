package main.java.gui.application;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.java.gui.controllers.PanelController;
import main.java.gui.util.ActionPerformer;


public class MainMenuPanelController extends PanelController {	
    
	
    @Override
    public JPanel createInitialPanel() {

    	JButton newGameButton = new JButton("Créer une partie");     	
    	JButton joinGameButton = new JButton("Rejoindre une partie");
    	JButton settingsButton = new JButton("Options");
    	JButton gameRulesButton = new JButton("Règles du jeu");
    	JButton quitButton = new JButton("Quitter");    	

    	newGameButton.addActionListener(new ActionPerformer(this, "newGame"));
    	joinGameButton.addActionListener(new ActionPerformer(this, "joinGame"));
    	settingsButton.addActionListener(new ActionPerformer(this, "settings"));
    	gameRulesButton.addActionListener(new ActionPerformer(this, "gameRules"));
    	quitButton.addActionListener(new ActionPerformer(this, "quit"));
    	
    	JPanel panel = super.createInitialPanel();
    	panel.setLayout(null);
    	panel.setSize(new Dimension(500, 450));
    	panel.setBackground(Color.black); 
    	
    	int defaultPaddingTop = 70;
    	int defaultPaddingLeft = 150;
    	int defaultSpace = 70;
    	int defaultWidth = 200;
    	int defaultHeight = 50;
    	newGameButton.setBounds(defaultPaddingLeft, defaultPaddingTop, defaultWidth, defaultHeight);
    	joinGameButton.setBounds(defaultPaddingLeft, defaultPaddingTop + defaultSpace, defaultWidth, defaultHeight);
    	settingsButton.setBounds(defaultPaddingLeft, defaultPaddingTop + defaultSpace*2, defaultWidth, defaultHeight);
    	gameRulesButton.setBounds(defaultPaddingLeft, defaultPaddingTop + defaultSpace*3, defaultWidth, defaultHeight);
    	quitButton.setBounds(defaultPaddingLeft, defaultPaddingTop + defaultSpace*4, defaultWidth, defaultHeight);
    	
    	panel.add(newGameButton);
    	panel.add(joinGameButton);
    	panel.add(settingsButton);
    	panel.add(gameRulesButton);
    	panel.add(quitButton);  
    	
    	return panel;    	
    }
    
    // Actions
    
    public void newGame() {    	
    	
    }    
    
    public void joinGame() {    	
    	
    }   
    
    public void settings() {    	
    	
    }   
    
    public void gameRules() {    	
    	
    }   
    
    public void quit() {    	
    	
    }
	
}
