package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import main.java.gui.components.Panel;

@SuppressWarnings("serial")
public class TilePanel extends Panel implements Transferable {
	
	// Constructors
	
	public TilePanel() {
		super();
		this.setTransferHandler(new DragTransferHandler());
		this.addMouseListener(new DraggableMouseListener());
		
	}
	
	class DraggableMouseListener extends MouseAdapter {

		@Override()
		public void mousePressed(MouseEvent e) {
			System.out.println("Step 1 of 7: Mouse pressed. Going to export our RandomDragAndDropPanel so that it is draggable.");
			
			JComponent c = (JComponent) e.getSource();
			TransferHandler handler = c.getTransferHandler();
			handler.exportAsDrag(c, e, TransferHandler.COPY);
		}
	}
	
	// Drawings

	public void paintComponent(Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	// Transferable
	
	public Object getTransferData(DataFlavor flavor) {
		DataFlavor thisFlavor = null;

		try {
			thisFlavor = TilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}

		if (thisFlavor != null && flavor.equals(thisFlavor)) {
			return TilePanel.this;
		}

		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {null};
		try {
			flavors[0] = TilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		DataFlavor[] flavors = {null};
		try {
			flavors[0] = TilePanel.getDragAndDropPanelDataFlavor();
		} catch (Exception ex) {
			System.err.println("Problem lazy loading: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return false;
		}

		for (DataFlavor f : flavors) {
			if (f.equals(flavor)) {
				return true;
			}
		}

		return false;
	}
	
	// Data flavor
	
	private static DataFlavor dragAndDropPanelDataFlavor = null;
	
	public static DataFlavor getDragAndDropPanelDataFlavor() throws Exception {
		if (dragAndDropPanelDataFlavor == null) {
			dragAndDropPanelDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=TilePanel");
		}
		return dragAndDropPanelDataFlavor;
	}
	
}
