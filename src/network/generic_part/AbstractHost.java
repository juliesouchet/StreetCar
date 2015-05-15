package network.generic_part;

import java.util.ArrayList;

public abstract class AbstractHost {
	ArrayList<Object> party;
	Server server;
	int maxNbOfPlayers = Integer.MAX_VALUE;
	
	public AbstractHost() {
		party = new ArrayList<Object>();
		server = new Server(this);
	}
	
	public AbstractHost(int maxNbOfPlayers) {
		this();
		this.maxNbOfPlayers = maxNbOfPlayers;
	}
	
	public void waitForGuests(int port, String endOfURL) {
		server.start(port, endOfURL);
	}
	
	protected abstract void onJoinRequest(Object guest);
}
