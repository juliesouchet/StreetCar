package main.java.player;

import java.rmi.RemoteException;

import main.java.automaton.Dumbest;
import main.java.automaton.PlayerAutomaton;
import main.java.automaton.Traveler;
import main.java.data.Action;
import main.java.data.Data;
import main.java.game.ExceptionEndGame;
import main.java.game.ExceptionForbiddenAction;
import main.java.game.ExceptionFullParty;
import main.java.game.ExceptionGameHasAlreadyStarted;
import main.java.game.ExceptionGameHasNotStarted;
import main.java.game.ExceptionNotYourTurn;
import main.java.game.ExceptionUsedPlayerColor;
import main.java.game.ExceptionUsedPlayerName;
import main.java.game.GameInterface;
import main.java.rubbish.InterfaceIHM;





@SuppressWarnings("serial")
public class PlayerAI extends PlayerAbstract implements Runnable
{
// --------------------------------------------
// Attributes:
// --------------------------------------------
	private PlayerAutomaton	automaton;
	private boolean			gameOver = false;

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**=====================================================================
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline	(caught by IHM)
	 * @throws ExceptionFullParty									(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 							(caught by IHM)
	 * @throws ExceptionUsedPlayerName 								(caught by IHM)
	 * @throws ExceptionGameHasAlreadyStarted						(caught by IHM)
	 =======================================================================*/
	public PlayerAI(String playerName, boolean isHost, GameInterface app, int iaLevel, InterfaceIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor, ExceptionGameHasAlreadyStarted
	{
		super(playerName, app, ihm);
		switch (iaLevel)
		{
			case 1	:this.automaton	= new Dumbest(playerName);	break;
			case 2	:this.automaton = new Traveler(playerName);	break;
//			case 3	:this.automaton = new 3eme_niveau_de_difficulte(playerName);	break;
			default	:throw new RuntimeException("Undefined AI difficulty : " + iaLevel);
		}
		super.game.onJoinGame(this, false, isHost, iaLevel);						// Log the player to the application
	}

// --------------------------------------------
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public boolean isHumanPlayer()
	{
		return false;
	}
	public synchronized void gameHasChanged(Data data) throws RemoteException
	{
		super.gameHasChanged(data);
		if (!data.isGameStarted())			return;
		if (!data.isPlayerTurn(playerName)) return;
		if (gameOver)						return;
		
		if (data.hasRemainingAction(playerName))					// choix d'action
		{
//System.out.println("-----------PlayerName: " + playerName);
//System.out.println("-----------HandSize  : " + data.getHandSize(playerName));
			if(data.getHandSize(playerName)>0 || data.hasStartedMaidenTravel(playerName)) {
				Action a = this.automaton.makeChoice(data.getClone(playerName));
	
	// TODO: check action type, do others action types !!
				try					{super.placeTile(a.tile1, a.positionTile1);}
				catch (Exception e) {e.printStackTrace(); return;}
			}
			else System.out.println(playerName + " BLOCKED");
			return;
		}

		int nbrCards = data.getPlayerRemainingTilesToDraw(playerName);
		if (nbrCards > 0 && data.getNbrRemainingDeckTile() > 0)		// pioche
		{
			try
			{
				if (!data.isEnougthTileInDeck(nbrCards))
				{
					nbrCards = data.getNbrRemainingDeckTile();
				}
				super.drawTile(nbrCards);
			}
			catch (Exception e) {e.printStackTrace(); return;}
			return;
		}
		else														// fin de tour
		{
			try {
				super.validate();
			} catch (ExceptionGameHasNotStarted | ExceptionNotYourTurn
					| ExceptionForbiddenAction e) {
				e.printStackTrace();
			} catch (ExceptionEndGame e) {
				System.out.println("******************");
				System.out.println("END OF GAME");
				gameOver = true;
				String winner = e.getWinner();
				if(winner != null)
					System.out.println(winner + " WINS !!");
				else
					System.out.println("GAME BLOCKED : NOBODY WINS");
				System.out.println("******************");
			}
		}
	}
	public synchronized void excludePlayer() throws RemoteException
	{
		if (super.ihm != null)	super.ihm.excludePlayer();
	}

// --------------------------------------------
// Local methods
// --------------------------------------------
	public void run()
	{
		
	}
}
