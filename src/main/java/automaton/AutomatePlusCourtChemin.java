package main.java.automaton;

import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;
import main.java.util.Direction;

/**
 * Premiere implementation d'un automate que cherche a poser des rails pour creer le chemin le plus courts de son terminus vers ses points de passage puis vers autre terminus
 * v1 : il crée un chemin vers entre une origine et un destiantion; fonctionne pour l'ia seule sur un terrain vierge
 * @author ulysse
 *
 */
public class AutomatePlusCourtChemin extends PlayerAutomaton {
	private int MAX_LENGTH_OF_PATH;

	Data currentData;

	public heuristicNode heuristic[][];
	int width;
	int height;
	ArrayList<Point> myTerminus;
	public ArrayList<Point> myStops;

	Point[] bestPathPoint;
	Tile[] bestPathTile;
	int bestPathLength;
	boolean write =false;

	ArrayList<Point> theBestPath;
	ArrayList<Tile> theBestTiles;


	public heuristicNode[][] bufferHeuristic;

	class heuristicNode{
		int North=0;
		int West=0;
		int South=0;
		int East=0;


		void setAllTo(int value){
			this.East = value;
			this.North = value;
			this.South = value;
			this.West = value;
		}

		int getMinValue(){
			if(this.North<=this.East && this.North<=this.West && this.North<=this.South){
				return this.North;
			}else if(this.West<=this.East && this.West<=this.North && this.West<=this.South){
				return this.West;
			}else if(this.East<=this.North && this.East<=this.West && this.East<=this.South){
				return this.East;
			}else {//if(this.South<this.East && this.South<this.West && this.South<this.South){
				return this.South;
			}
		}

		Direction getMinDirection(){
			if(this.North<=this.East && this.North<=this.West && this.North<=this.South){
				return Direction.NORTH;
			}else if(this.West<=this.East && this.West<=this.North && this.West<=this.South){
				return Direction.WEST;
			}else if(this.East<=this.North && this.East<=this.West && this.East<=this.South){
				return Direction.EAST;
			}else {//if(this.South<this.East && this.South<this.West && this.South<this.South){
				return Direction.SOUTH;
			}
		}
	}



	/*=============================================================================
	 * CONSTRUCTOR
	 *=============================================================================*/

	/**
	 * Instanciation d'un automate pour calculer le plus court chemin possible (imaginable).
	 * @param width Largeur du terrain.
	 * @param height Hauteur du terrain.
	 * @param numberOfStop Nombre de stop.
	 */
	public AutomatePlusCourtChemin(Data currentConfiguration, ArrayList<Point> terminus, ArrayList<Point> stops){

		this.currentData = currentConfiguration;
		this.width = currentConfiguration.getWidth();
		this.height = currentConfiguration.getHeight();
		this.heuristic = new heuristicNode[this.width][this.height];
		for (int i=0;i<this.width;i++){
			for (int j=0; j<this.height; j++){
				this.heuristic[i][j]= new heuristicNode();
			}
		}
		this.myTerminus = (ArrayList<Point>) terminus.clone();
		this.myStops = (ArrayList<Point>) stops.clone();

		this.MAX_LENGTH_OF_PATH = this.width * this.height;
		this.bestPathPoint = new Point[MAX_LENGTH_OF_PATH];
		this.bestPathTile = new Tile[MAX_LENGTH_OF_PATH];
		this.bestPathLength = 0;
		this.bufferHeuristic = new heuristicNode[this.width][this.height];
		for (int i=0;i<this.width;i++){
			for (int j=0; j<this.height; j++){
				this.bufferHeuristic[i][j]= new heuristicNode();
			}
		}
		ArrayList<Point> theBestPath = new ArrayList<Point>(50);
		ArrayList<Tile> theBestTiles  = new ArrayList<Tile>(50);

		this.write = false;

	}

	/*=============================================================================
	 * SETTER
	 *=============================================================================*/
	/**
	 * Calcule l'heuristique de distance de Manhatan.
	 */
	public void computeHeuristic(){

		reset(this.heuristic);
		reset(this.bufferHeuristic);
		computeUnaccessibleHeuristic(this.bufferHeuristic);
		mul(this.bufferHeuristic,1);
		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		reset(this.bufferHeuristic);

		computePositiveConnexityHeuristic(this.bufferHeuristic);
		mul(this.bufferHeuristic,3);
		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		reset(this.bufferHeuristic);

		computeNegativeConnexityHeuristic(this.bufferHeuristic);
		mul(this.bufferHeuristic,3);
		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		reset(this.bufferHeuristic);

		for(int i=0; i<this.myStops.size(); i++){
			computeManhatanHeuristic(this.myStops.get(i), this.bufferHeuristic);
			mul(this.bufferHeuristic,1);
			add(this.heuristic, this.heuristic,this.bufferHeuristic);
			reset(this.bufferHeuristic);
		}
		for(int i=0; i<this.myTerminus.size(); i++){
			computeManhatanHeuristic(this.myTerminus.get(i), this.bufferHeuristic);
			mul(this.bufferHeuristic,1);
			add(this.heuristic, this.heuristic,this.bufferHeuristic);
			reset(this.bufferHeuristic);
		}



	}

	public void computeHeuristic(ArrayList<Point> targets1, ArrayList<Point> targets2){
		boolean trace=false;

		if(trace)System.out.println("\n\n\n/!\\computeHeuristic/!\\\n\n\n");

		reset(this.heuristic);
		reset(this.bufferHeuristic);

		computeUnaccessibleHeuristic(this.bufferHeuristic);
		mul(this.bufferHeuristic,1);
		if(trace)System.out.println("computeUnaccessibleHeuristic");
		if(trace)printMatrice(bufferHeuristic);

		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		reset(this.bufferHeuristic);

		//		computePositiveConnexityHeuristic(this.bufferHeuristic);
		//		mul(this.bufferHeuristic,3);
		//		if(trace)System.out.println("computePositiveConnexityHeuristic");
		//		if(trace)printMatrice(bufferHeuristic);
		//
		//		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		//		reset(this.bufferHeuristic);
		//
		//		computeNegativeConnexityHeuristic(this.bufferHeuristic);
		//		mul(this.bufferHeuristic,3);
		//		if(trace)System.out.println("computeNegativeConnexityHeuristic");
		//		if(trace)printMatrice(bufferHeuristic);

		add(this.heuristic, this.heuristic,this.bufferHeuristic);
		reset(this.bufferHeuristic);

		for (int i=0; i<targets1.size(); i++){
			computeManhatanHeuristic(targets1.get(i), this.bufferHeuristic);
			mul(this.bufferHeuristic,1);
			add(this.heuristic, this.heuristic,this.bufferHeuristic);
			if(trace)System.out.println("computeManhatanHeuristic:"+targets1.get(i));
			if(trace)printMatrice(bufferHeuristic);			
			reset(this.bufferHeuristic);
		}

		for (int i=0; i<targets2.size(); i++){
			computeManhatanHeuristic(targets2.get(i), this.bufferHeuristic);
			mul(this.bufferHeuristic,1);
			add(this.heuristic, this.heuristic,this.bufferHeuristic);
			if(trace)System.out.println("computeManhatanHeuristic:"+targets2.get(i));
			if(trace)printMatrice(bufferHeuristic);			
			reset(this.bufferHeuristic);		}

		if(trace)	System.out.println("\n\n\n/!\\computeHeuristic DONE/!\\\n\n\n");


	}


	/**
	 * Calcule l'heuristique pour un point donné. (Pour l'instant distance de manhatan, TODO : pondérer avec tuiles existantes.)
	 * @param cible Le point visé.
	 * @param heuristique La matrice à remplir.
	 */
	@SuppressWarnings("unused")
	private void computeManhatanHeuristic(Point cible, heuristicNode[][] heuristique){

		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){

				if(j>cible.y){
					heuristique[i][j].North=(Math.abs(i-cible.x)+Math.abs(j-cible.y));
					heuristique[i][j].South=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
				}else if(j<cible.y){
					heuristique[i][j].North=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
					heuristique[i][j].South=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*2;
				}else{
					heuristique[i][j].North=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
					heuristique[i][j].South=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
				}
				if(i<cible.x){
					heuristique[i][j].East=(Math.abs(i-cible.x)+Math.abs(j-cible.y));
					heuristique[i][j].West=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
				}else if(i>cible.x){
					heuristique[i][j].East=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
					heuristique[i][j].West=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*2;
				}else{
					heuristique[i][j].East=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
					heuristique[i][j].West=(Math.abs(i-cible.x)+Math.abs(j-cible.y))*4;
				}


			}
		}
	}
	/**
	 * Le gain des chemins preexistants
	 * @param heuristique
	 */
	public void computePositiveConnexityHeuristic(heuristicNode[][] heuristique){
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				if(this.currentData.getTile(i, j).isPathTo(Direction.NORTH)){
					heuristique[i][j].North=-1;
				}
				if(this.currentData.getTile(i, j).isPathTo(Direction.WEST)){
					heuristique[i][j].West=-1;
				}
				if(this.currentData.getTile(i, j).isPathTo(Direction.EAST)){
					heuristique[i][j].East=-1;
				}
				if(this.currentData.getTile(i, j).isPathTo(Direction.SOUTH)){
					heuristique[i][j].South=-1;
				}
			}
		}
	}
	/**
	 * La perte des chemins pre-existant
	 * @param heuristique
	 */
	public void computeNegativeConnexityHeuristic(heuristicNode[][] heuristique){
		int value;
		for(int i=1; i<this.width-1;i++){
			for (int j=1; j<this.height-1; j++){
				if(this.currentData.getTile(i, j).isDeckTile()){
					if(this.currentData.getTile(i, j).isTree()){
						value = Integer.MAX_VALUE;
					}else{
						value = +1;
					}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.NORTH)){
						heuristique[i][j].North=value;						
					}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.WEST)){
						heuristique[i][j].West=value;
					}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.EAST)){
						heuristique[i][j].East=value;
					}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.SOUTH)){
						heuristique[i][j].South=value;
					}
				}
			}
		}
	}

	public void computeUnaccessibleHeuristic(heuristicNode[][] heuristique){
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				if (this.currentData.getTile(i, j).isBuilding()){
					heuristique[i][j].setAllTo(Integer.MAX_VALUE);
					heuristique[i+1][j].West = Integer.MAX_VALUE;
					heuristique[i-1][j].East = Integer.MAX_VALUE;
					heuristique[i][j+1].North = Integer.MAX_VALUE;
					heuristique[i][j-1].South = Integer.MAX_VALUE;
				}
				if(this.currentData.isOnEdge(i, j) ){

					if(!this.currentData.getTile(i, j).isPathTo(Direction.EAST)){heuristique[i][j].East = Integer.MAX_VALUE;}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.SOUTH)){heuristique[i][j].South = Integer.MAX_VALUE;}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.WEST)){heuristique[i][j].West = Integer.MAX_VALUE;}
					if(!this.currentData.getTile(i, j).isPathTo(Direction.NORTH)){heuristique[i][j].North = Integer.MAX_VALUE;}


					if(i==0){
						if(!this.currentData.getTile(i, j).isPathTo(Direction.EAST)){
							heuristique[i+1][j].West = Integer.MAX_VALUE;
						}
					}
					if(j==0){
						if(!this.currentData.getTile(i, j).isPathTo(Direction.SOUTH)){
							heuristique[i][j+1].North = Integer.MAX_VALUE;
						}
					}
					if(i==this.width){
						if(!this.currentData.getTile(i, j).isPathTo(Direction.WEST)){
							heuristique[i-1][j].East = Integer.MAX_VALUE;
						}
					}
					if(j==this.height){
						if(!this.currentData.getTile(i, j).isPathTo(Direction.NORTH)){
							heuristique[i][j-1].South= Integer.MAX_VALUE;
						}
					}

				}


			}
		}
	}

	/*=============================================================================
	 * GETTER
	 *=============================================================================*/

	private boolean isStop(Point p){
		for (int i=0; i<this.myStops.size(); i++){
			if (myStops.get(i).equals(p)){
				return true;
			}
		}
		return false;
	}
	private boolean isTerminus(Point p){
		for (int i=0; i<this.myTerminus.size(); i++){
			if (myTerminus.get(i).equals(p)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Les terminus ideaux.
	 */
	public Point[] getBestTerminus(){
		Point[] resultat = new Point[2];
		for(int j=0; j<2; j++){

			int bestValue=this.heuristic[this.myTerminus.get(0).x][this.myTerminus.get(0).y].getMinValue();
			int bestIndex=0;
			for (int i=1; i<this.myTerminus.size(); i++){
				if(this.heuristic[this.myTerminus.get(i).x][this.myTerminus.get(i).y].getMinValue()<bestValue){
					bestValue =  this.heuristic[this.myTerminus.get(i).x][this.myTerminus.get(i).y].getMinValue();
					bestIndex = i;
				}

			}

			resultat[j] = this.myTerminus.get(bestIndex);
			Point voisin = new Point(new Point(resultat[j].x+1,resultat[j].y));

			voisin.x = resultat[j].x+1;
			voisin.y = resultat[j].y;
			if(this.myTerminus.contains(voisin)){
				this.myTerminus.remove(voisin);
			}
			voisin.x = resultat[j].x;
			voisin.y = resultat[j].y+1;
			if(this.myTerminus.contains(voisin)){
				this.myTerminus.remove(voisin);
			}
			voisin.x = resultat[j].x-1;
			voisin.y = resultat[j].y;
			if(this.myTerminus.contains(voisin)){
				this.myTerminus.remove(voisin);
			}
			voisin.x = resultat[j].x;
			voisin.y = resultat[j].y-1;
			if(this.myTerminus.contains(voisin)){
				this.myTerminus.remove(voisin);
			}
			this.myTerminus.remove(resultat[j]);
		}
		this.myTerminus.add(resultat[0]);
		this.myTerminus.add(resultat[1]);
		return resultat;
	}

	public boolean[] myStopsAreSetted(){
		boolean[] result = new boolean[this.myStops.size()];
		Tile[][] currentBoard = this.currentData.getBoard();
		for( int i=0; i < this.myStops.size(); i++){
			if(currentBoard[this.myStops.get(i).x][this.myStops.get(i).y].isBuilding()){
				if (currentBoard[this.myStops.get(i).x+1][this.myStops.get(i).y].isStop()){
					this.myStops.get(i).x++;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops.get(i).x][this.myStops.get(i).y+1].isStop()){
					this.myStops.get(i).y++;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops.get(i).x-1][this.myStops.get(i).y].isStop()){
					this.myStops.get(i).x--;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops.get(i).x][this.myStops.get(i).y-1].isStop()){
					this.myStops.get(i).y--;
					result[i]=true;
					break;
				}
				else {
					result[i]=false; }
			}
			if(this.currentData.getBoard()[this.myStops.get(i).x][this.myStops.get(i).y].isStop()){
				result[i]=true;
			}
		}

		return result;
	}

	/*=============================================================================
	 * UTIL
	 *=============================================================================*/





	private static heuristicNode get(heuristicNode[][] matrice, Point position){
		return matrice[position.x][position.y];
	}
	private static void reset(heuristicNode[][] matrice){
		for(int i=0; i<matrice.length; i++)
			for( int j=0; j<matrice[i].length; j++){
				matrice[i][j].setAllTo(0);
			}
	}

	private static void mul(heuristicNode[][] matrice, int facteur){
		for (int i=0; i<matrice.length; i++){
			for(int j=0; j<matrice[i].length; j++){
				matrice[i][j].East = matrice[i][j].East * facteur;
				matrice[i][j].North = matrice[i][j].North * facteur;
				matrice[i][j].South = matrice[i][j].South * facteur;
				matrice[i][j].West = matrice[i][j].West * facteur;
			}
		}
	}

	private static boolean add(heuristicNode[][] resultat, heuristicNode[][] heuristique1, heuristicNode[][] heuristique2){
		if(heuristique1.length!=heuristique2.length){
			return false;
		}
		int largeur = heuristique1.length;
		int hauteur=heuristique1[0].length;
		for(int i=0; i<largeur; i++){
			if(heuristique1[i].length!=heuristique2[i].length){
				return false;
			}
			for(int j=0; j<hauteur; j++){

				if(  heuristique1[i][j].West==Integer.MAX_VALUE || heuristique2[i][j].West==Integer.MAX_VALUE){
					resultat[i][j].West =Integer.MAX_VALUE;
				} else {
					resultat[i][j].West = heuristique1[i][j].West + heuristique2[i][j].West;
				}
				if(  heuristique1[i][j].North==Integer.MAX_VALUE || heuristique2[i][j].North==Integer.MAX_VALUE){
					resultat[i][j].North =Integer.MAX_VALUE;
				} else {
					resultat[i][j].North = heuristique1[i][j].North + heuristique2[i][j].North;
				}
				if(  heuristique1[i][j].East==Integer.MAX_VALUE || heuristique2[i][j].East==Integer.MAX_VALUE){
					resultat[i][j].East =Integer.MAX_VALUE;
				} else {
					resultat[i][j].East = heuristique1[i][j].East + heuristique2[i][j].East;
				}				if(  heuristique1[i][j].South==Integer.MAX_VALUE || heuristique2[i][j].South==Integer.MAX_VALUE){
					resultat[i][j].South =Integer.MAX_VALUE;
				} else {
					resultat[i][j].South = heuristique1[i][j].South + heuristique2[i][j].South;
				}
			}
		}
		return true;
	}
	@Override 
	public String toString(){

		String resultat = "Width="+this.width+" Height="+this.height+"\nMy terminus:";

		for (int i=0; i<this.myTerminus.size(); i++){
			resultat += " " + this.myTerminus.get(i);
		}
		resultat += "\nMy stops:";
		for (int i=0; i<this.myStops.size(); i++){
			resultat += " " + this.myStops.get(i);
		}
		resultat += "\nMy heuristic:\n= \tE|N|S|W";
		printMatrice(this.heuristic);
		resultat += this.separateur(this.width,true);
		return resultat;
	}

	private String separateur(int size, boolean writeIndex){
		String result="\n";
		for (int i=0; i<size; i++){
			if(writeIndex){
				result += "\t\t+["+i+"]---------+";

			}else{
				result += "\t\t+------------+";
			}
		}
		result += "+\n";
		return result;
	}


	public void printMatrice(heuristicNode[][] matrice){
		String resultat = "\tW|N|S|E";
		String blank = "|";
		for (int j=0; j<this.height ; j++){
			resultat += this.separateur(this.width,false);
			for (int i=0; i<this.width; i++){
				if(isStop(new Point(i,j))){ blank="+";
				}else if(isTerminus(new Point(i,j))){blank="*";}
				if(i==0){resultat += "["+j+"]\t\t+ |";}
				else{
					resultat += "+\t\t+|";
				}

				if(!(matrice[i][j].West==Integer.MAX_VALUE)){resultat += matrice[i][j].West+blank;}
				else{resultat += "_"+blank;}
				if(!(matrice[i][j].North==Integer.MAX_VALUE)){resultat += matrice[i][j].North+blank;}
				else{resultat += "_"+blank;}
				if(!(matrice[i][j].South==Integer.MAX_VALUE)){resultat += matrice[i][j].South+blank;}
				else{resultat += "_"+blank;}
				if(!(matrice[i][j].East==Integer.MAX_VALUE)){resultat += matrice[i][j].East+blank;}
				else{resultat += "_"+blank;}
				blank = "|";
			}
			resultat += "+ ";
		}
		resultat += this.separateur(this.width,true);

		System.out.println(resultat);
	}

	public boolean checkIfStopDone(Point position){

		for (int i=0; i<myStops.size(); i++){
			if (this.myStopsAreSetted()[i] && position.equals(this.myStops.get(i))){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Tile> findSuitableTiles(ArrayList<Point> listOfPoints, ArrayList<Tile> theTiles, Point currentPosition, Direction comeFrom, Direction currentBestDirection){
		ArrayList<Tile> existingTile= Tile.getDeckTileList();
		Point targetedPosition = currentPosition;
		Tile currentTile = null;
		ArrayList<Tile> setOfPossibleTiles = new ArrayList<Tile>(10) ;
		boolean thisTileIsOkWithTheCurrentData;
		boolean thisTileIsOkWithMyPotentialPath;
		for(int i=0; i<existingTile.size(); i++){
			for(int direction=0; direction<4; direction++){
				currentTile = existingTile.get(i);
				for(int k=0; k<direction; k++){
					currentTile.turnLeft();
				}
				thisTileIsOkWithTheCurrentData = this.currentData.isAcceptableTilePlacement(targetedPosition.x, targetedPosition.y, currentTile);
				thisTileIsOkWithMyPotentialPath = currentTile.isPath(comeFrom, currentBestDirection);
				if( thisTileIsOkWithTheCurrentData && thisTileIsOkWithMyPotentialPath ){
					listOfPoints.add(currentPosition);
					theTiles.add(currentTile.getClone());
					setOfPossibleTiles.add(currentTile.getClone());
				}
			}
		}		
		return setOfPossibleTiles;
	}

	public class SituationInAStar implements Comparable<SituationInAStar>{

		@Override
		public int compareTo( SituationInAStar s2)
		{
			if(this.weightOfThePath<s2.weightOfThePath){return -1;}
			if(this.weightOfThePath==s2.weightOfThePath){return 0;}
			return +1;
		}	

		Point position;
		Direction comeFrom;
		Direction goingTo;
		int weightOfThePath;
		ArrayList<Point> SituationStops = new ArrayList<Point>();
		ArrayList<Point> SituationPath = new ArrayList<Point>();
		ArrayList<Tile> SituationTiles = new ArrayList<Tile>();

		public  void copy(SituationInAStar src){
			this.position = new Point(src.position);
			this.comeFrom = src.comeFrom;
			this.goingTo = src.goingTo;
			this.weightOfThePath = src.weightOfThePath;
			for (int i=0; i<src.SituationPath.size();i++){
				this.SituationPath.add(new Point(src.SituationPath.get(i)));
				if(src.SituationTiles.get(i)==null){this.SituationTiles.add(null);}
				else{this.SituationTiles.add(src.SituationTiles.get(i).getClone());}
			}

			for( int i=0; i< src.SituationStops.size(); i++){
				this.SituationStops.add(new Point(src.SituationStops.get(i)));
			}
		}

		public SituationInAStar clone(){
			SituationInAStar leClone = new SituationInAStar();
			leClone.copy(this);
			return leClone;
		}

	}

	public void incrementWithDirectionValue(SituationInAStar currentConfiguration){
		if(currentConfiguration.goingTo==Direction.EAST){
			currentConfiguration.weightOfThePath += get(heuristic,currentConfiguration.position).East;
		}else if (currentConfiguration.goingTo==Direction.WEST){
			currentConfiguration.weightOfThePath += get(heuristic,currentConfiguration.position).West;
		}else if (currentConfiguration.goingTo==Direction.NORTH){
			currentConfiguration.weightOfThePath += get(heuristic,currentConfiguration.position).North;
		}else if (currentConfiguration.goingTo==Direction.SOUTH){
			currentConfiguration.weightOfThePath += get(heuristic,currentConfiguration.position).South;
		}
	}

	public void addPositionAndTileToThePotentialBestPath(SituationInAStar currentConfiguration){
		if(this.currentData.getTile(currentConfiguration.position).isPath(currentConfiguration.comeFrom, currentConfiguration.goingTo)){
			currentConfiguration.SituationPath.add(new Point(currentConfiguration.position));
			currentConfiguration.SituationTiles.add(null);
		}else{
			 ArrayList<Tile> setOfPossibleTiles = findSuitableTiles(currentConfiguration.SituationPath, currentConfiguration.SituationTiles, currentConfiguration.position, currentConfiguration.comeFrom, currentConfiguration.goingTo);
			 
		}
	}

	

	@SuppressWarnings("unused")
	public boolean IveGotTheTiles(Tile tile1, Tile tile2){
		String currentName = currentData.getPlayerTurn();

		if(false){
			System.out.println("PoseTile ="+tile1);
			System.out.println("PoseTile="+tile2);
			for (int i = 0; i< currentData.getHandSize(currentName); i++){
				System.out.println("HandTile "+i+"="+currentData.getHandTile(currentName, i));
			}
		}
		for (int i = 0; i< currentData.getHandSize(currentName); i++){
			for (int j=i+1 ; j<currentData.getHandSize(currentName); j++ ){

				if((currentData.getHandTile(currentName, i).equals(tile1) && currentData.getHandTile(currentName, j).equals(tile2)) 
						|| (currentData.getHandTile(currentName, i).equals(tile2) && currentData.getHandTile(currentName, j).equals(tile1))){
					return true;
				}
			}
		}
		return false;
	}

	
	public void addNeighboorsToQueue(SituationInAStar currentConfiguration, PriorityQueue<SituationInAStar> PriorityQueue){
		SituationInAStar bufferAStarConfig = new SituationInAStar();
		if(get(heuristic, currentConfiguration.position).East!=Integer.MAX_VALUE){
			bufferAStarConfig = currentConfiguration.clone();
			bufferAStarConfig.goingTo=Direction.EAST;
			incrementWithDirectionValue(bufferAStarConfig);
			PriorityQueue.add(bufferAStarConfig);
		}
		if(get(heuristic, currentConfiguration.position).West!=Integer.MAX_VALUE){
			bufferAStarConfig = currentConfiguration.clone();
			bufferAStarConfig.goingTo=Direction.WEST;
			incrementWithDirectionValue(bufferAStarConfig);
			PriorityQueue.add(bufferAStarConfig);
		}
		if(get(heuristic, currentConfiguration.position).North!=Integer.MAX_VALUE){
			bufferAStarConfig = currentConfiguration.clone();
			bufferAStarConfig.goingTo=Direction.NORTH;
			incrementWithDirectionValue(bufferAStarConfig);
			PriorityQueue.add(bufferAStarConfig);
		}
		if(get(heuristic, currentConfiguration.position).South!=Integer.MAX_VALUE){
			bufferAStarConfig = currentConfiguration.clone();
			bufferAStarConfig.goingTo=Direction.SOUTH;
			incrementWithDirectionValue(bufferAStarConfig);
			PriorityQueue.add(bufferAStarConfig);
		}
	}

	public void traiterNoeud(SituationInAStar currentConfiguration, PriorityQueue<SituationInAStar> priorityQueue){

		addPositionAndTileToThePotentialBestPath(currentConfiguration);
		currentConfiguration.position = Direction.getPointInDirection(currentConfiguration.position, currentConfiguration.goingTo);
		currentConfiguration.comeFrom = currentConfiguration.goingTo.turnHalf();
		addNeighboorsToQueue(currentConfiguration,priorityQueue);


	}

	public void initAStar(Point origin, PriorityQueue<SituationInAStar> priorityQueue){
		SituationInAStar currentSituationInAStar = new SituationInAStar();
		SituationInAStar bufSituationInAStar = new SituationInAStar();

		currentSituationInAStar.position = new Point(origin);
		currentSituationInAStar.comeFrom = this.currentData.getTile(currentSituationInAStar.position).getPathTab()[0].end1;
		currentSituationInAStar.goingTo = this.currentData.getTile(currentSituationInAStar.position).getPathTab()[0].end0;
		currentSituationInAStar.weightOfThePath=0;
		incrementWithDirectionValue(currentSituationInAStar);
		bufSituationInAStar = currentSituationInAStar.clone();
		priorityQueue.add(bufSituationInAStar);

		currentSituationInAStar.comeFrom = this.currentData.getTile(currentSituationInAStar.position).getPathTab()[0].end0;
		currentSituationInAStar.goingTo = this.currentData.getTile(currentSituationInAStar.position).getPathTab()[0].end1;
		currentSituationInAStar.weightOfThePath=0;
		incrementWithDirectionValue(currentSituationInAStar);
		bufSituationInAStar = currentSituationInAStar.clone();
		priorityQueue.add(bufSituationInAStar);
	}

	public int makeBestPath(Point terminus1, Point terminus2){
		ArrayList<Point> target = new ArrayList<Point>(1);
		target.add(terminus2);
		boolean trace = true;
		PriorityQueue<SituationInAStar> maFileAPriorite = new PriorityQueue<SituationInAStar>();
		this.computeHeuristic(target,new ArrayList<Point>());
		System.out.println("\tHeuristique courant:\n\t===================================================================");
		printMatrice(heuristic);
		System.out.println("\n\t===================================================================");

		SituationInAStar currentSituationInAStar = new SituationInAStar();

		if(trace)System.out.println("\n\tJe commence la construction.\n");
		if(trace)System.out.println("Je pars de:"+terminus1);
		if(trace)System.out.println("Je vais a:"+terminus2);

		initAStar(terminus1, maFileAPriorite);
		currentSituationInAStar = maFileAPriorite.poll();
		while (!currentSituationInAStar.position.equals(terminus2)) {
			if (trace)
				System.out.println("Traite noeud:\n\tPosition="
						+ currentSituationInAStar.position + "\n\t Cible="
						+ terminus2 +"\n\tPoids=" +
						+ currentSituationInAStar.weightOfThePath
						+ "\n\tLongueur="
						+ currentSituationInAStar.SituationPath.size()
						+ "\n\tTaille de la file=" + maFileAPriorite.size()
						+ "\n\t=============");
			traiterNoeud(currentSituationInAStar, maFileAPriorite);
			currentSituationInAStar = maFileAPriorite.poll();
		}
		//public int makeBestPath(Point terminus1, Point terminus2, ArrayList<Point> stops, ArrayList<Point> thePath, ArrayList<Tile> theTiles){

		if(trace)System.out.println("TROUVE NOEUD:\n\tPosition="+currentSituationInAStar.position+"\n\tPoids="+currentSituationInAStar.weightOfThePath+"\n\tLongueur="+currentSituationInAStar.SituationPath.size()+"\n\t=============");

		theBestPath = (ArrayList<Point>) currentSituationInAStar.SituationPath.clone();
		theBestTiles = (ArrayList<Tile>) currentSituationInAStar.SituationTiles.clone();
		return currentSituationInAStar.weightOfThePath;
	}



	@Override
	public Action makeChoice(Data currentConfig) {
		boolean trace = true;

		this.currentData = currentConfig;
		this.computeHeuristic();
		Point[] extremityTerminus = this.getBestTerminus();
		Random monAlea = new Random();

		//TODO remettre int weightOfBestPath = this.makeBestPath(extremityTerminus[0], extremityTerminus[1]);
		int weightOfBestPath = this.makeBestPath(new Point(1,3), new Point(12,7));
		if(trace){
			System.out.println(extremityTerminus[0]);
			System.out.println(extremityTerminus[1]);
			System.out.println(weightOfBestPath);
			System.out.println(theBestPath.size());
			System.out.println(theBestTiles.size());
		}



		Action[] myPossiblesGoodActions = new Action[theBestPath.size()*theBestPath.size()];
		int nombreGoodActionsPossibles = 0;

		for(int i=0; i<theBestPath.size() ; i++ ){
			for(int j=i+1; j<(theBestPath.size()) ; j++ ){
				if(!(theBestTiles.get(i) == null || theBestTiles.get(j) == null || theBestPath.get(i).equals(theBestPath.get(j)) || !IveGotTheTiles(theBestTiles.get(i),theBestTiles.get(j)))){
					myPossiblesGoodActions[nombreGoodActionsPossibles] = Action.newBuildTwoSimpleAction(theBestPath.get(i), theBestTiles.get(i), theBestPath.get(j),theBestTiles.get(j));
					nombreGoodActionsPossibles++;
				}
			}		
		}


		return myPossiblesGoodActions[monAlea.nextInt(nombreGoodActionsPossibles)];

	}


}