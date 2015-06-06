package main.java.automaton;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;

/**
 * Premiere implementation d'un automate que cherche a poser des rails pour creer le chemin le plus courts de son terminus vers ses points de passage puis vers autre terminus
 * v1 : il crée un chemin vers entre une origine et un destiantion; fonctionne pour l'ia seule sur un terrain vierge
 * @author ulysse
 *
 */
public class AutomatePlusCourtChemin extends PlayerAutomaton {
	private int MAX_LENGTH_OF_PATH;

	Data currentData;

	int heuristic[][];
	int width;
	int height;
	Point[] myTerminus;
	public Point[] myStops;

	Point[] bestPathPoint;
	Tile[] bestPathTile;
	int bestPathLength;

	private int[][][] bufferHeuristic;

	/*=============================================================================
	 * CONSTRUCTOR
	 *=============================================================================*/

	/**
	 * Instanciation d'un automate pour calculer le plus court chemin possible (imaginable).
	 * @param width Largeur du terrain.
	 * @param height Hauteur du terrain.
	 * @param numberOfStop Nombre de stop.
	 */
	public AutomatePlusCourtChemin(Data currentConfiguration, Point[] terminus, Point[] stops){

		this.currentData = currentConfiguration;
		this.width = currentConfiguration.getWidth();
		this.height = currentConfiguration.getHeight();
		this.heuristic = new int[this.width][this.height];
		this.myTerminus = terminus.clone();
		this.myStops = stops.clone();

		this.MAX_LENGTH_OF_PATH = this.width * this.height;
		this.bestPathPoint = new Point[MAX_LENGTH_OF_PATH];
		this.bestPathTile = new Tile[MAX_LENGTH_OF_PATH];
		this.bestPathLength = 0;
		this.bufferHeuristic = new int[stops.length+terminus.length][this.width][this.height];
	}

	/*=============================================================================
	 * SETTER
	 *=============================================================================*/
	/**
	 * Calcule l'heuristique de distance de Manhatan.
	 */
	public void computeHeuristic(){
		for( int i=0; i< this.myTerminus.length; i++){
			this.computeThisHeuristic(this.myTerminus[i], this.bufferHeuristic[i]);
		}
		for( int i=0; i< this.myStops.length; i++){
			this.computeThisHeuristic(this.myStops[i], this.bufferHeuristic[i+this.myTerminus.length]);
		}		

		for(int i=0; i<this.myTerminus.length+this.myStops.length; i++){
			printMatrice(this.bufferHeuristic[i]);
		}
		for(int i=0; i<this.width; i++){
			for(int j=0; j<this.height;j++){
				this.heuristic[i][j] = 0;
				for (int k=0; k<this.bufferHeuristic.length ; k++){
					this.heuristic[i][j] += bufferHeuristic[k][i][j];
				}
				this.heuristic[i][j] = this.heuristic[i][j];
			}
		}
	}

	/**
	 * Calcule l'heuristique pour un point donné. (Pour l'instant distance de manhatan, TODO : pondérer avec tuiles existantes.)
	 * @param cible Le point visé.
	 * @param heuristique La matrice à remplir.
	 */
	private void computeThisHeuristic(Point cible, int[][] heuristique){
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				heuristique[i][j]=Math.abs(i-cible.x)+Math.abs(j-cible.y);
			}
		}
	}

	/*=============================================================================
	 * GETTER
	 *=============================================================================*/

	private boolean isStop(Point p){
		for (int i=0; i<this.myStops.length; i++){
			if (myStops[i].equals(p)){
				return true;
			}
		}
		return false;
	}
	private boolean isTerminus(Point p){
		for (int i=0; i<this.myTerminus.length; i++){
			if (myTerminus[i].equals(p)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Un des terminus de d'heurisitique minimal.
	 */
	public Point getBestTerminus(){
		int bestValue=this.heuristic[this.myTerminus[0].x][this.myTerminus[0].y];
		int bestIndex=0;
		for (int i=1; i<this.myTerminus.length; i++){
			if(this.heuristic[this.myTerminus[i].x][this.myTerminus[i].y]<bestValue){
				bestValue =  this.heuristic[this.myTerminus[i].x][this.myTerminus[i].y];
				bestIndex = i;
			}

		}
		return this.myTerminus[bestIndex];
	}

	public boolean[] myStopsAreSetted(){
		boolean[] result = new boolean[this.myStops.length];
		Tile[][] currentBoard = this.currentData.getBoard();



		for( int i =0; i < this.myStops.length; i++){
			if(currentBoard[this.myStops[i].x][this.myStops[i].y].isBuilding()){
				if (currentBoard[this.myStops[i].x+1][this.myStops[i].y].isStop()){
					this.myStops[i].x++;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops[i].x][this.myStops[i].y+1].isStop()){
					this.myStops[i].y++;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops[i].x-1][this.myStops[i].y].isStop()){
					this.myStops[i].x--;
					result[i]=true;
					break;
				}
				else if (currentBoard[this.myStops[i].x][this.myStops[i].y-1].isStop()){
					this.myStops[i].y--;
					result[i]=true;
					break;
				}
				else {
					result[i]=false; }
			}
			if(this.currentData.getBoard()[this.myStops[i].x][this.myStops[i].y].isStop()){
				result[i]=true;
			}
		}

		return result;
	}

	/*=============================================================================
	 * UTIL
	 *=============================================================================*/
	@Override 
	public String toString(){

		String blank = " ";
		String resultat = "Width="+this.width+" Height="+this.height+"\nMy terminus:";

		for (int i=0; i<this.myTerminus.length; i++){
			resultat += " " + this.myTerminus[i];
		}
		resultat += "\nMy stops:";
		for (int i=0; i<this.myStops.length; i++){
			resultat += " " + this.myStops[i];
		}
		resultat += "\nMy heuristic:\n";
		for (int j=0; j<this.height ; j++){
			resultat += this.separateur(this.width);
			for (int i=0; i<this.width; i++){
				if(isStop(new Point(i,j))){ blank="+";
				}else if(isTerminus(new Point(i,j))){blank="*";}
				resultat += "+";
				if(this.heuristic[i][j]<100){
					resultat += blank;
				}
				resultat += this.heuristic[i][j]+blank;
				if(this.heuristic[i][j]<10){
					resultat += blank;
				}
				blank = " ";
			}
			resultat += "+";
		}
		resultat += this.separateur(this.width);
		return resultat;
	}

	private String separateur(int size){
		String result="\n";
		for (int i=0; i<size; i++){
			result += "+----";
		}
		result += "+\n";
		return result;
	}

	private void printMatrice(int[][] matrice){
		String resultat = "";
		String blank = " ";
		for (int j=0; j<this.height ; j++){
			resultat += this.separateur(this.width);
			for (int i=0; i<this.width; i++){
				if(isStop(new Point(i,j))){ blank="+";
				}else if(isTerminus(new Point(i,j))){blank="*";}
				resultat += "+";
				if(matrice[i][j]<100){
					resultat += blank;
				}
				resultat += matrice[i][j]+blank;
				if(this.heuristic[i][j]<10){
					resultat += blank;
				}
				blank = " ";
			}
			resultat += "+";
		}
		resultat += this.separateur(this.width);

		System.out.println(resultat);
	}

	@Override
	public Action makeChoice(Data currentConfig) {
		// TODO Auto-generated method stub
		return null;
	}




}
