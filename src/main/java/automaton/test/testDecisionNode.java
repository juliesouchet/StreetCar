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
		try {
			monNoeudDeDecision = new DecisionNode(3, 0, "root");
		} catch (ExceptionUnknownNodeType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(monNoeudDeDecision.toString());
		
		Tile maTile = Tile.parseTile("Tile_FFFFZZ060123");
		Action monAction = Action.newBuildSimpleAction(new Point(5,4), maTile);
		
		CoupleActionIndex monCoupleActionIndex1 = monNoeudDeDecision.new CoupleActionIndex(monAction, 0);
		CoupleActionIndex monCoupleActionIndex2 = monNoeudDeDecision.new CoupleActionIndex(monAction, 1);

		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);
		
		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex2);
		

		System.out.println("(1)"+monNoeudDeDecision.toString());
		
		monCoupleActionIndex1.setIndex(8);
		System.out.println("(2)"+monNoeudDeDecision.toString());
		

		
	}

}
