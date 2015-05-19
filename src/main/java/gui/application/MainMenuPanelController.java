package main.java.gui.application;

import java.awt.Color;

import javax.swing.JPanel;

import main.java.gui.controllers.PanelController;


public class MainMenuPanelController extends PanelController{	
    
	//JButton 
	
    @Override
    public JPanel createInitialPanel() {
    	JPanel panel = super.createInitialPanel();
    	panel.setBackground(Color.black);
    	
    	
    	return panel;    	
    }
	
}
