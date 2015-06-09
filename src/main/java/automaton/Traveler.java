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
	Point[]	destinationTerminus, buildingsPosition;
	/** Automaton responsible of building the tracks */
	PlayerAutomaton slave; 
	
	public Traveler(String name) {
		super();
		if(name == null) this.name = "Traveler";
		else this.name = name;
		this.slave = new Worker(name);
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
			if(!currentConfig.hasStartedMaidenTravel(getName())) {
				startingPoint = initializeTravel(currentConfig);
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = currentConfig.getCompletedTrack(name);
String str = "Complete itinerary : \n\t";
ListIterator<Point> iterator = itinerary.listIterator();
while(iterator.hasNext())	str += iterator.next() + ",\n\t";
TraceDebugAutomate.debugTraveler(str+"\n");			

			// Find where the streetcar is currently
TraceDebugAutomate.debugTraveler(" Searching the current position... \n");
			iterator = itinerary.listIterator();
			int start = 0;
			while(iterator.hasNext() && !iterator.next().equals(lastPosition)) start++;
			if(start >= itinerary.size())	{System.out.println(name + " is at the end");return null;}
TraceDebugAutomate.debugTraveler("\t\t"+(itinerary.get(start).equals(lastPosition)?"OK":"Error : "+lastPosition+" != "+itinerary.get(start))+"\n");

			// Calculates the size of the path
TraceDebugAutomate.debugTraveler(" Calculating the path size... \n");
			iterator = itinerary.listIterator(start);
			int pathSize = 0;
			while(iterator.hasNext() && pathSize < currentConfig.getMaximumSpeed()+1) {
				pathSize++;
				if(currentConfig.getTile(iterator.next()).isStop()) {
					TraceDebugAutomate.debugTraveler("\tInterruption Stop : "+itinerary.get(start+pathSize)+"\n");
					break;
				}
			}
			if(pathSize >= currentConfig.getMaximumSpeed()+1)	TraceDebugAutomate.debugTraveler("\tMax size\n");
			else if(pathSize==1)	pathSize++;
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

	
	private Point initializeTravel(Data currentConfig) {
		destinationTerminus = currentConfig.getPlayerTerminusPosition(getName());
		buildingsPosition = currentConfig.getPlayerAimBuildings(getName());
		
String termPosString = destinationTerminus[0]+","+destinationTerminus[1]+","+destinationTerminus[2]+","+destinationTerminus[3],
		buildPosString = buildingsPosition[0].toString();
for(int x = 1; x<currentConfig.nbrBuildingInLine(); x++) {buildPosString+=","+buildingsPosition[x];}
TraceDebugAutomate.debugTraveler(" Initialization\n");
TraceDebugAutomate.debugTraveler(" \tPlayerLine : " + currentConfig.getPlayerLine(getName()) +"\n");
TraceDebugAutomate.debugTraveler(" \tPlayerTerminus : "+termPosString +"\n");
TraceDebugAutomate.debugTraveler(" \tNbrBuildings : "+currentConfig.nbrBuildingInLine()+"\n");
TraceDebugAutomate.debugTraveler(" \tPlayerBuildings : "+buildPosString +"\n");
TraceDebugAutomate.debugTraveler(" \tAllowedSpeed : " + currentConfig.getMaximumSpeed() +"\n");

		lastPosition = destinationTerminus[0];
		currentConfig.startMaidenTravel(name, lastPosition);
		destinationTerminus = currentConfig.getPlayerEndTerminus(name);
		currentConfig.stopMaidenTravel(name);
		return lastPosition;
	}

	
}
