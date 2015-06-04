package main.java.game;

import java.rmi.RemoteException;

import main.java.data.Data;

public class GameSimulation extends Game {

	/*==========================
	 *		Attributes
	 ==========================*/
	private static final long serialVersionUID = 1962846841593663477L;
	/*==========================
	 *		Builders
	 ==========================*/
	public GameSimulation(String gameName, String appIP, String boardName, int nbrBuildingInLine)
	throws RemoteException, ExceptionUnknownBoardName, RuntimeException
	{
		super(gameName, appIP, boardName, nbrBuildingInLine);
	}

	/**
	 * @param data : the data of the current game
	 * @param boardName : the name of the file where the board is stored
	 * @param aiLvl : the level of the AI that take the players' places
	 * @return A game, that is a copy of the current state of the game, used for simulations.
	 * @throws RemoteException
	 * @throws ExceptionUnknownBoardName
	 * @throws RuntimeException
	 */
	static public GameSimulation newGameSimulation(Data data, int aiLvl)
	throws RemoteException, ExceptionUnknownBoardName, RuntimeException
	{	
		Game.applicationPort = 5555;
		GameSimulation game = new GameSimulation("Simulation", "localhost", "src/main/resources/boards/newOrleans", data.nbrBuildingInLine());
		game.data = data;
		return game;
	}


	/**==========================================================
	 *	Result of the simulation : has this player won ?
	 ==========================================================*/
	public boolean isWinner(String playerName)
	{
		boolean result = false;
		
		String hostName = getHostName();
		try {
			this.hostStartGame(hostName);		// Launch game
		} catch (RemoteException | ExceptionForbiddenAction
				| ExceptionNotEnoughPlayers e) {
			e.printStackTrace();
		}
		
		// TODO récupérer le résultat de la partie
//System.out.println("TODO : récupérer le résultat de la partie");
		return result;
	}
	

}
