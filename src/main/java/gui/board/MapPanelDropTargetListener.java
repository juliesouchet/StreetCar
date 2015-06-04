package main.java.gui.board;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.rmi.RemoteException;

import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionGameIsOver;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionPlayerIsBlocked;
import main.java.game.ExceptionTooManyActions;
import main.java.gui.application.StreetCar;
import main.java.player.PlayerIHM;

class MapPanelDropTargetListener implements DropTargetListener {

	private final MapPanel mapPanel;

	
	private static final Cursor droppableCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private static final Cursor notDroppableCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);

	public MapPanelDropTargetListener(MapPanel sheet) {
		this.mapPanel = sheet;
	}

	// Could easily find uses for these, like cursor changes, etc.
	public void dragEnter(DropTargetDragEvent dtde) {}
	public void dragOver(DropTargetDragEvent dtde) {
		if (!this.mapPanel.getCursor().equals(droppableCursor)) {
			this.mapPanel.setCursor(droppableCursor);
		}
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {
		this.mapPanel.setCursor(notDroppableCursor);
	}

	/**
	 * <p>The user drops the item. Performs the drag and drop calculations and layout.</p>
	 * @param dtde
	 */
	public void drop(DropTargetDropEvent dropEvent) {
		Point p = this.mapPanel.cellPositionForLocation(dropEvent.getLocation());
		if (p == null || !dropEvent.isDataFlavorSupported(TilePanel.getDragDataFlavor())) {
			dropEvent.rejectDrop();
			return;
		}

		dropEvent.acceptDrop(dropEvent.getDropAction());
		Transferable transferable = dropEvent.getTransferable();
		DataFlavor dataFlavor = transferable.getTransferDataFlavors()[0];
		
		
		Tile tile = null;
		try {
			tile = (Tile)transferable.getTransferData(dataFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PlayerIHM player = StreetCar.player;
		try {
			player.placeTile(tile, p);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ExceptionGameHasNotStarted e) {
			e.printStackTrace();
		} catch (ExceptionNotYourTurn e) {
			e.printStackTrace();
		} catch (ExceptionForbiddenAction e) {
			e.printStackTrace();
		} catch (ExceptionTooManyActions e) {
			e.printStackTrace();
		} catch (ExceptionPlayerIsBlocked e) {
			e.printStackTrace();
		} catch (ExceptionGameIsOver e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (TilePanel.latestDraggedTilePanel != null) { // HACK
			TilePanel.latestDraggedTilePanel.setTile(null);
			TilePanel.latestDraggedTilePanel = null;
		}
		this.mapPanel.draggedPoint = null; // HACK too
		this.mapPanel.latestDroppedPosition = p;
		dropEvent.dropComplete(true);
	}
}