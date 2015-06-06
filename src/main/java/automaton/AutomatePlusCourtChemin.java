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
	
	int heuristique[][];
	int width;
	int height;
	Point[] myTerminus;
	Point[] myStops;
	
	Point[] bestPathPoint;
	Tile[] bestPathTile;
	int bestPathLength;
	
	/**
	 * Instanciation d'un automate pour calculer le plus court chemin possible (imaginable).
	 * @param width Largeur du terrain.
	 * @param height Hauteur du terrain.
	 * @param numberOfStop Nombre de stop.
	 */
	public AutomatePlusCourtChemin(Data currentConfiguration, Point[] terminus, Point[] stops){
		this.width = currentConfiguration.getWidth();
		this.height = currentConfiguration.getHeight();
		this.heuristique = new int[this.width][this.height];
		this.myTerminus = terminus.clone();
		this.myStops = stops.clone();
		
		this.MAX_LENGTH_OF_PATH = this.width * this.height;
		this.bestPathPoint = new Point[MAX_LENGTH_OF_PATH];
		this.bestPathTile = new Tile[MAX_LENGTH_OF_PATH];
		this.bestPathLength = 0;
		
		
		
		
	}
	/**
	 * Calcule l'heuristique pour un point donné. (Pour l'instant distance de manhatan, TODO : pondérer avec tuiles existantes.)
	 * @param cible Le point visé.
	 * @param heuristique La matrice à remplir.
	 */
	/*private*/ void computeThisHeuristique(Point cible, int[][] heuristique){
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				heuristique[i][j]=Math.abs(i-cible.x)+Math.abs(j-cible.y);
			}
		}
	}
	
	/**
	 * Calcule l'heuristique de distance de Manhatan.
	 */
	public void computeHeuristique(int[][][] heuristiquesToCompose){
		for(int i=0; i<this.width; i++){
			for(int j=0; j<this.height;j++){
				this.heuristique[i][j] = 0;
				for (int k=0; k<heuristiquesToCompose.length ; k++){
					this.heuristique[i][j] += heuristiquesToCompose[k][i][j];
				}
				this.heuristique[i][j] = this.heuristique[i][j]/heuristiquesToCompose.length;
			}
		}
	}
	
	
	@Override 
	public String toString(){
		String resultat = "Width="+this.width+" Height="+this.height+" Terminus="+this.myTerminus.toString()+" Stops="+this.myStops.toString();
		resultat += this.heuristique.toString();
		return resultat;
	}
	
	@Override
	public Action makeChoice(Data currentConfig) {
		// TODO Auto-generated method stub
		return null;
	}


}
