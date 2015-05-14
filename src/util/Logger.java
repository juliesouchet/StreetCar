package util;

public class Logger {

	// Properties

	public static boolean logEnabled = false;
	public static boolean appLogEnabled = false;
	public static boolean engineLoglogApp = false;
	public static boolean guiLoglogApp = false;
	public static boolean iaLogEnabled = false;
	public static boolean networkLogEnabled = false;

	// Logs

	private static void log(String str, Object o) {
		if (logEnabled) {
			System.out.print(str);
			System.out.println(o);
		}
	}
	
	public static void appLog(Object o) {
		if (appLogEnabled) {
			log("APP DEBUG", o);
		}
	}

	public static void engineLog(Object o) {
		if (engineLoglogApp) {
			log("ENGINE DEBUG : ", o);
		}
	}

	public static void guiLog(Object o) {
		if (guiLoglogApp) {
			log("GUI DEBUG : ", o);
		}
	}

	public static void iaLog(Object o) {
		if (iaLogEnabled) {
			log("IA DEBUG : ", o);
		}
	}

	public static void networkLog(Object o) {
		if (networkLogEnabled) {
			log("NETWORK DEBUG : ", o);
		}
	}

}
