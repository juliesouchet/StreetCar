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
		this.NodeTable[index]=node;
		this.freeSlots[index]=false;
	}
	
	/**
	 * retourne l'action possible ayant la configuration correspondante la plus avantageuse
	 * @param index
	 * @return
	 */
	public int getBestActionIndex(int index){
		int indexOfBestAction=0;
		for(int i=0; i<this.getDecisionNode(index).getNumberOfPossiblesActions();i++){
			if(this.getDecisionNode(indexOfBestAction).getQuality()<this.getDecisionNode(i).getQuality()){
				indexOfBestAction=i;
			}
		}
		return indexOfBestAction;
	}
	/**
	 * retourne l'action possible ayant la configuration correspondante la plus desantageuse
	 * @param index
	 * @return
	 */
	public int getWorstActionIndex(int index){
		int indexOfWorstAction=0;
		for(int i=0; i<this.getDecisionNode(index).getNumberOfPossiblesActions();i++){
			if(this.getDecisionNode(indexOfWorstAction).getQuality()>this.getDecisionNode(i).getQuality()){
				indexOfWorstAction=i;
			}
		}
		return indexOfWorstAction;
	}	
	
	@Override
	public String toString(){
		String result="";
		TraceDebugAutomate.debugDecisionTableTrace("toString: Begin ");
		for(int i=0;i<this.getSize();i++){
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
			this.setDecisionNode(i, new DecisionNode(maxCardinalActionPossible, 0, "root"));
			this.freeSlots[i]=true;
		}
	}
	
	
}
