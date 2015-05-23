package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JOptionPane;

import main.java.data.Data;
import main.java.game.UnknownBoardNameException;
import test.java.player.DataViewerFrame;

public class LoadListener implements MouseListener {
	DataViewerFrame frame;
		
	public LoadListener(DataViewerFrame f) {
		frame = f;
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		// Building a list of all pre-existing boards
		Path path = (new File(BoardCreator.boardPath)).toPath();
		Vector<String> availableBoards = null;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			availableBoards = new Vector<String>();
			for (Path board: stream) {
				String boardName = board.toString();
				System.out.println(boardName);
				availableBoards.add(boardName);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		if(availableBoards == null) { // No pre-existing board
			
		}
		
		// Dialog window to make the user choose
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Chargement d'un terrain",
				JOptionPane.QUESTION_MESSAGE, null, availableBoards.toArray(), "nom_du_terrain");
		
		if(fileName != null) {
			Data data = null;
			try {
				Data.boardDirectory = BoardCreator.boardPath;
				data = new Data("Board Creator", fileName, 2);
			} catch (UnknownBoardNameException | RuntimeException e1) {
				e1.printStackTrace();
			}		
			frame.setGameData(data);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
