package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
<<<<<<< HEAD
import java.util.HashMap;
=======
import java.util.Arrays;
>>>>>>> 6429a9550099d8271593cd9aea85ea5b8ac5f29c
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionMissingStartTerminus;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionTramwayExceededArrival;
import main.java.game.ExceptionTramwayJumpCell;
import main.java.game.ExceptionTrtamwayDoesNotStop;
import main.java.game.ExceptionWrongPlayerTerminus;
import main.java.game.ExceptionWrongTramwayPath;
import main.java.game.ExceptionWrongTramwaySpeed;
import main.java.game.ExceptionWrongTramwayStart;
import main.java.game.ExceptionWrongTramwayStartTerminus;
import main.java.gui.application.StreetCar;
import main.java.gui.components.Panel;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;
import main.java.util.Util;


@SuppressWarnings("serial")
public class MapPanel extends Panel implements MouseListener, ComponentListener, MouseMotionListener {

	// Properties

	private int originX;
	private int originY;
	private int mapWidth;
	private int cellWidth;
	<<<<<<< HEAD

	private Point trainPosition = new Point(3, 4);
	private LinkedList<Point> tramMove = new LinkedList<Point>();

	HashMap<Point, BufferedImage> highlights = new HashMap<Point, BufferedImage>();
	=======

			private LinkedList<Point> chosenPath = new LinkedList<Point>();

	// these are for the showPath thingie
	boolean playerIsShowingPath = false;
	boolean playerIsMovingTramForTheFirstTime = false;
	>>>>>>> 6429a9550099d8271593cd9aea85ea5b8ac5f29c

	// Constructors

	public MapPanel() {
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addComponentListener(this);

		MapPanelDropTargetListener dropTarget = new MapPanelDropTargetListener(this);
		this.setDropTarget(new DropTarget(this, dropTarget));
	}

	// Cells positions

	protected void updateMapGeometry() {
		Data data = StreetCar.player.getGameData();
		if (data == null) return;
		int sideBarWidth = 50;
		this.mapWidth = (int) (Math.min((0.96 * this.getWidth()) - sideBarWidth, 0.96 * this.getHeight()));
		this.cellWidth = Math.round((float)this.mapWidth / (float)data.getWidth());
		this.originX = (int)Math.round((float)(this.getWidth() - this.mapWidth - sideBarWidth) / 2.0);
		this.originY = (int)Math.round((float)(this.getHeight() - this.mapWidth) / 2.0);

		this.repaint();
	}

	public Point cellPositionForLocation(Point location) {
		if (location.x >= this.originX &&  location.x <= this.originX + this.mapWidth &&
				location.y >= this.originY &&  location.y <= this.originY + this.mapWidth) {
			Point cellPosition = new Point();
			cellPosition.x = (int)((float)(location.x - this.originX) / this.cellWidth);
			cellPosition.y = (int)((float)(location.y - this.originY) / this.cellWidth);
			return cellPosition;
		} else {
			return null;
		}
	}

	// Drawings

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Data data = StreetCar.player.getGameData();
		if (data == null) {
			System.out.println("data is null");
			return;
		}

		int x = this.originX;
		int y = this.originY;
		Graphics2D g2d = (Graphics2D)g;
		Tile[][] board = data.getBoard();

		// Grid
		for (int j=0; j < data.getHeight(); j++) {
			for (int i=0; i < data.getWidth(); i++) {
				Tile tile = board[i][j];
				TileImage.drawTile(g2d, tile, x, y, this.cellWidth);
				g2d.setColor(Color.GRAY);
				g2d.drawRect(x, y, cellWidth, cellWidth);
				x += this.cellWidth;
			}
			x = this.originX;
			y += this.cellWidth;
		}

		Color playerColor = null;
		String playerName = null;
		try {
			playerColor = StreetCar.player.getPlayerColor();
			playerName = StreetCar.player.getPlayerName();
		} catch (RemoteException e1) { }

		// Deck and its number of cards
		g2d.setColor(Color.BLACK);
		int deckX, deckY;
		deckX = originX + cellWidth*14 + 25;
		deckY = originY + cellWidth*7;
		BufferedImage bufferedImage = Resources.imageNamed(new String("deck_card"));	
		g2d.drawImage(bufferedImage, deckX-10, deckY-cellWidth/2, 50, 50, null);
		String NumberCardsInDeck = new String("" + data.getNbrRemainingDeckTile());
		g2d.drawString(NumberCardsInDeck, deckX+4, deckY-cellWidth/2+30);



		// Train movement
		//int pathLength = 0;
		for(Point p : chosenPath)
		{
			//pathLength++;
			x = this.originX + this.cellWidth * p.x;
			y = this.originY + this.cellWidth * p.y;
			//if(pathLength > allowedPathLength) playerColor = new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100);
			g2d.setColor(playerColor);
			g2d.drawRect(x, y, cellWidth, cellWidth);
		}

		for(String name : data.getPlayerNameList())
		{
			if(!data.hasStartedMaidenTravel(name)) continue;

			Color color = data.getPlayerColor(name);
			BufferedImage trainBufferedImage = null;
			if (color.equals(Color.BLACK)) {
				trainBufferedImage = Resources.imageNamed("tram_black");
			} else if (color.equals(Color.BLUE)) {
				trainBufferedImage = Resources.imageNamed("tram_blue");
			} else if (color.equals(Color.GREEN)) {
				trainBufferedImage = Resources.imageNamed("tram_green");
			} else if (color.equals(Color.ORANGE)) {
				trainBufferedImage = Resources.imageNamed("tram_orange");
			} else if (color.equals(Color.RED)) {
				trainBufferedImage = Resources.imageNamed("tram_red");
			} else if (color.equals(Color.WHITE)) {
				trainBufferedImage = Resources.imageNamed("tram_white");
			}

			Point currentTramPosition = data.getTramPosition(name);
			int tramX = this.originX + this.cellWidth * currentTramPosition.x;
			int tramY = this.originY + this.cellWidth * currentTramPosition.y;
			g2d.drawImage(trainBufferedImage, tramX+5, tramY+5, cellWidth-5, cellWidth-10, null);
			//Point previousTramPosition = data.getPreviousTramPosition(name);
		}

		for (Point p : highlights.keySet()) {
			BufferedImage img = highlights.get(p);
			int imgX = this.originX + this.cellWidth * p.x;
			int imgY = this.originX + this.cellWidth * p.y;			
			g2d.drawImage(img, imgX, imgY, cellWidth, cellWidth, null);
		}

		/*
		if(StreetCar.player.getGameData().hasStartedMaidenTravel(playerName)) {
			Point p = StreetCar.player.getGameData().getTramPosition(playerName);
			x = this.originX + this.cellWidth * p.x;
			y = this.originY + this.cellWidth * p.y;
			g2d.setColor(playerColor);
			g2d.drawRect(x, y, cellWidth, cellWidth);

//			Point previousPosition = new Point();
//			previousPosition = data.getPreviousTramPosition(playerName);
//			System.out.println(previousPosition);

			int i=0;
			if (trainMove.size() > 0) {
				i = trainMove.size();
				System.out.println("SIZE OF TRAINMOVE : " + i);

				if (trainMove.get(i-1).x == trainMove.get(i-2).x-1 || trainMove.get(i-1).x == trainMove.get(i-2).x+1) {
					// tram horizontal
					System.out.println("JE RENTRE DANS HORIZONTAL");
					AffineTransform at = new AffineTransform();
					if (trainBufferedImage == null) System.out.println("NULL");
					at.translate(trainBufferedImage.getWidth() / 2, trainBufferedImage.getHeight() / 2);
					at.rotate(Math.toRadians(90));
					at.translate(-trainBufferedImage.getWidth() / 2, -trainBufferedImage.getHeight() / 2);

					AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);		

					g2d.drawImage(op.filter(trainBufferedImage, null), x+5, y+5, cellWidth-5, cellWidth-5, null);

				} else if (trainMove.get(i-1).y == trainMove.get(i-2).y-1 || trainMove.get(i-1).y == trainMove.get(i-2).y+1) {
					// tram vertical
					System.out.println("JE RENTRE DANS HORIZONTAL");
					g2d.drawImage(trainBufferedImage, x+5, y+5, cellWidth-5, cellWidth-10, null);				
				} else {
					System.out.println("JE RENTRE DANS RIEN DU TOUT");				
				}

			} else {
				System.out.println("TRAINMOVE NULL");
			}


		}*/
	}

	// Mouse Listener

	public void mouseClicked(MouseEvent e) { }


	public void mousePressed(MouseEvent e) 
	{
		PlayerIHM player = StreetCar.player;
		Data data = player.getGameData();
		String name = null;
		try { name = player.getPlayerName(); } 
		catch (RemoteException e1) { }

		Point p = this.cellPositionForLocation(e.getPoint());
		if(data.hasStartedMaidenTravel(name))
		{
			if(!p.equals(data.getTramPosition(name))) return;
		}
		else
		{
			if(!data.isTrackCompleted(name)) return;
			LinkedList<Point> terminiPoints = new LinkedList<Point>(Arrays.asList(data.getPlayerTerminusPosition(name)));
			if(!terminiPoints.contains(p)) return;
		}
		chosenPath.clear();
		chosenPath.add(p);
		repaint();
	}

	public void mouseDragged(MouseEvent e) 
	{
		if(chosenPath.isEmpty()) return;
		Point p = cellPositionForLocation(e.getPoint());
		if(p == null) return;
		if(chosenPath.size() > 1 && p.equals(chosenPath.get(chosenPath.size() - 2)))
		{
			chosenPath.removeLast();
			repaint();
		}
		else
		{
			if(chosenPath.size() > StreetCar.player.getGameData().getMaximumSpeed()) return;
			if(p.equals(chosenPath.getLast())) return;
			if(!(Util.manhathanDistance(p, chosenPath.getLast()) == 1)) return;
			chosenPath.add(p);
			// TODO if(.data.isPath(gnagnagna))
			//			{
			//				chosenPath.removeLast()
			//				return;
			//			}
			repaint();
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if(chosenPath.isEmpty()) return;
		if(chosenPath.size() == 1) return;
		Point[] points = new Point[chosenPath.size()];
		for(int i = 0; i < chosenPath.size(); i++) 
		{
			points[i] = chosenPath.get(i);
		}
		try {
			StreetCar.player.moveTram(points, chosenPath.size(), chosenPath.getFirst());
		} catch (RemoteException | ExceptionNotYourTurn
				| ExceptionForbiddenAction | ExceptionGameHasNotStarted
				| ExceptionMissingStartTerminus | ExceptionWrongPlayerTerminus
				| ExceptionWrongTramwayPath | ExceptionWrongTramwaySpeed
				| ExceptionTramwayExceededArrival | ExceptionWrongTramwayStart
				| ExceptionWrongTramwayStartTerminus | ExceptionTramwayJumpCell
				| ExceptionTrtamwayDoesNotStop e1) {
			e1.printStackTrace();
		}

		chosenPath.clear();
		repaint();
	}

	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }

	// MouseMotionListener
	public void mouseMoved(MouseEvent e) { }

	// ComponentListener

	public void componentResized(ComponentEvent e) {
		this.updateMapGeometry();
	}

	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}

	// Refresh game

	public void refreshGame(PlayerIHM player, Data data) {
		this.updateMapGeometry();
	}

	public void moveTram(LinkedList<Point> tramPath) {
		// TODO Auto-generated method stub

	}

	private BufferedImage createHighlight(Color color) {
		BufferedImage bufferedImage = new BufferedImage(cellWidth, cellWidth, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(color);
		g2d.fillRect(0, 0, cellWidth, cellWidth);
		Color transparancyRectangle = new Color(0, 0, 0, 0);
		g2d.setColor(transparancyRectangle);
		g2d.fillRect(cellWidth/2, cellWidth/2, cellWidth/2, cellWidth/2);
		return bufferedImage;
	}

	private BufferedImage createTramTrail(Color color) {
		BufferedImage bufferedImage = new BufferedImage(cellWidth, cellWidth, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(color);
		int[] diamondTabX = {cellWidth/4, cellWidth/2, cellWidth/4*3, cellWidth/2};
		int[] diamondTabY = {cellWidth/4, cellWidth/2, cellWidth/4*3, cellWidth/2};
		g2d.drawPolygon(diamondTabX, diamondTabY, 4);
		return bufferedImage;
	}
}
