package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerInterface extends Remote {
	public void display(String str) throws RemoteException;
	public void setGame(GameInterface game) throws RemoteException;
	public String getName() throws RemoteException;
}
