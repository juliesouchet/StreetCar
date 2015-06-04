package test.java.player;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;





public class TestIP
{
	public static void main(String[] args)
	{
		try
		{ 
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while(interfaces.hasMoreElements())
			{
				NetworkInterface address = interfaces.nextElement();
//				System.out.println(address.getInterfaceAddresses());
				for(InterfaceAddress pokpokpok : address.getInterfaceAddresses())
				{
					System.out.println(pokpokpok.getAddress());
				}
			}
			
			System.out.println("POGEIJIOUNSFEN ");
		}
		catch (Exception e) { e.printStackTrace(); }
	}

}