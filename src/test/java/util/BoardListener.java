package test.java.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.data.Data;
import main.java.data.Tile;
import test.java.player.DataViewerFrame;
import test.java.player.DataViewerFrame.ViewerPanel;

public class BoardListener implements MouseListener {
	BoardCreator bc;

	public BoardListener(BoardCreator boardCreator) {
		bc = boardCreator;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ViewerPanel panel = bc.getViewerPanel();
		Data data = bc.getData();
		int height, width, x, y, tileWidth, tileHeight;
		height = panel.getHeight();
		width = panel.getWidth();
		tileWidth	= (width - DataViewerFrame.paddingWidth) / data.getWidth();
		tileHeight	= (height- DataViewerFrame.paddingHeight)/ data.getHeight();
		
		x = (e.getX()-tileWidth/2)/tileWidth;
		y = (e.getY()-tileHeight/2)/tileHeight;
		
		if(bc.getCurrentTile() == null) {
			Tile t = data.getTile(x, y);
			System.out.println("Direction de la tuile cliquée = " + t.getTileDirection());
		}
		else {			
			bc.drawTile(x, y);
			panel.repaint();
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
