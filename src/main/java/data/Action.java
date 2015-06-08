package main.java.data;

import java.awt.Point;
import java.io.Serializable;

import main.java.util.CloneableInterface;
import main.java.util.TraceDebugData;



public class Action implements Serializable, CloneableInterface<Action>
{
// -----------------------------------------------------
// Attributes
// -----------------------------------------------------
	public static final long	serialVersionUID	= -7830218892745210163L;
	public static final int		maxTramwayMove		= 145;

	public static final int NONE							= -1;
	public static final int MOVE							= 0;
	public static final int BUILD_SIMPLE					= 1;
	public static final int TWO_BUILD_SIMPLE				= 2;
	public static final int BUILD_DOUBLE					= 3;
	public static final int BUILD_AND_START_TRIP_NEXT_TURN	= 4;

	public int		action = NONE;

	public Point	positionTile1							= new Point(-1, -1);			// Build Attributes
	public Point	positionTile2							= new Point(-1, -1);
	public Tile		tile1									= Tile.specialNonRealTileConstructor(null, -1, null);
	public Tile		tile2									= Tile.specialNonRealTileConstructor(null, -1, null);

	public Point[]	tramwayMovement							= initMovementTab();			// Move Attributes
	public int		tramwayMovementSize						= 0;							//		Index of the last non null point
	public Point	startTerminus							= null;

// -----------------------------------------------------
// Builder
// -----------------------------------------------------
	/**
	 * Creates a new Action for moving the streetcar.
	 * @param path : the path traveled
	 * @param pathSize : the length of the path
	 * @param startTerminus : the starting point of the travel (at the very beginning)
	 * @return a new Action, of type MOVE
	 */
	public static Action newMoveAction(Point[] path, int pathSize, Point startTerminus)
	{
		Action res = new Action();

		res.action = MOVE;
		res.tramwayMovementSize = pathSize;
		for (int i=0; i<pathSize; i++)
		{
			res.tramwayMovement[i].x = path[i].x;
			res.tramwayMovement[i].y = path[i].y;
		}
		res.startTerminus = (startTerminus == null) ? null : new Point(startTerminus);
		return res;
	}

	/**
	 * Creates a new Action for placing/switching a single tile on the board
	 * @param x : the x-coordinate on the board
	 * @param y : the y-coordinate on the board
	 * @param t : the tile to place on the board
	 * @return a new Action, of type BUILD_SIMPLE
	 */
	public static Action newBuildSimpleAction (int x, int y, Tile t)
	{
		Action res = new Action();

		res.action			= BUILD_SIMPLE;
		res.tile1			.copy(t);
		res.positionTile1.x	= x;
		res.positionTile1.y	= y;

		return res;
	}
	
	/**
	 * Creates a new Action for placing/switching a single tile on the board
	 * @param position : the position on the board
	 * @param t : the tile to place on the board
	 * @return a new Action, of type BUILD_SIMPLE
	 */
	public static Action newBuildSimpleAction (Point position, Tile t)
	{
		return newBuildSimpleAction(position.x, position.y, t);
	}

	/**
	 * Creates a new Action for sequentially placing two tiles </br>
	 * /!\ This is different from newBuildDoubleAction : the tiles are placed one after the other
	 * @param position1 : the position of the first tile
	 * @param tile1 : the first tile to place
	 * @param position2 : the position of the second tile
	 * @param tile2 : the second tile to place
	 * @return a new Action, of type TWO_BUILD_SIMPLE
	 */
	public static Action newBuildTwoSimpleAction (Point position1, Tile tile1, Point position2, Tile tile2){
		Action res = new Action();

		res.action = TWO_BUILD_SIMPLE;
		res.tile1.copy(tile1);
		res.tile2.copy(tile2);
		
		res.positionTile1.x	= position1.x;
		res.positionTile1.y	= position1.y;
		res.positionTile1.x	= position2.x;
		res.positionTile1.y	= position2.y;

		return res;		
	}
	
	/**
	 * Creates a new Action for simultaneously switching two tiles </br>
	 * /!\ This is different from newBuildTwoSimpleAction : the tiles are placed at the same time </br>
	 * Cf. rules for double switching
	 * @param position1 : the position of the first tile
	 * @param tile1 : the first tile to switch
	 * @param position2 : the position of the second tile
	 * @param tile2 : the second tile to switch
	 * @return a new Action, of type BUILD_DOUBLE
	 */
	public static Action newBuildDoubleAction (Point position1, Tile tile1, Point position2, Tile tile2){
		Action res = new Action();

		res.action = BUILD_DOUBLE;
		res.tile1.copy(tile1);
		res.tile2.copy(tile2);
		
		res.positionTile1.x	= position1.x;
		res.positionTile1.y	= position1.y;
		res.positionTile2.x	= position2.x;
		res.positionTile2.y	= position2.y;

		return res;		
	}

	/**
	 * Creates a new Action for placing/switching a single tile and ending the turn by starting the maiden travel.
	 * @param position : the position of the tile on the board
	 * @param t : the tile to place/switch
	 * @return a new Action, of type BUILD_AND_START_TRIP_NEXT_TURN
	 */
	public static Action newBuildAndStartTripNextTurnAction (Point position, Tile t)
	{
		return newBuildAndStartTripNextTurnAction(position.x, position.y, t);
	}

	/**
	 * Creates a new Action for placing/switching a single tile and ending the turn by starting the maiden travel.
	 * @param x : the x-coordinate of the tile on the board
	 * @param y : the y-coordinate of the tile on the board
	 * @param t : the tile to place/switch
	 * @return a new Action, of type BUILD_AND_START_TRIP_NEXT_TURN
	 */
	public static Action newBuildAndStartTripNextTurnAction (int x, int y, Tile t)
	{
		Action res = new Action();

		res.action			= BUILD_AND_START_TRIP_NEXT_TURN;
		res.tile1			.copy(t);
		res.positionTile1.x	= x;
		res.positionTile1.y	= y;

		return res;
	}

// -----------------------------------------------------
// Getter
// -----------------------------------------------------
	/**===================================================
	 * @return true if the action is a double action (you can play one only of this action in by round)
	 =====================================================*/
	public boolean isTwoStepAction()
	{
		return ((this.action == TWO_BUILD_SIMPLE)				||
				(this.action == BUILD_DOUBLE)					||
				(this.action == BUILD_AND_START_TRIP_NEXT_TURN) ||
				(this.action == MOVE));
	}
	/**
	 * @return
	 * Vrai si l'action est de type construction (simple ou double).
	 * Faux sinon.
	 */
	public boolean isConstructing()
	{
		return ((this.action == BUILD_SIMPLE)		||
				(this.action == TWO_BUILD_SIMPLE)	||
				(this.action == BUILD_DOUBLE)		||
				(this.action == BUILD_AND_START_TRIP_NEXT_TURN));
	}

	/**
	 * @return true if this action makes you place one and only one tile (BUILD_SIMPLE or BUILD_AND_START_TRIP_NEXT_TURN)
	 */
	public boolean isSimpleConstructing()
	{
		return ((this.action == BUILD_SIMPLE)		||
				(this.action == BUILD_AND_START_TRIP_NEXT_TURN));
	}
	
	/**
	 * @return true if this action makes the streetcar move (MOVE)
	 */
	public boolean isMoving()
	{
		return this.action == MOVE;
	}

	//Add by Ulysse
	public boolean isNONE(){
		return this.action==NONE;
	}
	public boolean isMOVE(){
		return this.action==MOVE;
	}
	public boolean isBUILD_SIMPLE(){
		return this.action==BUILD_SIMPLE;
	}
	public boolean isTWO_BUILD_SIMPLE(){
		return this.action==TWO_BUILD_SIMPLE;
	}
	public boolean isBUILD_DOUBLE(){
		return this.action==BUILD_DOUBLE;
	}
	public boolean isBUILD_AND_START_TRIP_NEXT_TURN(){
		return this.action == BUILD_AND_START_TRIP_NEXT_TURN;
	}
	
	
	
	
	
	@Override
	public String	toString()
	{
		String str = "";

		switch(this.action)
		{
			case MOVE:								str += "MOVE : "					+ this.tramwayMovement.toString()	+ "    StartTerminus: " + this.startTerminus;	break;
			case BUILD_SIMPLE:						str += "BUILD_SIMPLE: " 			+ this.positionTile1.toString()		+ this.tile1.toString();	break;
			case TWO_BUILD_SIMPLE:					str += "TWO_BUILD_SIMPLE: "			+ this.positionTile1.toString()		+ this.tile1.toString()	+ this.positionTile2.toString() + this.tile2.toString();	break;
			case BUILD_DOUBLE:						str += "BUILD_DOUBLE: " 			+ this.positionTile1.toString()		+ this.tile1.toString()	+ this.positionTile2.toString() + this.tile2.toString();	break;
			case BUILD_AND_START_TRIP_NEXT_TURN:	str += "BUILD_START_TRIP_NEXT_TURN";break;
			default:								str += "Other action: " + this.action;
		}
		return str;
	}
	

// -----------------------------------------------------
// Setter
// -----------------------------------------------------
	/**
	 *	Affecte a l'appelant les parametres de src 
	 *	/!\ Pas d'allocation de memoire
	 *	/!\ Ne doit pas être appelé par une instance non allouée de Action.
	 * @param src
	 * Action dont on copie le contenu
	 */
	public void copy(Action src)
	{
		this.action				= src.action;
		this.positionTile1.x	= (src.positionTile1 == null) ? null : src.positionTile1.x;
		this.positionTile1.y	= (src.positionTile1 == null) ? null : src.positionTile1.y;
		this.positionTile2.x	= (src.positionTile2 == null) ? null : src.positionTile2.x;
		this.positionTile2.y	= (src.positionTile2 == null) ? null : src.positionTile2.y;
		this.tile1				. copy(src.tile1);
		this.tile2				. copy(src.tile2);
		this.tramwayMovementSize= src.tramwayMovementSize;
		for (int i=0; i<src.tramwayMovementSize; i++)
		{
			this.tramwayMovement[i].x = src.tramwayMovement[i].x;
			this.tramwayMovement[i].y = src.tramwayMovement[i].y;
		}
		this.startTerminus = (src.startTerminus == null) ? null : new Point(src.startTerminus);

	}
	/**===========================================================
	 * Set the current Action to the given simple building action
	 =============================================================*/
	public void setSimpleBuilding(int x, int y, Tile tile) {
		this.action			= BUILD_SIMPLE;
		this.tile1			.copy(tile);
		this.positionTile1.x	= x;
		this.positionTile1.y	= y;
	}
	/**===========================================================
	 * Set the current Action to the given two simple building actions
	 =============================================================*/
	public void setTwoSimpleBuildingAction(int x1, int y1, Tile t1, int x2, int y2, Tile t2)
	{
		this.action					= TWO_BUILD_SIMPLE;
		this.positionTile1.x		= x1;
		this.positionTile1.y		= y1;
		this.tile1					.copy(t1);
		this.positionTile2.x		= x2;
		this.positionTile2.y		= y2;
		this.tile2					.copy(t2);
		this.tramwayMovementSize	= 0;
	}
	/**===========================================================
	 * Set the current Action to the given simple building action, and start trip nest turn
	 =============================================================*/
	public void setSimpleBuildingAndStartTripNextTurnAction(int x1, int y1, Tile t1)
	{
		this.action					= BUILD_AND_START_TRIP_NEXT_TURN;
		this.positionTile1.x		= x1;
		this.positionTile1.y		= y1;
		this.tile1					.copy(t1);
		this.positionTile2.x		= -1;
		this.positionTile2.y		= -1;
		this.tramwayMovementSize	= 0;
	}
	/**===========================================================
	 * Set the current Action to the given simple building action
	 =============================================================*/
	public void setDoubleBuildingAction(int x1, int y1, Tile t1, int x2, int y2, Tile t2)
	{
		this.action					= BUILD_DOUBLE;
		this.positionTile1.x		= x1;
		this.positionTile1.y		= y1;
		this.tile1					.copy(t1);
		this.positionTile2.x		= x2;
		this.positionTile2.y		= y2;
		this.tile2					.copy(t2);
		this.tramwayMovementSize	= 0;
	}
	/**===========================================================
	 * Set the current Action to the given travel action
	 =============================================================*/
	public void setMoveAction(Point startTerminus, Point[] tramwayMovement, int tramwayMovementSize)
	{
		this.action					= MOVE;
		this.positionTile1.x		= -1;
		this.positionTile1.y		= -1;
		this.positionTile2.x		= -1;
		this.positionTile2.y		= -1;
		this.tramwayMovementSize	= tramwayMovementSize;

		for (int i=0; i<tramwayMovementSize; i++)
		{
			this.tramwayMovement[i].x = tramwayMovement[i].x;
			this.tramwayMovement[i].x = tramwayMovement[i].y;
		}
		this.startTerminus			= (startTerminus == null) ? null : new Point(startTerminus);
	}

// -----------------------------------------------------
// Local methods
// -----------------------------------------------------
	/**
	 * Test de l'égalité avec une autre instance d'Action
	 * @param otherAction
	 * L'instance à comparer
	 * @return
	 * Vrai si les 2 actions représentent des actions équivalentes sur le plateau de jeu
	 * (c'est à dire: par exemple ne prend pas compte de l'attribut tile2 pour une pose simple...)
	 */
	public boolean equals(Action otherAction)
	{
		
		TraceDebugData.debugActionEquals("Begin action equals\n");
		if (otherAction == null){
			TraceDebugData.debugActionEquals("\t otherAction is null: return FALSE \n");
			return false;
		}
		if (this.action != otherAction.action){
			TraceDebugData.debugActionEquals("\t this is null: return FALSE \n");
			return false;
		}
		TraceDebugData.debugActionEquals("\t otherAction et this are instanciated : CONTINUE \n");

		if(this.isMOVE() && otherAction.isMOVE()){
			TraceDebugData.debugActionEquals("\t Both isMoving(): CONTINUE \n");
			if (this.tramwayMovementSize!=otherAction.tramwayMovementSize){
				TraceDebugData.debugActionEquals("\t ptrTramwayMovement different: return FALSE \n");
				return false;
			}
		if (!this.startTerminus.equals(otherAction.startTerminus)){
			TraceDebugData.debugActionEquals("\t startTerminus different: return FALSE \n");
			return false;
		}
			TraceDebugData.debugActionEquals("\t ptrTramwayMovement are equals: return CONTINUE \n");
			for (int i=0; i< this.tramwayMovementSize;i++){
				if(!(this.tramwayMovement[i].equals(otherAction.tramwayMovement[i]))){
					TraceDebugData.debugActionEquals("\t this.tramwayMovement["+i+"]="+this.tramwayMovement[i]+" != this.tramwayMovement["+i+"]="+otherAction.tramwayMovement[i]+" return FALSE \n");
					return false;
				}
			}
			return true;
		}

		if(this.isBUILD_SIMPLE() && otherAction.isBUILD_SIMPLE()){
			if(!this.positionTile1.equals(otherAction.positionTile1)){
				return false;
			}
			if(! this.tile1.equals(otherAction.tile1)){
				return false;
			}
			return true;
		}
		if(this.isBUILD_DOUBLE() && otherAction.isBUILD_DOUBLE()){
			if(!this.positionTile1.equals(otherAction.positionTile1)){
				return false;
			}
			if(!this.positionTile2.equals(otherAction.positionTile2)){
				return false;
			}
			if(! this.tile1.equals(otherAction.tile1)){
				return false;
			}
			if(! this.tile2.equals(otherAction.tile2)){
				return false;
			}
			return true;
		}
		
		if(this.isBUILD_AND_START_TRIP_NEXT_TURN() && otherAction.isBUILD_AND_START_TRIP_NEXT_TURN()) {
			if(!this.positionTile1.equals(otherAction.positionTile1)){
				return false;
			}
			if(! this.tile1.equals(otherAction.tile1)){
				return false;
			}
			return true;
		}
		
		if(this.isNONE() && otherAction.isNONE())	return true;
		return false;
	}
	/**
	 * Alloue une nouvelle instance identique de l'action appelante
	 * @return
	 * Une instance d'Action
	 */
	public Action getClone()
	{
		Action res				= new Action();
		res.action				= this.action;
		res.positionTile1.x		= this.positionTile1.x;
		res.positionTile1.y		= this.positionTile1.y;
		res.positionTile2.x		= this.positionTile2.x;
		res.positionTile2.y		= this.positionTile2.y;
		res.tile1				.copy(this.tile1);
		res.tile2				.copy(this.tile2);
		res.tramwayMovementSize	= this.tramwayMovementSize;
		res.startTerminus		= (this.startTerminus == null) ? null : new Point(this.startTerminus);
		for (int i=0; i<this.tramwayMovementSize; i++)
		{
			res.tramwayMovement[i].x = this.tramwayMovement[i].x;
			res.tramwayMovement[i].y = this.tramwayMovement[i].y;
		}
		return res;
	}

// -----------------------------------------------------
// Private methods
// -----------------------------------------------------
	/**
	 * Initialise un tableau de Point représentant un mouvement de tramway
	 * @return
	 * Tableau de Point de taille maxTramwayMove (145)
	 */
	private Point[] initMovementTab()
	{
		Point[] res = new Point[maxTramwayMove];
		for (int i=0; i<maxTramwayMove; i++) res[i] = new Point();
		return res;
	}

}