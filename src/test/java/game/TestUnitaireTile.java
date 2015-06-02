package test.java.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import main.java.data.Tile;
import main.java.data.Tile.Path;
import main.java.util.Direction;

import org.junit.Test;

public class TestUnitaireTile {

	@Test
	public void testTile() {
		Tile maTileA = null;
		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertTrue(maTileA.equalsStrong(Tile.parseTile("Tile_FFFFZZ2113")));
		
		Path[] monPath = null;
		monPath = new Path[10];
		int monPtrEndPath = 0;
		monPath[0]= new Path(Direction.WEST, Direction.NORTH);
		
		Tile maTileB=null;
		
		maTileB = Tile.specialNonRealTileConstructor(monPath, monPtrEndPath, maTileA);
		
		assertFalse(maTileB.equalsStrong(Tile.parseTile("Tile_FFFFZZ2113")));
		assertTrue(maTileB.isPathTo(Direction.WEST));
		assertTrue(maTileB.isPathTo(Direction.NORTH));
		assertTrue(maTileA.equalsStrong(Tile.parseTile("Tile_FFFFZZ2113")));
	}

	@Test
	public void testGetClone() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetStop() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTileID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCardinal() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTileDirection() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsBuilding() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsTerminus() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsStop() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDeckTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testTurnLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testTurnRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testTurnHalf() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDirection() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPathTo() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAccessibleDirections() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsReplaceable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBuildingName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTerminusName() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitPathTab() {
		fail("Not yet implemented");
	}

}
