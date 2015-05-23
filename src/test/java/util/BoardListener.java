package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.data.Data;
import test.java.player.DataViewerFrame;

public class BoardListener implements MouseListener {
	BoardCreator bc;

	public BoardListener(BoardCreator boardCreator) {
		bc = boardCreator;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(bc.getCurrentTile() == null) return;
		
		Data data = bc.getData();
		int height, width, x, y, tileWidth, tileHeight;
		height = bc.getViewerPanel().getHeight();
		width = bc.getViewerPanel().getWidth();
		tileWidth	= (width - DataViewerFrame.paddingWidth) / data.getWidth();
		tileHeight	= (height- DataViewerFrame.paddingHeight)/ data.getHeight();
		
		x = (e.getX()-tileWidth/2)/tileWidth;
		y = (e.getY()-tileHeight/2)/tileHeight;
		
		bc.drawTile(x, y);
		bc.getViewerPanel().repaint();
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
