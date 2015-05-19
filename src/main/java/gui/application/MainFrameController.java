
package main.java.gui.application;

import javax.swing.JFrame;
import main.java.gui.controllers.FrameController;

public class MainFrameController extends FrameController {

    // Constructors

    // Create UI elements

    @Override
    public JFrame createInitialFrame() {
        JFrame frame = super.createInitialFrame();
        frame.setTitle("StreetCar");
        return frame;
    }

}
