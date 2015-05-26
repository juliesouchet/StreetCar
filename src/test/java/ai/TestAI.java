package test.java.ai;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import main.java.data.Data;
import main.java.game.ExceptionUnknownBoardName;
import main.java.ia.PlayerAutomaton;
import test.java.player.DataViewerFrame;
import test.java.util.BoardCreator;

public class TestAI implements Runnable {
	
	
	public static void main(String[] args) {
		TestAI test = new TestAI();
        SwingUtilities.invokeLater(test);
	}
	
	@Override
	public void run() {
		PlayerAutomaton AI;
		DataViewerFrame frame = new DataViewerFrame();
		JButton AIchoice = new JButton("AI test");
		
		// Building a list of all pre-existing boards
		Path path = (new File(BoardCreator.boardPath)).toPath();
		Vector<String> choices = null;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			choices = new Vector<String>();
			for (Path board: stream) {
				String boardName = board.toString();
				choices.add(boardName);
			}
		} catch (IOException e) {e.printStackTrace();}
		
		choices.add("src/main/resources/boards/newOrleans");
		
		// Dialog window to make the user choose
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Chargement d'un terrain",
				JOptionPane.QUESTION_MESSAGE, null, choices.toArray(), "nom_du_terrain");
		
		if(fileName != null) {
			Data data = null;
			try {
				Data.boardDirectory = "";
				data = new Data("Board Creator", fileName, 2);
			} catch (ExceptionUnknownBoardName | RuntimeException e1) {
				e1.printStackTrace();
			}		
			frame.setGameData(data);
			frame.repaint();
		}
		
		// Choosing the AI to test
		//frame.add(AIchoice);
		AIchoice.addActionListener(new AIchoiceListener());
		
		// Displaying the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	
	
	
	public TestAI() {
		// TODO Auto-generated constructor stub
	}

	
}
