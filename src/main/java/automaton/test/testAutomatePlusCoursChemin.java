package main.java.automaton.test;

import java.awt.Point;
import java.util.ArrayList;

import main.java.automaton.AutomatePlusCourtChemin;
import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.ExceptionUnknownBoardName;

public class testAutomatePlusCoursChemin {

	public static void main(String[] args) {
	//	DecisionTable maDecisionTable = null;
		Data[] monTableauDeData = new Data[1];
		for(int i=0; i<1; i++){
			try {
				monTableauDeData[i]= new Data("jeu", "newOrleans5", 2);
				if(i%1000==0)System.out.println(i);
			} catch (ExceptionUnknownBoardName | RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Point>  myTerminus = new ArrayList<Point> (4);
		myTerminus.add(new Point(2,0));
		myTerminus.add(new Point(3,0));
		myTerminus.add(new Point(3,6));
		myTerminus.add(new Point(4,6));

		ArrayList<Point>   myStops = new  ArrayList<Point> (2);
		myStops.add(new Point(4,1));
		myStops.add(new Point(2,2));

		
		
		AutomatePlusCourtChemin myAutomate = new AutomatePlusCourtChemin(monTableauDeData[0], myTerminus, myStops);
//		myAutomate.computeHeuristic();
//		System.out.println(myAutomate);
		Tile maTile1 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile2 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile3 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile4 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile5 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile6 = Tile.parseTile("Tile_FFFFZZ2003");
		System.out.println("\n\t(1)==============================================\n");

		maTile1.setStop(true);
		monTableauDeData[0].setTile(3, 1, maTile1);
//		myAutomate.computeHeuristic();
//		myAutomate.printMatrice(myAutomate.bufferHeuristic);
//		System.out.println(myAutomate);
//		System.out.println("\n\t(2)===============================================\n");

		maTile2.setStop(true);
		monTableauDeData[0].setTile(3, 2, maTile2);
//		myAutomate.computeHeuristic();
//		myAutomate.printMatrice(myAutomate.bufferHeuristic);
//		System.out.println(myAutomate);
//		System.out.println("\n\t(3)===============================================\n");

		monTableauDeData[0].setTile(3,3, maTile3);
//		myAutomate.computeHeuristic();
//		myAutomate.printMatrice(myAutomate.bufferHeuristic);
//		System.out.println(myAutomate);
//		System.out.println("\n\t(4)===============================================\n");

		monTableauDeData[0].setTile(3,4, maTile4);
//		myAutomate.computeHeuristic();
//		myAutomate.printMatrice(myAutomate.bufferHeuristic);
//		System.out.println(myAutomate);
		monTableauDeData[0].setTile(3,4, maTile5);
		maTile6.turnHalf();
		monTableauDeData[0].setTile(3,6, maTile5);
		myAutomate.computeHeuristic();
		System.out.println(myAutomate);
		
		Point[] myPath = new Point[100];
		Tile[] myTilePath = new Tile[100];
		System.out.println("\n\t(7)===============================================\n");


//		myAutomate.computeHeuristic(arretsReduits);
		System.out.println(myAutomate);

		myAutomate.makeBestPath(myTerminus.get(1), myTerminus.get(2), myStops, myPath, myTilePath);
		System.out.println("\n\t(8)===============================================\n");

		

		
		
		
		
		
		
		
		
		
		
		
		
//		
//		
//		System.out.println(myAutomate.getBestTerminus());
//		if(myAutomate.myStopsAreSetted()[0]){
//			System.out.println("Stop[0]=setted");
//		}
//		
//		System.out.println(myAutomate.myStops[0]);
//		System.out.println(myAutomate.myStops[1]);
//		
//		if(myAutomate.myStopsAreSetted()[1]){
//			System.out.println("Stop[1]=setted");
//		}
//		maTile = Tile.parseTile("Tile_FFFFZZ2113");
//		maTile.setStop(true);
//		
//
//		myAutomate.myStops[0]= new Point(2,4);
//		monTableauDeData[0].setTile(2, 4, maTile);
//		if(myAutomate.myStopsAreSetted()[0]){
//			System.out.println("Stop[0]=setted");
//		}
//		if(myAutomate.myStopsAreSetted()[1]){
//			System.out.println("Stop[1]=setted");
//		}
//		
//		System.out.println(myAutomate.myStops[0]);
//		System.out.println(myAutomate.myStops[1]);

	}

}