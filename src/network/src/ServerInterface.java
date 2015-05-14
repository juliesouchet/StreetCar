package network.src;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface ServerInterface extends Remote {
	public void onJoinRequest(RemotePlayerInterface rp) throws RemoteException;
}
