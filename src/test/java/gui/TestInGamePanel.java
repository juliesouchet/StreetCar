
package test.java.gui;

import javax.swing.SwingUtilities;

import main.java.gui.application.GameController;

public class TestInGamePanel implements Runnable {

    // Runnable

    public void run() {
        GameController mainFrameController = new GameController();
        mainFrameController.showInGamePanel();
        mainFrameController.showFrame();
    }

    // Main

    public static void main(String[] arguments) {
    	TestInGamePanel testInGamePanel = new TestInGamePanel();
        SwingUtilities.invokeLater(testInGamePanel);
    }

}
