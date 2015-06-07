package main.java.automaton;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.PathFinder;






public class TravelerRiyane extends PlayerAutomaton
{
// ------------------------------
// Attributes
// ------------------------------
	private String			playerName;
	private PlayerAutomaton	edouardo;
	private PathFinder		pathFinder;

// ------------------------------
// Buildetr
// ------------------------------
	public TravelerRiyane(String playerName)
	{
		this.playerName = new String(playerName);
		this.edouardo	= new Dumbest(playerName);
		this.pathFinder	= new PathFinder();
	}

// ------------------------------
// Local methods
// ------------------------------
	public Action makeChoice(Data currentConfig)
	{
		if ((!currentConfig.isTrackCompleted(playerName)) || (currentConfig.hasDoneRoundFirstAction(playerName)))
		{
			return this.edouardo.makeChoice(currentConfig);
		}
		else
		{
			Random	rnd				= new Random();
			Point[] terminus		= currentConfig.getPlayerTerminusPosition(playerName);
			Point	startTerminus	= null;
			Point[]	tramPath		= new Point[Data.maxSpeed+1];
			int		pathSize		= 1+rnd.nextInt(Data.maxSpeed);
			boolean	hasStartedTravel= currentConfig.hasStartedMaidenTravel(playerName);
			if (!hasStartedTravel)
			{
				startTerminus	= terminus[rnd.nextInt(terminus.length)];
				currentConfig.startMaidenTravel(playerName, startTerminus);
			}

			Point[]	endTerminus		= currentConfig.getPlayerEndTerminus(playerName);
			Point	tramPosition	= currentConfig.getTramPosition(playerName);
			LinkedList<Point> completePath = this.pathFinder.getPath(currentConfig, tramPosition, endTerminus[0]);
			if (completePath == null)	throw new RuntimeException("The traveler is blocked");
			pathSize = Math.min(pathSize, completePath.size());
			for (int i=0; i<pathSize; i++) tramPath[i] = completePath.get(i);
			if (!hasStartedTravel) currentConfig.stopMaidenTravel(playerName);
			return Action.newMoveAction(tramPath, pathSize, startTerminus);
		}
	}
}
