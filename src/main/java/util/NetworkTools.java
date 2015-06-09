package main.java.util;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;





public class NetworkTools
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	public static final int		minPort		= 2222;
	public static final int		maxPort		= 4444;
	public static final String	localHost1	= "127.0.0.1";
	public static final String	localHost2	= "127.0.1.1";

// --------------------------------------------
// Local methodes:
// --------------------------------------------
	/**============================================================
	 * @return Scan the machine open ports and return the first free one
	 ==============================================================*/
	public static int firstFreePort()
	{
		ServerSocket server;

		for (int i=minPort; i<=maxPort; i++)
		{
			try
			{
				server = new ServerSocket(i);
				server.close();
				return i;
			}
			catch (IOException e) {}
		}
		throw new RuntimeException("No Free ports found between " + minPort + " and " + maxPort);
	}
	/**============================================================
	 * @return Return my IP address, or local host if no IP address is found
	 ==============================================================*/
	public static String myIPAddress()
	{
		Enumeration<NetworkInterface> interfaces;
		String str;

		try							{interfaces = NetworkInterface.getNetworkInterfaces();}
		catch (SocketException e)	{return new String(localHost1);}

		while(interfaces.hasMoreElements())
		{
			for(InterfaceAddress ia : interfaces.nextElement().getInterfaceAddresses())
			{
System.out.println("Ip sea");
				str = ia.getAddress().toString();
				if (str.charAt(0) == '/') str = str.substring(1);
				if (!isIPV4(str))			continue;
				if (str.equals(localHost1))	continue;
				if (str.equals(localHost2))	continue;
				return str;
			}
		}
		return new String(localHost1);
	}
	/**============================================================
	 * @return Return true if the given string is a correct IP v4 address
	 ==============================================================*/
	public static boolean isIPV4(String address)
	{
		String 		delim	= "[.]";
		String[]	res		= address.split(delim);
		int x;

		for (int i=0; i<4; i++)
		{
			if (i >= res.length)	return false;
			try
			{
				x = Integer.parseInt(res[i]);
				if (x < 0)			return false;
				if (x > 255)		return false;
			}
			catch (Exception e)		{return false;}
		}
		return true;
	}
}