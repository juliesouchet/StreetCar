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
import java.util.LinkedList;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionMissingStartTerminus;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionWrongPlayerTerminus;
import main.java.game.ExceptionWrongTramwayPath;
import main.java.game.ExceptionWrongTramwaySpeed;
import main.java.gui.application.StreetCar;
import main.java.gui.components.Panel;
import main.java.gui.util.Resources;
import main.java.player.PlayerIHM;


@SuppressWarnings("serial")
public class MapPanel extends Panel implements MouseListener, ComponentListener, MouseMotionListener {

	// Properties

    private int originX;
    private int originY;
    private int mapWidth;
    private int cellWidth;
    
    private Point trainPosition = new Point(3, 4);
    private LinkedList<Point> trainMove = new LinkedList<Point>();
    
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
		for(Point p : trainMove)
		{
			x = this.originX + this.cellWidth * p.x;
			y = this.originY + this.cellWidth * p.y;
			g2d.setColor(playerColor);
			g2d.fillRect(x, y, cellWidth, cellWidth);
		}
		if(StreetCar.player.getGameData().hasStartedMaidenTravel(playerName))
		{
			Point p = StreetCar.player.getGameData().getTramPosition(playerName);

			x = this.originX + this.cellWidth * p.x;
			y = this.originY + this.cellWidth * p.y;
			g2d.setColor(playerColor);
			g2d.fillRect(x, y, cellWidth, cellWidth);
		}
    }
	
	// Mouse Listener
	
	public void mouseClicked(MouseEvent e) 
	{
		Point p = this.cellPositionForLocation(e.getPoint());
		System.out.println("Tile ID is : " + StreetCar.player.getGameData().getTile(p).getTileID());
	}
	

	public void mousePressed(MouseEvent e) 
	{	
		//if(!canMoveTram()) return; TODO check if can move tram
		
		Point p = this.cellPositionForLocation(e.getPoint());
		trainMove.clear();
		trainMove.add(p);
		repaint();
	}

	private boolean canMoveTram() {
		PlayerIHM player = StreetCar.player;
		String playerName = null;
		try { playerName = player.getPlayerName(); } 
		catch (RemoteException e1) { }

		if(player.getGameData().hasStartedMaidenTravel(playerName)) return true;
		//player.getGameData().
		return false;
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		// TODO check here if trip can be done
		Point[] points = new Point[trainMove.size()];
		for(int i = 0; i < trainMove.size(); i++) 
		{
			points[i] = trainMove.get(i);
		}
		try {
			StreetCar.player.moveTram(points, trainMove.size(), trainMove.getFirst());
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionNotYourTurn e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionForbiddenAction e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionGameHasNotStarted e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionMissingStartTerminus e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionWrongPlayerTerminus e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionWrongTramwayPath e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExceptionWrongTramwaySpeed e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		trainMove.clear();
		repaint();
	}
	
	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }
	
	// MouseMotionListener
	public void mouseMoved(MouseEvent e) { }
	
	public void mouseDragged(MouseEvent e) 
	{
		// TODO check here if tram can move
		Point p = cellPositionForLocation(e.getPoint());
		if(p == null) return;
		if(trainMove.size() > 1 && p.equals(trainMove.get(trainMove.size() - 2)))
		{
			trainMove.removeLast();
			repaint();
		}
		else
		{
			if(p.equals(trainMove.getLast())) return;
			trainMove.add(p);
			repaint();
		}
	}
		
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

	public void startMaidenVoyage(Point point) {
		trainPosition = point;
	}

	public void moveTram(LinkedList<Point> tramPath) {
		// TODO Auto-generated method stub
		
	}
}
