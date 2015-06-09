package main.java.automaton.test;

import java.awt.Point;

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
		
		Point[] myTerminus = new Point[4];
		myTerminus[0]=new Point(2,0);
		myTerminus[1]=new Point(3,0);
		myTerminus[2]=new Point(3,6);
		myTerminus[3]=new Point(4,6);

		Point[] myStops = new Point[2];
		myStops[0] = new Point(4,1);
		myStops[1] = new Point(2,2);

		
		
		AutomatePlusCourtChemin myAutomate = new AutomatePlusCourtChemin(monTableauDeData[0], myTerminus, myStops);
		myAutomate.computeHeuristic();
		System.out.println(myAutomate);
		Tile maTile1 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile2 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile3 = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTile4 = Tile.parseTile("Tile_FFFFZZ2113");
		System.out.println("\n\t(1)==============================================\n");

		monTableauDeData[0].setTile(3, 1, maTile1);
		myAutomate.computeHeuristic();
		myAutomate.printMatrice(myAutomate.bufferHeuristic);
		System.out.println(myAutomate);
		System.out.println("\n\t(2)===============================================\n");

		monTableauDeData[0].setTile(3, 2, maTile2);
		myAutomate.computeHeuristic();
		myAutomate.printMatrice(myAutomate.bufferHeuristic);
		System.out.println(myAutomate);
		System.out.println("\n\t(3)===============================================\n");

		monTableauDeData[0].setTile(3,3, maTile3);
		myAutomate.computeHeuristic();
		myAutomate.printMatrice(myAutomate.bufferHeuristic);
		System.out.println(myAutomate);

		monTableauDeData[0].setTile(4,3, maTile3);
		myAutomate.computeHeuristic();
		myAutomate.printMatrice(myAutomate.bufferHeuristic);
		System.out.println(myAutomate);
		
		monTableauDeData[0].setTile(5,3, maTile3);
		myAutomate.computeHeuristic();
		System.out.println(myAutomate);
		
		System.out.println("\n\t(4)===============================================\n");

		

		
		
		
		
		
		
		
		
		
		
		
		
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