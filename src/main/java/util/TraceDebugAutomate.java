package main.java.util;

public class TraceDebugAutomate {

	// Properties

	public static boolean dumbestTrace = false;
	public static boolean travelerTrace = false;
	public static boolean decisionNodeTrace = false;
	public static boolean decisionTableTrace = false;


	// Logs


	public static void debugDumbest(String str) {
		if (TraceDebugAutomate.dumbestTrace) {
			System.out.print(str);
		}
	}
	
	public static void debugTraveler(String str) {
		if (TraceDebugAutomate.travelerTrace) {
			System.out.print("travelerTrace:" + str);
		}
	}

	public static void debugDecisionNodeTrace(String str) {
		if (TraceDebugAutomate.decisionNodeTrace) {
			System.out.print("decisionNode Trace:" + str);
		}
	}

	public static void debugDecisionTableTrace(String str) {
		if (TraceDebugAutomate.decisionTableTrace) {
			System.out.print("decisionTableTrace:" + str);
		}
	}

}
