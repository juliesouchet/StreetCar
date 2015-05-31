package main.java.data;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

import main.java.util.CloneableInterface;
import main.java.util.Copier;



public class Action implements Serializable, CloneableInterface<Action>
{
// -----------------------------------------------------
// Attributes
// -----------------------------------------------------
	public static final long serialVersionUID = -7830218892745210163L;

	public static final int MOVE					= 0;
	public static final int BUILD_SIMPLE			= 1;
	public static final int BUILD_DOUBLE			= 2;
	public static final int START_TRIP_NEXT_TURN	= 3;

	public int		action;

	public Point	positionTile1;				// Build Attributes
	public Point	positionTile2;
	public Tile		tile1;
	public Tile		tile2;

	public LinkedList<Point> tramwayMovement;	// Move Attributes

// -----------------------------------------------------
// Builder
// -----------------------------------------------------
	public static Action newStartTripNextTurnAction()
	{
		Action res	= new Action();
		res.action	= START_TRIP_NEXT_TURN;
		return res;
	}
	public static Action newMoveAction(LinkedList<Point> tramwayMovement)
	{
		Action res			= new Action();
		res.action			= MOVE;
		res.tramwayMovement	= new LinkedList<Point>(tramwayMovement);
		return res;
	}
	public static Action newBuildSimpleAction(Point position, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1	= new Point(position);
		res.tile1			= tile.getClone();
		return res;
	}
	public static Action newBuildSimpleAction(int x, int y, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1	= new Point(x,y);
		res.tile1			= tile.getClone();
		return res;
	}
	public static Action newBuildDoubleAction(Point position1, Tile tile1, Point position2, Tile tile2)
	{
		Action res			= new Action();
		res.action			= BUILD_DOUBLE;
		res.positionTile1	= new Point(position1);
		res.tile1			= tile1.getClone();
		res.positionTile2	= new Point(position2);
		res.tile2			= tile2.getClone();
		return res;
	}
	private Action(){}
	public Action getClone()
	{
		Action res = new Action();
		res.action	= this.action;
		res.positionTile1	= (this.positionTile1	== null) ? res.positionTile1 = null	: new Point(this.positionTile1);
		res.positionTile2	= (this.positionTile2	== null) ? res.positionTile2 = null	: new Point(this.positionTile2);
		res.tile1			= (this.tile1			== null) ? res.tile1 = null			: this.tile1.getClone();
		res.tile2			= (this.tile2			== null) ? res.tile2 = null			: this.tile2.getClone();
		res.tramwayMovement	= (this.tramwayMovement	== null) ? res.tramwayMovement=null	: (new Copier<Point>()).copyList(this.tramwayMovement);
		return res;
	}

// -----------------------------------------------------
// Getter
// -----------------------------------------------------
	public boolean isConstructing()			{return ((this.action == BUILD_SIMPLE)	|| (this.action == BUILD_DOUBLE));}
	public boolean isSimpleConstructing()	{return  (this.action == BUILD_SIMPLE);}
	public boolean isMoving()				{return ((this.action == MOVE)			|| (this.action == START_TRIP_NEXT_TURN));}


	
	
	//Ajout par Ulysse:
	/**
	 * affecte a l'appelant les parametres de src sans nouvelle allocation memoire.
	 * @param src
	 */
	public void copy(Action src){
		this.action = src.action;
		this.positionTile1.x = src.positionTile1.x;
		this.positionTile1.y = src.positionTile1.y;
//		this.positionTile2.x = src.positionTile2.x;
//		this.positionTile2.y = src.positionTile2.y;		
		
		//TODO ramplacer getClone par copy() sans allocation memoire
//		this.tile1=src.tile1.getClone();
//		this.tile2=src.tile2.getClone();
//		this.tramwayMovement = (LinkedList<Point>)src.tramwayMovement.clone();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}