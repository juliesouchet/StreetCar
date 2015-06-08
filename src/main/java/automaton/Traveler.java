package main.java.automaton;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.util.TraceDebugAutomate;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 */






public class Traveler extends PlayerAutomaton {
	/** Table that contains the objectives of the player : starting terminus and buildings to visit */
	Point[] checkpoints;
	/** Index of the first checkpoint left to complete */
	int startCheckpoints;
	/** Table that contains the destination (both points of the ending terminus) */
	Point[]	destinationTerminus;
	/** Point before last of the latest streetcar movement */
	Point previous;
	/** Automaton responsible of building the tracks */
	PlayerAutomaton slave = new Dumbest(name); 
	
	public Traveler(String name) {
		super();
		if(name == null) this.name = "Traveler";
		else this.name = name;
	}
	
	@Override
	public Action makeChoice(Data currentConfig) {
		Action res = null;
		int i;
		Point startingPoint = null;
		boolean trackCompleted = currentConfig.isTrackCompleted(getName());

TraceDebugAutomate.debugTraveler(" PlayerName : " + name +"\n");
TraceDebugAutomate.debugTraveler(" trackCompleted = " + trackCompleted +"\n");
		/*============
		 *	Building
		 =============*/
		if(!trackCompleted || (currentConfig.hasDoneRoundFirstAction(name))) {
			res = slave.makeChoice(currentConfig);
		}
		
		
		/*==============
		 *	Traveling	
		 ===============*/
		else {
			// TODO déplacer ça dans initializeTravel une fois le debug fini
			Point[] terminusPosition = currentConfig.getPlayerTerminusPosition(getName()),
					buildingsPosition = currentConfig.getPlayerAimBuildings(getName());
			
String termPosString = terminusPosition[0]+","+terminusPosition[1]+","+terminusPosition[2]+","+terminusPosition[3],
		buildPosString = buildingsPosition[0].toString();
for(int x = 1; x<currentConfig.nbrBuildingInLine(); x++) {buildPosString+=","+buildingsPosition[x];}
TraceDebugAutomate.debugTraveler(" PlayerLine : " + currentConfig.getPlayerLine(getName()) +"\n");
TraceDebugAutomate.debugTraveler(" PlayerTerminus : "+termPosString +"\n");
TraceDebugAutomate.debugTraveler(" NbrBuildings : "+currentConfig.nbrBuildingInLine()+"\n");
TraceDebugAutomate.debugTraveler(" PlayerBuildings : "+buildPosString +"\n");
TraceDebugAutomate.debugTraveler(" MaximumSpeed : " + currentConfig.getMaximumSpeed() +"\n");

			if(!currentConfig.hasStartedMaidenTravel(getName())) {
				startingPoint = initializeTravel(currentConfig,	terminusPosition, buildingsPosition);
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(currentConfig);
			
			// Go forward the maximum allowed number of squares
			ListIterator<Point> iterator = itinerary.listIterator();
			int pathSize = 0;
			while(iterator.hasNext()
				&& !currentConfig.getTile(iterator.next()).isStop()
				&& pathSize < currentConfig.getMaximumSpeed()+1)	pathSize++;
			Point [] streetcarMovement = new Point[pathSize];
			i = 0;
			iterator = itinerary.listIterator();
			while(iterator.hasNext() && i < pathSize) {
				streetcarMovement[i] = iterator.next();
				i++;
			}
			// Updates the checkpoints : removes those passed by
			do {
				Point p = checkpoints[startCheckpoints];
				i = 0;
				while(i<streetcarMovement.length && streetcarMovement[i] != p)
					i++;
				if(i<streetcarMovement.length)
					startCheckpoints++;
			} while (i<streetcarMovement.length);
			// Updates the new starting point
			checkpoints[startCheckpoints] = streetcarMovement[streetcarMovement.length-1];
			res = Action.newMoveAction(streetcarMovement, streetcarMovement.length, startingPoint);
		}
		
TraceDebugAutomate.debugTraveler(" Action "+res+"\n");
		return res;
	}

	
	private Point initializeTravel(Data currentConfig, Point[] terminusPosition, Point[] buildingsPosition) {
		Point startingPoint;
		// Initializes the itinerary with randomly chosen extremities
		Random r = new Random();
		startCheckpoints = 0;
		checkpoints = new Point[currentConfig.nbrBuildingInLine()+1];
		for(int x = 1; x < currentConfig.nbrBuildingInLine()+1; x++) {
			checkpoints[x] = buildingsPosition[x-1];
		}
		destinationTerminus = new Point[2];
		//i = r.nextInt(2); // Random first terminus TODO prendre en compte les differentes combinaisons de points de terminus
		if(r.nextInt(2) == 0) { // Random direction of travel
			startingPoint = terminusPosition[0]; //i]; TODO
		}
		else {
			startingPoint = terminusPosition[3]; //i+2]; TODO
		}
		checkpoints[0] = startingPoint;
		
		currentConfig.startMaidenTravel(name, startingPoint);
		destinationTerminus = currentConfig.getPlayerEndTerminus(name);
		previous = currentConfig.getPreviousTramPosition(name);
		currentConfig.stopMaidenTravel(name);
		return startingPoint;
	}

	
	
	/**
	 * Calculates the shortest path that goes through all the checkpoints 
	 * using only rail tracks already present on the board
	 * @param checkpoints
	 * @param data
	 * @return a list that concatenates the shortest paths from one checkpoint to another
	 */
	public LinkedList<Point> getShortestItinerary(Data data)
	{
		if(startCheckpoints >= data.nbrBuildingInLine()+1)
			 throw new RuntimeException("Traveler " + name + " is already at his destination");
		
		LinkedList<Point> result = new LinkedList<Point>(), path1, path2;
		Point origin, destination = null;
		int i = startCheckpoints;
		 
		destination = checkpoints[startCheckpoints];
		while(i < data.nbrBuildingInLine()) {
			origin = destination;
			destination = checkpoints[i+1];
			destination = data.isStopNextToBuilding(destination);
			if(destination == null)
				 throw new RuntimeException("This building " + destination + " has no stop");
			
//TODO Riyane: 
// Cette fonction prend mnt en parametre le point par lequel tu viens
// Fais bien attention a l'ordre de tes parametres
			path1 = data.getShortestPath(previous, origin, destination);
			if(path1==null)
				throw new RuntimeException("The traveler "+name+" is blocked");
			//	 Action stopMaidenTravel TODO
		 	result.addAll(path1);
		 	previous = path1.get(path1.size()-2); // point before last
			i++;
		 }
		 // We check both points of the destination terminus, and keep the closest one
		 path1 = data.getShortestPath(previous, destination, destinationTerminus[0]);
		 path2 = data.getShortestPath(previous, destination, destinationTerminus[1]);
		 if(path1==null ^ path2==null) {
			 if(path1==null)	result.addAll(path2);
			 else				result.addAll(path1);
		 }
		 else if(path1!=null && path2!=null) {
			 if(path1.size() < path2.size())	result.addAll(path1);
			 else								result.addAll(path2);
		 }
		 else 
			throw new RuntimeException("The traveler "+name+" is blocked");
			 //Action stopMaidenTravel TODO
		
		return result;
	}
}
