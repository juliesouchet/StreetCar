package main.java.data;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;



public class Action implements Serializable
{
// -----------------------------------------------------
// Attributes
// -----------------------------------------------------
	private static final long serialVersionUID = -7830218892745210163L;

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
		return new Action();
	}
	public static Action newMoveAction(LinkedList<Point> tramwayMovement)
	{
		Action res			= new Action();
		res.tramwayMovement	= new LinkedList<Point>(tramwayMovement);
		return res;
	}
	public static Action newBuildSimpleAction(Point position, Tile tile)
	{
		Action res			= new Action();
		res.positionTile1	= new Point(position);
		res.tile1			= new Tile(tile);
		return res;
	}
	public static Action newBuildDoubleAction(Point position1, Tile tile1, Point position2, Tile tile2)
	{
		Action res			= new Action();
		res.positionTile1	= new Point(position1);
		res.tile1			= new Tile(tile1);
		res.positionTile2	= new Point(position2);
		res.tile2			= new Tile(tile2);
		return res;
	}

// -----------------------------------------------------
// Getter
// -----------------------------------------------------
	public boolean isConstructing()	{return ((this.action == BUILD_SIMPLE) || (this.action == BUILD_DOUBLE));}
}