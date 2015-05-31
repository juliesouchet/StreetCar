package main.java.automaton.test;

import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionTable;
import main.java.automaton.ExceptionUnknownNodeType;
import main.java.util.TraceDebugAutomate;

public class testDecisionTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecisionTable maTableDeDecision = null;
		TraceDebugAutomate.decisionTableTrace=true;

		
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
		
	}

}
