package main.java.automaton.test;

import java.awt.Point;

import main.java.automaton.AutomatePlusCourtChemin;
import main.java.data.Data;
import main.java.data.Tile;
import main.java.game.ExceptionUnknownBoardName;



public class testGrandeStruct {

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
		
		//System.out.println(myAutomate.getBestTerminus());
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
//		if(myAutomate.myStopsAreSetted()[2]){
//			System.out.println("Stop[2]=setted");
//		}
//		
//		int compteur = 0;
//		int nombreActions1 = 0;
//		int nombreActions = 0;
//
//for(nombreActions1=4;nombreActions1<1000;nombreActions1+=10){
//		for (int profondeur = 0; profondeur <= 10; profondeur++){
//			nombreActions = nombreActions1;
//			for(int i = 0; i<profondeur;i++){
//				compteur += nombreActions+((nombreActions-2)*nombreActions);
//				nombreActions = nombreActions-2;
//				if(nombreActions==0) break;
//			}
//			System.out.println("nombreActions="+nombreActions+" profondeur="+profondeur+" compteur="+compteur);
//			compteur = 0;
//			if(nombreActions==0) break;
//
//		}
//		System.out.println();
//}

		//		int[][][] ma_matrice_dentiers = new int[6000][6000][17];
		//		
		//		System.out.println("pop");
		//		for(int i = 0; i<6000;i++){
		//			if(i % 1000 ==0){
		//				System.out.println("i="+i);
		//			}
		//			for(int j = 0; j< 6000;j++){
		//				for(int k = 0; k<10; k++){
		//					ma_matrice_dentiers[i][j][k]=j;
		//				}
		//			}
		//		}
		//		System.out.println("pop");
		//		
		//		int mon_max= 10000000;
		//		ArrayList<Integer> maStructureArrayList = new ArrayList<Integer>(10);
		//		LinkedList<Integer> maStructureLinkedList = new LinkedList<Integer>();
		//		int[] maStructureTableau = new int[mon_max];
		//		int i;
		//		long tmps1;
		//		long tmps2;
		//		long tmps3;
		//		long tmps4;
		//		long tmpsMaStructureTableau;
		//		long tmpsMaStructureLinkedList;
		//		long tmpsMaStructureArrayList;
		//		
		//		tmps1 = System.nanoTime();
		//		System.out.println("pop");
		//		for(i = 0; i < mon_max; i++){
		//			maStructureTableau[i]=i;
		//		}
		//		tmps2 = System.nanoTime();
		//		tmpsMaStructureTableau = (tmps2-tmps1);
		//		System.out.println(maStructureTableau[mon_max-1]);
		//
		//		
		//		System.out.println("pop");
		//		for(i = 0; i < mon_max; i++){
		//			maStructureLinkedList.add(i);
		//		}
		//		tmps3 = System.nanoTime();
		//		tmpsMaStructureLinkedList = (tmps3-tmps2);
		//		System.out.println(maStructureLinkedList.get(mon_max-1));
		//	
		//		
		//		System.out.println("pop");
		//		for(i = 0; i < mon_max; i++){
		//			maStructureArrayList.add(i);
		//		}
		//		tmps4 = System.nanoTime();
		//		tmpsMaStructureArrayList = (tmps4-tmps3);
		//
		//		System.out.println(maStructureArrayList.get(mon_max-1));
		//
		//		
		//		System.out.println("maStructureTableau a mis:"+tmpsMaStructureTableau);
		//		System.out.println("maStructureLinkedList a mis:"+tmpsMaStructureLinkedList);
		//		System.out.println("maStructureArrayList a mis:"+tmpsMaStructureArrayList);
		//
		//		System.out.println("maStructureTableau est "+(tmpsMaStructureLinkedList/tmpsMaStructureTableau)+" X plus rapide que maStructureLinkedList" );
		//		System.out.println("maStructureTableau est "+(tmpsMaStructureArrayList/tmpsMaStructureTableau)+" X plus rapide que tmpsMaStructureArrayList" );
		//		System.out.println("tmpsMaStructureArrayList est "+(tmpsMaStructureLinkedList/tmpsMaStructureArrayList)+" X plus rapide que tmpsMaStructureLinkedList" );

	}

}
