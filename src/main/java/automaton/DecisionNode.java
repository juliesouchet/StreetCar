package main.java.automaton;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Tile;
import main.java.util.TraceDebugAutomate;

/**
 * Noeud de decision
 * @author coutaudu
 *
 */
public class DecisionNode {

	
	/* ===============================================================================================================
	 * 			ATTRIBUTS
	 * =============================================================================================================== */

	/* 
	 * La qualité de cette configuration 
	 * déterminée par min-max, plus tard alpha beta si on  le temps pour les noeuds internes
	 * déterminée par evaluateChoiceQuality pour les feuilles
	 */
	private double configurationQuality;

	/* 
	 * L'ensemble des actions possibles et des cases du tableau de decision correspondant
	 */
	private CoupleActionIndex[] possiblesActions;		

	/*
	 * nombre d'actions possibles
	 */
	private int cardinalPossiblesActions;

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


	
	/* ===============================================================================================================
	 * 			GETTER
	 * =============================================================================================================== */

	
	/**
	 * Vrai si le noeud est une feuille
	 */
	public boolean isRoot(){
		return this.isRoot;
	}

	/**
	 * Vrai si le noeud est un noeud interne,
	 * cad pas une feuille ni la racine
	 */
	public boolean isInternal(){
		return !this.isLeaf && !this.isRoot;
	}
	
	/**
	 * Vrai si le noeud est une feuille
	 * (cad dernier etage de l'exploration combinatoire, la suite est evalué par estimation)
	 */
	public boolean isLeaf(){
		return this.isLeaf;
	}

	/**
	 * La qualité estimée de la situation du noeud 
	 * @return
	 */
	public double getQuality(){
		return this.configurationQuality;
	}	

	/**
	 * La table des fils: 
	 * toutes les actions faisables dans la configuration courante, et l'indice ou ca me mene dans le tableau
	 * @return
	 */
	public CoupleActionIndex[] getPossibleFollowingAction(){
		return this.possiblesActions;
	}
	
	/**
	 * retourne le nombre de branchement maximal (pour la structure de donnees) possible a partir de la configuration courante
	 * @return
	 */
	public int getSizeOfPossiblesActionsTable(){
		return this.cardinalPossiblesActions;
	}	
	
	/**
	 * retourne le nombre de branchement possible (coups acceptables) enregistré dans la structure de donnée
	 * @return
	 */
	public int getNumberPossiblesActionsTable(){
		int numberOfSignificantValueInTable=0;
		for(int i=0;i<this.getSizeOfPossiblesActionsTable();i++){
			if(this.getCoupleActionIndex(i).isSignificant()){
				numberOfSignificantValueInTable++;
			}
		}		
		return numberOfSignificantValueInTable;
	}	
	
	/**
	 * numero d'action possible (doit etre inferieur a cardinalPossiblesActions)
	 * @param index
	 * retourne l'action possible et l'index du decision node correspondant
	 * @return
	 */
	public CoupleActionIndex getCoupleActionIndex(int index){
		return possiblesActions[index];
	}
	
	/**
	 * la profondeur du noeud courant
	 * @return
	 */
	public int getDepth(){
		return this.depth;
	}	

	
	/* ===============================================================================================================
	 * 			SETTER
	 * =============================================================================================================== */
		
	/**
	 * met l'attribut isRoot a vrai
	 */
	public void setRoot(){
		this.isRoot=true;
	}
	
	/**
	 * met l'attribut isLeaf a vrai
	 */
	public void setLeaf(){
		this.isLeaf=true;
	}

	/**
	 * met l'attribut isRoot a faux
	 */
	public void unsetRoot(){
		this.isRoot=false;
	}

	/**
	 * met l'attribut isLeaf a faux
	 */
	public void unsetLeaf(){
		this.isLeaf=false;
	}

	/**
	 * met les attributs isRoot et isLeaf a faux
	 */
	public void setInternalNode(){
		this.isRoot=false;
		this.isLeaf=false;
	}
	
	/**
	 * La qualité du noeud est mise a quality (double de 0.0 a 100.0)
	 * @param quality
	 */
	public void setQuality(double quality){
		this.configurationQuality=quality;
	}	

	/**
	 * met le nombre d'actions possible pour la configuration considérée a cardinal
	 * @param cardinal
	 */
	public void setSizeOfPossiblesActionsTable(int cardinal){
		this.cardinalPossiblesActions=cardinal;
	}

	/**
	 * la position du couple dans la table des actions possibles pour la configuration courante
	 * @param index
	 * le couple action, index du noeud de decision dans la table de decision
	 * @param coupleActionIndex
	 */
	public void setCoupleActionIndex(int index, CoupleActionIndex coupleActionIndex){
		this.possiblesActions[index].copy(coupleActionIndex);
	}

	/**
	 * Inscrit la valeur de la profondeur du noeud
	 * @param depth
	 */
	public void setDepth(int depth){
		this.depth=depth;
	}

	/**
	 * copy le noeud de decision src dans l'appelant sans nouvelle allocation memoire
	 * @param src
	 */
	public void copy(DecisionNode src){
		this.cardinalPossiblesActions = src.cardinalPossiblesActions;
		this.configurationQuality = src.configurationQuality ;
		this.depth = src.depth ;
		this.isLeaf = src.isLeaf ;
		this.isRoot = src.isRoot ;
		for(int i=0;i<this.getSizeOfPossiblesActionsTable();i++){
			this.possiblesActions[i].copy(src.possiblesActions[i]);
		}
		
	}

	//TODO: test unitaire
	/**
	 * Remet toutes les actions de la table de couples action possible/index comme non pertinentes
	 */
	public void reset(){
		for(int i=0;i<this.getNumberPossiblesActionsTable();i++){
			this.getCoupleActionIndex(i).setIndex(0);
		}
	}
	
	/* ===============================================================================================================
	 * 			CONSTRUCTEURS
	 * =============================================================================================================== */

	/**
	 * nombre d'action possibles maximal pouvant etre traité par la structure de donnée	
	 * @param numberMaxOfPossibleActions
	 * profondeur du noeud dans l'arbre
	 * @param depth
	 * type de noeud: peut etre root, internalNode ou leaf
	 * @param type
	 * @throws ExceptionUnknownNodeType
	 */
	public DecisionNode(int numberMaxOfPossibleActions, int depth, String type) throws ExceptionUnknownNodeType{
		this.setSizeOfPossiblesActionsTable(numberMaxOfPossibleActions);
		// On créé le tableau d'actions possible, pour l'instant vide
		this.possiblesActions = new CoupleActionIndex[numberMaxOfPossibleActions];
		// On rempli la table avec des actions (du coup non significatives)
		for(int i=0; i<numberMaxOfPossibleActions;i++){
			this.possiblesActions[i]=new CoupleActionIndex(Action.newBuildSimpleAction(new Point(0,0), Tile.parseTile("Tile_FFFFZZ060123")), -1);
		}
		//A la creation on fixe la qualité a -1 tant qu'une valeur significative n'a pas été calculé
		this.setQuality(-1.0);
		this.setDepth(depth);
		if(type.equals("root")){
			this.setRoot();
			this.unsetLeaf();
		} else
		if(type.equals("internalNode")){
			this.setInternalNode();
		} else
		if(type.equals("leaf")){
			this.setLeaf();
		} else
		if(type.equals("root&leaf") || type.equals("leaf&root")){
			this.setRoot();
			this.setLeaf();
		}else{
			throw new ExceptionUnknownNodeType();
		}
	}


	
	/* ===============================================================================================================
	 * 			UTILITAIRES
	 * =============================================================================================================== */
	
	@Override
	public String toString(){
		String affichage = null;
		for(int i=0; i<this.getDepth();i++){
			affichage+="\t";
		}
		affichage = "* Quality="+this.getQuality()+" Type=";
		if(this.isLeaf()){
			affichage+="|Leaf|";
		}
		if (this.isRoot()){
			affichage+="|Root|";
		}
		if(this.isInternal()){
			affichage+="|(isInternal)|";
		}
		affichage+=" Depth="+this.getDepth();
		affichage+=" Possibles Actions:\n";

		for(int i=0; i<this.getSizeOfPossiblesActionsTable();i++){
			TraceDebugAutomate.debugDecisionNodeTrace("tostring i="+i);
			for(int j=0; j<this.getDepth()+1;j++){
				affichage+="\t";
			}
			if( this.getCoupleActionIndex(i)!=null && this.getCoupleActionIndex(i).getIndex()!=0){
				affichage+="<"+this.getCoupleActionIndex(i).getAction()+","+this.getCoupleActionIndex(i).getIndex()+">\n";
			}
		}
		return affichage;
	}

	@Override
	public boolean equals (Object o){
		DecisionNode otherNode = null;
		if(o==null){
			return false;
		}
		else {
			otherNode=(DecisionNode) o;
			if (this.getSizeOfPossiblesActionsTable()!=otherNode.getSizeOfPossiblesActionsTable()){
				return false;
			}
			if (this.getQuality()!=otherNode.getQuality()){
				return false;
			}
			if (this.getDepth()!=otherNode.getDepth()){
				return false;
			}
			if (this.isLeaf()!=otherNode.isLeaf()){
				return false;
			}
			if (this.isRoot()!=otherNode.isRoot()){
				return false;
			}
			for (int i=0; i<this.getSizeOfPossiblesActionsTable();i++){
				if ( !this.getPossibleFollowingAction()[i].equals(otherNode.getPossibleFollowingAction()[i])){
					return false;
				}
			}
			return true;
		}
	}

	




}


