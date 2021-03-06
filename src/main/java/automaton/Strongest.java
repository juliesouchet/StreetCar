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

	public Action makeChoice(Data currentConfig){
		DecisionTable myDecisionTable = null;
		int nbActionsPossibles,profondeurExplorable =0;
		Action myAction = null, tempAction1 = null, tempAction2 = null;

		//=====================TRACE===================================
		TraceDebugAutomate.debugDecisionTableTrace(" Strongest Start makeChoice \n");

		nbActionsPossibles = currentConfig.getPossibleActions(currentConfig.getPlayerTurn(), new DecisionNode(1, 0, "root").getPossibleFollowingActionTable(),false);

		if( nbActionsPossibles==0){
			//=====================TRACE===================================
			TraceDebugAutomate.debugDecisionTableTrace("\t/!\\ NbActions possible nul ! \n");
			return null;
		}else{
			//=====================TRACE===================================
			TraceDebugAutomate.debugDecisionTableTrace("\tNbActions possible = "+nbActionsPossibles+" \n");

			profondeurExplorable = 1;//Math.min(150000/nbActionsPossibles, 2); // TODO enlever le min, garder le 150000...

			//=====================TRACE===================================
			TraceDebugAutomate.debugDecisionTableTrace("\tprofondeurExplorable = "+profondeurExplorable+" \n");

			if (profondeurExplorable==0) {
				PlayerAutomaton edouard = new Dumbest(name);
				tempAction1 = edouard.makeChoice(currentConfig);
				tempAction2 = edouard.makeChoice(currentConfig);
				myAction = Action.newBuildDoubleAction(tempAction1.positionTile1, tempAction1.tile1,tempAction2.positionTile1, tempAction2.tile1);
				myAction = edouard.makeChoice(currentConfig);
			}
			else {
				// Init
				myDecisionTable = new DecisionTable(profondeurExplorable+1, nbActionsPossibles, this.name);
				currentConfig.getPossibleActions(name, myDecisionTable.getDecisionNode(0).getPossibleFollowingActionTable(), true);
				myDecisionTable.reserveSlot(0);
				// Calcul
				myDecisionTable.applyMinMax(0, profondeurExplorable, currentConfig, nbActionsPossibles);
				// Resultat
				myDecisionTable.freeSlot(0);
				int bestIndex = myDecisionTable.getBestActionIndex(0);
				if(bestIndex == DecisionNode.NOT_SIGNIFICANT) throw new RuntimeException(name + " has no possible action (Strongest)");
				myAction = myDecisionTable.getDecisionNode(0).getCoupleActionIndex(bestIndex).getAction();
			}


			return myAction;
		}




		//			myDecisionTable = new DecisionTable(Data.maxPossibleAction, Data.maxPossibleAction, currentconfig.getPlayerTurn()); //TODO Faire cette allocation dans le constructeur et ce contenter d'un reset ici.
		//			//=======================================
		//			TraceDebugAutomate.debugDecisionTableTrace("Table allocated\n");
		//
		//		myDecisionTable.applyMinMax(0, 1, currentconfig);
		//		//return myDecisionTable.getDecisionNode(0).getCoupleActionIndex(myDecisionTable.getBestActionIndex(0)).getAction();

	}

}
