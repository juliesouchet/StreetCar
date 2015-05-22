package test.java.ai;

import javax.swing.SwingUtilities;

import main.java.data.Data;
import main.java.game.UnknownBoardNameException;
import test.java.player.DataViewerFrame;

public class TestBoardDisplay implements Runnable {

	@Override
	public void run() {
		DataViewerFrame frame = new DataViewerFrame();
		Data data = null;
		try {
			data = new Data("testAffichagePlateau", "newOrleans", 2);
		} catch (UnknownBoardNameException | RuntimeException e) {
			e.printStackTrace();
		}
		frame.setGameData(data);
		frame.setVisible(true);
	}

	 public static void main(String[] arguments) {
		 	TestBoardDisplay test = new TestBoardDisplay();
	        SwingUtilities.invokeLater(test);
	 }
}
