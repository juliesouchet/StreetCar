package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import main.java.data.Data;

public class SaveListener implements MouseListener {
	Data data;
	
	public SaveListener(Data d) {
		data = d;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Sauvegarde non implémentée");
		// TODO sauvegarder le terrain courant en fichier texte + demander un nom de fichier
		String fileName = (String) JOptionPane.showInputDialog(null, null, "Sauvegarde du terrain",
				JOptionPane.QUESTION_MESSAGE, null, null, "nom_du_terrain");
		
		System.out.println("nom choisi : "+fileName);
		if(fileName != null) {
			//data.saveBoard(fileName);
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
