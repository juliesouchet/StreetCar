package main.java.automaton;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 */
public class Traveler extends PlayerAutomaton {
	LinkedList<Point> checkpoints;
	
	public Traveler(String name) {
		super();
		if(name == null) this.name = "Traveler";
		else this.name = name;
	}
	@Override
	public Action makeChoice(Data currentConfig) {
		Action res = null;
		Random r = new Random();
		int i, j;
boolean trackCompleted = currentConfig.isTrackCompleted(getName());
/*
System.out.println("PlayerLine     : " + currentConfig.getPlayerLine(getName()));
System.out.println("PlayerTerminus : " + currentConfig.getPlayerTerminusPosition(getName()));
System.out.println("PlayerBuildings: " + currentConfig.getPlayerAimBuildings(getName()));
System.out.println("trackCompleted = " + trackCompleted);
*/		/*============
		 *	Building
		 =============*/
		if(!trackCompleted) {
			res = new Dumbest(name).makeChoice(currentConfig);
		}
		
		
		/*==============
		 *	Traveling	
		 ===============*/
		else {
			Point startingPoint = null;
			if(!currentConfig.hasStartedMaidenTravel(getName())) {
				// Initializes the itinerary with randomly chosen extremities				
				checkpoints = new LinkedList<Point> (Arrays.asList(currentConfig.getPlayerAimBuildings(getName())));
				LinkedList<Point> allTermini =  new LinkedList<Point> (Arrays.asList(currentConfig.getPlayerTerminusPosition(getName()))),
									destinationTerminus = new LinkedList<Point>();
				i = r.nextInt(2); // Random first terminus
				j = r.nextInt(2) + 2; // Random second terminus
				if(r.nextInt(2) == 0) { // Random direction of travel
					checkpoints.addFirst(allTermini.get(i));
					checkpoints.addLast(allTermini.get(j));
					destinationTerminus.add(allTermini.get(2));
					destinationTerminus.add(allTermini.getLast());
				}
				else {
					checkpoints.addFirst(allTermini.get(j));
					checkpoints.addLast(allTermini.get(i));
					destinationTerminus.add(allTermini.getFirst());
					destinationTerminus.add(allTermini.get(1));
				}
		// TODO setDestinationTerminus se fait dans l'action
//TODO riyane				currentConfig.setDestinationTerminus(name, (Point[])destinationTerminus.toArray());
				startingPoint = checkpoints.getFirst();
				
				if(currentConfig.hasDoneRoundFirstAction(getName())) {
					// ends current turn and starts traveling next turn
					//return Action.newStartTripNextTurnAction(); TODO Action.BUILD_AND_START_TRIP_NEXT_TURN
				}
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(checkpoints, currentConfig);
			
			// Go forward the maximum allowed number of squares
			ListIterator<Point> iterator = itinerary.listIterator();
			Point [] streetcarMovement = new Point[currentConfig.getMaximumSpeed()];
			i = 0;
			while(iterator.hasNext() && i < currentConfig.getMaximumSpeed()) {
				streetcarMovement[i] = iterator.next();
				i++;
			}
			// Updates the checkpoints : removes those passed by
			do {
				Point p = checkpoints.getFirst();
				i = 0;
				while(i<streetcarMovement.length && streetcarMovement[i] != p)
					i++;
				if(i<streetcarMovement.length)
					checkpoints.removeFirst();
			} while (i<streetcarMovement.length);
			// Updates the new starting point
			checkpoints.addFirst(streetcarMovement[streetcarMovement.length-1]);
			res = Action.newMoveAction(streetcarMovement, streetcarMovement.length, startingPoint);
		}
		
		
		return res;
	}

	
	
	/**
	 * Calculates the shortest path that goes through all the checkpoints 
	 * using only rail tracks already present on the board
	 * @param checkpoints
	 * @param data
	 * @return a list that concatenates the shortest paths from one checkpoint to another
	 */
	public LinkedList<Point> getShortestItinerary(LinkedList<Point> checkpoints, Data data)
	{
		 LinkedList<Point> result = new LinkedList<Point>();
		 Point origin, destination;
		 ListIterator<Point> iterator = checkpoints.listIterator();
		if(!iterator.hasNext())
			 throw new RuntimeException("Traveler " + name + " is already at his destination");
		 
		 destination = iterator.next();
		 while(iterator.hasNext()) {
			 origin = new Point(destination);
			 destination = iterator.next();
			 
			 result.addAll(data.getShortestPath(origin, destination));
		 }
		
		return result;
	}
}
