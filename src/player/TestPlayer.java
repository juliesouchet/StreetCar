package player;

import game.FullPartyException;
import game.InterfaceGame;
import game.UsedPlayerColorException;
import game.UsedPlayerNameException;
import ihm.TestIHM;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import util.NetworkTools;







public class TestPlayer extends AbstractPlayer
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
	 * @throws FullPartyException											(caught by IHM)
	 * @throws UsedPlayerColorException 									(caught by IHM)
	 * @throws UsedPlayerNameException 									    (caught by IHM)
	 */
	public TestPlayer(String playerName, Color playerColor, InterfaceGame app, TestIHM ihm) throws RemoteException, FullPartyException, UsedPlayerNameException, UsedPlayerColorException
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