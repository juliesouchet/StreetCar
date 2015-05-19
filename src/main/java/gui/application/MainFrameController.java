
package main.java.gui.application;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import main.java.gui.board.MovingMapPanel;
import main.java.gui.controllers.FrameController;

public class MainFrameController extends FrameController implements ComponentListener{
	
	MainMenuPanelController mainMenuPanelController;
	
    // Constructors

    // Create UI elements

    @Override
    public JFrame createInitialFrame() {
        JFrame frame = super.createInitialFrame();
        frame.setTitle("StreetCar"); 
        frame.setContentPane(new MovingMapPanel());
        frame.getContentPane().setLayout(null);
        this.mainMenuPanelController = new MainMenuPanelController();
        frame.add(this.mainMenuPanelController.getPanel()); 
        frame.addComponentListener(this);
        return frame;
    }

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {		
		int contentPaneWidth = (int)this.getFrameContentPane().getWidth();
		int contentPaneHeight = (int)this.getFrameContentPane().getHeight();
		
		int panelWidth = (int)mainMenuPanelController.getPanel().getWidth();
		int panelHeight = (int)mainMenuPanelController.getPanel().getHeight();
		
		int x = (int)(contentPaneWidth - panelWidth)/2;
		int y = (int)(contentPaneHeight - panelHeight)/2;
		mainMenuPanelController.getPanel().setBounds(x, y, panelWidth, panelHeight);	
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub		
	}

}
