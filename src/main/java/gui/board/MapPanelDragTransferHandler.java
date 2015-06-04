package main.java.gui.board;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class MapPanelDragTransferHandler extends TransferHandler implements DragSourceMotionListener {

	public Transferable createTransferable(JComponent c) {
		if (c instanceof MapPanel) {
			Transferable tip = (Transferable)c;
			return tip;
		}
		return null;
	}	

	public void dragMouseMoved(DragSourceDragEvent dsde) {}

	public int getSourceActions(JComponent c) {
		if (c instanceof MapPanel) {
			return TransferHandler.COPY;
		}
		return TransferHandler.NONE;
	}

}
