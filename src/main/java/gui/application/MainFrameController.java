
package main.java.gui.application;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;

import main.java.gui.components.FrameController;
import main.java.gui.components.Menu;
import main.java.gui.components.MenuItem;
import main.java.gui.components.Panel;
import main.java.gui.util.Constants;
import main.java.gui.util.UserDefaults;

public class MainFrameController extends FrameController implements ComponentListener{
	
	// Properties
	
	protected Panel centeredPanel = null;
	
    // Constructors
	
	public MainFrameController() {
		super();
        this.setupFrame();
        this.setupMenuBar();
        this.showWelcomeMenuPanel();
	}
	
	private void setupFrame() {
		this.frame.setTitle("StreetCar"); 
        this.frame.setContentPane(new InGamePanel());
        this.frame.getContentPane().setLayout(null); 
        this.frame.addComponentListener(this);
        this.frame.setSize(1200, 760);
	}
	
	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		Menu menu = new Menu("Game", null);
	    menuBar.add(menu);
	    
	    menu = new Menu("Edit", null);
	    menuBar.add(menu);
	    
	    menu = new Menu("Window", null);
	    MenuItem item = new MenuItem("Toogle Fullscreen", null);
	    item.addAction(this, "toggleFullScreen", KeyEvent.VK_F);
	    menu.add(item);
	    menuBar.add(menu);
	    
	    menu = new Menu("Help", null);
	    menuBar.add(menu);

	    this.setFrameMenuBar(menuBar);
	}

	// Setters / getters
	
	public Panel getCenteredPanel() {
		return this.centeredPanel;
	}
	
	public void setCenteredPanel(Panel panel) {
		Panel contentPanel = (Panel) this.frame.getContentPane();
		if (this.centeredPanel != null) {
			contentPanel.remove(this.centeredPanel);
		}
		this.centeredPanel = panel;
		if (this.centeredPanel != null) {
			contentPanel.add(this.centeredPanel);
			this.componentResized(null);
		}
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	public void setFrameContentPane(Panel panel) {
		Panel centeredPanel = this.getCenteredPanel();
		if (centeredPanel == null) {
			this.setCenteredPanel(null);
			super.setFrameContentPane(panel);
			this.setCenteredPanel(centeredPanel);
		} else {
			super.setFrameContentPane(panel);
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
		UserDefaults.getSharedInstance().synchronize();
		System.exit(0);
	}
	
	public void showInGamePanel() {
		this.setCenteredPanel(null);
		this.setFrameContentPane(new InGamePanel());
	}
	
	public void showHostWaitingRoomPanel() {
		Panel newPanel = new HostWaitingRoomPanel();
		this.setCenteredPanel(newPanel);	
	}
	
	public void showClientWaitingRoomPanel() {
		Panel newPanel = new ClientWaitingRoomPanel();
		this.setCenteredPanel(newPanel);	
	}
	
	// Show / hide frame
	
	@Override
	public void showFrame() {
        super.showFrame();
        this.frame.setLocationRelativeTo(null);
        UserDefaults ud = UserDefaults.getSharedInstance();
        if (ud.getBool(Constants.USE_FULLSCREEN_KEY)) {
        	this.toggleFullScreen();
        }
    }
	
	@Override
	public void toggleFullScreen() {
		super.toggleFullScreen();
		
        UserDefaults ud = UserDefaults.getSharedInstance();
        ud.setBool(Constants.USE_FULLSCREEN_KEY, this.isInFullScreen());
        ud.synchronize();
	}
	
	// Mouse listener
	
    public void componentResized(ComponentEvent e) {
    	if (this.centeredPanel == null) return;
    	
		int contentPaneWidth = this.getFrameContentPane().getWidth();
		int contentPaneHeight = this.getFrameContentPane().getHeight();
		int panelWidth = this.centeredPanel.getWidth();
		int panelHeight = this.centeredPanel.getHeight();
		int x = (int)(contentPaneWidth - panelWidth)/2;
		int y = (int)(contentPaneHeight - panelHeight)/2;
		this.centeredPanel.setBounds(x, y, panelWidth, panelHeight);
	}
	
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }

}
