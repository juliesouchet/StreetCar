package network;

import java.rmi.RemoteException;

import game.Player;
import network.generic_part.AbstractHost;

public abstract class AbstractGameHost extends AbstractHost {
	
	class RemotePlayerStub implements Player {
		RemotePlayerInterface rp;
		
		public RemotePlayerStub(RemotePlayerInterface rp) {
			this.rp = rp;
		}
		
		@Override
		public void display(String str) {
			try {
				rp.display(str);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	protected synchronized void onJoinRequest(Object guest) {
		if(guest instanceof Player) { 
			onJoinRequest((Player)guest);
		}
		if(guest instanceof RemotePlayerInterface) {
			onJoinRequest(new RemotePlayerStub((RemotePlayerInterface)guest));
		}
		else {
			System.out.println("Is not a player");
		}

	}

	protected abstract void onJoinRequest(Player p);
	protected abstract void onFullParty();
	protected abstract void onGuestLeaves();

}
