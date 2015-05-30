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
		this.name = new String(playerName);
	}
	

	public Action makeChoice(Data currentconfig) {
//TODO: -- riyane modif de Data			Hand myHand = currentConfig.getHand(name);
// sert a ne plus faire de new dans les fonctions de data qui sont appelles par l'automate)
		int handSize = currentconfig.getHandSize(name);
		Action choix ;
		Random rand = new Random();
		Tile t;
		int i, j, k;

		do{
			// On choisit un emplacement au hasard
			i = rand.nextInt(currentconfig.getWidth());
			j = rand.nextInt(currentconfig.getHeight());
			
			// On choisit une tuile parmi les 5 de notre main
			k = rand.nextInt(handSize);
			t = currentconfig.getHandTile(name, k);
//TODO: faux -- riyane			myHand.add(t);
			//On la fait tourner
			for(int rotation = 0; rotation < rand.nextInt(4); rotation++) {
				t.turnLeft();
			}
// TODO que faire s'il n'y a aucun choix valide ?			
		}while( !currentconfig.isAcceptableTilePlacement(i, j, t));
		choix = Action.newBuildSimpleAction(i, j, t);
		
		return choix;
	}

}
