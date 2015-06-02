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
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		Tile maTileB = null;

		maTileB = maTileA.getClone();
		
		assertTrue(maTileA.equalsStrong(maTileB));
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(maTileB.equalsStrong(Tile.parseTile("Tile_FFFFZZ2113")));

	}

	@Test
	public void testParseTile() {
		Tile maTile1 = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertFalse(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertFalse(maTile1.isPathTo(Direction.NORTH));
		assertFalse(maTile1.isPathTo(Direction.EAST));
		assertFalse(maTile1.isPathTo(Direction.SOUTH));
		assertFalse(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));
		
		Tile maTile2 = Tile.parseTile("Tile_FFFFZZ2003");
		assertFalse(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertTrue(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertFalse(maTile1.isPathTo(Direction.NORTH));
		assertFalse(maTile1.isPathTo(Direction.EAST));
		assertTrue(maTile1.isPathTo(Direction.SOUTH));
		assertTrue(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));
		
		Tile maTile3 = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertTrue(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertTrue(maTile1.isPathTo(Direction.NORTH));
		assertFalse(maTile1.isPathTo(Direction.EAST));
		assertTrue(maTile1.isPathTo(Direction.SOUTH));
		assertFalse(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));
		
		Tile maTile4 = Tile.parseTile("Tile_FFFFZZ060123");
		assertFalse(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertTrue(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertTrue(maTile1.isPathTo(Direction.NORTH));
		assertTrue(maTile1.isPathTo(Direction.EAST));
		assertTrue(maTile1.isPathTo(Direction.SOUTH));
		assertTrue(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));
		
		Tile maTile5 = Tile.parseTile("Tile_FFFFZZ100102");
		assertFalse(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertTrue(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertTrue(maTile1.isPathTo(Direction.NORTH));
		assertTrue(maTile1.isPathTo(Direction.EAST));
		assertFalse(maTile1.isPathTo(Direction.SOUTH));
		assertTrue(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));		
		
		Tile maTile6 = Tile.parseTile("Tile_FFFFZZ100103");
		assertFalse(maTile1.isEmpty());
		assertFalse(maTile1.isBuilding());
		assertTrue(maTile1.isDeckTile());
		assertFalse(maTile1.isStop());
		assertFalse(maTile1.isTerminus());
		assertFalse(maTile1.isTree());
		assertTrue(maTile1.isPathTo(Direction.NORTH));
		assertTrue(maTile1.isPathTo(Direction.EAST));
		assertFalse(maTile1.isPathTo(Direction.SOUTH));
		assertTrue(maTile1.isPathTo(Direction.WEST));
		assertTrue(maTile1.getTileDirection().equals(Direction.WEST));			
		
		Tile maTile7 = Tile.parseTile("Tile_FFFFZZ100203");
		Tile maTile8 = Tile.parseTile("Tile_FFFTZ10101");
		Tile maTile9 = Tile.parseTile("Tile_FFFTZ10103");
		Tile maTile10 = Tile.parseTile("Tile_FFFTZ10112");
		Tile maTile11 = Tile.parseTile("Tile_FFFTZ10123");
		Tile maTile12 = Tile.parseTile("Tile_FFFTZ20101");
		Tile maTile13 = Tile.parseTile("Tile_FFFTZ20103");
		Tile maTile14 = Tile.parseTile("Tile_FFFTZ20112");
		Tile maTile15 = Tile.parseTile("Tile_FFFTZ20123");
		Tile maTile16 = Tile.parseTile("Tile_FFFTZ30101");
		Tile maTile17 = Tile.parseTile("Tile_FFFTZ30103");
		Tile maTile18 = Tile.parseTile("Tile_FFFTZ30112");
		Tile maTile19 = Tile.parseTile("Tile_FFFTZ30123");
		Tile maTile20 = Tile.parseTile("Tile_FFFTZ40101");
		Tile maTile21 = Tile.parseTile("Tile_FFFTZ40103");
		Tile maTile22 = Tile.parseTile("Tile_FFFTZ40112");
		Tile maTile23 = Tile.parseTile("Tile_FFFTZ40123");
		Tile maTile24 = Tile.parseTile("Tile_FFFTZ50101");
		Tile maTile25 = Tile.parseTile("Tile_FFFTZ50103");
		Tile maTile26 = Tile.parseTile("Tile_FFFTZ50112");
		Tile maTile27 = Tile.parseTile("Tile_FFFTZ50123");
		Tile maTile28 = Tile.parseTile("Tile_FFFTZ60101");
		Tile maTile29 = Tile.parseTile("Tile_FFFTZ60103");
		Tile maTile30 = Tile.parseTile("Tile_FFFTZ60112");
		Tile maTile31 = Tile.parseTile("Tile_FFFTZ60123");
		Tile maTile32 = Tile.parseTile("Tile_FTFFAZ01");
		Tile maTile33 = Tile.parseTile("Tile_FTFFBZ01");
		Tile maTile34 = Tile.parseTile("Tile_FTFFCZ01");
		Tile maTile35 = Tile.parseTile("Tile_FTFFDZ01");
		Tile maTile36 = Tile.parseTile("Tile_FTFFEZ01");
		Tile maTile37 = Tile.parseTile("Tile_FTFFFZ01");
		Tile maTile38 = Tile.parseTile("Tile_FTFFGZ01");
		Tile maTile39 = Tile.parseTile("Tile_FTFFHZ01");
		Tile maTile40 = Tile.parseTile("Tile_FTFFIZ01");
		Tile maTile41 = Tile.parseTile("Tile_FTFFKZ01");
		Tile maTile42 = Tile.parseTile("Tile_FTFFLZ01");
		Tile maTile43 = Tile.parseTile("Tile_FTFFMZ01");
		Tile maTile44 = Tile.parseTile("Tile_TFFFZZ040213");
		Tile maTile45 = Tile.parseTile("Tile_TFFFZZ02010223");
		Tile maTile46 = Tile.parseTile("Tile_TFFFZZ02021203");
		Tile maTile47 = Tile.parseTile("Tile_TFFFZZ06031323");
		Tile maTile48 = Tile.parseTile("Tile_TFFFZZ06121323");
		Tile maTile49 = Tile.parseTile("Tile_TFFFZZ0401122303");

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
