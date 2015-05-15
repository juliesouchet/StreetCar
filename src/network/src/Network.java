package network.src;

import game.Player;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class Network {

	private Network() {};

	public static void trySendingRemoteToHost(String url, Player p) throws RemoteException {
		try {
			Remote r = Naming.lookup(url);
			if (r instanceof ServerInterface) {
				ServerInterface serv = (ServerInterface) r;
				RemotePlayer rp = new RemotePlayer(p);
				UnicastRemoteObject.exportObject(rp, 0);
				serv.onJoinRequest(rp);
			}
			else {
				System.err.println("Couldn't connect, no server found.");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}