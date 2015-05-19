package main.java.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;





public class NetworkTools
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	public static final int minPort	= 2222;
	public static final int maxPort	= 4444;

// --------------------------------------------
// Local methodes:
// --------------------------------------------
	public static class InetLocalHost
	{
		public String	IP		= null;
		public int		port	= -1;
		public String	name	= null;
	}
	public static int firstFreePort()	{return firstFreeSocketInfo().port;}
	public static String myIPAddress()	{return firstFreeSocketInfo().IP;}
	/**===============================================================
	 * @return the ip address of the local machine
	 * @return the first free port in the local machine
	 =================================================================*/
	public static InetLocalHost firstFreeSocketInfo()
	{
		ServerSocket server;
		InetLocalHost res = new InetLocalHost();

		try
		{
			InetAddress inetadr = InetAddress.getLocalHost();
			res.IP		= (String) InetAddress.getLocalHost().getHostAddress();	// IP
			res.name	= inetadr.getCanonicalHostName();						// Host Name
		}
		catch (UnknownHostException e) {e.printStackTrace(); System.exit(0);}
		
		for (int i=minPort; i<=maxPort; i++)
		{
			try
			{
				server = new ServerSocket(i);
				server.close();
				res.port= i;													// Port
				return res;
			}
			catch (IOException e) {}
		}
		throw new RuntimeException("No Free ports found between " + minPort + " and " + maxPort);
	}
}