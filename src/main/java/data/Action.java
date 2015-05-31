package main.java.data;

import java.awt.Point;
import java.io.Serializable;

import main.java.util.CloneableInterface;



public class Action implements Serializable, CloneableInterface<Action>
{
// -----------------------------------------------------
// Attributes
// -----------------------------------------------------
	public static final long	serialVersionUID	= -7830218892745210163L;
	public static final int		maxTramwayMove		= 145;

	public static final int MOVE					= 0;
	public static final int BUILD_SIMPLE			= 1;
	public static final int BUILD_DOUBLE			= 2;
	public static final int START_TRIP_NEXT_TURN	= 3;

	public int		action;

	public Point	positionTile1					= new Point();					// Build Attributes
	public Point	positionTile2					= new Point();
	public Tile		tile1							= new Tile(null, -1, null);
	public Tile		tile2							= new Tile(null, -1, null);

	public Point[]	tramwayMovement					= initMovementTab();			// Move Attributes
	public int		ptrTramwayMovement				= -1;							//		Index of the last non null point

// -----------------------------------------------------
// Builder
// -----------------------------------------------------
	public static Action newStartTripNextTurnAction()
	{
		Action res	= new Action();
		res.action	= START_TRIP_NEXT_TURN;
		return res;
	}
	public static Action newMoveAction(Point[] tramwayMovement)
	{
		Action res				= new Action();
		res.action				= MOVE;
		res.ptrTramwayMovement	= tramwayMovement.length-1;
		for (int i=0; i<tramwayMovement.length; i++)
		{
			res.tramwayMovement[i].x = tramwayMovement[i].x;
			res.tramwayMovement[i].y = tramwayMovement[i].y;
		}
		return res;
	}
	public static Action newBuildSimpleAction(Point position, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1.x	= position.x;
		res.positionTile1.y	= position.y;
		res.tile1			.setTile(tile);
		return res;
	}
	public static Action newBuildSimpleAction(int x, int y, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1.x	= x;
		res.positionTile1.y	= y;
		res.tile1			.setTile(tile);
		return res;
	}
	public static Action newBuildDoubleAction(Point position1, Tile tile1, Point position2, Tile tile2)
	{
		Action res			= new Action();
		res.action			= BUILD_DOUBLE;
		res.positionTile1.x	= position1.x;
		res.positionTile1.y	= position1.y;
		res.positionTile2.x	= position2.x;
		res.positionTile2.y	= position2.y;
		res.tile1			.setTile(tile1);
		res.tile2			.setTile(tile2);
		return res;
	}
	private Action(){}
	public Action getClone()
	{
		Action res				= new Action();
		res.action				= this.action;
		res.positionTile1.x		= this.positionTile1.x;
		res.positionTile1.y		= this.positionTile1.y;
		res.positionTile2.x		= this.positionTile2.x;
		res.positionTile2.y		= this.positionTile2.y;
		res.tile1				.setTile(this.tile1);
		res.tile2				.setTile(this.tile2);
		res.ptrTramwayMovement	= this.ptrTramwayMovement;
		for (int i=0; i<=this.ptrTramwayMovement; i++)
		{
			res.tramwayMovement[i].x = this.tramwayMovement[i].x;
			res.tramwayMovement[i].y = this.tramwayMovement[i].y;
		}
		return res;
	}

// -----------------------------------------------------
// Getter
// -----------------------------------------------------
	public boolean isConstructing()			{return ((this.action == BUILD_SIMPLE)	|| (this.action == BUILD_DOUBLE));}
	public boolean isSimpleConstructing()	{return  (this.action == BUILD_SIMPLE);}
	public boolean isMoving()				{return ((this.action == MOVE)			|| (this.action == START_TRIP_NEXT_TURN));}

// -----------------------------------------------------
// Setter
// -----------------------------------------------------
	/**
	 * affecte a l'appelant les parametres de src sans nouvelle allocation memoire.
	 * @param src
	 */

	public void copy(Action src)
	{
		this.action				= src.action;
		this.positionTile1.x	= (src.positionTile1 == null) ? null : src.positionTile1.x;
		this.positionTile1.y	= (src.positionTile1 == null) ? null : src.positionTile1.y;
		this.positionTile2.x	= (src.positionTile2 == null) ? null : src.positionTile2.x;
		this.positionTile2.y	= (src.positionTile2 == null) ? null : src.positionTile2.y;
		this.tile1				. setTile(src.tile1);
		this.tile2				. setTile(src.tile2);
		this.ptrTramwayMovement	= src.ptrTramwayMovement;
		for (int i=0; i<=src.ptrTramwayMovement; i++)
		{
			this.tramwayMovement[i].x = this.tramwayMovement[i].x;
			this.tramwayMovement[i].y = this.tramwayMovement[i].y;
		}
	}

// -----------------------------------------------------
// Private methods
// -----------------------------------------------------
	private Point[] initMovementTab()
	{
		Point[] res = new Point[maxTramwayMove];
		for (int i=0; i<maxTramwayMove; i++) res[i] = new Point();
		return res;
	}
}
