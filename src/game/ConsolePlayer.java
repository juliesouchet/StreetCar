package game;

import java.rmi.RemoteException;

import util.IO;
import network.AbstractPlayer;

public class ConsolePlayer extends AbstractPlayer {
	
	@Override
	public void display(String str) throws RemoteException {
		System.out.println("\nMessage from server :");
		System.out.println(str);
		System.out.print(" > ");
	}
	
	public static void main(String[] args) {

		ConsolePlayer cplayer = new ConsolePlayer();
		String name = IO.getConsoleInput("what is your name? \n > ");
		cplayer.setName(name);
		System.out.println("Hello " + name + ", what would you like to do? (type help for list of commands)");
		String command = "";
		while(!(command.equals("quit") || command.equals("exit"))) {
			String input = IO.getConsoleInput(" > ");
			String delims = "[ ]+";
			String[] tokens = input.split(delims);
			command = tokens[0];
			
			if(command.equals("help")) {
				System.out.println("\nCommand list : ");
				System.out.println(" -  connect <host IP> <host name>");
				System.out.println(" -  rename <new_name> - to rename your self");
				System.out.println(" -  exit");
				System.out.println(" -  quit");
				System.out.println(" -  help");
			} else if (command.equals("rename")) {
				if(input.length() < 2) {
					System.out.println("Not enough arguments, eg : \n -  rename <new_name> - to rename your self");
				} else {
					String new_name = input.substring(input.indexOf(tokens[1]));
					cplayer.setName(new_name);
					System.out.println("\n -  You have been renamed " + new_name);
				}
			} else if(command.equals("connect")) {
				if(input.length() < 3) {
					System.out.println("Not enough arguments, eg : \n -  connect <host IP> <host name>");
				}
				else {
					String ip = tokens[1];
					String hostName = tokens[2];
					cplayer.tryJoiningGame("rmi://" + ip + "/" + hostName);
					System.out.println("\n -  connection to server successfull\n > ");
				}
			}
		}
		
		String url = IO.getConsoleInput("enter a url : ");
		cplayer.tryJoiningGame(url);
	}

}
