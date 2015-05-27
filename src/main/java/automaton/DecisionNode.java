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
	Data currentConfiguration; 
	
	/* 
	 * La qualité de cette configuration 
	 * déterminée par min-max, plus tard alpha beta si on  le temps pour les noeuds internes
	 * déterminée par evaluateChoiceQuality pour les feuilles
	 */
	int configurationQuality;
	
	/* 
	 * L'action qui m'a mené a cette configuration 
	 */
	Action lastAction;				
	
	/* 
	 * Les enfants: representés par une table de noeuds
	 */
	ArrayList<DecisionNode> choicesTable;		
	
	/*
	 * La profondeur du noeud courant (vis a vis de l'appel d'origine)  	
	 */
	int depth;
	
	/*
	 * Vrai si le noeud est une feuille:
	 * cad on est arrivé a la profondeur d'exploration voulu
	 */
	boolean isLeaf;
	
	/*
	 * Vrai si le noeud est le noeud d'origine de l'appel a l'algo
	 */
	boolean isRoot;

	
	
	
	
	
}


