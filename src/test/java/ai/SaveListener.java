package test.java.ai;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SaveListener implements MouseListener {

	public SaveListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Sauvegarde non implémentée");
		// TODO sauvegarder le terrain courant en fichier texte + demander un nom de fichier
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
