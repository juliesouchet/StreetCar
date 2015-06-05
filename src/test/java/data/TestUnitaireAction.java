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
		Action monAction = new Action();
		assertTrue(monAction.action==Action.NONE);
	}

	//	@Test
	//	public void testnewStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") ) {
	//		Action monAction = Action.newBuildAndStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") );
	//		assertTrue(monAction.action==Action.START_TRIP_NEXT_TURN);
	//	}

	@Test
	public void testNewMoveAction() {
		Point[] chemin = unChemin();
		Point debut = new Point(-1,-1);

		Action monAction = Action.newMoveAction(chemin,10,debut);
		assertTrue(monAction.action==Action.MOVE);
		assertTrue(chemin[0].equals(new Point(0,0)));
		assertTrue(chemin[9].equals(new Point(9,9)));
		assertTrue(debut.equals(monAction.startTerminus));
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
		Action monAction = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		assertTrue(monAction.positionTile1.equals(new Point(0,1)));
		assertTrue(monAction.tile1.equals(Tile.parseTile("Tile_FFFFZZ060123")));
		assertTrue(monAction.positionTile2.equals(new Point(2,3)));
		assertTrue(monAction.tile2.equals(Tile.parseTile("Tile_FFFFZZ060123")));
	}

	@Test
	public void testGetClone() {
		Action monActionSrc = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		Action monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.equals(monActionSrc));

		Point[] chemin = unChemin();
		Point debut = new Point(-1,-1);

		monActionSrc = Action.newMoveAction(chemin,10,debut);
		//monActionCible = null;
		monActionCible = monActionSrc.getClone();
		assertFalse(monActionCible==monActionSrc);
		assertTrue(monActionCible.equals(monActionSrc));
		/*assertTrue(monActionCible.action==monActionSrc.action);
		
		monActionCible.tramwayMovement[0].equals(monActionSrc.tramwayMovement[0]);
		
		for(int i=0; i<chemin.length; i++){
			assertTrue(monActionCible.tramwayMovement[i].equals(monActionSrc.tramwayMovement[i]));
			
		}
		*/

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

		monActionA = Action.newBuildAndStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") );
		Point[] chemin = unChemin();

		Point debut = new Point(-1,-1);
		monActionB = Action.newMoveAction(chemin,10,debut);
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));

		monActionA = Action.newBuildSimpleAction(0,1, Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionA.action=Action.BUILD_DOUBLE;
		assertFalse(monActionA.equals(monActionB));
		assertFalse(monActionB.equals(monActionA));

		monActionA =Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));
		monActionB = Action.newMoveAction(chemin,10,debut);
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
		Point[] chemin = unChemin();

		Point debut = new Point(-1,-1);
		monActionB = Action.newMoveAction(chemin,10,debut);
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newMoveAction(chemin,10,debut)));

		//move<-double
		monActionA = Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"));

		monActionB = Action.newMoveAction(chemin,10,debut);
		monActionB.copy(monActionA);
		monActionA.equals(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionB.equals(Action.newBuildDoubleAction(new Point(0,1), Tile.parseTile("Tile_FFFFZZ060123"), new Point(2,3), Tile.parseTile("Tile_FFFFZZ060123"))));		

		//move<-move

		monActionA = Action.newMoveAction(chemin,10,debut);
		chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i+1,i+1);
		}
		monActionB = Action.newMoveAction(chemin,10,debut);
		monActionB.ptrTramwayMovement=3;
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));

		//startmove<-move
		monActionA= Action.newBuildAndStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") );
		monActionB = Action.newMoveAction(chemin,10,debut);
		monActionA.copy(monActionB);
		assertTrue(monActionA.equals(monActionB));
		assertTrue(monActionA.equals(Action.newMoveAction(chemin,10,debut)));

		//move<-startmove
		monActionA= Action.newBuildAndStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") );
		monActionB = Action.newMoveAction(chemin,10,debut);		
		monActionB.copy(monActionA);
		assertTrue(monActionA.equals(Action.newBuildAndStartTripNextTurnAction(new Point(1,1),Tile.parseTile("Tile_FFFFZZ060123") )));
		assertTrue(monActionA.equals(monActionB));

	}

	static private Point[] unChemin(){
		Point[] chemin = new Point[10];
		for(int i=0; i<chemin.length;i++){
			chemin[i] = new Point(i,i);
		}		return chemin;
	}

}