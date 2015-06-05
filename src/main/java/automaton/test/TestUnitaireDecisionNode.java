package main.java.automaton.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import main.java.automaton.CoupleActionIndex;
import main.java.automaton.DecisionNode;
import main.java.data.Action;
import main.java.data.Tile;

import org.junit.Test;

public class TestUnitaireDecisionNode {

	@Test
	public void testConstructeur() {
		@SuppressWarnings("unused")
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 0, "leaf&root");
	}


	@Test
	public void testIsRoot()  {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		assertTrue(monNoeudDeDecision.isRoot());
		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		assertFalse(monNoeudDeDecision.isRoot());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		assertFalse(monNoeudDeDecision.isRoot());
		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		assertTrue(monNoeudDeDecision.isRoot());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		assertTrue(monNoeudDeDecision.isRoot());
	}

	@Test
	public void testSetRoot()  {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		monNoeudDeDecision.setRoot();
		assertTrue(monNoeudDeDecision.isRoot());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		monNoeudDeDecision.setRoot();
		assertTrue(monNoeudDeDecision.isRoot());		
		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		monNoeudDeDecision.setRoot();
		assertTrue(monNoeudDeDecision.isRoot());		
	}



	@Test
	public void testUnsetRoot()  {
		DecisionNode monNoeudDeDecision = null;

		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		monNoeudDeDecision.unsetRoot();
		assertFalse(monNoeudDeDecision.isRoot());

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		monNoeudDeDecision.unsetRoot();
		assertFalse(monNoeudDeDecision.isRoot());
		assertTrue(monNoeudDeDecision.isLeaf());

		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		monNoeudDeDecision.unsetRoot();
		assertFalse(monNoeudDeDecision.isRoot());
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		monNoeudDeDecision.unsetRoot();
		assertTrue(monNoeudDeDecision.isLeaf());
		assertFalse(monNoeudDeDecision.isRoot());

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		monNoeudDeDecision.unsetRoot();
		assertTrue(monNoeudDeDecision.isLeaf());
		assertFalse(monNoeudDeDecision.isRoot());		
	}

	@Test
	public void testIsLeaf()  {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		assertTrue(monNoeudDeDecision.isLeaf());
		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		assertFalse(monNoeudDeDecision.isLeaf());
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		assertFalse(monNoeudDeDecision.isLeaf());
		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		assertTrue(monNoeudDeDecision.isLeaf());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		assertTrue(monNoeudDeDecision.isLeaf());
	}

	@Test
	public void testSetLeaf()  {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		monNoeudDeDecision.setLeaf();
		assertTrue(monNoeudDeDecision.isLeaf());
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		monNoeudDeDecision.setLeaf();
		assertTrue(monNoeudDeDecision.isLeaf());		
		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		monNoeudDeDecision.setLeaf();
		assertTrue(monNoeudDeDecision.isLeaf());
	}


	@Test
	public void testUnsetLeaf()  {
		DecisionNode monNoeudDeDecision = null;

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		monNoeudDeDecision.unsetLeaf();
		assertFalse(monNoeudDeDecision.isLeaf());

		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		monNoeudDeDecision.unsetLeaf();
		assertFalse(monNoeudDeDecision.isLeaf());
		assertTrue(monNoeudDeDecision.isRoot());

		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		monNoeudDeDecision.unsetLeaf();
		assertFalse(monNoeudDeDecision.isLeaf());
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		monNoeudDeDecision.unsetLeaf();
		assertTrue(monNoeudDeDecision.isRoot());
		assertFalse(monNoeudDeDecision.isLeaf());

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		monNoeudDeDecision.unsetLeaf();
		assertTrue(monNoeudDeDecision.isRoot());
		assertFalse(monNoeudDeDecision.isLeaf());
	}

	@Test
	public void testIsInternal()  {
		DecisionNode monNoeudDeDecision = null;

		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		assertTrue(monNoeudDeDecision.isInternal());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		assertFalse(monNoeudDeDecision.isInternal());
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		assertFalse(monNoeudDeDecision.isInternal());
		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		assertFalse(monNoeudDeDecision.isInternal());
		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		assertFalse(monNoeudDeDecision.isInternal());

		monNoeudDeDecision.unsetLeaf();
		monNoeudDeDecision.unsetRoot();
		assertTrue(monNoeudDeDecision.isInternal());

	}

	@Test
	public void testSetInternalNode()  {
		DecisionNode monNoeudDeDecision = null;

		monNoeudDeDecision = new DecisionNode(10, 1, "internalNode");
		monNoeudDeDecision.setInternalNode();
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf");
		monNoeudDeDecision.setInternalNode();
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		monNoeudDeDecision.setInternalNode();
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "root&leaf");
		monNoeudDeDecision.setInternalNode();
		assertTrue(monNoeudDeDecision.isInternal());

		monNoeudDeDecision = new DecisionNode(10, 1, "leaf&root");
		monNoeudDeDecision.setInternalNode();
		assertTrue(monNoeudDeDecision.isInternal());

	}


	@Test
	public void testGetAndSetQuality()  {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		assertTrue(monNoeudDeDecision.getQuality()==-1.0);
		monNoeudDeDecision.setQuality(100.0);
		assertTrue(monNoeudDeDecision.getQuality()==100.0);
		assertFalse(monNoeudDeDecision.getQuality()==90.0);

	}


	@Test
	public void testGetPossibleFollowingAction()  {
		DecisionNode monNoeudDeDecision = null;
		CoupleActionIndex maTableCoupleActionIndex[];
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		maTableCoupleActionIndex = monNoeudDeDecision.getPossibleFollowingActionTable();
		assertFalse(maTableCoupleActionIndex==null);
		assertTrue(maTableCoupleActionIndex[0].getIndex()==-1); //Init a -1
		assertTrue(maTableCoupleActionIndex[9].getIndex()==-1);
	}

	@Test
	public void testGetSizeOfPossiblesActionsTable()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		assertTrue(monNoeudDeDecision.getSizeOfPossiblesActionsTable()==size);
		for (int i=0;i<monNoeudDeDecision.getSizeOfPossiblesActionsTable();i++){
			assertFalse(monNoeudDeDecision.getPossibleFollowingActionTable()[i]==null);
		}
	}

	@Test
	public void testSetSizeOfPossiblesActionsTable()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		int otherSize=8;
		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		monNoeudDeDecision.setSizeOfPossiblesActionsTable(otherSize);
		assertFalse(monNoeudDeDecision.getSizeOfPossiblesActionsTable()==size);
		assertTrue(monNoeudDeDecision.getSizeOfPossiblesActionsTable()==otherSize);
		assertFalse(monNoeudDeDecision.getSizeOfPossiblesActionsTable()==otherSize+1);
	}

	@Test
	public void getNumberPossiblesActionsTable()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");	// A l'initialisation, aucune action n'a été entrée:
		assertTrue(monNoeudDeDecision.getNumberPossiblesActionsTable()==0); // Il y a 0 action possible
		assertFalse(monNoeudDeDecision.getNumberPossiblesActionsTable()==1); // Il n'y a pas 1 action possible
		CoupleActionIndex monCoupleActionIndex1 = new CoupleActionIndex(Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123")), 1, 8.0);
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex1);
		CoupleActionIndex monCoupleActionIndex2 = new CoupleActionIndex(Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123")), 2, 45.0);
		monNoeudDeDecision.setCoupleActionIndex(1, monCoupleActionIndex2);
		assertTrue(monNoeudDeDecision.getNumberPossiblesActionsTable()==2);
		assertFalse(monNoeudDeDecision.getNumberPossiblesActionsTable()==0);
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex2);
		assertTrue(monNoeudDeDecision.getNumberPossiblesActionsTable()==2);
		assertFalse(monNoeudDeDecision.getNumberPossiblesActionsTable()==0);		


	}

	@Test
	public void testGetandSetCoupleActionIndex()  {
		DecisionNode monNoeudDeDecision = null;
		CoupleActionIndex monCoupleActionIndex = null;
		int size=10;
		Tile maTile = Tile.parseTile("Tile_FFFFZZ060123");
		Action monAction = Action.newBuildSimpleAction(new Point(5,4), maTile);

		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		assertTrue(monNoeudDeDecision.getCoupleActionIndex(0).equals(monNoeudDeDecision.getCoupleActionIndex(1))); //La table des couples action/index est initialisé avec des valeurs par default, ils doivent donc etre egaux 2 a deux
		monCoupleActionIndex = new CoupleActionIndex(monAction, 1, 34.0);
		monNoeudDeDecision.setCoupleActionIndex(0, monCoupleActionIndex);
		assertFalse(monCoupleActionIndex==monNoeudDeDecision.getCoupleActionIndex(0)); //Le couple a été copié, ce n'est pas le meme objet
		assertTrue(monCoupleActionIndex.equals(monNoeudDeDecision.getCoupleActionIndex(0))); // Mais leur contenu est identique

	}


	@Test
	public void testGetDepth()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		assertTrue(monNoeudDeDecision.getDepth()==1);
		assertFalse(monNoeudDeDecision.getDepth()==0);
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		assertFalse(monNoeudDeDecision.getDepth()==1);
		assertTrue(monNoeudDeDecision.getDepth()==0);		
	}

	@Test
	public void testSetDepth()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		monNoeudDeDecision.setDepth(1);
		assertTrue(monNoeudDeDecision.getDepth()==1);
		assertFalse(monNoeudDeDecision.getDepth()==0);
		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		monNoeudDeDecision.setDepth(0);
		assertFalse(monNoeudDeDecision.getDepth()==1);
		assertTrue(monNoeudDeDecision.getDepth()==0);		}

	@Test
	public void testCopy()  {
		DecisionNode monNoeudDeDecisionCible = null;
		DecisionNode monNoeudDeDecisionSource = null;
		int size=10;
		monNoeudDeDecisionSource = new DecisionNode(size, 0, "root");
		monNoeudDeDecisionCible = new DecisionNode(size, 0, "root");

		CoupleActionIndex monCoupleActionIndex = new CoupleActionIndex(Action.newBuildSimpleAction(new Point(5,4), Tile.parseTile("Tile_FFFFZZ060123")), 1, 25.6);
		monNoeudDeDecisionSource.setCoupleActionIndex(0, monCoupleActionIndex);
		assertFalse(monNoeudDeDecisionCible.equals(monNoeudDeDecisionSource));
		monNoeudDeDecisionCible.copy(monNoeudDeDecisionSource);
		assertTrue(monNoeudDeDecisionCible.equals(monNoeudDeDecisionSource));

	}


	@Test
	public void testActionIsSignificant() {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		assertFalse(monNoeudDeDecision.actionIsSignificant(0));
		monNoeudDeDecision.getCoupleActionIndex(0).setIndex(1);
		assertTrue(monNoeudDeDecision.actionIsSignificant(0));
	}
	
	@Test
	public void testQualityIsSignificant() {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		assertFalse(monNoeudDeDecision.qualityIsSignificant());
		monNoeudDeDecision.setQuality(8.0);
		assertTrue(monNoeudDeDecision.qualityIsSignificant());
	}
	
	@Test
	public void testReset() {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		monNoeudDeDecision.setQuality(10.0);
		for(int i = 0; i < size; i++){
			monNoeudDeDecision.getCoupleActionIndex(i).setIndex(i);
		}
		monNoeudDeDecision.reset();
		for(int i = 0; i < size; i++){
			assertFalse(monNoeudDeDecision.actionIsSignificant(0));
		}
	}

	@Test
	public void testToString()  {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 0, "root");
		String resultToString = monNoeudDeDecision.toString();
		assertFalse(resultToString==null);
	}

}
