package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IO {
	
	private IO() {}
	
	public static String getConsoleInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static String getConsoleInput(String descriptionOfWhatIWant) {
		System.out.print(descriptionOfWhatIWant);
		return getConsoleInput();
	}
}
