package main.java.gui.board;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

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
		if (p == null) {
			return;
		}
		
		System.out.println("DROP at point " + p);
		try {
			Transferable transferable = dropEvent.getTransferable();
			DataFlavor dataFlavor = transferable.getTransferDataFlavors()[0];
			String transferedData = (String)transferable.getTransferData(dataFlavor);
			System.out.println(transferedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/*DropTargetContext c = dtde.getDropTargetContext();

		// What does the Transferable support
		if (transferable.isDataFlavorSupported(dragAndDropPanelFlavor)) {
			transferableObj = dtde.getTransferable().getTransferData(dragAndDropPanelFlavor);
		} 

	} catch (Exception ex) {}

	// If didn't find an item, bail
	if (transferableObj == null) {
		return;
	}

		// Done with cursors, dropping
		this.rootPanel.setCursor(Cursor.getDefaultCursor());

		// Just going to grab the expected DataFlavor to make sure
		// we know what is being dropped
		DataFlavor dragAndDropPanelFlavor = null;

		Object transferableObj = null;
		Transferable transferable = null;

		try {
			// Grab expected flavor
			dragAndDropPanelFlavor = TilePanel.getDragDataFlavor();

			transferable = dtde.getTransferable();
			DropTargetContext c = dtde.getDropTargetContext();

			// What does the Transferable support
			if (transferable.isDataFlavorSupported(dragAndDropPanelFlavor)) {
				transferableObj = dtde.getTransferable().getTransferData(dragAndDropPanelFlavor);
			} 

		} catch (Exception ex) {}

		// If didn't find an item, bail
		if (transferableObj == null) {
			return;
		}

		// Cast it to the panel. By this point, we have verified it is 
		// a RandomDragAndDropPanel.
		RandomDragAndDropPanel droppedPanel = (RandomDragAndDropPanel)transferableObj;

		// Get the y offset from the top of the WorkFlowSheetPanel
		// for the drop option (the cursor on the drop)
		final int dropYLoc = dtde.getLocation().y;

		// We need to map the Y axis values of drop as well as other
		// RandomDragAndDropPanel so can sort by location.
		Map<Integer, RandomDragAndDropPanel> yLocMapForPanels = new HashMap<Integer, RandomDragAndDropPanel>();
		yLocMapForPanels.put(dropYLoc, droppedPanel);

		// Iterate through the existing demo panels. Going to find their locations.
		for (RandomDragAndDropPanel nextPanel : rootPanel.getDragAndDropPanelsDemo().getRandomDragAndDropPanels()) {

			// Grab the y value
			int y = nextPanel.getY();

			// If the dropped panel, skip
			if (!nextPanel.equals(droppedPanel)) {
				yLocMapForPanels.put(y, nextPanel);
			}
		}

		// Grab the Y values and sort them
		List<Integer> sortableYValues = new ArrayList<Integer>();
		sortableYValues.addAll(yLocMapForPanels.keySet());
		Collections.sort(sortableYValues);

		// Put the panels in list in order of appearance
		List<RandomDragAndDropPanel> orderedPanels = new ArrayList<RandomDragAndDropPanel>();
		for (Integer i : sortableYValues) {
			orderedPanels.add(yLocMapForPanels.get(i));
		}

		// Grab the in-memory list and re-add panels in order.
		List<RandomDragAndDropPanel> inMemoryPanelList = this.rootPanel.getDragAndDropPanelsDemo().getRandomDragAndDropPanels();
		inMemoryPanelList.clear();
		inMemoryPanelList.addAll(orderedPanels);

		// Request relayout contents, or else won't update GUI following drop.
		// Will add back in the order to which we just sorted
		this.rootPanel.getDragAndDropPanelsDemo().relayout();
		*/
	}
}