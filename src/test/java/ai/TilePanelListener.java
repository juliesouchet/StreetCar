package test.java.ai;

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
		System.out.println("clic sur tuile "+tp.getTileNumber());
		bc.setCurrentTile(tp.getTileNumber());
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
