package test.java.automata;

import main.java.automaton.DecisionNode;
import main.java.data.Data;





public class TestPossibleChoiceRiyane
{
// -------------------------------------------------
// Attributes
// -------------------------------------------------
	public static final String	boardName			= "newOrleans";
	public static final String	gameName			= "Test game";
	public static final String	playerName			= "edouardo";
	public static final int		nbrBuildingInLine	= 2;

// -------------------------------------------------
// Local methodes
// -------------------------------------------------
	public static void main(String[] args)
	{
		DecisionNode monNoeudDedecision = null;
		Data data = null;
		int nbrChoice;

		try					{data = new Data("Test game", boardName, nbrBuildingInLine);}
		catch(Exception e)	{e.printStackTrace(); return;}
		data.addPlayer(null, playerName, true, false);
		data.hostStartGame(playerName);
		try					{monNoeudDedecision = new DecisionNode(4000000, 0, "root");}
		catch (Exception e)	{e.printStackTrace();}
System.out.println("Debut du test: " + System.nanoTime()); 
		nbrChoice = data.getPossibleActions(playerName, monNoeudDedecision.getPossibleFollowingActionTable());
System.out.println("Fin du test: " + System.nanoTime()); 
		System.out.println("Nbr possible choice = " + nbrChoice);
	}
}
