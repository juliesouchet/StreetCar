package main.java.gui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import main.java.util.NetworkTools;

public class IP {

	// Setters / getters
	
	public static String getIpAdressFromInet() {
		return NetworkTools.myIPAddress();
	}
	
	public static String getIpAddressFromWebService() {
		String ipAddress = null;
		URL checkipURL = null;
		try {
			checkipURL = new URL("http://checkip.amazonaws.com");
			InputStreamReader isp = new InputStreamReader(checkipURL.openStream());
			BufferedReader  br = new BufferedReader(isp);
			ipAddress = br.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(checkipURL + " output is invalid");
			ipAddress = null;
		}
		
		if (ipAddress != null) {
			return ipAddress;
		} else {
			return Resources.localizedString("Unknown IP address", null);
		}
	}
	
}
