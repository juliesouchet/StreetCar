package network;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public abstract class AbstractGame implements GameInterface {
	protected HashMap<String, PlayerInterface> players; // HashMap<playerName, player>
	protected int maxNumberOfPlayers;

	protected AbstractGame () {
		players = new HashMap<>();
		maxNumberOfPlayers = Integer.MAX_VALUE;
	}

	public void setMaxNumberOfPlayers(int n) {
		maxNumberOfPlayers = n;
		// TODO kick last players
	}

	public void startAcceptingPlayers(int port, String endOfURL) {
		try {
			UnicastRemoteObject.exportObject(this, 0);
			LocateRegistry.createRegistry(port);
			String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/" + endOfURL;
			Naming.rebind(url, this);
			System.out.println("server url : " + url);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onJoinRequest(PlayerInterface player) throws RemoteException {
		if(getNumberOfPlayers() >= maxNumberOfPlayers) {
			player.display("(message from server) - Sorry, I am full.");
		}
		players.put(player.getName(), player);
		player.display("Message from server\n -  Hello " + player.getName() + ", you have joined.");
		System.out.println("\nPlayer " + player.getName() + " has joined.");
		if(getNumberOfPlayers() == maxNumberOfPlayers) {
			onPlayerLimitReached();
		}
	}

	public int getNumberOfPlayers() {
		return players.size();
	}

	public abstract void beginGame();
	public abstract void onPlayerLimitReached();
	public abstract void onNewPlayer(PlayerInterface player);

}
