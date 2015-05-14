package network.test.basic;

import java.rmi.RemoteException;

import util.IO;
import game.Player;
import network.src.Network;

public class BasicClient {

	class ConsolePlayer implements Player {

		@Override
		public void display(String str) {
			System.out.println(str);
			
		}
		
	}
	
	public static void main(String[] args) {
		ConsolePlayer cp = new BasicClient().new ConsolePlayer();
		try {
			System.out.print("Enter url : ");
			String url = IO.getConsoleInput();
			if(url.equals("")) url = "rmi://192.168.1.3/pokpokpok";
			Network.trySendingRemoteToHost(url, cp);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
