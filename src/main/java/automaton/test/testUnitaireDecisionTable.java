package main.java.automaton.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import main.java.automaton.CoupleActionIndex;
import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionTable;
import main.java.data.Action;
import main.java.data.Tile;

import org.junit.Test;

public class testUnitaireDecisionTable {


	/*
	 * 	--------------------------------------------------------------------------------------------------
	 *	| 		0		|		1		|		2		|		3		|		4		|		5		|
	 *	--------------------------------------------------------------------------------------------------
	 * 	| 	90.0		| 	90.0		| 		20.0	|		90.0	|		30.0	|		20.0	|
	 * 	--------------------------------------------------------------------------------------------------
	 * 	| 		0		|		1		|		1		|		2		|		2		|		2		|
	 * 	--------------------------------------------------------------------------------------------------
	 * 	|	 root		|		int		|		int		|		leaf 	|	 	leaf	|		leaf	|	
	 * 	==================================================================================================
	 * 	|	<A;1;90.0>	| 	<A;3;90.0>	|	<A;4;30.0> 	|		...		|		...		|		...		|
	 * 	--------------------------------------------------------------------------------------------------
	 * 	|	 <A;2;20.0>	| 	...			|	<A;5;20.0>	|		...		|		...		|		...		|
	 * 	--------------------------------------------------------------------------------------------------
	 * 	|		...		|	 ...		|		...		|		...		|		...		|		...		|
	 * 	--------------------------------------------------------------------------------------------------
	 * 	|		...		| 	...			|		...		|		...		|		...		|		...		|
	 * 	--------------------------------------------------------------------------------------------------
	 *
	 */
	public DecisionTable tableTest() {
		DecisionTable maTableDeDecision = null;

		DecisionNode decisionNode = null;
		int sizeTable=7;
		int nodeTable=4;
		CoupleActionIndex coupleActionIndex = null;
		Action action = null;

		// Je créé la table de decision
		maTableDeDecision = new DecisionTable(sizeTable, nodeTable, "leNomDuJoueur");

		// Je créé un noeud qui me servira de buffer pour remplir la table
		decisionNode = new DecisionNode(nodeTable, 0, "root");

		// Je créé une action qulconque qui me servira pour remplir les noeuds
		action = Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123"));

		// Je créé un couple index action pour remplir la liste des actions possibles
		coupleActionIndex = new CoupleActionIndex(action, 1, 90.0);

		//Colonne 0
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		coupleActionIndex.setIndex(2);
		coupleActionIndex.setQuality(20.0);

		decisionNode.setCoupleActionIndex(1, coupleActionIndex);
		decisionNode.setQuality(90.0);

		maTableDeDecision.setDecisionNode(0, decisionNode);

		//Colonne 1
		decisionNode = new DecisionNode(nodeTable, 1, "internalNode");
		coupleActionIndex.setIndex(3);
		coupleActionIndex.setQuality(90.0);
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		decisionNode.setQuality(90.0);

		maTableDeDecision.setDecisionNode(1, decisionNode);


		//Colonne 2
		decisionNode = new DecisionNode(nodeTable, 1, "internalNode");
		coupleActionIndex.setIndex(4);
		coupleActionIndex.setQuality(30.0);

		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		coupleActionIndex.setIndex(5);
		coupleActionIndex.setQuality(20.0);

		decisionNode.setCoupleActionIndex(1, coupleActionIndex);
		decisionNode.setQuality(20.0);
		maTableDeDecision.setDecisionNode(2, decisionNode);	

		//Colonne 3
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(90.0);
		maTableDeDecision.setDecisionNode(3, decisionNode);	

		//Colonne 4
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(30.0);
		maTableDeDecision.setDecisionNode(4, decisionNode);	

		//Colonne 5
		decisionNode = new DecisionNode(nodeTable, 2, "leaf");
		decisionNode.setQuality(20.0);
		maTableDeDecision.setDecisionNode(5, decisionNode);
		return maTableDeDecision;
	}

	//
	@Test
	public void testConstructorDecisionTable(){
		DecisionTable maTableDeDecision = null;
		int sizeTable=10;
		int nodeTable=5;
		maTableDeDecision = new DecisionTable(sizeTable, nodeTable, "leNomDuJoueur");

		for(int i=0;i<maTableDeDecision.getSize();i++){
			assertTrue(maTableDeDecision.slotIsFree(i)); // Les cases du tableaux ne sont pas occupés
			assertTrue(maTableDeDecision.getDecisionNode(i)!=null); // Les noeud ont étaient alloués en mémoire
		}
		assertTrue(maTableDeDecision.getSize()==sizeTable);
		assertFalse(maTableDeDecision.getSize()==sizeTable+1);
	}

	@Test
	public void testGetBestWorstAction() {
		DecisionTable maTableDeDecision = tableTest();

		assertFalse(maTableDeDecision.getBestActionIndex(0)==1);
		assertTrue(maTableDeDecision.getBestActionIndex(1)==0);
		assertTrue(maTableDeDecision.getBestActionIndex(2)==0);
		assertTrue(maTableDeDecision.getBestActionIndex(3)==-1);

		assertTrue(maTableDeDecision.getWorstActionIndex(0)==1);
		assertTrue(maTableDeDecision.getWorstActionIndex(1)==0);
		assertTrue(maTableDeDecision.getWorstActionIndex(2)==1);
		assertTrue(maTableDeDecision.getWorstActionIndex(3)==-1);


	}

	@Test
	public void testSetAndGetDecisionNode() {
		DecisionTable maTableDeDecision = null;

		DecisionNode decisionNode = null;
		int sizeTable=6;
		int nodeTable=4;
		CoupleActionIndex coupleActionIndex = null;
		Action action = null;

		// Je créé la table de decision
		maTableDeDecision = new DecisionTable(sizeTable, nodeTable, "leNomDuJoueur");

		// Je créé un noeud qui me servira de buffer pour remplir la table
		decisionNode = new DecisionNode(nodeTable, 0, "root");

		// Je créé une action qulconque qui me servira pour remplir les noeuds
		action = Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123"));

		// Je créé un couple index action pour remplir la liste des actions possibles
		coupleActionIndex = new CoupleActionIndex(action, 1, 0.0);

		//Colonne 0
		decisionNode.setCoupleActionIndex(0, coupleActionIndex);
		coupleActionIndex.setIndex(2);
		decisionNode.setCoupleActionIndex(1, coupleActionIndex);
		decisionNode.setQuality(90.0);

		maTableDeDecision.setDecisionNode(0, decisionNode);
		
		assertTrue(maTableDeDecision.getDecisionNode(0).equals(decisionNode));
		
	}
	
	@Test
	public void testGetSize() {
		DecisionTable maTableDeDecision = null;
		int sizeTable=10;
		int nodeTable=5;
		maTableDeDecision = new DecisionTable(sizeTable, nodeTable, "leNomDuJoueur");
		assertTrue(maTableDeDecision.getSize()==sizeTable);
	}

	@Test
	public void testSlotIsFree(){
		DecisionTable maTableDeDecision = tableTest();
		assertFalse(maTableDeDecision.slotIsFree(0));
		assertTrue(maTableDeDecision.slotIsFree(maTableDeDecision.getSize()-1));
		
	}
	

	@Test
	public void testToString() {
		DecisionTable maTableDeDecision = tableTest();
		assertTrue(maTableDeDecision.toString()!=null);
	}


	@Test
	public void testFindFreeSlot() {
		DecisionTable maTableDeDecision = tableTest();
		assertTrue(maTableDeDecision.findFreeSlot()==6);
		maTableDeDecision.setDecisionNode(6, new DecisionNode(4, 3, "leaf"));
		assertTrue(maTableDeDecision.findFreeSlot()==DecisionTable.TABLE_IS_FULL);

	}




}
