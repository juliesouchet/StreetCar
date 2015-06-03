
package main.java.gui.application;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;

import main.java.data.Data;
import main.java.gui.board.MovingMapPanel;
import main.java.gui.components.FrameController;
import main.java.gui.components.Menu;
import main.java.gui.components.MenuItem;
import main.java.gui.components.Panel;
import main.java.gui.menus.ClientRoomMenuPanel;
import main.java.gui.menus.HostRoomMenuPanel;
import main.java.gui.menus.JoinGameMenuPanel;
import main.java.gui.menus.MenuPanel;
import main.java.gui.menus.NewGameMenuPanel;
import main.java.gui.menus.RulesMenuPanel;
import main.java.gui.menus.SettingsMenuPanel;
import main.java.gui.menus.WelcomeMenuPanel;
import main.java.gui.util.Constants;
import main.java.gui.util.UserDefaults;
import main.java.rubbish.InterfaceIHM;

public class GameController extends FrameController implements InterfaceIHM, ComponentListener {

	// Properties

	protected MenuPanel menuPanel = null;

	// Constructors

	public GameController() {
		super();
		this.setupFrame();
		this.setupMenuBar();
		this.showWelcomeMenuPanel();
	}

	private void setupFrame() {
		this.frame.setTitle("StreetCar"); 
		this.frame.setContentPane(new MovingMapPanel());
		this.frame.getContentPane().setLayout(null); 
		this.frame.addComponentListener(this);
		this.frame.setSize(1250, 830);
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

	// Getters

	public MenuPanel getMenuPanel() {
		return this.menuPanel;
	}

	// Setters

	public void setMenuPanel(MenuPanel menuPanel) {
		Panel contentPanel = this.getFrameContentPane();
		if (this.menuPanel != null) {
			contentPanel.remove(this.menuPanel);
		}
		this.menuPanel = menuPanel;
		if (this.menuPanel != null) {
			contentPanel.add(this.menuPanel);
			this.componentResized(null);
		}
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	public void setFrameContentPane(Panel panel) {
		MenuPanel menuPanel = this.getMenuPanel();
		if (menuPanel == null) {
			this.setMenuPanel(null);
			super.setFrameContentPane(panel);
			this.setMenuPanel(menuPanel);
		} else {
			super.setFrameContentPane(panel);
		}
	}

	// Actions

	public void showWelcomeMenuPanel() {
		MenuPanel newPanel = new WelcomeMenuPanel(this);
		this.setMenuPanel(newPanel);
	}

	public void showNewGamePanel() {
		MenuPanel newPanel = new NewGameMenuPanel(this);
		this.setMenuPanel(newPanel);
	}

	public void showJoinGamePanel() {
		MenuPanel newPanel = new JoinGameMenuPanel(this);
		this.setMenuPanel(newPanel);	
	}

	public void showSettingsPanel() {
		MenuPanel newPanel = new SettingsMenuPanel(this);
		this.setMenuPanel(newPanel);	
	}

	public void showRulesPanel() {
		MenuPanel newPanel = new RulesMenuPanel(this);
		this.setMenuPanel(newPanel);	
	}

	public void quitGame() {
		System.exit(0);
	}

	public void showInGamePanel() {
		this.setMenuPanel(null);
		this.setFrameContentPane(new InGamePanel(this));
	}

	public void showHostWaitingRoomPanel() {
		MenuPanel newPanel = new HostRoomMenuPanel(this);
		this.setMenuPanel(newPanel);
	}

	public void showClientWaitingRoomPanel() {
		MenuPanel newPanel = new ClientRoomMenuPanel(this);
		this.setMenuPanel(newPanel);
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
		if (this.menuPanel == null) return;

		int contentPaneWidth = this.getFrameContentPane().getWidth();
		int contentPaneHeight = this.getFrameContentPane().getHeight();
		int panelWidth = this.menuPanel.getWidth();
		int panelHeight = this.menuPanel.getHeight();
		int x = (int)(contentPaneWidth - panelWidth)/2;
		int y = (int)(contentPaneHeight - panelHeight)/2;
		this.menuPanel.setBounds(x, y, panelWidth, panelHeight);
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}

	public void stopGame() {
		StreetCar.player = null;
	}

	public void refresh(Data data) {
		System.out.println("RESFRESH DU GAME");
		if (this.menuPanel != null) {
			this.menuPanel.refreshMenu(StreetCar.player, data);
		}
		if (this.getFrameContentPane() instanceof InGamePanel) {
			InGamePanel panel = (InGamePanel)this.getFrameContentPane();
			panel.refreshGame(StreetCar.player, data);
		}
	}

	public void excludePlayer() {

	}

}
