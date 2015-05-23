package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import main.java.data.Data;

public class SaveListener implements MouseListener {
	Data data;
	
	public SaveListener(Data d) {
		data = d;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO : S'assurer que le FileWriter écrit bien
		System.out.println("Sauvegarde n'écrivant rien");
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Sauvegarde du terrain",
				JOptionPane.QUESTION_MESSAGE, null, null, "nom_du_terrain");
		
		System.out.println("nom choisi : "+fileName);
		if(fileName != null) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(BoardCreator.boardPath+fileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			data.writeBoardInFile(fw);;
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
