package main.java.player;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;

import test.java.engine.TestIHM;
import main.java.engine.data.Tile;
import main.java.engine.game.ExceptionFullParty;
import main.java.engine.game.ExceptionUsedPlayerColor;
import main.java.engine.game.ExceptionUsedPlayerName;
import main.java.engine.game.GameInterface;









public class PlayerTest extends PlayerAbstract
{
// --------------------------------------------
// Attributes:
// --------------------------------------------

	private static final long serialVersionUID = 8115400749334702829L;

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
// Public methods: may be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------
	public String getName()	throws RemoteException	{return super.name;}

	@Override
	public boolean isHumanPlayer() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void distributeLineCard() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distributeRouteCard() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tileHasBeenPlaced(String playerName, Tile t, Point position)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exchangeTile(String playerName, Tile t, Point p)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distributeTile(Tile t) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveTileFromPlayer(String chosenPlayer, Tile t)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeStop(Point p) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revealLine(String playerName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revealRoute(String playerName) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


// --------------------------------------------
// Local methods:
// --------------------------------------------

}