package main.java.ia;

import java.awt.Point;
import java.util.Comparator;
import java.util.LinkedList;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;

/**
 * Acts like the Dumbest one while building, but checks if its objectives are completed. 
 * Then it puts its streetcar in one of its terminus and travels to the other at maximum speed.
 * @author souchet julie
 */
public class Traveler extends PlayerAutomaton {
	LinkedList<Point> checkpoints;
		
	@Override
	public Action makeChoice(Hand hand, Data currentConfig) {
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
				// initializes the itinerary with randomly chosen direction
				Random r = new Random();
				checkpoints = currentConfig.getStops(this);
				if(r.nextInt() == 0) {
					checkpoints.addFirst(currentConfig.firstTerminus(this));
					checkpoints.add(currentConfig.secondTerminus(this));
				}
				else {
					checkpoints.addFirst(currentConfig.secondTerminus(this));
					checkpoints.add(currentConfig.firstTerminus(this));
				}
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(checkpoints, currentConfig);
			
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

	
	@SuppressWarnings("unused")
	/**
	 * Calculates the shortest path that goes through all the checkpoints 
	 * using only tracks already present on the board
	 * @param checkpoints
	 * @param data
	 * @return
	 */
	private LinkedList<Point> getShortestItinerary(LinkedList<Point> checkpoints, Data data) {
		/* TODO : ajouté les passages par les arrêts
		 int[][] distance;
		 int width, height, arcWeight = 1;
		 PriorityQueue<WeightedPoint> queue;
		 Point origin, destination, u, v;
		 WeightedPoint wp;
		 HashMap<Point,Point> previous; // previous.get(p) = the point before p in the final path
		 LinkedList<Point> ;
		 
		 width = data.getWidth();
		 height = data.getHeight;
		 distance = int[width][height];
		 for (int x = 0; x < width; x++) {
		 	for (int y = 0; y < height; y++) {
		 		distance = Integer.MAX_VALUE;
		 	}
		 }
		 origin = checkpoints.pollFirst();
		 destination = checkpoints.pollLast();
		 queue = new PriorityQueue<WeightedPoint>(4, new WeightComparator());
		 queue.add(new WeightedPoint(origin,0));
		 distance[origin.x][origin.y] = 0;
		 previous = new HashMap<Point,Point>();
		 
		 while(!queue.isEmpty()) {
		 	wp = queue.poll();
		 	if(wp.sameCoordinates(destination)) { 
		 		// solution found => we build the itinerary starting from the end
		 		result = new LinkedList<Point>();
		 		result.add(destination);
		 		u = destination;
		 		while(!u.equals(origin)) {
		 			v = previous.get(u);
		 			result.addFirst(v);
		 		}
		 		result.addFirst(u);
		 	}
		 	foreach (v : wp.getAccessibleNeighboursCoordinates(wp.x,wp.y)) {
		 		if(distance[wp.x][wp.y] + arcWeight < distance[v.x][v.y]) {
		 			distance[v.x][v.y] = distance[wp.x][wp.y] + arcWeight;
		 			queue.add(new WeightedPoint(v,distance[v.x][v.y] + heuristic(v, destination)));
		 			previous.put(v,wp);
		 		}
		 	}
		 }
		 
		 
		 */// no solution
		return null;		
	}
	
	@SuppressWarnings("unused")
	private int heuristic(Point p, Point dest) {
		// TODO : utiliser les distances de manhattan
		return 0;
	}
	
	
	@SuppressWarnings("unused")
	private class WeightedPoint extends Point {
		private static final long serialVersionUID = -8325050887533486905L;
		int weight;
		
		// Builders
		public WeightedPoint(int x, int y) {
			super(x,y);
			weight = Integer.MAX_VALUE;
		}
		public WeightedPoint(Point p) {
			super(p);
			weight = Integer.MAX_VALUE;
		}
		public WeightedPoint(int x, int y, int w) {
			super(x,y);
			weight = w;
		}
		public WeightedPoint(Point p, int w) {
			super(p);
			weight = w;
		}
		// Getter & Setter
		public int getWeigth() {
			return weight;
		}
		public void setWeigth(int w) {
			weight = w;
		}
		// Other useful methods
		public boolean sameCoordinates(Point p) {
			return p.x == this.x && p.y == this.y;
		}
		public boolean equals(Object o) {
			return super.equals(o) && this.weight == ((WeightedPoint) o).weight;
		}
		public String toString() {
			return "("+this.x+","+this.y+" ;"+this.weight+")";
		}
	}
	
	@SuppressWarnings("unused")
	private class WeightComparator implements Comparator<WeightedPoint> {
		@Override
		public int compare(WeightedPoint p1, WeightedPoint p2) {
			return Integer.compare(p1.getWeigth(), p2.getWeigth());
		}
	}
}
