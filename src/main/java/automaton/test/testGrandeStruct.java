package main.java.automaton.test;


public class testGrandeStruct {

	public static void main(String[] args) {
//		ArrayList<Integer> maStructure = new ArrayList<Integer>(30000000);
		int mon_max= 147483647;
		int[] monAutreStruct = new int[mon_max];
		int i;

//		System.out.println("pop");
//		for(i = 0; i < 30000000; i++){
//			maStructure.add(i);
//		}
//			System.out.println(maStructure.get(1));

			
		System.out.println("pop");
		for(i = 0; i < mon_max; i++){
			monAutreStruct[i]=i;
		}
			System.out.println(monAutreStruct[mon_max-1]);
			
			
			
			
	}

}
