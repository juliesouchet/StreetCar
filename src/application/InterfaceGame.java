package application;

import java.rmi.Remote;
import java.rmi.RemoteException;

import player.InterfacePlayer;




public interface InterfaceGame extends Remote
{
	public void		onJoinRequest	(InterfacePlayer player)			throws RemoteException, FullPartyException;
	public boolean	quitGame		(String gameName, String playerName)throws RemoteException;
}