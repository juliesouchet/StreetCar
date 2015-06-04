package main.java.automaton.test;

import java.awt.Point;

import main.java.automaton.CoupleActionIndex;
import main.java.automaton.DecisionNode;
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
		int taille_de_case = 6000;
		int nb_de_case = 6000;
		long sommeTps = 0;
		boolean[] freeSlots = new boolean[nb_de_case];
		DecisionNode[] mesNoeudsDeDecision = new DecisionNode[nb_de_case];
		/*
		 * Notes:
		 * decisionNode Trace:moyenne pour 1case de 5000actions= 7.09E-7s 
		 * decisionNode Trace:topdecisionNode Trace:Temps pour 30000000 new decisionNode de taille 5000 = beaucoup trop long
		 */
		
		//On teste l'allocation de la structure et surtout l'ordre de temps requis
		TraceDebugAutomate.debugDecisionNodeTrace("top");
		long tmps3 = System.nanoTime();
		for(int i=0;i<nb_de_case;i++){
			try {
				long tmps1 = System.nanoTime();
				monNoeudDeDecision = new DecisionNode(taille_de_case, 0, "leaf&root");
				TraceDebugAutomate.debugDecisionNodeTrace("toc1");
				mesNoeudsDeDecision[i]=monNoeudDeDecision;
				TraceDebugAutomate.debugDecisionNodeTrace("toc["+i+"]\n");

				freeSlots[i]=true;
				long tmps2 = System.nanoTime();
				//TraceDebugAutomate.debugDecisionNodeTrace("Temps pour new decisionNode de taille "+taille_de_case+" = "+ (tmps2-tmps1)*Math.pow(10.0, -9)+"s \n");
				sommeTps+=(tmps2-tmps1);
			} catch (ExceptionUnknownNodeType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long tmps4 = System.nanoTime();

		
		TraceDebugAutomate.debugDecisionNodeTrace("Temps pour "+nb_de_case+" new decision	Node de taille "+taille_de_case+" = "+ (tmps4-tmps3)*Math.pow(10.0, -9)+"s \n");
		TraceDebugAutomate.debugDecisionNodeTrace("moyenne pour 1case de "+taille_de_case+"actions= "+ (sommeTps/30000000)*Math.pow(10.0, -9)+"s \n");
		
		
		//Affichage
		//System.out.println(monNoeudDeDecision.toString());

		Tile maTile = Tile.parseTile("Tile_FFFFZZ060123");
		Action monAction = Action.newBuildSimpleAction(new Point(5,4), maTile);

		//Teste creation d'un couple action index
		// TODO: etudier si creation d'n nouveau est necessaire ou possiblilité de juste modifier celui deja alloué
		CoupleActionIndex monCoupleActionIndex1 = new CoupleActionIndex(monAction, 0);
		CoupleActionIndex monCoupleActionIndex2 = new CoupleActionIndex(monAction, 1);

		
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);

		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex2);
		monNoeudDeDecision.setQuality(100.0);

		//System.out.println("(1)"+monNoeudDeDecision.toString());

		monNoeudDeDecision.setQuality(50.0);
		monNoeudDeDecision.setDepth(1);
		monNoeudDeDecision.setLeaf();
		monCoupleActionIndex1.setIndex(8);
		
		//System.out.println("(2)"+monNoeudDeDecision.toString());



	}

}
