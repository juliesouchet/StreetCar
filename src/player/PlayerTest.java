package player;

import game.ExceptionFullParty;
import game.GameInterface;
import game.ExceptionUsedPlayerColor;
import game.ExceptionUsedPlayerName;
import ihm.TestIHM;
import java.awt.Color;
import java.rmi.RemoteException;









public class PlayerTest extends PlayerAbstract
{
// --------------------------------------------
// Attributs:
// --------------------------------------------

// --------------------------------------------
// Builder:
// --------------------------------------------
	/**
	 * @return Creates a local player that can be called as a local object
	 * @throws RemoteException 			: The web host is offline			(caught by IHM)
	 * @throws ExceptionFullParty											(caught by IHM)
	 * @throws ExceptionUsedPlayerColor 									(caught by IHM)
	 * @throws ExceptionUsedPlayerName 									    (caught by IHM)
	 */
	public PlayerTest(String playerName, Color playerColor, GameInterface app, TestIHM ihm) throws RemoteException, ExceptionFullParty, ExceptionUsedPlayerName, ExceptionUsedPlayerColor
	{
		super(playerName, playerColor, app, ihm);
	}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String getName()	throws RemoteException	{return super.name;}

// --------------------------------------------
// Local methodes:
// --------------------------------------------

}