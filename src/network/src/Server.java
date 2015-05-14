package network.src;

import game.AbstractGame;
import game.Player;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ServerInterface  {

	public class RemotePlayerStub implements Player {

		RemotePlayerInterface rp;

		RemotePlayerStub(RemotePlayerInterface rp2) {
			this.rp = rp2;
		}

		@Override
		public void display(String str) {
			try {
				rp.display(str);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	AbstractGame game;

	public Server(AbstractGame g) {
		game = g;
	}

	public void start(int port, String endOfURL) {
		try {
			UnicastRemoteObject.exportObject(this, 0);
			LocateRegistry.createRegistry(port);
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/" + endOfURL;
			Naming.rebind(url, this);
			System.out.println("server url : " + url);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onJoinRequest(RemotePlayerInterface rp) throws RemoteException { 
		System.out.println("Received connection request.");
		game.onJoinRequest(new RemotePlayerStub(rp));
	}

}
