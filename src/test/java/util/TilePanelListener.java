package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TilePanelListener implements MouseListener {
	BoardCreator bc;
	TilePanel tp;

	public TilePanelListener(BoardCreator bc, TilePanel tp) {
		this.bc = bc;
		this.tp = tp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		bc.setCurrentTile(tp.getTile());
		bc.repaintTilePanels();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		tp.setMouseOver(true);
		tp.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		tp.setMouseOver(false);
		tp.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
