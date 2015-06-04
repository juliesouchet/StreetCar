package main.java.automaton;

import java.util.Random;

import main.java.data.Action;
import main.java.data.Data;
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
	

	public Dumbest(String playerName) {
		super();
		if(playerName == null) this.name = "Dumbest";
		else this.name = playerName;
	}
	
	public Action makeChoice(Data currentconfig) {
		int handSize = currentconfig.getHandSize(name);
		Action choix ;
		Random rand = new Random();
		Tile t;
		int i, j, k;

		if(handSize == 0 ) return null;
// TODO que faire s'il n'y a aucun choix valide ?
//		if(!currentconfig.canPlaceTile(name))	{System.out.println(name + " est bloqué");return null;}
		
		do{
			// On choisit un emplacement au hasard
			i = rand.nextInt(currentconfig.getWidth());
			j = rand.nextInt(currentconfig.getHeight());
			
			// On choisit une tuile parmi les 5 de notre main
			k = rand.nextInt(handSize);
			t = currentconfig.getHandTile(name, k);
			//On la fait tourner
			for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
				t.turnLeft();
			}
		}while( !currentconfig.isAcceptableTilePlacement(i, j, t));
		choix = Action.newBuildSimpleAction(i, j, t);
//System.out.println("choixxxxxx " + choix);
//System.out.println("tuileeeeee " + t);
		return choix;
	}

}
