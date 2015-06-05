package main.java.automaton.test;

import main.java.automaton.DecisionTable;



public class testGrandeStruct {

	public static void main(String[] args) {
		DecisionTable maTaDecisionTable = null;

		int compteur = 0;
		int nombreActions1 = 0;
		int nombreActions = 0;

for(nombreActions1=4;nombreActions1<1000;nombreActions1+=10){
		for (int profondeur = 0; profondeur <= 10; profondeur++){
			nombreActions = nombreActions1;
			for(int i = 0; i<profondeur;i++){
				compteur += nombreActions+((nombreActions-2)*nombreActions);
				nombreActions = nombreActions-2;
				if(nombreActions==0) break;
			}
			System.out.println("nombreActions="+nombreActions+" profondeur="+profondeur+" compteur="+compteur);
			compteur = 0;
			if(nombreActions==0) break;

		}
		System.out.println();
}

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
