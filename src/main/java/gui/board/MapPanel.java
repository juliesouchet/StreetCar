package main.java.gui.board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import main.java.data.Data;
import main.java.data.Tile;
import main.java.gui.components.Panel;
import main.java.player.PlayerIHM;


public class MapPanel extends Panel implements MouseListener {

	// Properties
	
	private static final long serialVersionUID = 1L;
	
    float min;
    float width;
    float widthPerCase;
    int originX;
    int originY;
    Data data;
	BufferedImage bufferedImage;
    
    // Constructors
    
	public MapPanel() {
		this.setBackground(Color.WHITE);
		this.addMouseListener(this);

		MapPanelDropTargetListener dropTarget = new MapPanelDropTargetListener(this);
        this.setDropTarget(new DropTarget(this, dropTarget));
	}
	
	//
	
	protected void changeGlobalValues() {
		this.min = Math.min(this.getWidth(), this.getHeight());
		this.width = (float) (0.96 * min);
		this.widthPerCase = width/14;
	    this.originX = (this.getWidth() - Math.round(width)) / 2;
	    this.originY = (this.getHeight() - Math.round(width)) / 2;
	}
	
	// Cells
	
	public Point cellPositionForLocation(Point location) {
		if (location.x >= originX &&  location.x <= originX + width &&
			location.y >= originY &&  location.y <= originY + width) {
			Point cellPosition = new Point();
			cellPosition.x = (int)((float)(location.x - originX) / widthPerCase);
			cellPosition.y = (int)((float)(location.y - originY) / widthPerCase);
			return cellPosition;
		} else {
			return null;
		}
	}
	
	// Paint Component
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.data == null) {
        	System.out.println("data is null");
        	return;
        }
        
        System.out.println("data is not null");
        
        changeGlobalValues();
        int x = (int)(widthPerCase/2);
        int y = (int)(widthPerCase/2);
        
        for (float i=0; i<13; i++) {
        g.drawLine(originX,
        		   originY + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originX + Math.round(width),
        		   originY + Math.round(widthPerCase*i) + Math.round(widthPerCase));
        
        g.drawLine(originX + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originY,
        		   originX + Math.round(widthPerCase*i) + Math.round(widthPerCase),
        		   originY + Math.round(width));        
        }          
        g.drawRect(originX, originY, (int)width+1, (int)width+1);

    	Tile[][] board = this.data.getBoard();
		for (int j=0; j < this.data.getHeight(); j++) {
			for (int i=0; i < this.data.getWidth(); i++) {
				TileImage tileImage = new TileImage();
				this.bufferedImage = tileImage.getImage(board[i][j].getTileID());
				if (bufferedImage == null) {
					System.out.println("image null");
				} else {
					AffineTransformOp transform = getRotation(board[i][j], bufferedImage, (int)widthPerCase, (int)widthPerCase);
					g.drawImage(transform.filter(bufferedImage,null), x, y, null);
					x += (int)(widthPerCase);
				}

			}
			x = (int)(widthPerCase)/2;
			y += (int)(widthPerCase);
		}
    }

	
	/*private int tileWidth;
	private int tileHeight;
	private int paddingWidth = 40;
	private int paddingHeight = 40;
	
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);

		if (this.data == null) {
			System.out.println("data is null");
			return;
		}
		
		System.out.println("data is not null");
		
		this.tileWidth	= (getWidth() - paddingWidth) / data.getWidth();
		this.tileHeight	= (getHeight()- paddingHeight)/ data.getHeight();
		String cst = "src/main/resources/images/tiles/";
		BufferedImage img = null;
		String tileName;
		Tile[][] board = this.data.getBoard();
		int x = tileWidth/2;
		int y = tileHeight/2;

		System.out.println(this.data.getHeight() + " " + this.data.getWidth());
		
		for (int j=0; j < this.data.getHeight(); j++)
		{
			for (int i=0; i < this.data.getWidth(); i++)
			{
				tileName = cst + board[i][j].getTileID();
				System.out.println(tileName);
				try {
					img = ImageIO.read(new File(tileName));
				}
				catch (IOException e) {
					e.printStackTrace(); 
					System.exit(0);
				}
				AffineTransformOp transform = getRotation(board[i][j], img, tileWidth, tileHeight);
				g.drawImage(transform.filter(img,null), x, y, null);
				x += tileWidth;
			}
			x = tileWidth/2;
			y += tileHeight;
		}
	}*/

	private AffineTransformOp getRotation(Tile tile, BufferedImage img, int tileWidth, int tileHeight) {
		int rightRotations = tile.getTileDirection().getVal();
		double rotationRequired = Math.toRadians(rightRotations*90);
		double locationX = tileWidth / 2;
		double locationY = tileHeight / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		double xScale = (double)tileWidth/(double)img.getWidth();
		double yScale = (double)tileHeight/(double)img.getHeight();
		tx.concatenate(AffineTransform.getScaleInstance(xScale, yScale));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
	
	// Mouse Action
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = this.cellPositionForLocation(e.getPoint());
		if (p != null) {
			System.out.println(p);
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// not needed
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// not needed
	}   

	@Override
	public void mouseEntered(MouseEvent e) {
		// not needed
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not needed
	}
	
	// Refresh game
	
	public void refreshGame(PlayerIHM player, Data data) {
		System.out.println("REFRESH GAME in board");
		this.data = data;
		this.repaint();
	}
}
