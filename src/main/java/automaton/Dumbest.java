package main.java.automaton;

import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
import main.java.data.Hand;
import main.java.data.Tile;

/**
 * 
 * @author Ulysse
 *	
 *	Ce joueur automatique est bidon:
 *	il ne fait que poser des tuiles au pif sur la carte.
 *	Il ne tente jamais de faire un voyage, ne sais pas si il a atteint un objectif
 *	Il ne fait que des coups simples
 *
 */
public class Dumbest extends PlayerAutomaton {
	
	public Dumbest(String name) {
		super();
		this.name = new String(name);
	}
	


	public Action makeChoice(Data currentconfig) {
		Hand myHand = currentconfig.getHand(name);
		Action choix ;
		Random rand = new Random();
		Tile t;
		int i, j, k, n;
		
		do{
			// On choisit un emplacement au hasard
			i = rand.nextInt(currentconfig.getWidth());
			j = rand.nextInt(currentconfig.getHeight());
			
			// On choisit une tuile parmi les 5 de notre main
			n = myHand.size();			
			k = rand.nextInt(n);
			t = myHand.get(k);
			myHand.add(t);
			//On la fait tourner
			for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
				t.turnLeft();
			}
			
		}while( !currentconfig.isAcceptableTilePlacement(i, j, t));
		
		System.out.println("Pose tuile "+ t.toString()+" a la position: ("+i+","+j+")");
		
		choix = Action.newBuildSimpleAction(i, j, t);
		
		return choix;
	}

}
