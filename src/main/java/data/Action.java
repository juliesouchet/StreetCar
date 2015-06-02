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

	public static final int NONE					= -1;
	public static final int MOVE					= 0;
	public static final int BUILD_SIMPLE			= 1;
	public static final int BUILD_DOUBLE			= 2;
	public static final int START_TRIP_NEXT_TURN	= 3;

	public int		action;

	public Point	positionTile1					= new Point(-1, -1);			// Build Attributes
	public Point	positionTile2					= new Point(-1, -1);
	public Tile		tile1							= Tile.specialNonRealTileConstructor(null, -1, null);
	public Tile		tile2							= Tile.specialNonRealTileConstructor(null, -1, null);

	public Point[]	tramwayMovement					= initMovementTab();			// Move Attributes
	public int		ptrTramwayMovement				= -1;							//		Index of the last non null point

// -----------------------------------------------------
// Builder
// -----------------------------------------------------
	/**
	 * Constructeur genérique: la mémoire des attributs n'est pas allouée,
	 * l'attribut action est mis a la valeur spéciale: NONE (-1)
	 */
	public Action(){ this.action = NONE;}
	
// TODO rajouter un param pour indiquer le terminus de depart
	/**
	 * Créer une instance d'une action de type start trip next turn.
	 * Remarque fait un new Action()
	 * @return
	 * un objet Action avec l'attribut action à la valeur speciale: START_TRIP_NEXT_TURN (3)
	 */
	public static Action newStartTripNextTurnAction()
	{
		Action res	= new Action();
		res.action	= START_TRIP_NEXT_TURN;
		return res;
	}
	
// TODO rajouter un param pour indiquer le terminus de depart
	/**
	 * Créé une instance d'Action de type mouvement du tramway
	 * @param tramwayMovement
	 * Tableau de point représentant la série de tuile par laquelle le tram va cheminer
	 * @return
	 * un objet Action avec l'attribut action à la valeur speciale: MOVE (3)
	 */
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
	/**
	 * Créer une instance d'Action de type pose d'une unique tuile.
	 * @param position
	 * Position où l'on souhaite poser la tuile
	 * @param tile
	 * La tuile que l'on souhaite déposer
	 * @return
	 * un objet Action avec les attributs:
	 * 		action=BUILD_SIMPLE
	 * 		positionTile1= coordonnée du point position (copié)
	 * 		tile1=tuile tile
	 */
	public static Action newBuildSimpleAction(Point position, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1.x	= position.x;
		res.positionTile1.y	= position.y;
		res.tile1			.setTile(tile);
		return res;
	}
	
	/**
	 * Créé une instance d'Action de type pose d'une unique tuile.
	 * @param x
	 * Abscisse où l'on souhaite poser la tuile
	 * @param y
	 * Ordonnée où l'on souhaite poser la tuile
	 * @param tile
	 * La tuile que l'on souhaite déposer
	 * @return
	 * un objet Action avec les attributs:
	 * 		action=BUILD_SIMPLE
	 * 		positionTile1= coordonnée du point position (copié)
	 * 		tile1=tuile tile
	 */
	public static Action newBuildSimpleAction(int x, int y, Tile tile)
	{
		Action res			= new Action();
		res.action			= BUILD_SIMPLE;
		res.positionTile1.x	= x;
		res.positionTile1.y	= y;
		res.tile1			.setTile(tile);
		return res;
	}
	
	/**
	 * Créé une instance d'Action de type pose de 2 tuiles
	 * @param position1
	 * Coordonnées de pose de la 1ere tuile
	 * @param tile1
	 * La 1ere tuile que l'on souhaite poser
	 * @param position2
	 * Coordonnées de pose de la 2nd tuile
	 * @param tile2
	 * La 2nd tuile que l'on souhaite poser
	 * @return
	 * un objet Action avec les attributs:
	 * 		action=BUILD_DOUBLE
	 * 		positionTile1= coordonnée du point position 1(copié)
	 * 		tile1=tuile tile1
	 * 		positionTile2= coordonnée du point position 2(copié)
	 * 		tile2=tuile tile2
	 */
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
	/**
	 * @return
	 * Vrai si l'action est de type construction (simple ou double).
	 * Faux sinon.
	 */
	public boolean isConstructing()			{return ((this.action == BUILD_SIMPLE)	|| (this.action == BUILD_DOUBLE));}

	/**
	 * @return
	 * Vrai si et seulement si l'action est de type construction simple.
	 * Faux sinon.
	 */
	public boolean isSimpleConstructing()	{return  (this.action == BUILD_SIMPLE);}
	
	/**
	 * @return
	 * Vrai si l'action est de type déplacement du tramway (déjà débuté ou pour le tour suivant)
	 */
	public boolean isMoving()				{return ((this.action == MOVE)			|| (this.action == START_TRIP_NEXT_TURN));}
	
	@Override
	public String	toString()
	{
		String str = "";

		switch(this.action)
		{
			case MOVE:					str += "MOVE : "				+ this.tramwayMovement.toString();	break;
			case BUILD_SIMPLE:			str += "BUILD_SIMPLE: " 		+ this.positionTile1.toString()		+ this.tile1.toString();	break;
			case BUILD_DOUBLE:			str += "BUILD_DOUBLE: " 		+ this.positionTile1.toString()		+ this.tile1.toString()	+ this.positionTile2.toString() + this.tile2.toString();	break;
			case START_TRIP_NEXT_TURN:	str += "START_TRIP_NEXT_TURN";	break;
		}
		return str;
	}
	
	//Ajout par Ulysse:
	/**
	 * Test de l'égalité aec une autre instance d'Action
	 * @param otherAction
	 * L'instance à comparer
	 * @return
	 * Vrai si les 2 actions représentent des actions équivalentes sur le plateu de jeu
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

		if(this.isMoving() && otherAction.isMoving()){
			TraceDebugData.debugActionEquals("\t Both isMoving(): CONTINUE \n");
			if (this.ptrTramwayMovement!=otherAction.ptrTramwayMovement){
				TraceDebugData.debugActionEquals("\t ptrTramwayMovement different: return FALSE \n");
				return false;
			}
			TraceDebugData.debugActionEquals("\t ptrTramwayMovement are equals: return CONTINUE \n");
			for (int i=0; i<= this.ptrTramwayMovement;i++){
				if(!(this.tramwayMovement[i].equals(otherAction.tramwayMovement[i]))){
					TraceDebugData.debugActionEquals("\t this.tramwayMovement["+i+"]="+this.tramwayMovement[i]+" != this.tramwayMovement["+i+"]="+otherAction.tramwayMovement[i]+" return FALSE \n");
					return false;
				}
			}
			return true;
		}

		if(this.isSimpleConstructing() && otherAction.isSimpleConstructing()){
			if(!this.positionTile1.equals(otherAction.positionTile1)){
				return false;
			}
			if(! this.tile1.equals(otherAction.tile1)){
				return false;
			}
			return true;
		}
		if(this.action==BUILD_DOUBLE && otherAction.action==BUILD_DOUBLE){
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
		return false;
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
		this.tile1				. setTile(src.tile1);
		this.tile2				. setTile(src.tile2);
		this.ptrTramwayMovement	= src.ptrTramwayMovement;
		for (int i=0; i<=src.ptrTramwayMovement; i++)
		{
			this.tramwayMovement[i].x = src.tramwayMovement[i].x;
			this.tramwayMovement[i].y = src.tramwayMovement[i].y;
		}
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
