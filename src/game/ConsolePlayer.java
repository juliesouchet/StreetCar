package game;

import java.rmi.RemoteException;

import util.IO;
import network.AbstractPlayer;

public class ConsolePlayer extends AbstractPlayer {
	
	@Override
	public void display(String str) throws RemoteException {
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		String url = IO.getConsoleInput("enter a url : ");
		ConsolePlayer cplayer = new ConsolePlayer();
		cplayer.tryJoiningGame(url);
	}

}
