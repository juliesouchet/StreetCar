package test.java.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Tile;

import org.junit.Test;

public class TestUnitaireClasseAction {

	@Test
	public void testNewMoveAction() {
		Point[] chemin = new Point[10];
		Point debut = new Point(-1,-1);
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		Action monAction = Action.newMoveAction(chemin,10,debut);
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

		assertFalse(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.NONE;
		assertFalse(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.MOVE;
		assertFalse(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.BUILD_SIMPLE;
		assertFalse(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.TWO_BUILD_SIMPLE;
		assertTrue(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.BUILD_DOUBLE;
		assertFalse(monAction.isTWO_BUILD_SIMPLE());

		monAction.action=Action.BUILD_AND_START_TRIP_NEXT_TURN;
		assertFalse(monAction.isTWO_BUILD_SIMPLE());
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
		assertFalse(monAction.isMoving());
	}

	@Test
	public void testToString() {
		Action monAction = new Action();
		assertTrue(monAction.toString()!=null);
	}

	@Test
	public void testSetSimpleBuildingAndStartTripNextTurnAction() {
		Action monAction = new Action();
		assertFalse(monAction.isBUILD_AND_START_TRIP_NEXT_TURN());
		
		monAction.setSimpleBuildingAndStartTripNextTurnAction(0, 0, null);
		assertTrue(monAction.isBUILD_AND_START_TRIP_NEXT_TURN());
	}

	@Test
	public void testSetDoubleBuildingAction() {
		Action monAction = new Action();
		assertFalse(monAction.isBUILD_DOUBLE());
		
		monAction.setDoubleBuildingAction(0, 0, null, 0, 0, null);
		assertTrue(monAction.isBUILD_DOUBLE());
	}

	@Test
	public void testSetMoveAction() {
		Action monAction = new Action();
		assertFalse(monAction.isMOVE());
		
		monAction.setMoveAction(null, null, 0);
		assertTrue(monAction.isMOVE());
	}

	@Test
	public void testEqualsAction() {
		Tile t1 = Tile.parseTile("Tile_FFFFZZ060123"),
			t2 = Tile.parseTile("Tile_FFFFZZ99");
		Action a1 = Action.newBuildSimpleAction(0, 0, t1),
			a2 = Action.newBuildSimpleAction(0, 0, t1);
		assertTrue(a1.equals(a2));
		
		a2 = Action.newBuildSimpleAction(0, 0, t2);
		assertFalse(a1.equals(a2));
		
		a2 = Action.newBuildSimpleAction(1, 0, t1);
		assertFalse(a1.equals(a2));
		
		a2 = Action.newBuildDoubleAction(new Point(0,0), t1, new Point(0,0), t1);
		assertFalse(a1.equals(a2));
	}

	@Test
	public void testGetClone() {
		Tile t1 = Tile.parseTile("Tile_FFFFZZ060123"),
				t2 = Tile.parseTile("Tile_FFFFZZ99");
		Action a1 = Action.newBuildSimpleAction(0, 0, t1),
			a2 = Action.newBuildSimpleAction(0, 0, t2);
		
		a2 = a1.getClone();
		assertTrue(a1.equals(a2));
	}

}
