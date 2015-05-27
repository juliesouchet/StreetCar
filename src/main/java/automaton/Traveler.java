package main.java.automaton;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
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
	
	public Traveler() {
		super();
		setName("Traveler");
	}
		
	@Override
	public Action makeChoice(Hand hand, Data currentConfig) {
		Action res = null;
		Random rand = new Random();
		Point p;
		Tile t;
		int i,j, k;
		//
		if(!currentConfig.isTrackCompleted(getName())) {
			// Random tile and position choice for construction (extracted from Dumbest)
			
			do{
				// random position choice
				i = rand.nextInt(currentConfig.getWidth());
				j = rand.nextInt(currentConfig.getHeight());
				p = new Point(i,j);
				
				// random tile choice in the player's hand
				k = rand.nextInt(hand.size());
				t = hand.get(k);
			}while( !currentConfig.isAcceptableTilePlacement(i, j, t));
			
			res = Action.newBuildSimpleAction(p, t);
		}
		/**/
		
		
		// Transition to travel
		
		else {
			if(!currentConfig.isMoving(getName())) {
				if(currentConfig.hasDoneFirstAction(getName())) {
					// ends current turn and starts traveling next turn
					return Action.newStartTripNextTurnAction();
				}
				// initializes the itinerary with randomly chosen direction
				Random r = new Random();
				checkpoints = currentConfig.getBuildings(getName());
				LinkedList<Point> terminus = currentConfig.getTerminus(getName());
				if(r.nextInt() == 0) {
					checkpoints.addFirst(terminus.getFirst());
					checkpoints.add(terminus.getLast());
				}
				else {
					checkpoints.addFirst(terminus.getLast());
					checkpoints.add(terminus.getFirst());
				}
			}
			
			// Calculates the shortest itinerary
			LinkedList<Point> itinerary = getShortestItinerary(checkpoints, currentConfig);
			
			// Advances the maximum allowed number of squares
			ListIterator<Point> iterator = itinerary.listIterator();
			LinkedList<Point> streetcarMovement = new LinkedList<Point>();
			i = 0;
			while(iterator.hasNext() && i < currentConfig.getMaximumSpeed()) {
				streetcarMovement.add(iterator.next());
				i++;
			}
			checkpoints.removeFirst();
			checkpoints.addFirst(streetcarMovement.getLast());
			res = Action.newMoveAction(streetcarMovement);
		}
		
		
		return res;
	}

	
	
	/**
	 * Calculates the shortest path that goes through all the checkpoints 
	 * using only tracks already present on the board
	 * @param checkpoints
	 * @param data
	 * @return
	 */
	public LinkedList<Point> getShortestItinerary(LinkedList<Point> checkpoints, Data data)
	{
		// TODO : ajouter les passages par les arrets
		 int[][] distance;
		 int width, height, arcWeight = 1;
		 PriorityQueue<WeightedPoint> queue;
		 Point origin, destination, u;
		 WeightedPoint wp;
		 HashMap<Point,Point> previous; // previous.get(p) = the point before p in the final path
		 LinkedList<Point> result;
		 
		 width = data.getWidth();
		 height = data.getHeight();
		 distance = new int[width][height];
		 for (int x = 0; x < width; x++) {
		 	for (int y = 0; y < height; y++) {
		 		distance[x][y] = Integer.MAX_VALUE;
		 	}
		 }
		 origin = checkpoints.getFirst();
		 destination = checkpoints.getLast();
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
		 			Point v = previous.get(u);
		 			result.addFirst(v);
		 		}
		 		result.addFirst(u);
		 	}
		 	for (Point v : data.getConnectedNeighborPositions(wp.x,wp.y)) {
		 		if(distance[wp.x][wp.y] + arcWeight < distance[v.x][v.y]) {
		 			distance[v.x][v.y] = distance[wp.x][wp.y] + arcWeight;
		 			queue.add(new WeightedPoint(v,distance[v.x][v.y] + heuristic(v, destination)));
		 			previous.put(v,wp);
		 		}
		 	}
		 }
		 
		 
		 // no solution
		return null;		
	}
	
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
	
	private class WeightComparator implements Comparator<WeightedPoint> {
		@Override
		public int compare(WeightedPoint p1, WeightedPoint p2) {
			return Integer.compare(p1.getWeigth(), p2.getWeigth());
		}
	}
}
