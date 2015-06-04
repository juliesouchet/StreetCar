package main.java.util;

public class TraceDebugData {

	// Properties

	public static boolean actionEqualsTrace = false;
	public static boolean traceGetPossiblesActions = false;



	// Logs

	public static void debugActionEquals(String str) {
		if (TraceDebugData.actionEqualsTrace) {
			System.out.print(str);
		}
	}

	public static void traceGetPossiblesActions(String str) {
		if (TraceDebugData.traceGetPossiblesActions) {
			System.out.print("traceGetPossiblesActions:"+str);
		}
	}



}
