package main.java.gui.application;

import javax.swing.SwingUtilities;

import main.java.gui.util.Constants;
import main.java.gui.util.OS;
import main.java.gui.util.UserDefaults;

public class StreetCar {
	
	public static void registersDefaults() {
		UserDefaults ud = UserDefaults.getSharedInstance();
		ud.setStringIfNoValue(Constants.LANGUAGE_KEY, Constants.LANGUAGE_EN);
		ud.setBoolIfNoValue(Constants.USE_FULLSCREEN_KEY, true);
		ud.setStringIfNoValue(Constants.PLAYER_NAME_KEY, "Player1");
		ud.setStringIfNoValue(Constants.PLAYER_NAME_KEY, "Untitled");
		ud.synchronize();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				UserDefaults.getSharedInstance().synchronize();
			}
		});
	}
	
	public static void setupNativeUI() {
		if (OS.isMac()) {
	    	System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
	}
	
	public static void launchGUI() {
		GameController mainFrameController = new GameController();
		mainFrameController.showFrame();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StreetCar.registersDefaults();
				StreetCar.setupNativeUI();
				StreetCar.launchGUI();
			}
		});
	}
}
