package network.test.basic;

import game.AbstractGame;
import game.Player;
import network.src.Server;

public class BasicServer {
	
	class Game extends AbstractGame {
		
		Game() {
			
		}

		@Override
		public void onJoinRequest(Player p) {
			p.display("SUCCESS!");
		}
		
	}

	public static void main(String[] args) {
		Server serv = new Server(new BasicServer().new Game()); // lol
		serv.start(1099, "pokpokpok");
	}

}
