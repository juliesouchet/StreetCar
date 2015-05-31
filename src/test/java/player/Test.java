package test.java.player;

import main.java.util.Direction;

public class Test {

	public static void main(String[] args)
	{

		Direction dir = Direction.WEST;
		Direction dir1= dir;

		System.out.println(dir == dir1);
		dir = Direction.EAST;
		System.out.println(dir == dir1);
		dir = Direction.WEST;
		System.out.println(dir == dir1);
		
		
		
/*
		if(dir.equals(Direction.WEST)){
			System.out.println(dir);	
		}
		
		dir = Direction.EAST;
		if(dir.equals(Direction.WEST)){
			System.out.println("WEST");
			System.out.println(dir);	
		}else if (dir.equals(Direction.EAST)){
			System.out.println("EAST");
			System.out.println(dir);
		}
*/	}

}
