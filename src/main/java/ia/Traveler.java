package main.java.ia;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Data;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 * @author souchet julie
 */
public class Traveler implements PlayerAutomata {
	Point start, end;
		
	@Override
	public Action makeChoice(Data currentConfig) {
		Action res = null;
		// TODO Building
		// if(!currentConfig.objectivesCompleted(this)) {}
		
		// Transition to travel
		/*
		else {
			if(currentConfig.isContructing(this)) {
				if(currentConfig.hasDoneFirstAction(this)) {
					// ends current turn and starts traveling next turn
					return newStartTripNextTurnAction();
				}
				// initializes the itinerary
				Random r = new Random();
				int whereToStart = r.nextInt();
				if(whereToStart == 0) {
					start = currentConfig.firstTerminus(this);
					end = currentConfig.secondTerminus(this);
				}
				else {
					start = currentConfig.secondTerminus(this);
					end = currentConfig.firstTerminus(this);
				}
			}
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getBestPath(start, end);
			// Advances
			ListIterator<Point> iterator = itinerary.listIterator();
			LinkedList<Point> streetcarMovement = new LinkedList<Point>();
			int i = 0;
			while(iterator.hasNext() && i < currentConfig.maximumSpeed()) {
				streetcarMovement.add(iterator.next());
				i++;
			}
			start = streetcarMovement.getLast();
			return newMoveAction(streetcarMovement);
		}
		
		*/
		return res;
	}

	/*
	 * private LinkedList<Point> getBestPath() {}
	 */
	
}
