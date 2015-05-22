package test.java.ai;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

public class SaveListener implements MouseListener {

	public SaveListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Sauvegarde non implémentée");
		// TODO sauvegarder le terrain courant en fichier texte + demander un nom de fichier
		String initialName = "nom_du_terrain", boardName;
		boardName = (String) JOptionPane.showInputDialog(null, null, "Sauvegarde du terrain",
				JOptionPane.QUESTION_MESSAGE, null, null, initialName);
		
		System.out.println("nom choisi : "+boardName);
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
