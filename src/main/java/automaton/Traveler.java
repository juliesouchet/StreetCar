package main.java.automaton;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;
import main.java.util.TraceDebugAutomate;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 */
public class Traveler extends PlayerAutomaton {
	/** Table that contains the objectives of the player : starting terminus and buildings to visit */
	Point[] checkpoints;
	/** Index of the first checkpoint to complete */
	int remainingCheckpoints;
	/** Table that contains the destination (both points of the ending terminus) */
	Point[]	destinationTerminus;
	
	public Traveler(String name) {
		super();
		if(name == null) this.name = "Traveler";
		else this.name = name;
	}
	
	@Override
	public Action makeChoice(Data currentConfig) {
		Action res = null;
		Random r = new Random();
		int i, j, k, nbEssais = 0;
		Point startingPoint = null;
		boolean trackCompleted = currentConfig.isTrackCompleted(getName());

TraceDebugAutomate.debugTraveler(" PlayerName : " + name +"\n");
TraceDebugAutomate.debugTraveler(" trackCompleted = " + trackCompleted +"\n");
		/*============
		 *	Building
		 =============*/
		if(!trackCompleted) {
			int handSize = currentConfig.getHandSize(name);
			Random rand = new Random();
			Tile t;
			
			if(handSize == 0) {
				TraceDebugAutomate.debugTraveler(" Empty hand\n");
				return null;
			}
			
			do {
				nbEssais++;
				if(nbEssais > 10000)	throw new RuntimeException(name + " cannot find a valid move");
				// On choisit un emplacement au hasard
				i = rand.nextInt(currentConfig.getWidth());
				j = rand.nextInt(currentConfig.getHeight());
				
				// On choisit une tuile parmi les 5 de notre main
				k = rand.nextInt(handSize);
				t = currentConfig.getHandTile(name, k);
				//On la fait tourner
				for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
					t.turnLeft();
				}
			} while(!currentConfig.isAcceptableTilePlacement(i, j, t));
			res = Action.newBuildSimpleAction(i, j, t);
		}
		
		
		/*==============
		 *	Traveling	
		 ===============*/
		else {
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
				// Initializes the itinerary with randomly chosen extremities
				remainingCheckpoints = 0;
				checkpoints = new Point[currentConfig.nbrBuildingInLine()+1];
				for(int x = 1; x < currentConfig.nbrBuildingInLine()+1; x++) {
					checkpoints[x] = buildingsPosition[x-1];
				}
				destinationTerminus = new Point[2];
				//i = r.nextInt(2); // Random first terminus TODO prendre en compte les differentes combinaisons de points de terminus
				if(r.nextInt(2) == 0) { // Random direction of travel
					startingPoint = terminusPosition[0]; //i]; TODO
					destinationTerminus[0] = terminusPosition[2];
					destinationTerminus[1] = terminusPosition[3];
				}
				else {
					destinationTerminus[0] = terminusPosition[0];
					destinationTerminus[1] = terminusPosition[1];
					startingPoint = terminusPosition[3]; //i+2]; TODO
				}
				checkpoints[0] = startingPoint;
				
				// TODO if the automaton has already played once this turn => start_trip_next_turn
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(currentConfig);
			
			// Go forward the maximum allowed number of squares
			ListIterator<Point> iterator = itinerary.listIterator();
			Point [] streetcarMovement = new Point[currentConfig.getMaximumSpeed()+1];
			i = 0;
			while(iterator.hasNext() && i < currentConfig.getMaximumSpeed()+1) {
				streetcarMovement[i] = iterator.next();
				i++;
			}
			// Updates the checkpoints : removes those passed by
			do {
				Point p = checkpoints[remainingCheckpoints];
				i = 0;
				while(i<streetcarMovement.length && streetcarMovement[i] != p)
					i++;
				if(i<streetcarMovement.length)
					remainingCheckpoints++;
			} while (i<streetcarMovement.length);
			// Updates the new starting point
			checkpoints[remainingCheckpoints] = streetcarMovement[streetcarMovement.length-1];
			res = Action.newMoveAction(streetcarMovement, streetcarMovement.length, startingPoint);
		}
		
TraceDebugAutomate.debugTraveler(" Action "+res+"\n");
		return res;
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
		if(remainingCheckpoints >= data.nbrBuildingInLine()+1)
			 throw new RuntimeException("Traveler " + name + " is already at his destination");
		
		LinkedList<Point> result = new LinkedList<Point>(), path1, path2;
		Point origin, destination = null;
		int i = remainingCheckpoints;
		 
		destination = checkpoints[remainingCheckpoints];
		 while(i < data.nbrBuildingInLine()) {
			 origin = destination;
			 destination = checkpoints[i+1];
			 destination = data.isStopNextToBuilding(destination);
			 if(destination == null)
				 throw new RuntimeException("This building " + destination + " has no stop");
			 path1 = data.getShortestPath(origin, destination);
			 if(path1==null)
				 throw new RuntimeException("No path from " + origin + " to " + destination);
			 result.addAll(path1);
			 i++;
		 }
		 // We check both points of the destination terminus, and keep the closest one
		 path1 = data.getShortestPath(destination, destinationTerminus[0]);
		 path2 = data.getShortestPath(destination, destinationTerminus[1]);
		 if(path1==null) {
			 if(path2==null)
				 throw new RuntimeException("No path from " + destination + " to end terminus " + destinationTerminus[0] + ", " + destinationTerminus[1]);
			 else
				 result.addAll(path2);
		 }
		 else {
			 if(path2==null || path1.size()<path2.size())
				 result.addAll(path1);
			 else
				 result.addAll(path2);
		 }
		
		return result;
	}
}
