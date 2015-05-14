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
			System.out.println("What do you want to do? (type help for command list)");
			String input = IO.getConsoleInput(" > ");
			while( !input.equals("quit") && !input.equals("exit") ) {
				input = IO.getConsoleInput(" > ");
				String delims = "[ ]+";
				String[] tokens = input.split(delims);
				String command = tokens[0];
				if(command.equals("send")) {
					int player;
					try {
						player = Integer.parseInt(tokens[1]);
					} catch (NumberFormatException e) {
						continue; // if is not integer, continue loop (skips it)
					}
					if(player < 1) {
						player *= -1;
					}
					if(player > getNumberOfPlayers()) {
						System.out.println(" -  There are only " + getNumberOfPlayers() + " players.");
						continue;
					}

					String message = input.substring(input.indexOf(tokens[2]));
					players.get(player - 1).display(message);

				} else if (command.equals("help")) {
					System.out.println("Commands are : \n -  send player_number message \n -  quit \n -  exit\n -  help\n > ");
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
