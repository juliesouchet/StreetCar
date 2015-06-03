package test.java.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

//	@Test
//	public void testNewStartTripNextTurnAction() {
//		Action monAction = Action.newStartTripNextTurnAction();
//		assertTrue(monAction.action==Action.START_TRIP_NEXT_TURN);
//	}

	@Test
	public void testNewMoveAction() {

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
		Action monActionSrc = Action.newBuildSimpleAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123"));
		Action monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.action==monActionSrc.action);

		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionSrc = Action.newMoveAction(chemin,10);
		monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.action==monActionSrc.action);
		for(int i=0; i<chemin.length; i++){
			assertTrue(monActionCible.tramwayMovement[i].equals(monActionSrc.tramwayMovement[i]));
		}


	}

	@Test
	public void testIsConstructing() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.isConstructing());
		monAction =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.isConstructing());
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monAction = Action.newMoveAction(chemin);
		assertFalse(monAction.isConstructing());
		monAction = Action.newStartTripNextTurnAction();
		assertFalse(monAction.isConstructing());
		monAction = new Action();
		assertFalse(monAction.isConstructing());

	}

	@Test
	public void testIsSimpleConstructing() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.isSimpleConstructing());
		monAction =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monAction.isSimpleConstructing());
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monAction = Action.newMoveAction(chemin);
		assertFalse(monAction.isSimpleConstructing());
		monAction = Action.newStartTripNextTurnAction();
		assertFalse(monAction.isSimpleConstructing());
		monAction = new Action();
		assertFalse(monAction.isSimpleConstructing());
	}

	@Test
	public void testIsMoving() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monAction.isMoving());
		monAction =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monAction.isMoving());
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monAction = Action.newMoveAction(chemin);
		assertTrue(monAction.isMoving());
		monAction = Action.newStartTripNextTurnAction();
		assertTrue(monAction.isMoving());
		monAction = new Action();
		assertFalse(monAction.isMoving());
	}

	@Test
	public void testToString() {
		Action monAction = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monAction.toString()==null);
	}

	@Test
	public void testEqualsAction() {
		Action monActionA = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		Action monActionB = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionB.equals(monActionA));
		
		monActionA = Action.newBuildSimpleAction(0,1, Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));
		
		monActionA = Action.newBuildSimpleAction(0,1, Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));

		
		monActionA =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionB.equals(monActionA));
		
		monActionA = Action.newBuildSimpleAction(0,1, Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB.action=Action.BUILD_SIMPLE;
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionB.equals(monActionA));
		
		monActionA = Action.newStartTripNextTurnAction();
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionB = Action.newMoveAction(chemin);
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));

		monActionA = Action.newBuildSimpleAction(0,1, Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionA.action=Action.BUILD_DOUBLE;
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));
		
		monActionA =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB = Action.newMoveAction(chemin);
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));
		
	}

	@Test
	public void testCopy() {
		//simple<-simple
		Action monActionA = Action.newBuildSimpleAction(1,1, Tile.parseTile("Tile_FFFFZZ060123"));
		Action monActionB = Action.newBuildSimpleAction(0,0, Tile.parseTile("Tile_FFFFZZ100103"));
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(Action.newBuildSimpleAction(0,0, Tile.parseTile("Tile_FFFFZZ100103"))));
		assertTrue(monActionA.equals(monActionB));

		//simple<-double
		monActionB = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"))));

		//double<-simple
		monActionA = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB = Action.newBuildSimpleAction(0,0, Tile.parseTile("Tile_FFFFZZ100103"));
		monActionB.copy(monActionA);
		assertTrue(monActionB.equals(monActionA));
		assertTrue(monActionB.equals(Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"))));

		//double<-move
		monActionA = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionB = Action.newMoveAction(chemin);
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newMoveAction(chemin)));
		
		//move<-double
		monActionA = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionB = Action.newMoveAction(chemin);
		monActionB.copy(monActionA);
		monActionA.equals(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionB.equals(Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"))));		
		
		//move<-move
		chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}
		monActionA = Action.newMoveAction(chemin);
		chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i+1,i+1);
		}
		monActionB = Action.newMoveAction(chemin);
		monActionB.ptrTramwayMovement=3;
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));

		//startmove<-move
		monActionA= Action.newStartTripNextTurnAction();
		monActionB = Action.newMoveAction(chemin);
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newMoveAction(chemin)));

		//move<-startmove
		monActionA= Action.newStartTripNextTurnAction();
		monActionB = Action.newMoveAction(chemin);		
		monActionB.copy(monActionA);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newStartTripNextTurnAction()));
		
	}

}
