
package test.java.gui;

import javax.swing.SwingUtilities;

import main.java.gui.application.MainFrameController;

public class TestInGamePanel implements Runnable {

    // Runnable

    public void run() {
        MainFrameController mainFrameController = new MainFrameController();
        mainFrameController.showInGamePanel();
        mainFrameController.showFrame();
    }

    // Main

    public static void main(String[] arguments) {
    	TestInGamePanel testInGamePanel = new TestInGamePanel();
        SwingUtilities.invokeLater(testInGamePanel);
    }

}
