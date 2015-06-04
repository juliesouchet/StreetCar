package test.java.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Tile;

import org.junit.Test;

public class TestUnitaireClasseAction {

	@Test
	public void testNewMoveAction() {
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		Action monAction = Action.newMoveAction(chemin,10);
		assertTrue(monAction.action==Action.MOVE);
		assertTrue(chemin[0].equals(new Point(0,0)));
		assertTrue(chemin[9].equals(new Point(9,9)));
	}

	@Test
	public void testNewBuildSimpleActionIntIntTile() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(1,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));	}

	@Test
	public void testNewBuildSimpleActionPointTile() {
		Action monAction = Action.newBuildSimpleAction(new Point(1,1), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(1,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));	}

	//return ((this.action == TWO_BUILD_SIMPLE) || (this.action == BUILD_DOUBLE) || (this.action == BUILD_AND_START_TRIP_NEXT_TURN));

	@Test
	public void testIsTwoStepAction() {
		Action monAction = new Action();

		assertFalse(monAction.isTwoStepAction());

		monAction.action=Action.NONE;
		assertFalse(monAction.isTwoStepAction());

		monAction.action=Action.MOVE;
		assertTrue(monAction.isTwoStepAction());

		monAction.action=Action.BUILD_SIMPLE;
		assertFalse(monAction.isTwoStepAction());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertTrue(monAction.isTwoStepAction());

		monAction.action=Action.BUILD_DOUBLE;
		assertTrue(monAction.isTwoStepAction());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertTrue(monAction.isTwoStepAction());
	}

	@Test
	public void testIsConstructing() {
		Action monAction = new Action();

		assertFalse(monAction.isConstructing());

		monAction.action=Action.NONE;
		assertFalse(monAction.isConstructing());

		monAction.action=Action.MOVE;
		assertFalse(monAction.isConstructing());

		monAction.action=Action.BUILD_SIMPLE;
		assertTrue(monAction.isConstructing());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertTrue(monAction.isConstructing());

		monAction.action=Action.BUILD_DOUBLE;
		assertTrue(monAction.isConstructing());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertTrue(monAction.isConstructing());
	}

	@Test
	public void testIsSimpleConstructing() {
		Action monAction = new Action();

		assertFalse(monAction.isSimpleConstructing());

		monAction.action=Action.NONE;
		assertFalse(monAction.isSimpleConstructing());

		monAction.action=Action.MOVE;
		assertFalse(monAction.isSimpleConstructing());

		monAction.action=Action.BUILD_SIMPLE;
		assertTrue(monAction.isSimpleConstructing());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertFalse(monAction.isSimpleConstructing());

		monAction.action=Action.BUILD_DOUBLE;
		assertFalse(monAction.isSimpleConstructing());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertTrue(monAction.isSimpleConstructing());
	}

	@Test
	public void testIsTwoSimpleConstructing() {
		Action monAction = new Action();

		assertFalse(monAction.isTwoSimpleConstructing());

		monAction.action=Action.NONE;
		assertFalse(monAction.isTwoSimpleConstructing());

		monAction.action=Action.MOVE;
		assertFalse(monAction.isTwoSimpleConstructing());

		monAction.action=Action.BUILD_SIMPLE;
		assertFalse(monAction.isTwoSimpleConstructing());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertTrue(monAction.isTwoSimpleConstructing());

		monAction.action=Action.BUILD_DOUBLE;
		assertFalse(monAction.isTwoSimpleConstructing());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertFalse(monAction.isTwoSimpleConstructing());
	}

	@Test
	public void testIsMoving() {
		Action monAction = new Action();

		assertFalse(monAction.isMoving());

		monAction.action=Action.NONE;
		assertFalse(monAction.isMoving());

		monAction.action=Action.MOVE;
		assertTrue(monAction.isMoving());

		monAction.action=Action.BUILD_SIMPLE;
		assertFalse(monAction.isMoving());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertFalse(monAction.isMoving());

		monAction.action=Action.BUILD_DOUBLE;
		assertFalse(monAction.isMoving());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertTrue(monAction.isMoving());
	}

	@Test
	public void testToString() {
		Action monAction = new Action();
		assertTrue(monAction.toString()!=null);
	}

	@Test
	public void testCopy() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSimpleBuildingAndStartTripNextTurnAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDoubleBuildingAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTravelAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClone() {
		fail("Not yet implemented");
	}

}
