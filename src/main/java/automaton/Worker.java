package main.java.automaton;

import java.awt.Point;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;

public class Worker extends PlayerAutomaton {

	private int width, height, nbrObjectives, min, max, limit;
	private Point[]	objectives = null;
	private int[][] heuristic;
	
	public Worker(String playerName) {
		super();
		if(playerName == null) this.name = "Worker";
		else this.name = playerName;
	}

	@Override
	public Action makeChoice(Data data) {
		Action res = null;
		width = data.getWidth();
		height = data.getHeight();
		int nbrBuildings = data.nbrBuildingInLine();
		nbrObjectives = nbrBuildings + 2;
		Random rand = new Random();
		
		/*==================
		 *	Initialization
		 ==================*/
		if(objectives==null) {
			Point[] buildings, terminus;
			buildings = data.getPlayerAimBuildings(name);
			terminus = data.getPlayerTerminusPosition(name);
			objectives = new Point[nbrObjectives];
			for(int i = 0; i < nbrBuildings; i++)	objectives[i+1] = buildings[i];
			if(rand.nextInt(2) == 0) {
				objectives[0] = terminus[0];
				objectives[nbrObjectives-1] = terminus[3];
			}
			else {
				objectives[0] = terminus[3];
				objectives[nbrObjectives-1] = terminus[0];
			}
			heuristic = new int[width][height];
			
			for(int i = 0; i < nbrObjectives; i++) {
				Point p = objectives[i];
				computeThisHeuristic(p, heuristic);
			}
			min = Integer.MAX_VALUE;
			max = 0;
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					if(heuristic[i][j]<min) min = heuristic[i][j];
					if(heuristic[i][j]>max) max = heuristic[i][j];
				}
			}
			limit = (min+max)/2;
		}
		
		/*================
		 *	Construction
		 =================*/
		//if(!data.isTrackCompleted(name)) {
			
			/*
			// Displaying the heuristic 
			String blank = " ";
			String display = "Min  " + min + ", max " + max + " limit " + limit + "\n Heuristic matrix : ";
			for (int j=0; j<this.height ; j++){
				display += this.separateur(this.width);
				for (int i=0; i<this.width; i++) {
					if(isObjective(new Point(i,j))) blank="+";
					display += "+";
					if(heuristic[i][j]<100) {
						display += blank;
					}
					display += heuristic[i][j]+blank;
					if(heuristic[i][j]<10) {
						display += blank;
					}
					blank = " ";
				}
				display += "+";
			}
			System.out.println(display);
			*/
		
		
			int handSize = data.getHandSize(name);
			Tile t;
			int i, j1, k, nbrTries = 0;

			if(handSize == 0) return null;
			
			do{
				nbrTries++;
				if(nbrTries > 10000)	return scanAllPossibleChoices(data);
				i = rand.nextInt(data.getWidth());
				j1 = rand.nextInt(data.getHeight());
				
				k = rand.nextInt(handSize);
				t = data.getHandTile(name, k);
				
				for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
					t.turnLeft();
				}
			} while(heuristic[i][j1] >= limit || !data.isAcceptableTilePlacement(i, j1, t));
			res = Action.newBuildSimpleAction(i, j1, t);
			
		//}
		
		return res;
	}

	
	@SuppressWarnings("unused")
	private boolean isObjective(Point p) {
		for (int i=0; i<this.objectives.length; i++){
			if (objectives[i].equals(p)){
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private String separateur(int size){
		String result="\n";
		for (int i=0; i<size; i++){
			result += "+----";
		}
		result += "+\n";
		return result;
	}
	
	
	/**
	 * Calcule l'heuristique pour un point donné. (distance de Manhattan)
	 * @param p Le point visé.
	 * @param heuristique La matrice à remplir.
	 */
	private void computeThisHeuristic(Point p, int[][] heuristic){
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				int calcul = Math.abs(i-p.x)+Math.abs(j-p.y);
				heuristic[i][j] += calcul;
			}
		}
	}
	
	
	/**
	 * Vérifie tous les choix possibles de pose
	 * @param currentConfig
	 * @return une Action simple build s'il en existe une valide, lève une exception sinon
	 */
	private Action scanAllPossibleChoices(Data currentConfig)
	{
		Tile tile;
		Tile[] rotations = new Tile[4];
		for(int j = 0; j < 4; j++)	rotations[j] = new Tile();

		for (int i=0; i<currentConfig.getHandSize(this.name); i++)
		{
			tile = currentConfig.getHandTile(this.name, i);
			int nbrRotations = tile.getUniqueRotationList(rotations);
			for (int r=0; r<nbrRotations; r++)
			{
				for (int x=1; x<currentConfig.getWidth()-1; x++)
				{
					for (int y=1; y<currentConfig.getHeight()-1; y++)
					{
						if (currentConfig.isAcceptableTilePlacement(x, y, rotations[r]))	return Action.newBuildSimpleAction(x, y, tile);
					}
				}
			}
		}

		throw new RuntimeException(name + " cannot find a valid move");
	}
}
