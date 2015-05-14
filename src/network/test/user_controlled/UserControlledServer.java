package network.test.user_controlled;

import java.util.ArrayList;

import util.IO;
import game.AbstractGame;
import game.Player;
import network.src.Server;

public class UserControlledServer {

	class Game extends AbstractGame {

		ArrayList<Player> players;
		int nbOfExpectedPlayers;
		Server serv;

		Game() {
			players = new ArrayList<>();
		}

		@Override
		public void onJoinRequest(Player p) {
			if(getNumberOfPlayers() >= nbOfExpectedPlayers) {
				p.display("(message from server) - Sorry, I am full.");
			}
			players.add(p);
			p.display("You are player " + getNumberOfPlayers());
			System.out.println("a player has joined, I named him player " + getNumberOfPlayers());
			if(getNumberOfPlayers() == nbOfExpectedPlayers) {
				start();
			}
		}

		private int getNumberOfPlayers() {
			return players.size();
		}
		
		public void start() {
			if(getNumberOfPlayers() == 0) {
				System.out.println("no players, nothing to do, exiting");
				return;
			}
			System.out.println("All players have joined, starting game.");
			System.out.println("What do you want to do?");
			String input = IO.getConsoleInput(" > ");
			while( !input.equals("quit") && !input.equals("exit") ) {
				if(input.equals("send")) {
					System.out.print("Select a player number (there are currently " + getNumberOfPlayers() + " players) : ");
					int playerNumber = Integer.valueOf(IO.getConsoleInput());
					while(playerNumber < 1 && playerNumber > getNumberOfPlayers())
					{
						System.out.println();
						playerNumber  = Integer.valueOf(IO.getConsoleInput("please enter a value between 1 and " + getNumberOfPlayers() + " : "));
					}
					String message = IO.getConsoleInput("Whad do you want to say to player " + playerNumber + " : ");
					Player p = players.get(playerNumber - 1);
					p.display(message);
				}
				else
				{
					input = IO.getConsoleInput(" Commands are : send quit exit\n > ");
				}
			}
			System.out.println("Game stops");
			return;
		}

		void waitForPlayers(int nbOfPlayers) {
			if(nbOfPlayers == 0) start();
			Server serv = new Server(this);
			nbOfExpectedPlayers = nbOfPlayers;
			serv.start(1099, "pokpokpok");
		}

	}

	public static void main(String[] args) {
		int nbOfPlayers = Integer.valueOf(IO.getConsoleInput("select number of players : "));
		Game g = new UserControlledServer().new Game();
		g.waitForPlayers(nbOfPlayers);
	}

}
