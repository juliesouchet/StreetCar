package test.java.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Point;

import main.java.data.Action;
import main.java.data.Tile;

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
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		Action monAction = Action.newMoveAction(chemin);
		assertTrue(monAction.action==Action.MOVE);
		assertTrue(chemin[0].equals(new Point(0,0)));
		assertTrue(chemin[9].equals(new Point(9,9)));
	}

	@Test
	public void testNewBuildSimpleActionPointTile() {
		Action monAction = Action.newBuildSimpleAction(new Point(1,1), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(1,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));
	}

	@Test
	public void testNewBuildSimpleActionIntIntTile() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(1,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));
	}

	@Test
	public void testNewBuildDoubleAction() {
		Action monAction =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(0,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));
		assertTrue(monAction.positionTile2.equals(new Point(2,3)));
		assertTrue(monAction.tile2.equals(Tile.parseTile("Tile_FFFFZZ060123")));
	}

	@Test
	public void testGetClone() {
		Action monActionSrc = Action.newStartTripNextTurnAction();
		Action monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.action==monActionSrc.action);
		
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionSrc = Action.newMoveAction(chemin);
		monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.action==monActionSrc.action);
		for(int i=0; i<chemin.length; i++){
			assertTrue(monActionCible.tramwayMovement[i].equals(monActionSrc.tramwayMovement[i]));
		}

		//TODO Ulysse: j'en suis ici 02/06/15 1h39
		
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
