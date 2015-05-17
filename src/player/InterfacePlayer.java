package player;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;





public interface InterfacePlayer extends Remote
{
	public String	getName()	throws RemoteException;
	public Color	getColor()	throws RemoteException;
}
