package main.java.util;

public class TraceDebugAutomate {

	// Properties

	public static boolean dumbestTrace = false;
	public static boolean decisionNodeTrace = false;


	// Logs


	public static void debugDumbest(String str) {
		if (TraceDebugAutomate.dumbestTrace) {
			System.out.print(str);
		}
	}

	public static void debugDecisionNodeTrace(String str) {
		if (TraceDebugAutomate.decisionNodeTrace) {
			System.out.print("decisionNode Trace:" + str);
		}
	}

}
