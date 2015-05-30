package main.java.automaton;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;
import main.java.data.Tile;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 * @author souchet julie
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
		Tile t;
		int i, j, k;
		
		/*============
		 *	Building
		 =============*/
		if(!currentConfig.isTrackCompleted(getName())) {
			// Random tile and position choice for construction (extracted from Dumbest)
			Hand hand = currentConfig.getHand(name);
			do{
				// random position choice
				i = r.nextInt(currentConfig.getWidth());
				j = r.nextInt(currentConfig.getHeight());
				
				// random tile choice in the player's hand
				k = r.nextInt(hand.getSize());
				t = hand.get(k);
				hand.add(t);
				
				// random rotation
				for(int rotation = 0; rotation < r.nextInt(4); rotation++) {
					t.turnLeft();
				}
			}while( !currentConfig.isAcceptableTilePlacement(i, j, t));
			
			res = Action.newBuildSimpleAction(i, j, t);
		}
		/**/
		
		
		/*==============
		 *	Traveling	
		 ===============*/
		else {
			if(!currentConfig.hasStartedMaidenTravel(getName())) {
				// Initializes the itinerary with randomly chosen extremities				
				checkpoints = currentConfig.getBuildings(getName());
				LinkedList<Point> allTermini = currentConfig.getTerminiPoints(getName()),
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
				// TODO setDestinationTerminus doit être fait au niveau du moteur
				currentConfig.setDestinationTerminus(name, destinationTerminus);
				
				
				if(currentConfig.hasDoneFirstAction(getName())) {
					// ends current turn and starts traveling next turn
					return Action.newStartTripNextTurnAction();
				}
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(checkpoints, currentConfig);
			
			// Go forward the maximum allowed number of squares
			ListIterator<Point> iterator = itinerary.listIterator();
			LinkedList<Point> streetcarMovement = new LinkedList<Point>();
			i = 0;
			while(iterator.hasNext() && i < currentConfig.getMaximumSpeed()) {
				streetcarMovement.add(iterator.next());
				i++;
			}
			// Updates the checkpoints : removes those passed by
			while(streetcarMovement.contains(checkpoints.getFirst()))
					checkpoints.removeFirst();
					
			checkpoints.addFirst(streetcarMovement.getLast());
			res = Action.newMoveAction(streetcarMovement);
		}
		
		
		return res;
	}

	
	
	/**
	 * Calculates the shortest path that goes through all the checkpoints 
	 * using only rail tracks already present on the board
	 * @param checkpoints
	 * @param data
	 * @return
	 */
	public LinkedList<Point> getShortestItinerary(LinkedList<Point> checkpoints, Data data)
	{
		 LinkedList<Point> result = new LinkedList<Point>();
		 Point origin, destination;
		 ListIterator<Point> iterator = checkpoints.listIterator();
		 if(!iterator.hasNext())
			 throw new RuntimeException("No more checkpoints");
		 
		 destination = iterator.next();
		 while(iterator.hasNext()) {
			 origin = new Point(destination);
			 destination = iterator.next();
			 
			 result.addAll(data.getShortestPath(origin, destination));
		 }
		
		return result;
	}
}
