package network.test.user_controlled;

import java.util.ArrayList;

import game.AbstractGame;
import game.Player;
import network.src.Server;

public class BasicTestServer {
	
	class Game extends AbstractGame {
		
		ArrayList<Player> players;
		
		Game() {
			players = new ArrayList<>();
		}

		@Override
		public void onJoinRequest(Player p) {
			players.add(p);
			System.out.println("player " + players.size() + " has joined");
			p.display("You are player " + players.size());
		}
		
		void start() {
			
		}
		
	}

	public static void main(String[] args) {
		Server serv = new Server(new BasicTestServer().new Game());
		serv.start(1099, "pokpokpok");
	}

}
