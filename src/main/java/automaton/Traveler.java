package main.java.automaton;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import main.java.data.Action;
import main.java.data.Data;
import main.java.util.TraceDebugAutomate;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 */






public class Traveler extends PlayerAutomaton {
	/** Last position of the streetcar */
	Point lastPosition;
	/** Table that contains the destination (both points of the ending terminus) */
	Point[]	destinationTerminus;
	/** Automaton responsible of building the tracks */
	PlayerAutomaton slave; 
	
	public Traveler(String name) {
		super();
		if(name == null) this.name = "Traveler";
		else this.name = name;
		this.slave = new Worker(name);
		
TraceDebugAutomate.travelerTrace = true; // TODO à enlever
	}
	
	@Override
	public Action makeChoice(Data currentConfig) {
		Action res = null;
		int i;
		Point startingPoint = null;
		boolean trackCompleted = currentConfig.isTrackCompleted(getName());

TraceDebugAutomate.debugTraveler("\n PlayerName : " + name +"\n");
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
TraceDebugAutomate.debugTraveler(" AllowedSpeed : " + currentConfig.getMaximumSpeed() +"\n");

			if(!currentConfig.hasStartedMaidenTravel(getName())) {
				startingPoint = initializeTravel(currentConfig,	terminusPosition, buildingsPosition);
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = currentConfig.getCompletedTrack(name);

			
			// Find where the streetcar is currently
			ListIterator<Point> iterator = itinerary.listIterator();
			int start = 0;
			while(iterator.hasNext() && !iterator.next().equals(lastPosition)) start++;
			
			// Calculates the size of the path
			iterator = itinerary.listIterator(start);
			int pathSize = 0;
			while(iterator.hasNext() && pathSize < currentConfig.getMaximumSpeed()+1) {
				pathSize++;
				if(currentConfig.getTile(iterator.next()).isStop()) break;
			}
			Point [] streetcarMovement = new Point[pathSize];
			
TraceDebugAutomate.debugTraveler(" PathSize : "+ pathSize +"\n");

			// Prepares the array for return
TraceDebugAutomate.debugTraveler(" Construction path \n");
			i = 0;
			iterator = itinerary.listIterator(start);
			while(iterator.hasNext() && i < pathSize) {
				streetcarMovement[i] = iterator.next();
TraceDebugAutomate.debugTraveler("\t Ajoute " + streetcarMovement[i] + " au trajet\n");
				i++;
			}
			
			// Updates the new starting point
			lastPosition = streetcarMovement[streetcarMovement.length-1];
TraceDebugAutomate.debugTraveler(" Maj de la position : "+ lastPosition +"\n");
			
			
			res = Action.newMoveAction(streetcarMovement, streetcarMovement.length, startingPoint);
		}
		
TraceDebugAutomate.debugTraveler(" Action "+res+"\n");
		return res;
	}

	
	private Point initializeTravel(Data currentConfig, Point[] terminusPosition, Point[] buildingsPosition) {
		destinationTerminus = new Point[2];
		lastPosition = terminusPosition[0];
		
		currentConfig.startMaidenTravel(name, lastPosition);
		destinationTerminus = currentConfig.getPlayerEndTerminus(name);
		currentConfig.stopMaidenTravel(name);
		
		
		return lastPosition;
	}

	
}
