package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import test.java.player.DataViewerFrame;

public class SaveListener implements MouseListener {
	DataViewerFrame frame;
	
	public SaveListener(DataViewerFrame frame) {
		this.frame = frame;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Sauvegarde du terrain",
				JOptionPane.QUESTION_MESSAGE, null, null, "nom_du_terrain");
		
		System.out.println("Sauvegarde : "+fileName);
		if(fileName != null) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(BoardCreator.boardPath+fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.getGameData().writeBoardInFile(fw);
			try {
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
