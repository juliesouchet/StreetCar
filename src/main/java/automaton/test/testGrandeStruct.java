package main.java.automaton.test;

import java.util.ArrayList;
import java.util.LinkedList;


public class testGrandeStruct {

	public static void main(String[] args) {
		

		
		int[][] ma_matrice_dentiers = new int[3000000][5000];
		
		System.out.println("pop");
		//Trop gros!
		for(int i = 0; i<30000000;i++){
			if(i % 100000 ==0){
				System.out.println("i="+i);
			}
			for(int j = 0; j< 5000;j++){
				ma_matrice_dentiers[i][j]=j;
			}
		}
		System.out.println("pop");
		
		int mon_max= 10000000;
		ArrayList<Integer> maStructureArrayList = new ArrayList<Integer>(10);
		LinkedList<Integer> maStructureLinkedList = new LinkedList<Integer>();
		int[] maStructureTableau = new int[mon_max];
		int i;
		long tmps1;
		long tmps2;
		long tmps3;
		long tmps4;
		long tmpsMaStructureTableau;
		long tmpsMaStructureLinkedList;
		long tmpsMaStructureArrayList;
		
		tmps1 = System.nanoTime();
		System.out.println("pop");
		for(i = 0; i < mon_max; i++){
			maStructureTableau[i]=i;
		}
		tmps2 = System.nanoTime();
		tmpsMaStructureTableau = (tmps2-tmps1);
		System.out.println(maStructureTableau[mon_max-1]);

		
		System.out.println("pop");
		for(i = 0; i < mon_max; i++){
			maStructureLinkedList.add(i);
		}
		tmps3 = System.nanoTime();
		tmpsMaStructureLinkedList = (tmps3-tmps2);
		System.out.println(maStructureLinkedList.get(mon_max-1));
	
		
		System.out.println("pop");
		for(i = 0; i < mon_max; i++){
			maStructureArrayList.add(i);
		}
		tmps4 = System.nanoTime();
		tmpsMaStructureArrayList = (tmps4-tmps3);

		System.out.println(maStructureArrayList.get(mon_max-1));

		
		System.out.println("maStructureTableau a mis:"+tmpsMaStructureTableau);
		System.out.println("maStructureLinkedList a mis:"+tmpsMaStructureLinkedList);
		System.out.println("maStructureArrayList a mis:"+tmpsMaStructureArrayList);

		System.out.println("maStructureTableau est "+(tmpsMaStructureLinkedList/tmpsMaStructureTableau)+" X plus rapide que maStructureLinkedList" );
		System.out.println("maStructureTableau est "+(tmpsMaStructureArrayList/tmpsMaStructureTableau)+" X plus rapide que tmpsMaStructureArrayList" );
		System.out.println("tmpsMaStructureArrayList est "+(tmpsMaStructureLinkedList/tmpsMaStructureArrayList)+" X plus rapide que tmpsMaStructureLinkedList" );

	}

}
