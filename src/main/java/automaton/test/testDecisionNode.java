package main.java.automaton.test;

import java.awt.Point;

import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionNode.CoupleActionIndex;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.data.Action;
import main.java.data.Tile;
import main.java.util.TraceDebugAutomate;

public class testDecisionNode {

	/**
	 * @param args
	 */


	public static void main(String[] args) {
		TraceDebugAutomate.decisionNodeTrace=true;
		DecisionNode monNoeudDeDecision = null;
		int taille_de_case = 5000;
		int nb_de_case = 30000000;
		long sommeTps = 0;
		/*
		 * Notes:
		 * decisionNode Trace:moyenne pour 1case de 5000actions= 7.09E-7s 
		 * decisionNode Trace:topdecisionNode Trace:Temps pour 30000000 new decisionNode de taille 5000 = 21.470737712000002
		 */
		
		TraceDebugAutomate.debugDecisionNodeTrace("top");
		long tmps3 = System.nanoTime();
		for(int i=0;i<5000;i++){
			try {
				long tmps1 = System.nanoTime();
				monNoeudDeDecision = new DecisionNode(taille_de_case, 0, "root");
				long tmps2 = System.nanoTime();
				//TraceDebugAutomate.debugDecisionNodeTrace("Temps pour new decisionNode de taille "+taille_de_case+" = "+ (tmps2-tmps1)*Math.pow(10.0, -9)+"s \n");
				sommeTps+=(tmps2-tmps1);
			} catch (ExceptionUnknownNodeType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long tmps4 = System.nanoTime();
		TraceDebugAutomate.debugDecisionNodeTrace("Temps pour "+nb_de_case+" new decisionNode de taille "+taille_de_case+" = "+ (tmps4-tmps3)*Math.pow(10.0, -9)+"s \n");
		TraceDebugAutomate.debugDecisionNodeTrace("moyenne pour 1case de "+taille_de_case+"actions= "+ (sommeTps/30000000)*Math.pow(10.0, -9)+"s \n");
		//System.out.println(monNoeudDeDecision.toString());

		Tile maTile = Tile.parseTile("Tile_FFFFZZ060123");
		Action monAction = Action.newBuildSimpleAction(new Point(5,4), maTile);

		CoupleActionIndex monCoupleActionIndex1 = monNoeudDeDecision.new CoupleActionIndex(monAction, 0);
		CoupleActionIndex monCoupleActionIndex2 = monNoeudDeDecision.new CoupleActionIndex(monAction, 1);

		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);

		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex2);


		//System.out.println("(1)"+monNoeudDeDecision.toString());

		monCoupleActionIndex1.setIndex(8);
		//System.out.println("(2)"+monNoeudDeDecision.toString());



	}

}
