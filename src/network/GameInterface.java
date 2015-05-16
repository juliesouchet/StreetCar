package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
	public void onJoinRequest(PlayerInterface player) throws RemoteException;
}
