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

	/* 
	 * La qualité de cette configuration 
	 * déterminée par min-max, plus tard alpha beta si on  le temps pour les noeuds internes
	 * déterminée par evaluateChoiceQuality pour les feuilles
	 */
	private double configurationQuality;


	public class CoupleActionIndex{
		// L'action choisie
		private Action action;
		//Nous mène à l'index du tableau de decision
		private Integer index;

		public CoupleActionIndex(Action a, Integer i){
			action=a;
			index=i;
		}
		public void setAction(Action action){
			this.action = action;
		}
		public Action getAction(){
			return this.action;
		}
		public void setIndex(Integer index){
			this.index=index;
		}
		public Integer getIndex(){
			return this.index;
		}
	}

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


	/**
	 * Vrai si le noeud est une feuille
	 */
	public boolean isRoot(){
		return this.isRoot;
	}

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
		this.isRoot=true;
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
		this.isRoot=false;
	}
	/**
	 * met les attributs isRoot et isLeaf a faux
	 */
	public void setInternalNode(){
		this.isRoot=false;
		this.isLeaf=false;
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
	public double getConfigurationQuality(){
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
	 * retourne le nombre de branchement possible a partir de la configuration courante
	 * @return
	 */
	public int getNumberOfPossiblesActions(){
		return this.cardinalPossiblesActions;
	}

	/** 
	 * incremente le nombre d'action possible
	 */
	public void incrementNumberOfPossiblesActions(){
		this.cardinalPossiblesActions++;
	}

	/** 
	 * decremente le nombre d'action possible
	 */
	public void decrementNumberOfPossiblesActions(){
		this.cardinalPossiblesActions--;
	}

	/**
	 * met le nombre d'actions possible pour la configuratin considérée a cardinal
	 * @param cardinal
	 */
	public void setNumberOfPossiblesActions(int cardinal){
		this.cardinalPossiblesActions=cardinal;
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
	 * la position du couple dans la table des actions possibles pour la configuration courante
	 * @param index
	 * le couple action, index du noeud de decision dans la table de decision
	 * @param coupleActionIndex
	 */
	public void setCoupleActionIndex(int index, CoupleActionIndex coupleActionIndex){
		this.possiblesActions[index]=coupleActionIndex;
	}



	// TODO: doit etre fait dans la classe DecisionTable	
	//	/**
	//	 * Retourne l'indice du meilleur choix estimé
	//	 * @return
	//	 */
	//	public int getBestAction(){
	//		int bestActionIndex = 0;
	//		// recherche du max... TODO implémenter recherche plus efficace ? 
	//		// semblerai opportun étant donné la taille de la table de choix > 5000
	//		for(int i=0; i<this.choicesTable.size(); i++){
	//			if( this.choicesTable.get(i).getConfigurationQuality() > this.choicesTable.get(bestActionIndex).getConfigurationQuality()){
	//				bestActionIndex = i;
	//			}
	//		}
	//		return bestActionIndex;
	//	}
	//	
	//	/**
	//	 * Retourne l'indice du pire choix estimé
	//	 * @return
	//	 */
	//	public int getWorstAction(){
	//		int worstActionIndex = 0;
	//		// recherche du min... TODO implémenter recherche plus efficace ? 
	//		// semblerai opportun étant donné la taille de la table de choix > 5000
	//		for(int i=0; i<this.choicesTable.size(); i++){
	//			if( this.choicesTable.get(i).getConfigurationQuality() > this.choicesTable.get(worstActionIndex).getConfigurationQuality()){
	//				worstActionIndex = i;
	//			}
	//		}
	//		return worstActionIndex;
	//	}	

	/**
	 * la profondeur du noeud courant
	 * @return
	 */
	public int getDepth(){
		return this.depth;
	}
	/**
	 * Inscrit la valeur de la profondeur du noeud
	 * @param depth
	 */
	public void setDepth(int depth){
		this.depth=depth;
	}


	/**
	 * nombre d'action possibles	
	 * @param numberOfPossibleActions
	 * profondeur du noeud dans l'arbre
	 * @param depth
	 * type de noeud: peut etre root, internalNode ou leaf
	 * @param type
	 * @throws ExceptionUnknownNodeType
	 */
	public DecisionNode(int numberOfPossibleActions, int depth, String type) throws ExceptionUnknownNodeType{
		this.setNumberOfPossiblesActions(numberOfPossibleActions);
		// On créé le tableau d'actions possible, pour l'instant vide
		this.possiblesActions = new CoupleActionIndex[numberOfPossibleActions];
		// On rempli la table avec des actions (du coup non significatives)
		for(int i=0; i<numberOfPossibleActions;i++){
			this.possiblesActions[i]=this.new CoupleActionIndex(Action.newBuildSimpleAction(new Point(0,0), Tile.parseTile("Tile_FFFFZZ060123")), 0);
		}
		this.setDepth(depth);
		if(type.equals("root")){
			this.setRoot();
			this.unsetLeaf();
		}else if(type.equals("internalNode")){
			this.setInternalNode();
		}else if(type.equals("leaf")){
			this.setLeaf();
		}else{
			throw new ExceptionUnknownNodeType();
		}
	}


	@Override
	public String toString(){
		String affichage;
		affichage = "[Quality="+this.getConfigurationQuality()+" Type=";
		if(this.isLeaf()){
			affichage+="|Leaf|";
		}else if (this.isRoot()){
			affichage+="|Root|";
		}
		if(this.isInternal()){
			affichage+="|(isInternal)|";
		}
		affichage+=" Depth="+this.getDepth();
		affichage+=" Possibles Actions=[";

		for(int i=0; i<this.getNumberOfPossiblesActions();i++){
			TraceDebugAutomate.debugDecisionNodeTrace("tostring i="+i);
			if( this.getCoupleActionIndex(i)!=null){
				affichage+="<"+
						this.getCoupleActionIndex(i).getAction()+
						","+
						this.getCoupleActionIndex(i).getIndex()+
						">";
			}
		}
		affichage+="]]";
		return affichage;
	}


	// TODO le deplacer dans decisionTable	
	//	// TODO le minimax
	//	/**
	//	 * Si type = leaf: fonction d'évaluation 
	//	 * sinon minimax
	//	 * 
	//	 * @param currentConfig
	//	 * 	L'etat du jeu courant
	//	 * @param height
	//	 * La profondeur a construire: 
	//	 * si 0 alors c'est une feuille, on fait appel a la fonction d'evaluation
	//	 */
	//	public DecisionNode(Data currentConfig, int height){
	//		if (height<=0){	//C'est une feuille
	//			//Evaluator.evaluateSituationQuality(currentConfig.get, gamesNumber, config, difficulty)
	//		}
	//		else if (height>0) { // C'est un noeud interne on fait un appel récursif
	//			
	//		}
	//		
	//	}	




}


