package main.java.automaton;

import java.util.ArrayList;

import main.java.data.Action;
import main.java.data.Data;

/**
 * Arbre du minimax
 * @author coutaudu
 *
 */
public class DecisionNode {
	
	/*
	 *  La configuration dans laquelle je suis
	 */
	private Data currentConfiguration; 
	
	/* 
	 * La qualité de cette configuration 
	 * déterminée par min-max, plus tard alpha beta si on  le temps pour les noeuds internes
	 * déterminée par evaluateChoiceQuality pour les feuilles
	 */
	private int configurationQuality;
	
	/* 
	 * L'action qui m'a mené a cette configuration 
	 */
	private Action lastAction;				
	
	/* 
	 * Les enfants: representés par une table de noeuds
	 */
	private ArrayList<DecisionNode> choicesTable;		
	
	/*
	 * La profondeur du noeud courant (vis a vis de l'appel d'origine)  	
	 */
	private int depth;
	
	/*
	 * Vrai si le noeud est une feuille:
	 * cad on est arrivé a la profondeur d'exploration voulu
	 */
	private boolean isLeaf;
	
	/*
	 * Vrai si le noeud est le noeud d'origine de l'appel a l'algo
	 */
	private boolean isRoot;

	
	/**
	 * Vrai si le noeud est une feuille
	 */
	public boolean isRoot(){
		return this.isRoot;
	}
	
	/**
	 * Vrai si le noeud est un noeud interne,
	 * cad pas une feuille
	 */
	public boolean isInternal(){
		return !this.isLeaf;
	}
	
	/**
	 * Vrai si le noeud est une feuille
	 * (cad dernier etage de l'exploration combinatoire, la suite est evalué par estimation)
	 */
	public boolean isLeaf(){
		return this.isLeaf;
	}
	
	/**
	 * Configuration courante du noeud
	 * @return
	 */
	public Data getCurrentConfiguration(){
		return this.currentConfiguration;
	}
	
	/**
	 * La qualité estimée de la situation du noeud 
	 * @return
	 */
	public int getConfigurationQuality(){
		return this.configurationQuality;
	}
	
	/**
	 * L'action qui m'a mené dans cette situation
	 * @return
	 */
	public Action getLeadingAction(){
		return this.lastAction;
	}
	
	/**
	 * La table des fils: 
	 * pour chaque action faisable dans la configuration courante, la configuration a laquelle ça nous mène et la qualité de cette situation
	 * @return
	 */
	public ArrayList<DecisionNode> getPossibleFollowingAction(){
		return this.choicesTable;
	}
	
	/**
	 * Retourne l'indice du meilleur choix estimé
	 * @return
	 */
	public int getBestAction(){
		int bestActionIndex = 0;
		// recherche du max... TODO implémenter recherche plus efficace ? 
		// semblerai opportun étant donné la taille de la table de choix > 5000
		for(int i=0; i<this.choicesTable.size(); i++){
			if( this.choicesTable.get(i).getConfigurationQuality() > this.choicesTable.get(bestActionIndex).getConfigurationQuality()){
				bestActionIndex = i;
			}
		}
		return bestActionIndex;
	}
	
	/**
	 * Retourne l'indice du pire choix estimé
	 * @return
	 */
	public int getWorstAction(){
		int worstActionIndex = 0;
		// recherche du min... TODO implémenter recherche plus efficace ? 
		// semblerai opportun étant donné la taille de la table de choix > 5000
		for(int i=0; i<this.choicesTable.size(); i++){
			if( this.choicesTable.get(i).getConfigurationQuality() > this.choicesTable.get(worstActionIndex).getConfigurationQuality()){
				worstActionIndex = i;
			}
		}
		return worstActionIndex;
	}	
	
	/**
	 * la profondeur du noeud courant
	 * @return
	 */
	public int getDepth(){
		return this.depth;
	}
	
	
	// TODO le minimax
	/**
	 * Si type = leaf: fonction d'évaluation 
	 * sinon minimax
	 * 
	 * @param currentConfig
	 * 	L'etat du jeu courant
	 * @param height
	 * La profondeur a construire: 
	 * si 0 alors c'est une feuille, on fait appel a la fonction d'evaluation
	 */
	public DecisionNode(Data currentConfig, int height){
		if (height<=0){	//C'est une feuille
			
		}
		else if (height>0) { // C'est un noeud interne on fait un appel récursif
			
		}
		
	}	
	
	
	
	
}


