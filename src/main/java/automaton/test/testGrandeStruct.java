package main.java.automaton.test;

import java.util.ArrayList;
import java.util.LinkedList;


public class testGrandeStruct {

	public static void main(String[] args) {
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