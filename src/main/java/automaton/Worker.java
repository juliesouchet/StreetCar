package main.java.automaton;

import java.awt.Point;
import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Tile;

public class Worker extends PlayerAutomaton {

	private int width, height, nbrObjectives;
	private Point[]	objectives = null;

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
		int[][] heuristic = new int[width][height];
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
				objectives[nbrObjectives-1] = terminus[2];
			}
		}
		
		/*================
		 *	Construction
		 =================*/
		if(!data.isTrackCompleted(name)) {
			int min = 0, max = 0;
			Point minMax;
			for(int i = 0; i < nbrObjectives; i++) {
				Point p = objectives[i];
				minMax = computeThisHeuristic(p, heuristic);
				min += minMax.x;
				max += minMax.y;
			}
			
			int limit = (min+max)/2;

			int handSize = data.getHandSize(name);
			Tile t;
			int i, j, k, nbrTries = 0;

			if(handSize == 0) return null;
			
			do{
				nbrTries++;
				if(nbrTries > 10000)	return scanAllPossibleChoices(data);
				i = rand.nextInt(data.getWidth());
				j = rand.nextInt(data.getHeight());
				
				k = rand.nextInt(handSize);
				t = data.getHandTile(name, k);
				
				for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
					t.turnLeft();
				}
			} while(heuristic[i][j] >= limit || !data.isAcceptableTilePlacement(i, j, t));
			res = Action.newBuildSimpleAction(i, j, t);
			
		}
		
		return res;
	}

	
	
	
	
	/**
	 * Calcule l'heuristique pour un point donné. (distance de Manhattan)
	 * @param p Le point visé.
	 * @param heuristique La matrice à remplir.
	 */
	private Point computeThisHeuristic(Point p, int[][] heuristic){
		Point minMax = new Point(Integer.MAX_VALUE,0);
		for(int i=0; i<this.width;i++){
			for (int j=0; j<this.height; j++){
				int calcul = Math.abs(i-p.x)+Math.abs(j-p.y);
				
				if(calcul < minMax.x)	minMax.x = calcul;
				if(calcul > minMax.y)	minMax.y = calcul;
				
				heuristic[i][j] += calcul;
			}
		}
		return minMax;
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
