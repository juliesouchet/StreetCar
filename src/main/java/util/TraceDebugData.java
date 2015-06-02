package main.java.util;

public class TraceDebugData {

	// Properties

	public static boolean actionEqualsTrace = false;



	// Logs

	public static void debugActionEquals(String str) {
		if (TraceDebugData.actionEqualsTrace) {
			System.out.print(str);
		}
	}



}
