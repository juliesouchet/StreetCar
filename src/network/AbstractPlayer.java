package network;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class AbstractPlayer implements PlayerInterface {

	GameInterface game;

	@Override
	public void setGame(GameInterface game) throws RemoteException {
		this.game = game;
	}

	public void tryJoiningGame(String url) {
		try {
			Remote r = Naming.lookup(url);
			if (r instanceof GameInterface) {
				game = (GameInterface) r;
				UnicastRemoteObject.exportObject(this, 0);
				game.onJoinRequest(this);
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
