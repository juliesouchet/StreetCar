
package main.java.gui.application;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import main.java.gui.board.MovingMapPanel;
import main.java.gui.components.FrameController;
import main.java.gui.components.Panel;

public class MainFrameController extends FrameController implements ComponentListener{
	
	public Panel centeredPanel = null;
	
    // Constructors
	
	public MainFrameController() {
        this.frame.setTitle("StreetCar"); 
        this.frame.setContentPane(new MovingMapPanel());
        this.frame.getContentPane().setLayout(null); 
        this.frame.addComponentListener(this);
        this.showWelcomeMenuPanel();
	}

	// Setters / getters
	
	public Panel getCenteredPanel() {
		return this.centeredPanel;
	}
	
	public void setCenteredPanel(Panel panel) {
		if (this.centeredPanel != null) {
			this.frame.getContentPane().remove(this.centeredPanel);
		}
		this.centeredPanel = panel;
		if (this.centeredPanel != null) {
			this.frame.getContentPane().add(this.centeredPanel);
			this.componentResized(null);
		}
	}
	
	// Actions
	
	public void showWelcomeMenuPanel() {
		Panel newPanel = new MainMenuPanel();
		this.setCenteredPanel(newPanel);
	}
	
	public void showNewGamePanel() {
		Panel newPanel = new NewGameMenuPanel();
		this.setCenteredPanel(newPanel);
	}
	
	public void showJoinGamePanel() {
		Panel newPanel = new JoinGameMenuPanel();
		this.setCenteredPanel(newPanel);		
	}
	
	public void showSettingsPanel() {
		Panel newPanel = new SettingsMenuPanel();
		this.setCenteredPanel(newPanel);	
	}
	
	public void showRulesPanel() {
		Panel newPanel = new RulesMenuPanel();
		this.setCenteredPanel(newPanel);	
	}
	
	public void quitGame() {
		
	}
	
	public void showInGamePanel() {
		Panel newPanel = new InGamePanel();
		this.setCenteredPanel(newPanel);	
	}
	
	public void showHostWaitingRoomPanel() {
		Panel newPanel = new HostWaitingRoomPanel();
		this.setCenteredPanel(newPanel);	
	}
	
	public void showClientWaitingRoomPanel() {
		Panel newPanel = new ClientWaitingRoomPanel();
		this.setCenteredPanel(newPanel);	
	}

	
	// Mouse listener
	
    public void componentResized(ComponentEvent e) {
    	if (this.centeredPanel == null) return;
    	
		int contentPaneWidth = (int)this.getFrameContentPane().getWidth();
		int contentPaneHeight = (int)this.getFrameContentPane().getHeight();
		int panelWidth = (int)this.centeredPanel.getWidth();
		int panelHeight = (int)this.centeredPanel.getHeight();
		int x = (int)(contentPaneWidth - panelWidth)/2;
		int y = (int)(contentPaneHeight - panelHeight)/2;
		this.centeredPanel.setBounds(x, y, panelWidth, panelHeight);
	}
	
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }

}
