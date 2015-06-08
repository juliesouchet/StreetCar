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
		myTerminus[0]=new Point(1,0);
		myTerminus[1]=new Point(2,0);
		myTerminus[2]=new Point(3,6);
		myTerminus[3]=new Point(4,6);

		Point[] myStops = new Point[2];
		myStops[0] = new Point(1,4);
		myStops[1] = new Point(5,2);
		monTableauDeData[0].setTile(1, 4, Tile.parseTile("Tile_FTFFAZ01"));

		
		
		AutomatePlusCourtChemin myAutomate = new AutomatePlusCourtChemin(monTableauDeData[0], myTerminus, myStops);
		myAutomate.computeHeuristic();
		
		
		System.out.println(myAutomate);
		
		System.out.println(myAutomate.getBestTerminus());
		if(myAutomate.myStopsAreSetted()[0]){
			System.out.println("Stop[0]=setted");
		}
		
		System.out.println(myAutomate.myStops[0]);
		System.out.println(myAutomate.myStops[1]);
		
		if(myAutomate.myStopsAreSetted()[1]){
			System.out.println("Stop[1]=setted");
		}
		Tile maTile = Tile.parseTile("Tile_FFFFZZ2113");
		maTile.setStop(true);
		

		//myAutomate.myStops[0]= new Point(2,4);
		monTableauDeData[0].setTile(2, 4, maTile);
		if(myAutomate.myStopsAreSetted()[0]){
			System.out.println("Stop[0]=setted");
		}
		if(myAutomate.myStopsAreSetted()[1]){
			System.out.println("Stop[1]=setted");
		}
		
		System.out.println(myAutomate.myStops[0]);
		System.out.println(myAutomate.myStops[1]);

	}

}
