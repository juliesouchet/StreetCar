package player;

import java.rmi.Remote;
import java.rmi.RemoteException;





public interface InterfacePlayer extends Remote
{
	public String getPlayerName()	throws RemoteException;
	public String getGameName()		throws RemoteException;
}
