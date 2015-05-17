package player;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;





/**============================================================
 * Remote IHM
 * URL: rmi://ip:port/gameName/playerName
 * @author kassuskley
 ==============================================================*/



public abstract class AbstractPlayer extends UnicastRemoteObject implements InterfacePlayer
{
// --------------------------------------------
// Attributs:
// --------------------------------------------
	public final static int				defaultplayerPort	= 4000;
	public final static String			playerProtocol		= "rmi";

// --------------------------------------------
// Builder:
// --------------------------------------------
	protected AbstractPlayer() throws RemoteException {super();}

// --------------------------------------------
// Public methodes: my be called by the remote object
// Must implement "throws RemoteException"
// --------------------------------------------

}