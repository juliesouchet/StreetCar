package main.java.gui.application;

import javax.swing.SwingUtilities;

import main.java.gui.util.Constants;
import main.java.gui.util.UserDefaults;

public class StreetCar {
	
	public static void registersDefaults() {
		UserDefaults ud = UserDefaults.sharedUserDefaults;
		ud.setStringIfNoValue(Constants.LANGUAGE_KEY, Constants.LANGUAGE_EN);
		ud.setBoolIfNoValue(Constants.USE_FULLSCREEN_KEY, true);
		ud.synchronize();
	}
	
	public static void launchGUI() {
		MainFrameController mainFrameController = new MainFrameController();
		mainFrameController.showFrame();	
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				StreetCar.registersDefaults();
				StreetCar.launchGUI();
			}
		});
	}

}
