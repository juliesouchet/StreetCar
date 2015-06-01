package test.java.game;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import main.java.data.Action;

import org.junit.Test;

public class TestUnitaireAction {

	@Test
	public void testAction() {
		@SuppressWarnings("unused")
		Action monAction = new Action();
		assertTrue(monAction.action==Action.NONE);
	}

	@Test
	public void testNewStartTripNextTurnAction() {
		Action monAction = Action.newStartTripNextTurnAction();
		assertTrue(monAction.action==Action.START_TRIP_NEXT_TURN);
	}

	@Test
	public void testNewMoveAction() {
		Action monAction = Action.newMoveAction();
		assertTrue(monAction.action==Action.START_TRIP_NEXT_TURN);	}

	@Test
	public void testNewBuildSimpleActionPointTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewBuildSimpleActionIntIntTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewBuildDoubleAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClone() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsConstructing() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSimpleConstructing() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMoving() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopy() {
		fail("Not yet implemented");
	}

}
