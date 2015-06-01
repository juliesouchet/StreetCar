package main.java.automaton.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import main.java.automaton.DecisionNode;
import main.java.automaton.DecisionNode.CoupleActionIndex;
import main.java.automaton.ExceptionUnknownNodeType;

import org.junit.Test;

public class TestUnitaireDecisionNode {

	@Test
	public void testConstructeur() throws ExceptionUnknownNodeType{
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 0, "leaf&root");
	}


	@Test
	public void testIsRoot() throws ExceptionUnknownNodeType {
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
	public void testSetRoot() throws ExceptionUnknownNodeType {
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
	public void testUnsetRoot() throws ExceptionUnknownNodeType {
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
	public void testIsLeaf() throws ExceptionUnknownNodeType {
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
	public void testSetLeaf() throws ExceptionUnknownNodeType {
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
	public void testUnsetLeaf() throws ExceptionUnknownNodeType {
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
	public void testIsInternal() throws ExceptionUnknownNodeType {
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
	public void testSetInternalNode() throws ExceptionUnknownNodeType {
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
	public void testGetAndSetQuality() throws ExceptionUnknownNodeType {
		DecisionNode monNoeudDeDecision = null;
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		assertTrue(monNoeudDeDecision.getQuality()==-1.0);
		monNoeudDeDecision.setQuality(100.0);
		assertTrue(monNoeudDeDecision.getQuality()==100.0);
		assertFalse(monNoeudDeDecision.getQuality()==90.0);

	}


	@Test
	public void testGetPossibleFollowingAction() throws ExceptionUnknownNodeType {
		DecisionNode monNoeudDeDecision = null;
		CoupleActionIndex maTableCoupleActionIndex[];
		monNoeudDeDecision = new DecisionNode(10, 1, "root");
		maTableCoupleActionIndex = monNoeudDeDecision.getPossibleFollowingAction();
		assertFalse(maTableCoupleActionIndex==null);
		assertTrue(maTableCoupleActionIndex[0].getIndex()==0); //Init a 0
		assertTrue(maTableCoupleActionIndex[9].getIndex()==0);
	}

	@Test
	public void testGetSizeOfPossiblesActionsTable() throws ExceptionUnknownNodeType {
		DecisionNode monNoeudDeDecision = null;
		int size=10;
		monNoeudDeDecision = new DecisionNode(size, 1, "root");
		assertTrue(monNoeudDeDecision.getSizeOfPossiblesActionsTable()==10);
		for (int i=0;i<monNoeudDeDecision.getSizeOfPossiblesActionsTable();i++){
			assertFalse(monNoeudDeDecision.getPossibleFollowingAction()[i]==null);
		}
	}

	@Test
	public void testIncrementNumberOfPossiblesActions() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecrementNumberOfPossiblesActions() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetNumberOfPossiblesActions() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoupleActionIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCoupleActionIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDepth() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDepth() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopy() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecisionNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
