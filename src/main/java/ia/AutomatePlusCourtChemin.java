package main.java.ia;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;

/**
 * Premiere implementation d'un automate que cherche a poser des rails pour creer le chemin le plus courts de son terminus vers ses points de passage puis vers autre terminus
 * v1 : il crée un chemin vers entre une origine et un destiantion; fonctionne pour l'ia seule sur un terrain vierge
 * @author ulysse
 *
 */
public class AutomatePlusCourtChemin extends PlayerAutomaton {
	int heuristique[][];
	int largeur, hauteur;
	
	public AutomatePlusCourtChemin(Data initialtConfig, Point Destination ){
		this.largeur = initialtConfig.getWidth();
		this.hauteur = initialtConfig.getHeight();
		this.heuristique = new int [this.largeur][this.hauteur];
	}

	public void computeHeuristique(Point Destination){
		for (int i = 0; i < this.largeur; i++){
			for (int j =0; j < this.hauteur; j++){
				heuristique[i][j] = Math.abs(Destination.x-i)+Math.abs(Destination.y-j);
			}
		}
	}
	protected void printHeuristique(){
		for (int i = 0; i < this.largeur; i++){
			for (int j =0; j < this.hauteur; j++){
				System.out.print("+-");
			}
			System.out.println("+");
			for (int j =0; j < this.hauteur; j++){
				System.out.println("+"+heuristique[i][j]);
			}
			System.out.println("+");
		}
	}
	
	@Override
	public Action makeChoice(Hand myHand, Data currentConfig) {
		// TODO Auto-generated method stub
		return null;
	}

}
