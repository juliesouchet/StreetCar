package main.java.gui.board;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class TilePanelDragTransferHandler extends TransferHandler implements DragSourceMotionListener {

	public Transferable createTransferable(JComponent c) {
		if (c instanceof TilePanel) {
			Transferable tip = (TilePanel)c;
			return tip;
		}
		return null;
	}	

	public void dragMouseMoved(DragSourceDragEvent dsde) {}

	public int getSourceActions(JComponent c) {
		if (c instanceof TilePanel) {
			return TransferHandler.COPY;
		}
		return TransferHandler.NONE;
	}

}
