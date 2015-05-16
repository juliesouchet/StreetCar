package network;

import java.rmi.RemoteException;

import util.IO;

public class TestGame extends AbstractGame {

	TestGame() {
		super();
	}

	@Override
	public void beginGame() {
		// TODO Auto-generated method stub

	}

	public int getNumberOfPlayers() {
		return players.size();
	}

	public void start() {
		System.out.println("starting game.");
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
					System.out.println(" There is no \"player " + tokens[1] + "\"");
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
				try {
					players.get(player - 1).display(message);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (command.equals("help")) {
				System.out.println("Commands are : \n -  send player_number message \n -  quit \n -  exit\n -  help\n > ");
			} else if(command.equals("get")) {

			}
		}
		System.out.println("Game stops");
		return;
	}


	public static void main(String[] args) {
		TestGame game = new TestGame();
		game.startAcceptingPlayers(1099, "pokpokpok");
		game.start();
	}

	@Override
	public void onPlayerLimitReached() {
		// TODO Auto-generated method stub

	}
}
