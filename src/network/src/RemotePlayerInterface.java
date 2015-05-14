package network.src;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface RemotePlayerInterface extends Remote {
	public void display(String str) throws RemoteException;
}
