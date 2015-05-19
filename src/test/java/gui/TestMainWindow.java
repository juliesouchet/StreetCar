
package test.java.gui;

import javax.swing.SwingUtilities;
import main.java.gui.application.MainFrameController;

public class TestMainWindow implements Runnable {

    // Runnable

    public void run() {
        MainFrameController mainFrameController = new MainFrameController();
        mainFrameController.toggleFullscreenMode();
        mainFrameController.showFrame();
    }

    // Main

    public static void main(String[] arguments) {
        TestMainWindow testMainWindow = new TestMainWindow();
        SwingUtilities.invokeLater(testMainWindow);
    }

}
