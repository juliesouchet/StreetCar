
package test.java.gui;

import javax.swing.SwingUtilities;

import main.java.gui.application.GameController;

public class TestMainWindow implements Runnable {

    // Runnable

    public void run() {
        GameController mainFrameController = new GameController();
        mainFrameController.showFrame();
        //mainFrameController.toggleFullScreenMode();
    }

    // Main

    public static void main(String[] arguments) {
        TestMainWindow testMainWindow = new TestMainWindow();
        SwingUtilities.invokeLater(testMainWindow);
    }

}
