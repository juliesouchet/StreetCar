package network.src;

import java.rmi.RemoteException;

import game.Player;

class RemotePlayer implements RemotePlayerInterface {
	public final Player player;

	public RemotePlayer(Player p) {
		player = p;
	}

	@Override
	public void display(String str) throws RemoteException {
		player.display(str);
	}

}
