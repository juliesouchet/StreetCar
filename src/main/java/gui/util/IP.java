package main.java.gui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class IP {

	// Setters / getters
	
	public static String getIpAddress() {
		String ipAddress = null;
		try {
			URL checkipURL = new URL("http://checkip.amazonaws.com");
			InputStreamReader isp = new InputStreamReader(checkipURL.openStream());
			BufferedReader  br = new BufferedReader(isp);
			ipAddress = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (ipAddress != null) {
			return ipAddress;
		} else {
			return Resources.localizedString("Unknown IP address", null);
		}
	}
	
}