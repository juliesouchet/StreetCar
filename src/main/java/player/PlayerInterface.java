package main.java.player;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;





public interface PlayerInterface extends Remote
{
	public String	getName()	throws RemoteException;
	public Color	getColor()	throws RemoteException;
}
