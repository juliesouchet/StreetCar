package main.java.player;

import java.awt.Color;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import main.java.data.Data;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.Game;
import main.java.game.GameInterface;
import main.java.util.NetworkTools;
import test.java.player.TestIHM;









@SuppressWarnings("serial")
public class PlayerIHM extends PlayerAbstract
{
// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=========================================================================
	 * @return Creates a new Game.  If gameCreation==true the application is launched
	 * @param gameCreation equals true if the player shelters a new game, and false if the player join an existing game
	 * @param applicationIP equals null if the player shelters the game
	 * @throws NotBoundException 		: The app IP or game name is wrong	(caught by IHM)
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUnknownBoardName:									(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 
	 * @throws ExceptionUsedPlayerName
	 * @throws ExceptionHostAlreadyExists
 	 ===========================================================================*/
	public static PlayerIHM launchPlayer(String playerName, String gameName, String boardName, int nbrBuildingInLine, Color playerColor, boolean gameCreation, String applicationIP, TestIHM ihm) throws RemoteException, NotBoundException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionUnknownBoardName, ExceptionHostAlreadyExists
	{
		String localIP = NetworkTools.firstFreeSocketInfo().IP;
		GameInterface game;

		if (gameCreation)															// App thread creation
		{
			Game gameT	= new Game(gameName, localIP, boardName, nbrBuildingInLine);
			Thread t	= new Thread(gameT);
			t.start();
			game		= Game.getRemoteGame(localIP, gameName);					// Remote application pointer
		}
		else	game	= Game.getRemoteGame(applicationIP, gameName);				// Remote application pointer
		return new PlayerIHM(gameCreation, playerName, playerColor, game, ihm);		// Player Creation
	}
	/**=====================================================================
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 									(caught by IHM)
	 * @throws ExceptionUsedPlayerName 									    (caught by IHM)
	 =======================================================================*/
	private PlayerIHM(boolean isHost, String playerName, Color playerColor, GameInterface app, TestIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(playerName, playerColor, app, ihm);
		super.game.onJoinGame(this, isHost);						// Log the player to the application
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public boolean isHumanPlayer() throws RemoteException
	{
		return true;
	}
	public void gameHasChanged(Data data) throws RemoteException
	{
		if (super.ihm != null) super.ihm.refresh(data);
	}
}