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

System.out.println("Testeteur: ");
		try					{data = new Data("Test game", boardName, nbrBuildingInLine);}
		catch(Exception e)	{e.printStackTrace(); return;}
		data.addPlayer(null, playerName, true, false);
		data.hostStartGame(playerName);
long temps = (System.nanoTime() * (10^9));
		try					{monNoeudDedecision = new DecisionNode(700000, 0, "root");}
		catch (Exception e)	{e.printStackTrace();}
long dt = (System.nanoTime() * (10^9)) - temps;
temps = System.nanoTime();
System.out.println("Temps de chargement          : " + dt); 
System.out.println("Debut du test"); 
		nbrChoice = data.getPossibleActions(playerName, monNoeudDedecision.getPossibleFollowingActionTable(), true);
dt = (System.nanoTime() * (10^9)) - temps;
System.out.println("Temps Data.getPossibleAction : " + dt + "\tsecondes"); 
System.out.println("Nbr possible choice = " + nbrChoice);
	}
}
