package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		System.out.println("Chargement non implémenté");
		String[] availableBoards = null;
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Sauvegarde du terrain",
				JOptionPane.QUESTION_MESSAGE, null, availableBoards, "nom_du_terrain");
		
		System.out.println("nom choisi : "+fileName);
		if(fileName != null) {
			Data data = null;
			try {
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
