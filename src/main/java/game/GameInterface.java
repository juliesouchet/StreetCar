package main.java.game;

import java.rmi.Remote;
import java.rmi.RemoteException;

import main.java.player.PlayerInterface;





public interface GameInterface extends Remote
{
	public void		onJoinRequest	(PlayerInterface player)			throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor;
	public boolean	quitGame		(String gameName, String playerName)throws RemoteException;
}