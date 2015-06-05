package main.java.automaton;

import main.java.data.Action;
import main.java.data.Data;
import main.java.util.TraceDebugAutomate;

/**
 * 
 * @author Ulysse
 *	
 *	Cet automate est destiné à être le niveau d'adversaire automatique le plus difficile.
 *	L'ensemble des algorithme n'étant pour l'instant pas implémenté, il joue pour l'instant aléatoirement.
 *
 */
public class Strongest extends PlayerAutomaton {
	

	public Strongest(String playerName) {
		super();
		if(playerName == null) this.name = "Strongest";
		else this.name = playerName;
	}
	
	public Action makeChoice(Data currentconfig){
		DecisionTable myDecisionTable = null;
		
		//=======================================
		TraceDebugAutomate.debugDecisionTableTrace("Strongest Start makeChoice \n");
		

			
			myDecisionTable = new DecisionTable(Data.maxPossibleAction, Data.maxPossibleAction, currentconfig.getPlayerTurn()); //TODO Faire cette allocation dans le constructeur et ce contenter d'un reset ici.
			//=======================================
			TraceDebugAutomate.debugDecisionTableTrace("Table allocated\n");

		myDecisionTable.applyMinMax(0, 1, currentconfig);
		return myDecisionTable.getDecisionNode(0).getCoupleActionIndex(myDecisionTable.getBestActionIndex(0)).getAction();

	}

}
