package main.java.game;

import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.data.LoginInfo;

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

	
	static  public GameSimulation newGameSimulation(Data data, String boardName, int aiLvl)
	throws RemoteException, ExceptionUnknownBoardName, RuntimeException
	{	
		String[] playerNameList = new String[data.getPlayerNameList().size()];
		int x = 0;
		for(String name : data.getPlayerNameList()) {
			playerNameList[x] = name;
			x++;
		}
		
		for(int j = 0; j < LoginInfo.initialLoginTable.length; j++) { // Game setup
			if(j<playerNameList.length) {					
				boolean isClosed, isHost;
				if(j<playerNameList.length) {
					isHost = j==0;
					isClosed = false;
				}
				else {
					isHost = false;
					isClosed = true;
				}
				LoginInfo.initialLoginTable[j] = new LoginInfo(isClosed, playerNameList[j], isHost, false, aiLvl);
			}
		}
		Game.applicationPort = 5555;		
		GameSimulation game = new GameSimulation("Simulation", "localhost", boardName, data.nbrBuildingInLine());
		return game;
	}


	/*=============================
	 *	Result of the simulation
	 =============================*/
	public boolean isWinner(String playerName)
	{
	System.out.println("SIMULATION : " + playerName);
		boolean result = false;
		
		String hostName = getHostName();
		try {
			this.hostStartGame(hostName);// Launch game
		} catch (RemoteException | ExceptionForbiddenAction	| ExceptionNotEnougthPlayers e) {
			e.printStackTrace();
		}	
		
		// TODO récupérer le résultat de la partie

	System.out.println("FIN DE LA SIMULATION");
		return result;
	}
	

}
