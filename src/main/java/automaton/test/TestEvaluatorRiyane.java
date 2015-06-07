package main.java.automaton.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import main.java.automaton.EvaluatorRiyane;
import main.java.automaton.PlayerAutomaton;
import main.java.data.Data;
import main.java.data.LoginInfo;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionHostAlreadyExists;
import main.java.game.ExceptionNotEnoughPlayers;
import main.java.game.ExceptionUnknownBoardName;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.player.PlayerIHM;







public class TestEvaluatorRiyane
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	public static final LoginInfo[] initialLoginTable =	{	new LoginInfo(false,	null,	true,	true,	-1),
															new LoginInfo(false,	null,	false,	false,	PlayerAutomaton.travelerLvl),
															new LoginInfo(false,	null,	false,	false,	PlayerAutomaton.dumbestLvl),
															new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl),
															new LoginInfo(true,		null,	false,	false,	PlayerAutomaton.dumbestLvl)};
	public static final String	gameName			= "TestEvaluator";
	public static final String	hostName			= "Testeur";
	public static final String	boardDirectory		= "src/test/resources/boards/";
	public static final String	boardName			= "test";
	public static final int		nbrBuildingInLine	= 3;
	public static final int		nbrGamesSimulated	= 100;
	public static final int		aiLevel				= 2;

// --------------------------------------------
// Local methodes:
// --------------------------------------------
	public static void main(String[] args) throws RemoteException, ExceptionFullParty, ExceptionHostAlreadyExists, NotBoundException, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionUnknownBoardName, ExceptionGameHasAlreadyStarted, ExceptionForbiddenAction, ExceptionNotEnoughPlayers, InterruptedException
	{
		for(int i = 0; i < initialLoginTable.length; i++) LoginInfo.initialLoginTable[i] = initialLoginTable[i];
		Data.boardDirectory = boardDirectory;

		PlayerIHM player = PlayerIHM.launchPlayer(hostName, gameName, boardName, nbrBuildingInLine, true, "localHisst", null);
		player.hostStartGame();
		waitGameStart(player);
		Data	data	= player.getGameData();
		double	winProportion	= EvaluatorRiyane.evaluateSituationQuality(hostName, nbrGamesSimulated, data, aiLevel);

		System.out.println(hostName + " victory proportion = " + winProportion);
	}

	
	
	
	
	
// DSL, mais c la seule solution que g
private static void waitGameStart(PlayerIHM player) throws InterruptedException
{
	Object o = new Object();
	synchronized (o)
	{
		while ((player.getGameData() == null) || (!player.getGameData().isGameStarted())) o.wait(2000);
	}
}
}