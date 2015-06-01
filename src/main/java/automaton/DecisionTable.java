package main.java.automaton;

import main.java.util.TraceDebugAutomate;


/**
 * Tableau de noeuds de decision,
 * classe implementant le minimax 
 * @author ulysse
 *
 */
public class DecisionTable {
	private DecisionNode[] NodeTable;
	private boolean[] freeSlots;
	private int size;


	/**
	 * Met la valeur de la taille de la table a size
	 * @param size
	 */
	public void setSize(int size){
		this.size=size;
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


	/**
	 * PRECONDITION: Le noeud doit avoir 1 action possible (retourne -1 sinon)
	 * retourne le numero de l'action possible ayant la configuration correspondante la plus avantageuse
	 * @param index
	 * @return
	 */
	public int getBestActionIndex(int index){
		int indexBestActionInTable=-1;
		int indexBestActionInNode=-1;
		int indexCourantInTable;
		int indexCourantInNode;
		double bestValue=-1;
		double currentValue;
		DecisionNode decisionNode = this.getDecisionNode(index);

		for (indexCourantInNode=0;indexCourantInNode<decisionNode.getSizeOfPossiblesActionsTable();indexCourantInNode++){
			indexCourantInTable = decisionNode.getCoupleActionIndex(indexCourantInNode).getIndex();
			if (indexCourantInTable!=0){
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




		//		DecisionNode decisionNode = this.getDecisionNode(index);
		//		int n=0;
		//		
		//		CoupleActionIndex currentCouple, bestCouple;
		//		int currentValue, bestValue;
		//		decisionNode.getCoupleActionIndex(0).getIndex();
		//		for(int i=0; i<decisionNode.getNumberOfPossiblesActions();i++){
		//			currentCouple = decisionNode.getCoupleActionIndex(i);
		//			currentValue = currentCouple.getIndex();
		//			valueBestAction = this.getDecisionNode(index).getCoupleActionIndex(indexOfBestAction).getIndex();
		//			if(this.getDecisionNode(index).getCoupleActionIndex(indexOfBestAction).getIndex()<this.getDecisionNode(index).getCoupleActionIndex(i)){
		//				indexOfBestAction=i;
		//			}
		//		}
		//		return n;

	/**
	 * PRECONDITION: Le noeud doit avoir 1 action possible (retourne -1 sinon)
	 * retourne le numero de l'action possible ayant la configuration correspondante la plus avantageuse
	 * @param index
	 * @return
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
			indexCourantInTable = decisionNode.getCoupleActionIndex(indexCourantInNode).getIndex();
			if (indexCourantInTable!=0){
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


	/**
	 * Creer une table de decision de taille size
	 * Pour etre sur que l'allocation mémoire est faire en amont, je la rempli de decisionsNodes non représentatifs
	 * TODO demander si c'est vraiment utile.
	 * @param tableSize
	 * nombre d'action possible a partir de la configuration courante
	 * @param maxCardinalActionPossible
	 * @throws ExceptionUnknownNodeType 
	 */
	public DecisionTable(int tableSize, int maxCardinalActionPossible) throws ExceptionUnknownNodeType{
		this.NodeTable = new DecisionNode[tableSize];
		this.freeSlots = new boolean[tableSize];
		for(int i=0;i<tableSize;i++){
			this.initDecisionNode(i, new DecisionNode(maxCardinalActionPossible, 0, "root"));
			this.freeSlots[i]=true;
		}
	}


}
