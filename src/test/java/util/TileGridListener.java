package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

public class TileGridListener extends MouseInputAdapter {
	BoardCreator bc;
	TileGrid tg;

	public TileGridListener(BoardCreator bc, TileGrid tg) {
		this.bc = bc;
		this.tg = tg;
	}

	public void mouseClicked(MouseEvent e) {
		int width, x, y, tileSide, i=0;
		width = tg.getWidth();
		tileSide = width/2;
		
		x = e.getX() / tileSide;
		y = e.getY() / tileSide;		
		i = 2*y + x;
		
		if(i<14) {
			tg.setSelection(i);
			bc.setCurrentTile(tg.getSelected());
			tg.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
		tg.setMouseOver(null);
		tg.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		int width, x, y, tileSide, i=0;
		width = tg.getWidth();
		tileSide = width/2;
		
		x = e.getX() / tileSide;
		y = e.getY() / tileSide;		
		i = 2*y + x;
		
		if(i<14) {
			tg.setMouseOver(i);
			tg.repaint();
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation()%4;
		tg.rotate(rotation);
		tg.repaint();
	}
}
