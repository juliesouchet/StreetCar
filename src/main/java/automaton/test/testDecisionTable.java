package main.java.automaton.test;

import java.awt.Point;

import main.java.automaton.CoupleActionIndex;
import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionTable;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.data.Action;
import main.java.data.Tile;
import main.java.util.TraceDebugAutomate;

public class testDecisionTable {

	/**
	 * @param args
	 * @throws ExceptionUnknownNodeType 
	 */
	public static void main(String[] args) throws ExceptionUnknownNodeType {
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
//			maTableDeDecision.setSize(1);

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
		CoupleActionIndex monCoupleActionIndex1 = new CoupleActionIndex(monAction, 1);
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);
		monNoeudDeDecision.setQuality(90.0);
		
		//System.out.println("=====================\n"+monNoeudDeDecision.toString()+"\n=====================\n");
		
		maTableDeDecision.setDecisionNode(0, monNoeudDeDecision);
		
		monNoeudDeDecision.setQuality(40.0);
		monNoeudDeDecision.setDepth(1);
		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex1);
		maTableDeDecision.setDecisionNode(1, monNoeudDeDecision);
//		maTableDeDecision.setSize(2);
		
		monNoeudDeDecision.setQuality(100.0);
		monNoeudDeDecision.setDepth(2);
		monCoupleActionIndex1.setIndex(6);
		monNoeudDeDecision.setCoupleActionIndex(2, monCoupleActionIndex1);
		maTableDeDecision.setDecisionNode(2, monNoeudDeDecision);
//		maTableDeDecision.setSize(3);		
		
		System.out.println(maTableDeDecision.toString());
		System.out.println("toc");

		
		monCoupleActionIndex1.setIndex(10);
		monNoeudDeDecision.setCoupleActionIndex(2, monCoupleActionIndex1);

		System.out.println(maTableDeDecision.toString());
		System.out.println("toc");
		
		System.out.println(maTableDeDecision.getBestActionIndex(0));
		System.out.println(maTableDeDecision.getWorstActionIndex(0));
		
		System.out.println(maTableDeDecision.getBestActionIndex(2));
		System.out.println(maTableDeDecision.getWorstActionIndex(2));
		
		////////////////////////////////////////////////////
		System.out.println("\n\n\t==============================================================\n\n");
		
		maTableDeDecision = null;
		DecisionNode decisionNode = null;
		int sizeTable=6;
		int nodeTable=4;
		CoupleActionIndex coupleActionIndex = null;
		Action action = null;
		
		
		/*
		 * 	-------------------------------------------------
		 *	| 	0	|	1	|	2	|	3	|	4	|	5	|
		 *	-------------------------------------------------
		 * 	| 90.0	| 90.0	| 20.0	| 90.0	| 30.0	| 20.0	|
		 * 	-------------------------------------------------
		 * 	| 	0	|	1	|	1	|	2	|	2	|	2	|
		 * 	-------------------------------------------------
		 * 	| root	| int	| int	| leaf 	| leaf	| leaf	|	
		 * 	=================================================
		 * 	| <A;1>	| <A;3>	| <A;4> |	...	|	...	|	...	|
		 * 	-------------------------------------------------
		 * 	| <A;2>	| ...	| <A;5> |	...	|	...	|	...	|
		 * 	-------------------------------------------------
		 * 	|	...	| ...	| ...	|	...	|	...	|	...	|
		 * 	-------------------------------------------------
		 * 	|	...	| ...	| ...	|	...	|	...	|	...	|
		 * 	-------------------------------------------------
		 *
		 */
		
		// Je créé la table de decision
		maTableDeDecision = new DecisionTable(sizeTable, nodeTable);

		// Je créé un noeud qui me servira de buffer pour remplir la table
		decisionNode = new DecisionNode(nodeTable, 0, "root");
		
		// Je créé une action qulconque qui me servira pour remplir les noeuds
		action = Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123"));
	
		// Je créé un couple index action pour remplir la liste des actions possibles
		coupleActionIndex = new CoupleActionIndex(action, 1);
		
		//Colonne 0
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		coupleActionIndex.setIndex(2);
		decisionNode.setCoupleActionIndex(1, coupleActionIndex);
		decisionNode.setQuality(90.0);
	
		maTableDeDecision.setDecisionNode(0, decisionNode);
		
		//Colonne 1
		decisionNode = new DecisionNode(nodeTable, 1, "internalNode");
		coupleActionIndex.setIndex(3);
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		decisionNode.setQuality(90.0);
		
		maTableDeDecision.setDecisionNode(1, decisionNode);
		
		
		//Colonne 2
		decisionNode = new DecisionNode(nodeTable, 1, "internalNode");
		coupleActionIndex.setIndex(4);
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		coupleActionIndex.setIndex(5);
		decisionNode.setCoupleActionIndex(1, coupleActionIndex);
		decisionNode.setQuality(20.0);
		maTableDeDecision.setDecisionNode(2, decisionNode);		
		
		//Colonne 3
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(90.0);
		maTableDeDecision.setDecisionNode(3, decisionNode);	

		//Colonne 4
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(30.0);
		maTableDeDecision.setDecisionNode(4, decisionNode);	

		//Colonne 5
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(20.0);
		maTableDeDecision.setDecisionNode(5, decisionNode);
		
		
		
		System.out.println(maTableDeDecision.toString());
	
		
	}

}
