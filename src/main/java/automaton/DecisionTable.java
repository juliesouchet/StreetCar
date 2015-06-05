package main.java.automaton;

import main.java.data.Data;
import main.java.util.TraceDebugAutomate;


/**
 * Tableau de noeuds de decision,
 * classe implementant le minimax 
 * @author ulysse
 *
 */
public class DecisionTable {

	public static final int ALLY=0;
	public static final int OPPONENT=1;
	public static final int TABLE_IS_FULL=-1;


	/* ===============================================================================================================
	 * 			ATTRIBUTS
	 * =============================================================================================================== */
	private DecisionNode[] NodeTable;
	private boolean[] freeSlots;
	private int size;
	private String myName;

	/* ===============================================================================================================
	 * 			GETTERS
	 * =============================================================================================================== */

	/**
	 * @return Le nom du player que cet table de décision simule.
	 */
	public String getMyName(){
		return this.myName;
	}
	/**
	 * renvoi la taille de la table
	 * @return
	 */
	public int getSize(){
		return this.size;
	}

	/**
	 * Noeud de decision de la table a index
	 * @param index
	 * @return
	 */
	public DecisionNode getDecisionNode(int index){
		return this.NodeTable[index];
	}	

	/**
	 * PRECONDITION: Le noeud doit avoir 1 action possible (retourne NOT_SIGNIFICANT (-1) sinon)
	 * @param index
	 * Numero de noeud dans le tableau
	 * @return
	 * retourne le numero de l'action possible ayant la configuration correspondante la plus avantageuse
	 */
	public int getBestActionIndex(int index){
		int indexBestActionInTable=DecisionNode.NOT_SIGNIFICANT;
		int indexBestActionInNode=DecisionNode.NOT_SIGNIFICANT;
		int indexCourantInTable;
		int indexCourantInNode;
		double bestValue = -1.0;
		double currentValue;
		DecisionNode decisionNode = this.getDecisionNode(index);

		for (indexCourantInNode=0;indexCourantInNode<decisionNode.getSizeOfPossiblesActionsTable();indexCourantInNode++){
			if (this.getDecisionNode(index).actionIsSignificant(indexCourantInNode)){
				indexCourantInTable = decisionNode.getCoupleActionIndex(indexCourantInNode).getIndex();
				currentValue = this.getDecisionNode(indexCourantInTable).getQuality();
				if( currentValue>=bestValue){
					indexBestActionInNode = indexCourantInNode;
					indexBestActionInTable = decisionNode.getCoupleActionIndex(indexBestActionInNode).getIndex();
					bestValue = this.getDecisionNode(indexBestActionInTable).getQuality();
				}
			}
		}
		return indexBestActionInNode;
	}		

	/**
	 * PRECONDITION: Le noeud doit avoir 1 action possible (retourne -1 sinon)
	 *
	 * @param index
	 * Numero de noeud dans le tableau
	 * @return
	 * Le numero de l'action dans la liste des actions possibles ayant la configuration correspondante la plus desavantageuse
	 */
	public int getWorstActionIndex(int index){
		int indexWorstActionInTable=-1;
		int indexWorstActionInNode=-1;
		int indexCourantInTable;
		int indexCourantInNode;
		double worstValue=+101.0;
		double currentValue;
		DecisionNode decisionNode = this.getDecisionNode(index);

		for (indexCourantInNode=0;indexCourantInNode<decisionNode.getSizeOfPossiblesActionsTable();indexCourantInNode++){
			if (this.getDecisionNode(index).actionIsSignificant(indexCourantInNode)){
				indexCourantInTable = decisionNode.getCoupleActionIndex(indexCourantInNode).getIndex();
				currentValue = this.getDecisionNode(indexCourantInTable).getQuality();
				if( currentValue<=worstValue){
					indexWorstActionInNode = indexCourantInNode;
					indexWorstActionInTable = decisionNode.getCoupleActionIndex(indexWorstActionInNode).getIndex();
					worstValue = this.getDecisionNode(indexWorstActionInTable).getQuality();
				}
			}
		}
		return indexWorstActionInNode;
	}	

	/**
	 * Renvoi le statut de la case du tableau: vrai si la case est disponible pour enregistrer un noeud
	 * @param index
	 * 	L'index de la case du tableau visée
	 * @return
	 */
	public boolean slotIsFree(int index){
		return this.freeSlots[index];
	}	

	/**
	 * Met la valeur de la taille de la table a size
	 * @param size
	 */
	private void setSize(int size){
		this.size=size;
	}

	/**
	 * Methode pour trouver un slot disponible dans la table de decision
	 * @return
	 * L'indice d'un slot contenant une donnée non pertinente sucestible d'être écrasée
	 */
	public int findFreeSlot(){	//TODO mais pas pressé: modifier cette implémentation: dans une pile d'index libre et on pioche dedans: plus rapide et permettra multi thread.
		for (int i=0; i<this.getSize();i++){
			if(this.slotIsFree(i)){
				return i;
			}
		}
		return TABLE_IS_FULL;
	}

	/* ===============================================================================================================
	 * 			SETTERS
	 * =============================================================================================================== */

	/**
	 * met le noeud numero index a node
	 * @param index
	 * @param node
	 */
	public void setDecisionNode(int index, DecisionNode node){
		this.NodeTable[index].copy(node);
		this.freeSlots[index]=false;
	}

	/**
	 * initie (alloue) le noeud numero index a node
	 * @param index
	 * @param node
	 */
	private void initDecisionNode(int index, DecisionNode node){
		this.NodeTable[index]=node;
		this.freeSlots[index]=true;
	}

	/* ===============================================================================================================
	 * 			CONSTRUCTORS
	 * =============================================================================================================== */

	/**
	 * Creer une table de decision de taille size
	 * Pour etre sur que l'allocation mémoire est faite en amont, je la rempli de decisionsNodes non représentatifs
	 * @param tableSize
	 * 	Taille du tableau de DecisionNode: correspond au nombre total d'action que la table peut traiter
	 * @param maxCardinalActionPossible
	 * 	Taille de la table d'action de chaque noeud: le nombre maximal d'action que chaque noeud pourra traiter: dois être un majorant.
	 * @param myName nom du player que cette table de décision simule.
	 * @throws ExceptionUnknownNodeType 
	 */
	public DecisionTable(int tableSize, int maxCardinalActionPossible, String myName) throws ExceptionUnknownNodeType{
		this.myName = new String(myName); 		
		this.NodeTable = new DecisionNode[tableSize];
		this.freeSlots = new boolean[tableSize];
		this.setSize(tableSize);
		for(int i=0;i<this.getSize();i++){
			this.initDecisionNode(i, new DecisionNode(maxCardinalActionPossible, 0, "root"));
			this.freeSlots[i]=true;
		}
	}	



	/* ===============================================================================================================
	 * 			UTILITAIRES
	 * =============================================================================================================== */

	@Override
	public String toString(){
		String result="Table (size="+this.getSize()+"):\n";
		TraceDebugAutomate.debugDecisionTableTrace("toString: Begin ");
		for(int i=0;i<this.getSize();i++){
			result+="["+i+"]";
			TraceDebugAutomate.debugDecisionTableTrace("\n toString: ("+i+")");
			if(!this.freeSlots[i]){
				for(int j=0;j<this.getDecisionNode(i).getDepth();j++){
					result+="\t";
				}
				result+=this.getDecisionNode(i).toString()+"\n";
			}
		}
		return result;
	}


	/*
	 * TODO finir implementation:
	 * demander a julie fonctionnement Evaluator.evaluateSituationQuality (gameNumber? difficulty?)
	 * TODO passer methode en argument ? (a priori non...)
	 */
	/**
	 * Implémentation de l'algorithme minimax: la table de décision est calculée
	 * @param index
	 * 	L'indice du noeud en construction dans la table de decision
	 * @param wantedDepth
	 * 	la profondeur (restante) a explorer) 
	 * @param currentConfiguration
	 * 	le data courant: cad l'etat de la partie
	 */
	public void applyMinMax(int index, int wantedDepth, Data currentConfiguration ){
		double evaluatedQuality = DecisionNode.NOT_SIGNIFICANT;
		String playerName = currentConfiguration.getPlayerTurn();
		int numberOfPossiblesActions;
		int aFreeSlot = 0;

		Data copyDeCOnfigurationCourante = currentConfiguration.getClone(playerName); //TODO a enlever ça sera fait par le rollback

		// Cas d'une feuille on estime la qualité avec la fonction de Julie
		if(wantedDepth==0){
			//===============================================================================//
			TraceDebugAutomate.debugDecisionTableTrace("\t======Feuille["+index+"]======.\n");
			//evaluatedQuality = Evaluator.evaluateSituationQuality(currentConfiguration.getPlayerTurn(), gamesNumber, currentConfiguration, difficulty)
			evaluatedQuality = 50.0;

			this.getDecisionNode(index).setQuality(evaluatedQuality);
			this.getDecisionNode(index).setLeaf();

			// Cas d'une recurrence	
		} else if(wantedDepth>0){
			//===============================================================================//
			TraceDebugAutomate.debugDecisionTableTrace("\t======Noeud["+index+"]======.\n");
			//Dans un premier temps je compte le nombre d'actions possibles
			numberOfPossiblesActions = currentConfiguration.getPossibleActions(playerName, this.getDecisionNode(index).getPossibleFollowingActionTable(),false);
			//J'alloue un tableau en conséquence
			//===============================================================================//
			TraceDebugAutomate.debugDecisionTableTrace("numberOfPossiblesActions="+numberOfPossiblesActions+"\n");

			for (int i=0; i<numberOfPossiblesActions; i++){
				aFreeSlot = this.findFreeSlot();
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("aFreeSlot="+aFreeSlot+"\n");
				assert(this.getDecisionNode(index).getCoupleActionIndex(i).getIndex()==CoupleActionIndex.SIGNIFICANT_BUT_NOT_TREATED_YET);
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("On set ce freeslot comme index de l'action en traitement.\n");
				this.getDecisionNode(index).getCoupleActionIndex(i).setIndex(aFreeSlot);
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("On fait l'action.\n");
				currentConfiguration.doAction(playerName,this.getDecisionNode(index).getCoupleActionIndex(i).getAction());
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("Appel récursif\n");
				this.applyMinMax(aFreeSlot,wantedDepth-1, currentConfiguration);
				
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("On rollback \n");
				currentConfiguration = copyDeCOnfigurationCourante.getClone(myName);
				// TODO			
				//				currentConfiguration.rollBack(); //TODO a enlever ça sera fait par le rollback
			}
			if (playerName.equals(this.getMyName())){
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("C'est un noeud ou JE joue: noeud max\nOn set la qualité du noeud\n");
				this.getDecisionNode(index).setQuality(this.getDecisionNode(this.getBestActionIndex(index)).getQuality());
			}
			else if (!playerName.equals(this.getMyName())){
				//===============================================================================//
				TraceDebugAutomate.debugDecisionTableTrace("C'est un noeud ou l'ADVERSAIRE joue: noeud min\n On set la qualité du noeud\n");
				this.getDecisionNode(index).setQuality(this.getDecisionNode(this.getWorstActionIndex(index)).getQuality());	

			}

		}
	}



}
