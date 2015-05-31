package main.java.automaton.test;

import java.awt.Point;

import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionNode.CoupleActionIndex;
import main.java.automaton.DecisionTable;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.data.Action;
import main.java.data.Tile;
import main.java.util.TraceDebugAutomate;

public class testDecisionTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecisionTable maTableDeDecision = null;
		TraceDebugAutomate.decisionTableTrace=false;
		DecisionNode monNoeudDeDecision = null;

		
		System.out.println("toc");
		try {
			maTableDeDecision= new DecisionTable(50, 20);
		} catch (ExceptionUnknownNodeType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("toc");
		try {
			maTableDeDecision.setDecisionNode(0, new DecisionNode(20, 0, "root"));
			maTableDeDecision.setSize(1);

		} catch (ExceptionUnknownNodeType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("toc");
		System.out.println(maTableDeDecision.toString());
		System.out.println("toc");
		
		
		
		try {
			monNoeudDeDecision = new DecisionNode(20, 0, "root");
		} catch (ExceptionUnknownNodeType e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		Tile maTile = Tile.parseTile("Tile_FFFFZZ060123");
		Action monAction = Action.newBuildSimpleAction(new Point(5,4), maTile);

		//Teste creation d'un couple action index
		// TODO: etudier si creation d'n nouveau est necessaire ou possiblilité de juste modifier celui deja alloué
		CoupleActionIndex monCoupleActionIndex1 = monNoeudDeDecision.new CoupleActionIndex(monAction, 1);
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);
		monNoeudDeDecision.setQuality(100.0);
		
		//System.out.println("=====================\n"+monNoeudDeDecision.toString()+"\n=====================\n");
		
		maTableDeDecision.setDecisionNode(0, monNoeudDeDecision);
		
		monNoeudDeDecision.setDepth(1);
		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex1);
		maTableDeDecision.setDecisionNode(1, monNoeudDeDecision);
		maTableDeDecision.setSize(2);
		
		monNoeudDeDecision.setDepth(2);
		monCoupleActionIndex1.setIndex(6);
		monNoeudDeDecision.setCoupleActionIndex(2, monCoupleActionIndex1);
		maTableDeDecision.setDecisionNode(2, monNoeudDeDecision);
		maTableDeDecision.setSize(3);		
		
		System.out.println(maTableDeDecision.toString());
		System.out.println("toc");

		
		monCoupleActionIndex1.setIndex(10);
		monNoeudDeDecision.setCoupleActionIndex(2, monCoupleActionIndex1);

		System.out.println(maTableDeDecision.toString());
		System.out.println("toc");
		
	}

}
