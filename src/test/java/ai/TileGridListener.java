package test.java.ai;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class TileGridListener implements MouseListener {
	BoardCreator bc;
	JPanel p;

	public TileGridListener(BoardCreator boardCreator, JPanel panel) {
		bc = boardCreator;
		p = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Cliqué dans la grille");

		for (Component c : p.getComponents()) {
			TilePanel tp = (TilePanel) c;
			boolean isSelected = tp.getTileNumber() == bc.getCurrentTile();
			tp.setSelection(isSelected);
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
