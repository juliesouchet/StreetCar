package test.java.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.LinkedList;

import main.java.data.Deck;
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
	public void testCopy() {
		Tile maTileA = null;
		Tile maTileB = null;

		maTileA = Tile.parseTile("Tile_FFFFZZ99");
		maTileB = Tile.parseTile("Tile_FFFTZ60123");
		maTileA.copy(maTileB);
		assertTrue(maTileA.equalsStrong(maTileB));

		maTileA = Tile.parseTile("Tile_FFFFZZ99");
		maTileB = Tile.parseTile("Tile_FFFTZ60123");
		maTileB.copy(maTileA);
		assertTrue(maTileA.equalsStrong(maTileB));

	}

	@Test
	public void testSetStop() {
		Tile maTileA = Tile.parseTile("Tile_FFFTZ60123");
		assertFalse(maTileA.isStop());
		maTileA.setStop(true);
		assertTrue(maTileA.isStop());
		maTileA.setStop(false);
		assertFalse(maTileA.isStop());

	}

	@Test
	public void testEqualsObject() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		Tile maTileB = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTileA.equals(maTileB));

		maTileA.turnHalf();
		assertTrue(maTileA.equals(maTileB));

		maTileA.setStop(true);
		assertTrue(maTileA.equals(maTileB));
		

		File f			= new File(Deck.stackDirectory);
		String[] IDList	= f.list();
		LinkedList<Tile> tileList = new LinkedList<Tile>();
		Deck d = new Deck();
		for (String str: IDList)
		{
			Tile t = Tile.parseTile(str);
			if (t.isDeckTile()) tileList.add(t);
		}
		
		while(!d.isEmpty()) {
			Tile t = d.drawTile();
			assertTrue(t + " n'est pas dans la liste des tuiles", 
						tileList.contains(t));
		}
	}
	
	@Test
	public void testPathEquals() {
		Tile tuile1 = Tile.parseTile("Tile_FFFFZZ2113"),
			tuile2 = Tile.parseTile("Tile_FFFFZZ2113"); // tuile ligne droite
		Path[] pathTab1 = tuile1.getPathTab(),
			pathTab2 = tuile2.getPathTab();
		assertTrue("Les deux path égaux sont considérés différents", pathTab1[0].equals(pathTab2[0]));
		
		tuile2.turnHalf();
		pathTab2 = tuile2.getPathTab();
		assertTrue("Les deux path égaux (symétriques et opposés) sont considérés différents", pathTab1[0].equals(pathTab2[0]));
	
		tuile1 = Tile.parseTile("Tile_FFFFZZ2003"); // tuile courbe
		tuile1 = Tile.parseTile("Tile_FFFFZZ2003");
		tuile2.turnHalf();
		pathTab1 = tuile1.getPathTab();
		pathTab2 = tuile2.getPathTab();
		assertFalse("Les deux path différents (non symétriques et opposés) sont considérés différents", pathTab1[0].equals(pathTab2[0]));
	}
	
	@Test
	public void testIsEquivalent(){
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ2113"); // tuile ligne droite
		Tile maTileB = Tile.parseTile("Tile_FFFFZZ2113");
		assertTrue("les deux tuiles parsées à partir du même ID sont considérées différentes", maTileA.isEquivalent(maTileB));
		
		maTileA.turnHalf();
		assertTrue("les deux tuiles (symétriques) sont considérées différentes après rotation 1/2", maTileA.isEquivalent(maTileB));
		
		maTileA = Tile.parseTile("Tile_FFFFZZ2003"); // tuile courbe
		maTileB = Tile.parseTile("Tile_FFFFZZ2003");
		maTileB.turnHalf();
		assertFalse("les deux tuiles (non symétriques) sont considérées identiques après rotation 1/2", maTileA.isEquivalent(maTileB));
	}

	@Test
	public void testGetTileID() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTileA.getTileID().equals("Tile_FFFFZZ99"));
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertTrue(maTileA.getTileID().equals("Tile_TFFFZZ06121323"));
		maTileA.turnLeft();
		assertTrue(maTileA.getTileID().equals("Tile_TFFFZZ06121323"));
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertTrue(maTileA.getTileID().equals("Tile_TFFFZZ0401122303"));
	}

	@Test
	public void testGetCardinal() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		
		assertTrue(maTileA.getCardinal()==99);
		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(maTileA.getCardinal()==20);
		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertTrue(maTileA.getCardinal()==21);
		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		assertTrue(maTileA.getCardinal()==6);
		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		assertTrue(maTileA.getCardinal()==10);
		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		assertTrue(maTileA.getCardinal()==10);
		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		assertTrue(maTileA.getCardinal()==10);
		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FTFFBZ01");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FTFFCZ01");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FTFFDZ01");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_FTFFEZ01");
		assertTrue(maTileA.getCardinal()==1);
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(maTileA.getCardinal()==4);
		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertTrue(maTileA.getCardinal()==2);
		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertTrue(maTileA.getCardinal()==2);
		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertTrue(maTileA.getCardinal()==6);
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertTrue(maTileA.getCardinal()==6);
		maTileA.turnLeft();
		assertTrue(maTileA.getCardinal()==6);
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertTrue(maTileA.getCardinal()==4);
	
	}

	@Test
	public void testGetTileDirection() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTileA.getTileDirection().equals(Direction.WEST));
		assertFalse(maTileA.getTileDirection().equals(Direction.EAST));
		assertFalse(maTileA.getTileDirection().equals(Direction.SOUTH));
		assertFalse(maTileA.getTileDirection().equals(Direction.NORTH));
	}

	@Test
	public void testIsTree() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertFalse(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_FTFFBZ01");
		assertFalse(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_FTFFCZ01");
		assertFalse(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_FTFFDZ01");
		assertFalse(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_FTFFEZ01");
		assertFalse(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertTrue(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertTrue(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertTrue(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertTrue(maTileA.isTree());
		maTileA.turnLeft();
		assertTrue(maTileA.isTree());
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertTrue(maTileA.isTree());
	}

	@Test
	public void testIsBuilding() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertTrue(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FTFFBZ01");
		assertTrue(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FTFFCZ01");
		assertTrue(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FTFFDZ01");
		assertTrue(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_FTFFEZ01");
		assertTrue(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertFalse(maTileA.isBuilding());
		maTileA.turnLeft();
		assertFalse(maTileA.isBuilding());
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertFalse(maTileA.isBuilding());
		
	}

	@Test
	public void testIsTerminus() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertTrue(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		assertTrue(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		assertTrue(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		assertTrue(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FTFFBZ01");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FTFFCZ01");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FTFFDZ01");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_FTFFEZ01");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertFalse(maTileA.isTerminus());
		maTileA.turnLeft();
		assertFalse(maTileA.isTerminus());
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertFalse(maTileA.isTerminus());
	
	}

	@Test
	public void testIsStop() {
		Tile maTileA = Tile.parseTile("Tile_FFFTZ60123");
		assertFalse(maTileA.isStop());
		maTileA.setStop(true);
		assertTrue(maTileA.isStop());
		maTileA.setStop(false);
		assertFalse(maTileA.isStop());
	
	}

	@Test
	public void testIsEmpty() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FTFFBZ01");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FTFFCZ01");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FTFFDZ01");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_FTFFEZ01");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertFalse(maTileA.isEmpty());
		maTileA.turnLeft();
		assertFalse(maTileA.isEmpty());
		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertFalse(maTileA.isEmpty());	}

	@Test
	public void testIsDeckTile() {
		assertFalse(Tile.parseTile("Tile_FFFFZZ99").isDeckTile());
		assertTrue(Tile.parseTile("Tile_FFFFZZ2003").isDeckTile());
		assertFalse(Tile.parseTile("Tile_FFFTZ10112").isDeckTile());
		assertFalse(Tile.parseTile("Tile_FTFFAZ01").isDeckTile());
		assertTrue(Tile.parseTile("Tile_TFFFZZ040213").isDeckTile());
	}

	@Test
	public void testTurnLeft() {
		Tile maTile = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(maTile.isPathTo(Direction.WEST));
		assertTrue(maTile.isPathTo(Direction.SOUTH));
		assertFalse(maTile.isPathTo(Direction.NORTH));
		assertFalse(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.WEST));

		maTile.turnLeft();
		assertFalse(maTile.isPathTo(Direction.WEST));
		assertTrue(maTile.isPathTo(Direction.SOUTH));
		assertFalse(maTile.isPathTo(Direction.NORTH));
		assertTrue(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.SOUTH));		
		
		maTile.turnLeft();
		assertFalse(maTile.isPathTo(Direction.WEST));
		assertFalse(maTile.isPathTo(Direction.SOUTH));
		assertTrue(maTile.isPathTo(Direction.NORTH));
		assertTrue(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.EAST));
		
		maTile.turnLeft();
		assertTrue(maTile.isPathTo(Direction.WEST));
		assertFalse(maTile.isPathTo(Direction.SOUTH));
		assertTrue(maTile.isPathTo(Direction.NORTH));
		assertFalse(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.NORTH));
		
		maTile.turnLeft();
		assertTrue(maTile.equalsStrong(Tile.parseTile("Tile_FFFFZZ2003")));
		}

	@Test
	public void testTurnRight() {
		Tile maTile = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(maTile.isPathTo(Direction.WEST));
		assertTrue(maTile.isPathTo(Direction.SOUTH));
		assertFalse(maTile.isPathTo(Direction.NORTH));
		assertFalse(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.WEST));

		maTile.turnRight();
		assertTrue(maTile.isPathTo(Direction.WEST));
		assertFalse(maTile.isPathTo(Direction.SOUTH));
		assertTrue(maTile.isPathTo(Direction.NORTH));
		assertFalse(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.NORTH));
		
		
		maTile.turnRight();
		assertFalse(maTile.isPathTo(Direction.WEST));
		assertFalse(maTile.isPathTo(Direction.SOUTH));
		assertTrue(maTile.isPathTo(Direction.NORTH));
		assertTrue(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.EAST));
		
		maTile.turnRight();
		assertFalse(maTile.isPathTo(Direction.WEST));
		assertTrue(maTile.isPathTo(Direction.SOUTH));
		assertFalse(maTile.isPathTo(Direction.NORTH));
		assertTrue(maTile.isPathTo(Direction.EAST));
		assertTrue(maTile.getTileDirection().equals(Direction.SOUTH));
		
		maTile.turnRight();
		assertTrue(maTile.equalsStrong(Tile.parseTile("Tile_FFFFZZ2003")));
				
	}

	@Test
	public void testTurnHalf() {
		Tile maTile = Tile.parseTile("Tile_FFFFZZ2003");
		maTile.turnHalf();
		assertTrue(maTile.getTileDirection().equals(Direction.EAST));
		maTile.turnHalf();
		assertTrue(maTile.equalsStrong(Tile.parseTile("Tile_FFFFZZ2003")));
		
		maTile.turnRight();
		maTile.turnHalf();
		assertTrue(maTile.getTileDirection().equals(Direction.SOUTH));
	}

	@Test
	public void testSetDirection() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		maTileA.setDirection(Direction.NORTH);
		assertTrue(maTileA.getTileDirection()==Direction.NORTH);
		assertFalse(maTileA.getTileDirection()==Direction.EAST);
		assertFalse(maTileA.getTileDirection()==Direction.WEST);
		assertFalse(maTileA.getTileDirection()==Direction.SOUTH);

		maTileA.setDirection(Direction.EAST);
		assertFalse(maTileA.getTileDirection()==Direction.NORTH);
		assertTrue(maTileA.getTileDirection()==Direction.EAST);
		assertFalse(maTileA.getTileDirection()==Direction.WEST);
		assertFalse(maTileA.getTileDirection()==Direction.SOUTH);
		
		maTileA.setDirection(Direction.WEST);
		assertFalse(maTileA.getTileDirection()==Direction.NORTH);
		assertFalse(maTileA.getTileDirection()==Direction.EAST);
		assertTrue(maTileA.getTileDirection()==Direction.WEST);
		assertFalse(maTileA.getTileDirection()==Direction.SOUTH);
		
		maTileA.setDirection(Direction.SOUTH);
		assertFalse(maTileA.getTileDirection()==Direction.NORTH);
		assertFalse(maTileA.getTileDirection()==Direction.EAST);
		assertFalse(maTileA.getTileDirection()==Direction.WEST);
		assertTrue(maTileA.getTileDirection()==Direction.SOUTH);
	}

	@Test
	public void testIsPathTo() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
				
		maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));		

		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		assertFalse(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		assertFalse(maTileA.isPathTo(Direction.WEST));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertFalse(maTileA.isPathTo(Direction.WEST));
		assertFalse(maTileA.isPathTo(Direction.NORTH));
		assertFalse(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));


		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		assertFalse(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));

		maTileA.turnLeft();
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertFalse(maTileA.isPathTo(Direction.SOUTH));

		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertTrue(maTileA.isPathTo(Direction.WEST));
		assertTrue(maTileA.isPathTo(Direction.NORTH));
		assertTrue(maTileA.isPathTo(Direction.EAST));
		assertTrue(maTileA.isPathTo(Direction.SOUTH));
	}

	@Test
	public void testIsReplaceable() {

				Tile maTileA = Tile.parseTile("Tile_FFFFZZ2113"); // la tuile virage de base
				Path[] additionalPath = null;
//		assertTrue(-1==maTileA.isReplaceable(Tile.parseTile("Tile_TFFFZZ040213"), additionalPath)); //On test si elle est remplacable par la route horizontale + virage vers le bas

		
		maTileA = Tile.parseTile("Tile_FFFFZZ2003"); // la tuile virage de base
		additionalPath = null;
assertTrue(0==maTileA.isReplaceable(Tile.parseTile("Tile_FFFFZZ100203"), additionalPath)); //On test si elle est remplacable par la route horizontale + virage vers le bas
		
	}

	@Test
	public void testGetBuildingName() {
		Tile maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(maTileA.getBuildingName()==null);

		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(maTileA.getBuildingName()==null);
		
		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertTrue(maTileA.getBuildingName()==null);
		
		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertTrue(maTileA.getBuildingName().equals("A"));

		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(maTileA.getBuildingName()==null);

	}

	@Test
	public void testGetTerminusName() {
		Tile maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertTrue(1==maTileA.getTerminusName());

		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		assertTrue(-1==maTileA.getTerminusName());

		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		assertTrue(-1==maTileA.getTerminusName());
		maTileA = Tile.parseTile("Tile_FFFFZZ99");
		assertTrue(-1==maTileA.getTerminusName());
		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		assertTrue(-1==maTileA.getTerminusName());
	}

	@Test
	public void testToString() {
		Tile maTileA = Tile.parseTile("Tile_FFFTZ10101");
		assertFalse(maTileA.toString()==null);
	}

	@Test
	public void testInitPathTab() {
		Path[] monTableauDePath = Tile.initPathTab();
		for(int i=0;i<Tile.maxNbrPathInTile;i++){
			assertTrue(monTableauDePath[i].equals(new Path(Direction.WEST,Direction.WEST)));
		}
	}

	@Test
	public void testGetAccessibleDirections() {
		Tile maTileA = null;
		int mesDirectionsAccessibles;

		maTileA = Tile.parseTile("Tile_FFFFZZ99");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFFZZ2003");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));		

		maTileA = Tile.parseTile("Tile_FFFFZZ2113");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));
		maTileA = Tile.parseTile("Tile_FFFFZZ060123");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFFZZ100102");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFFZZ100103");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFFZZ100203");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFTZ10101");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFTZ10103");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFTZ10112");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FFFTZ10123");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_FTFFAZ01");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));


		maTileA = Tile.parseTile("Tile_TFFFZZ040213");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_TFFFZZ02010223");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_TFFFZZ02021203");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_TFFFZZ06031323");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_TFFFZZ06121323");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertFalse(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA.turnLeft();
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertFalse(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

		maTileA = Tile.parseTile("Tile_TFFFZZ0401122303");
		mesDirectionsAccessibles = maTileA.getAccessibleDirections();		
		assertTrue(Direction.WEST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.NORTH.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.EAST.isDirectionInList(mesDirectionsAccessibles));
		assertTrue(Direction.SOUTH.isDirectionInList(mesDirectionsAccessibles));

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
		assertFalse(maTile2.isEmpty());
		assertFalse(maTile2.isBuilding());
		assertTrue(maTile2.isDeckTile());
		assertFalse(maTile2.isStop());
		assertFalse(maTile2.isTerminus());
		assertFalse(maTile2.isTree());
		assertFalse(maTile2.isPathTo(Direction.NORTH));
		assertFalse(maTile2.isPathTo(Direction.EAST));
		assertTrue(maTile2.isPathTo(Direction.SOUTH));
		assertTrue(maTile2.isPathTo(Direction.WEST));
		assertTrue(maTile2.getTileDirection().equals(Direction.WEST));

		Tile maTile3 = Tile.parseTile("Tile_FFFFZZ2113");
		assertFalse(maTile3.isEmpty());
		assertFalse(maTile3.isBuilding());
		assertTrue(maTile3.isDeckTile());
		assertFalse(maTile3.isStop());
		assertFalse(maTile3.isTerminus());
		assertFalse(maTile3.isTree());
		assertTrue(maTile3.isPathTo(Direction.NORTH));
		assertFalse(maTile3.isPathTo(Direction.EAST));
		assertTrue(maTile3.isPathTo(Direction.SOUTH));
		assertFalse(maTile3.isPathTo(Direction.WEST));
		assertTrue(maTile3.getTileDirection().equals(Direction.WEST));

		Tile maTile4 = Tile.parseTile("Tile_FFFFZZ060123");
		assertFalse(maTile4.isEmpty());
		assertFalse(maTile4.isBuilding());
		assertTrue(maTile4.isDeckTile());
		assertFalse(maTile4.isStop());
		assertFalse(maTile4.isTerminus());
		assertFalse(maTile4.isTree());
		assertTrue(maTile4.isPathTo(Direction.NORTH));
		assertTrue(maTile4.isPathTo(Direction.EAST));
		assertTrue(maTile4.isPathTo(Direction.SOUTH));
		assertTrue(maTile4.isPathTo(Direction.WEST));
		assertTrue(maTile4.getTileDirection().equals(Direction.WEST));

		Tile maTile5 = Tile.parseTile("Tile_FFFFZZ100102");
		assertFalse(maTile5.isEmpty());
		assertFalse(maTile5.isBuilding());
		assertTrue(maTile5.isDeckTile());
		assertFalse(maTile5.isStop());
		assertFalse(maTile5.isTerminus());
		assertFalse(maTile5.isTree());
		assertTrue(maTile5.isPathTo(Direction.NORTH));
		assertTrue(maTile5.isPathTo(Direction.EAST));
		assertFalse(maTile5.isPathTo(Direction.SOUTH));
		assertTrue(maTile5.isPathTo(Direction.WEST));
		assertTrue(maTile5.getTileDirection().equals(Direction.WEST));		

		Tile maTile6 = Tile.parseTile("Tile_FFFFZZ100103");
		assertFalse(maTile6.isEmpty());
		assertFalse(maTile6.isBuilding());
		assertTrue(maTile6.isDeckTile());
		assertFalse(maTile6.isStop());
		assertFalse(maTile6.isTerminus());
		assertFalse(maTile6.isTree());
		assertTrue(maTile6.isPathTo(Direction.NORTH));
		assertFalse(maTile6.isPathTo(Direction.EAST));
		assertTrue(maTile6.isPathTo(Direction.SOUTH));
		assertTrue(maTile6.isPathTo(Direction.WEST));
		assertTrue(maTile6.getTileDirection().equals(Direction.WEST));			

		Tile maTile7 = Tile.parseTile("Tile_FFFFZZ100203");
		assertFalse(maTile7.isEmpty());
		assertFalse(maTile7.isBuilding());
		assertTrue(maTile7.isDeckTile());
		assertFalse(maTile7.isStop());
		assertFalse(maTile7.isTerminus());
		assertFalse(maTile7.isTree());
		assertFalse(maTile7.isPathTo(Direction.NORTH));
		assertTrue(maTile7.isPathTo(Direction.EAST));
		assertTrue(maTile7.isPathTo(Direction.SOUTH));
		assertTrue(maTile7.isPathTo(Direction.WEST));
		assertTrue(maTile7.getTileDirection().equals(Direction.WEST));	


		Tile maTile8 = Tile.parseTile("Tile_FFFTZ10101");
		assertFalse(maTile8.isEmpty());
		assertFalse(maTile8.isBuilding());
		assertFalse(maTile8.isDeckTile());
		assertFalse(maTile8.isStop());
		assertTrue(maTile8.isTerminus());
		assertFalse(maTile8.isTree());
		assertTrue(maTile8.isPathTo(Direction.NORTH));
		assertFalse(maTile8.isPathTo(Direction.EAST));
		assertFalse(maTile8.isPathTo(Direction.SOUTH));
		assertTrue(maTile8.isPathTo(Direction.WEST));
		assertTrue(maTile8.getTileDirection().equals(Direction.WEST));			

		Tile maTile9 = Tile.parseTile("Tile_FFFTZ10103");
		assertFalse(maTile9.isEmpty());
		assertFalse(maTile9.isBuilding());
		assertFalse(maTile9.isDeckTile());
		assertFalse(maTile9.isStop());
		assertTrue(maTile9.isTerminus());
		assertFalse(maTile9.isTree());
		assertFalse(maTile9.isPathTo(Direction.NORTH));
		assertFalse(maTile9.isPathTo(Direction.EAST));
		assertTrue(maTile9.isPathTo(Direction.SOUTH));
		assertTrue(maTile9.isPathTo(Direction.WEST));
		assertTrue(maTile9.getTileDirection().equals(Direction.WEST));	

		Tile maTile10 = Tile.parseTile("Tile_FFFTZ10112");
		assertFalse(maTile10.isEmpty());
		assertFalse(maTile10.isBuilding());
		assertFalse(maTile10.isDeckTile());
		assertFalse(maTile10.isStop());
		assertTrue(maTile10.isTerminus());
		assertFalse(maTile10.isTree());
		assertTrue(maTile10.isPathTo(Direction.NORTH));
		assertTrue(maTile10.isPathTo(Direction.EAST));
		assertFalse(maTile10.isPathTo(Direction.SOUTH));
		assertFalse(maTile10.isPathTo(Direction.WEST));
		assertTrue(maTile10.getTileDirection().equals(Direction.WEST));		

		Tile maTile11 = Tile.parseTile("Tile_FFFTZ10123");
		assertFalse(maTile11.isEmpty());
		assertFalse(maTile11.isBuilding());
		assertFalse(maTile11.isDeckTile());
		assertFalse(maTile11.isStop());
		assertTrue(maTile11.isTerminus());
		assertFalse(maTile11.isTree());
		assertFalse(maTile11.isPathTo(Direction.NORTH));
		assertTrue(maTile11.isPathTo(Direction.EAST));
		assertTrue(maTile11.isPathTo(Direction.SOUTH));
		assertFalse(maTile11.isPathTo(Direction.WEST));
		assertTrue(maTile11.getTileDirection().equals(Direction.WEST));	


		/*======================================================================================*/		

		Tile maTile12 = Tile.parseTile("Tile_FFFTZ20101");
		assertFalse(maTile12.isEmpty());
		assertFalse(maTile12.isBuilding());
		assertFalse(maTile12.isDeckTile());
		assertFalse(maTile12.isStop());
		assertTrue(maTile12.isTerminus());
		assertFalse(maTile12.isTree());
		assertTrue(maTile12.isPathTo(Direction.NORTH));
		assertFalse(maTile12.isPathTo(Direction.EAST));
		assertFalse(maTile12.isPathTo(Direction.SOUTH));
		assertTrue(maTile12.isPathTo(Direction.WEST));
		assertTrue(maTile12.getTileDirection().equals(Direction.WEST));			

		Tile maTile13 = Tile.parseTile("Tile_FFFTZ20103");
		assertFalse(maTile13.isEmpty());
		assertFalse(maTile13.isBuilding());
		assertFalse(maTile13.isDeckTile());
		assertFalse(maTile13.isStop());
		assertTrue(maTile13.isTerminus());
		assertFalse(maTile13.isTree());
		assertFalse(maTile13.isPathTo(Direction.NORTH));
		assertFalse(maTile13.isPathTo(Direction.EAST));
		assertTrue(maTile13.isPathTo(Direction.SOUTH));
		assertTrue(maTile13.isPathTo(Direction.WEST));
		assertTrue(maTile13.getTileDirection().equals(Direction.WEST));	

		Tile maTile14 = Tile.parseTile("Tile_FFFTZ20112");
		assertFalse(maTile14.isEmpty());
		assertFalse(maTile14.isBuilding());
		assertFalse(maTile14.isDeckTile());
		assertFalse(maTile14.isStop());
		assertTrue(maTile14.isTerminus());
		assertFalse(maTile14.isTree());
		assertTrue(maTile14.isPathTo(Direction.NORTH));
		assertTrue(maTile14.isPathTo(Direction.EAST));
		assertFalse(maTile14.isPathTo(Direction.SOUTH));
		assertFalse(maTile14.isPathTo(Direction.WEST));
		assertTrue(maTile14.getTileDirection().equals(Direction.WEST));		

		Tile maTile15 = Tile.parseTile("Tile_FFFTZ20123");
		assertFalse(maTile15.isEmpty());
		assertFalse(maTile15.isBuilding());
		assertFalse(maTile15.isDeckTile());
		assertFalse(maTile15.isStop());
		assertTrue(maTile15.isTerminus());
		assertFalse(maTile15.isTree());
		assertFalse(maTile15.isPathTo(Direction.NORTH));
		assertTrue(maTile15.isPathTo(Direction.EAST));
		assertTrue(maTile15.isPathTo(Direction.SOUTH));
		assertFalse(maTile15.isPathTo(Direction.WEST));
		assertTrue(maTile15.getTileDirection().equals(Direction.WEST));	
		/*======================================================================================*/		



		/*======================================================================================*/		

		Tile maTile16 = Tile.parseTile("Tile_FFFTZ30101");
		assertFalse(maTile16.isEmpty());
		assertFalse(maTile16.isBuilding());
		assertFalse(maTile16.isDeckTile());
		assertFalse(maTile16.isStop());
		assertTrue(maTile16.isTerminus());
		assertFalse(maTile16.isTree());
		assertTrue(maTile16.isPathTo(Direction.NORTH));
		assertFalse(maTile16.isPathTo(Direction.EAST));
		assertFalse(maTile16.isPathTo(Direction.SOUTH));
		assertTrue(maTile16.isPathTo(Direction.WEST));
		assertTrue(maTile16.getTileDirection().equals(Direction.WEST));			

		Tile maTile17 = Tile.parseTile("Tile_FFFTZ30103");
		assertFalse(maTile17.isEmpty());
		assertFalse(maTile17.isBuilding());
		assertFalse(maTile17.isDeckTile());
		assertFalse(maTile17.isStop());
		assertTrue(maTile17.isTerminus());
		assertFalse(maTile17.isTree());
		assertFalse(maTile17.isPathTo(Direction.NORTH));
		assertFalse(maTile17.isPathTo(Direction.EAST));
		assertTrue(maTile17.isPathTo(Direction.SOUTH));
		assertTrue(maTile17.isPathTo(Direction.WEST));
		assertTrue(maTile17.getTileDirection().equals(Direction.WEST));	

		Tile maTile18 = Tile.parseTile("Tile_FFFTZ30112");
		assertFalse(maTile18.isEmpty());
		assertFalse(maTile18.isBuilding());
		assertFalse(maTile18.isDeckTile());
		assertFalse(maTile18.isStop());
		assertTrue(maTile18.isTerminus());
		assertFalse(maTile18.isTree());
		assertTrue(maTile18.isPathTo(Direction.NORTH));
		assertTrue(maTile18.isPathTo(Direction.EAST));
		assertFalse(maTile18.isPathTo(Direction.SOUTH));
		assertFalse(maTile18.isPathTo(Direction.WEST));
		assertTrue(maTile18.getTileDirection().equals(Direction.WEST));		

		Tile maTile19 = Tile.parseTile("Tile_FFFTZ30123");
		assertFalse(maTile19.isEmpty());
		assertFalse(maTile19.isBuilding());
		assertFalse(maTile19.isDeckTile());
		assertFalse(maTile19.isStop());
		assertTrue(maTile19.isTerminus());
		assertFalse(maTile19.isTree());
		assertFalse(maTile19.isPathTo(Direction.NORTH));
		assertTrue(maTile19.isPathTo(Direction.EAST));
		assertTrue(maTile19.isPathTo(Direction.SOUTH));
		assertFalse(maTile19.isPathTo(Direction.WEST));
		assertTrue(maTile19.getTileDirection().equals(Direction.WEST));	
		/*======================================================================================*/
		/*======================================================================================*/		

		Tile maTile20 = Tile.parseTile("Tile_FFFTZ40101");
		assertFalse(maTile20.isEmpty());
		assertFalse(maTile20.isBuilding());
		assertFalse(maTile20.isDeckTile());
		assertFalse(maTile20.isStop());
		assertTrue(maTile20.isTerminus());
		assertFalse(maTile20.isTree());
		assertTrue(maTile20.isPathTo(Direction.NORTH));
		assertFalse(maTile20.isPathTo(Direction.EAST));
		assertFalse(maTile20.isPathTo(Direction.SOUTH));
		assertTrue(maTile20.isPathTo(Direction.WEST));
		assertTrue(maTile20.getTileDirection().equals(Direction.WEST));			

		Tile maTile21 = Tile.parseTile("Tile_FFFTZ40103");
		assertFalse(maTile21.isEmpty());
		assertFalse(maTile21.isBuilding());
		assertFalse(maTile21.isDeckTile());
		assertFalse(maTile21.isStop());
		assertTrue(maTile21.isTerminus());
		assertFalse(maTile21.isTree());
		assertFalse(maTile21.isPathTo(Direction.NORTH));
		assertFalse(maTile21.isPathTo(Direction.EAST));
		assertTrue(maTile21.isPathTo(Direction.SOUTH));
		assertTrue(maTile21.isPathTo(Direction.WEST));
		assertTrue(maTile21.getTileDirection().equals(Direction.WEST));	

		Tile maTile22 = Tile.parseTile("Tile_FFFTZ40112");
		assertFalse(maTile22.isEmpty());
		assertFalse(maTile22.isBuilding());
		assertFalse(maTile22.isDeckTile());
		assertFalse(maTile22.isStop());
		assertTrue(maTile22.isTerminus());
		assertFalse(maTile22.isTree());
		assertTrue(maTile22.isPathTo(Direction.NORTH));
		assertTrue(maTile22.isPathTo(Direction.EAST));
		assertFalse(maTile22.isPathTo(Direction.SOUTH));
		assertFalse(maTile22.isPathTo(Direction.WEST));
		assertTrue(maTile22.getTileDirection().equals(Direction.WEST));		

		Tile maTile23 = Tile.parseTile("Tile_FFFTZ40123");
		assertFalse(maTile23.isEmpty());
		assertFalse(maTile23.isBuilding());
		assertFalse(maTile23.isDeckTile());
		assertFalse(maTile23.isStop());
		assertTrue(maTile23.isTerminus());
		assertFalse(maTile23.isTree());
		assertFalse(maTile23.isPathTo(Direction.NORTH));
		assertTrue(maTile23.isPathTo(Direction.EAST));
		assertTrue(maTile23.isPathTo(Direction.SOUTH));
		assertFalse(maTile23.isPathTo(Direction.WEST));
		assertTrue(maTile23.getTileDirection().equals(Direction.WEST));	
		/*======================================================================================*/
		/*======================================================================================*/		

		Tile maTile24 = Tile.parseTile("Tile_FFFTZ50101");
		assertFalse(maTile24.isEmpty());
		assertFalse(maTile24.isBuilding());
		assertFalse(maTile24.isDeckTile());
		assertFalse(maTile24.isStop());
		assertTrue(maTile24.isTerminus());
		assertFalse(maTile24.isTree());
		assertTrue(maTile24.isPathTo(Direction.NORTH));
		assertFalse(maTile24.isPathTo(Direction.EAST));
		assertFalse(maTile24.isPathTo(Direction.SOUTH));
		assertTrue(maTile24.isPathTo(Direction.WEST));
		assertTrue(maTile24.getTileDirection().equals(Direction.WEST));			

		Tile maTile25 = Tile.parseTile("Tile_FFFTZ50103");
		assertFalse(maTile25.isEmpty());
		assertFalse(maTile25.isBuilding());
		assertFalse(maTile25.isDeckTile());
		assertFalse(maTile25.isStop());
		assertTrue(maTile25.isTerminus());
		assertFalse(maTile25.isTree());
		assertFalse(maTile25.isPathTo(Direction.NORTH));
		assertFalse(maTile25.isPathTo(Direction.EAST));
		assertTrue(maTile25.isPathTo(Direction.SOUTH));
		assertTrue(maTile25.isPathTo(Direction.WEST));
		assertTrue(maTile25.getTileDirection().equals(Direction.WEST));	

		Tile maTile26 = Tile.parseTile("Tile_FFFTZ50112");
		assertFalse(maTile26.isEmpty());
		assertFalse(maTile26.isBuilding());
		assertFalse(maTile26.isDeckTile());
		assertFalse(maTile26.isStop());
		assertTrue(maTile26.isTerminus());
		assertFalse(maTile26.isTree());
		assertTrue(maTile26.isPathTo(Direction.NORTH));
		assertTrue(maTile26.isPathTo(Direction.EAST));
		assertFalse(maTile26.isPathTo(Direction.SOUTH));
		assertFalse(maTile26.isPathTo(Direction.WEST));
		assertTrue(maTile26.getTileDirection().equals(Direction.WEST));		

		Tile maTile27 = Tile.parseTile("Tile_FFFTZ50123");
		assertFalse(maTile27.isEmpty());
		assertFalse(maTile27.isBuilding());
		assertFalse(maTile27.isDeckTile());
		assertFalse(maTile27.isStop());
		assertTrue(maTile27.isTerminus());
		assertFalse(maTile27.isTree());
		assertFalse(maTile27.isPathTo(Direction.NORTH));
		assertTrue(maTile27.isPathTo(Direction.EAST));
		assertTrue(maTile27.isPathTo(Direction.SOUTH));
		assertFalse(maTile27.isPathTo(Direction.WEST));
		assertTrue(maTile27.getTileDirection().equals(Direction.WEST));	
		/*======================================================================================*/
		/*======================================================================================*/		

		Tile maTile28 = Tile.parseTile("Tile_FFFTZ60101");
		assertFalse(maTile28.isEmpty());
		assertFalse(maTile28.isBuilding());
		assertFalse(maTile28.isDeckTile());
		assertFalse(maTile28.isStop());
		assertTrue(maTile28.isTerminus());
		assertFalse(maTile28.isTree());
		assertTrue(maTile28.isPathTo(Direction.NORTH));
		assertFalse(maTile28.isPathTo(Direction.EAST));
		assertFalse(maTile28.isPathTo(Direction.SOUTH));
		assertTrue(maTile28.isPathTo(Direction.WEST));
		assertTrue(maTile28.getTileDirection().equals(Direction.WEST));			

		Tile maTile29 = Tile.parseTile("Tile_FFFTZ60103");
		assertFalse(maTile29.isEmpty());
		assertFalse(maTile29.isBuilding());
		assertFalse(maTile29.isDeckTile());
		assertFalse(maTile29.isStop());
		assertTrue(maTile29.isTerminus());
		assertFalse(maTile29.isTree());
		assertFalse(maTile29.isPathTo(Direction.NORTH));
		assertFalse(maTile29.isPathTo(Direction.EAST));
		assertTrue(maTile29.isPathTo(Direction.SOUTH));
		assertTrue(maTile29.isPathTo(Direction.WEST));
		assertTrue(maTile29.getTileDirection().equals(Direction.WEST));	

		Tile maTile30 = Tile.parseTile("Tile_FFFTZ60112");
		assertFalse(maTile30.isEmpty());
		assertFalse(maTile30.isBuilding());
		assertFalse(maTile30.isDeckTile());
		assertFalse(maTile30.isStop());
		assertTrue(maTile30.isTerminus());
		assertFalse(maTile30.isTree());
		assertTrue(maTile30.isPathTo(Direction.NORTH));
		assertTrue(maTile30.isPathTo(Direction.EAST));
		assertFalse(maTile30.isPathTo(Direction.SOUTH));
		assertFalse(maTile30.isPathTo(Direction.WEST));
		assertTrue(maTile30.getTileDirection().equals(Direction.WEST));		

		Tile maTile31 = Tile.parseTile("Tile_FFFTZ60123");
		assertFalse(maTile31.isEmpty());
		assertFalse(maTile31.isBuilding());
		assertFalse(maTile31.isDeckTile());
		assertFalse(maTile31.isStop());
		assertTrue(maTile31.isTerminus());
		assertFalse(maTile31.isTree());
		assertFalse(maTile31.isPathTo(Direction.NORTH));
		assertTrue(maTile31.isPathTo(Direction.EAST));
		assertTrue(maTile31.isPathTo(Direction.SOUTH));
		assertFalse(maTile31.isPathTo(Direction.WEST));
		assertTrue(maTile31.getTileDirection().equals(Direction.WEST));	
		/*======================================================================================*/

		Tile maTile32 = Tile.parseTile("Tile_FTFFAZ01");
		assertFalse(maTile32.isEmpty());
		assertTrue(maTile32.isBuilding());
		assertFalse(maTile32.isDeckTile());
		assertFalse(maTile32.isStop());
		assertFalse(maTile32.isTerminus());
		assertFalse(maTile32.isTree());
		assertFalse(maTile32.isPathTo(Direction.NORTH));
		assertFalse(maTile32.isPathTo(Direction.EAST));
		assertFalse(maTile32.isPathTo(Direction.SOUTH));
		assertFalse(maTile32.isPathTo(Direction.WEST));
		assertTrue(maTile32.getTileDirection().equals(Direction.WEST));

		Tile maTile33 = Tile.parseTile("Tile_FTFFBZ01");
		assertFalse(maTile33.isEmpty());
		assertTrue(maTile33.isBuilding());
		assertFalse(maTile33.isDeckTile());
		assertFalse(maTile33.isStop());
		assertFalse(maTile33.isTerminus());
		assertFalse(maTile33.isTree());
		assertFalse(maTile33.isPathTo(Direction.NORTH));
		assertFalse(maTile33.isPathTo(Direction.EAST));
		assertFalse(maTile33.isPathTo(Direction.SOUTH));
		assertFalse(maTile33.isPathTo(Direction.WEST));
		assertTrue(maTile33.getTileDirection().equals(Direction.WEST));

		Tile maTile34 = Tile.parseTile("Tile_FTFFCZ01");
		assertFalse(maTile34.isEmpty());
		assertTrue(maTile34.isBuilding());
		assertFalse(maTile34.isDeckTile());
		assertFalse(maTile34.isStop());
		assertFalse(maTile34.isTerminus());
		assertFalse(maTile34.isTree());
		assertFalse(maTile34.isPathTo(Direction.NORTH));
		assertFalse(maTile34.isPathTo(Direction.EAST));
		assertFalse(maTile34.isPathTo(Direction.SOUTH));
		assertFalse(maTile34.isPathTo(Direction.WEST));
		assertTrue(maTile34.getTileDirection().equals(Direction.WEST));

		Tile maTile35 = Tile.parseTile("Tile_FTFFDZ01");
		assertFalse(maTile35.isEmpty());
		assertTrue(maTile35.isBuilding());
		assertFalse(maTile35.isDeckTile());
		assertFalse(maTile35.isStop());
		assertFalse(maTile35.isTerminus());
		assertFalse(maTile35.isTree());
		assertFalse(maTile35.isPathTo(Direction.NORTH));
		assertFalse(maTile35.isPathTo(Direction.EAST));
		assertFalse(maTile35.isPathTo(Direction.SOUTH));
		assertFalse(maTile35.isPathTo(Direction.WEST));
		assertTrue(maTile35.getTileDirection().equals(Direction.WEST));

		Tile maTile36 = Tile.parseTile("Tile_FTFFEZ01");
		assertFalse(maTile36.isEmpty());
		assertTrue(maTile36.isBuilding());
		assertFalse(maTile36.isDeckTile());
		assertFalse(maTile36.isStop());
		assertFalse(maTile36.isTerminus());
		assertFalse(maTile36.isTree());
		assertFalse(maTile36.isPathTo(Direction.NORTH));
		assertFalse(maTile36.isPathTo(Direction.EAST));
		assertFalse(maTile36.isPathTo(Direction.SOUTH));
		assertFalse(maTile36.isPathTo(Direction.WEST));
		assertTrue(maTile36.getTileDirection().equals(Direction.WEST));

		Tile maTile37 = Tile.parseTile("Tile_FTFFFZ01");
		assertFalse(maTile37.isEmpty());
		assertTrue(maTile37.isBuilding());
		assertFalse(maTile37.isDeckTile());
		assertFalse(maTile37.isStop());
		assertFalse(maTile37.isTerminus());
		assertFalse(maTile37.isTree());
		assertFalse(maTile37.isPathTo(Direction.NORTH));
		assertFalse(maTile37.isPathTo(Direction.EAST));
		assertFalse(maTile37.isPathTo(Direction.SOUTH));
		assertFalse(maTile37.isPathTo(Direction.WEST));
		assertTrue(maTile37.getTileDirection().equals(Direction.WEST));

		Tile maTile38 = Tile.parseTile("Tile_FTFFGZ01");
		assertFalse(maTile38.isEmpty());
		assertTrue(maTile38.isBuilding());
		assertFalse(maTile38.isDeckTile());
		assertFalse(maTile38.isStop());
		assertFalse(maTile38.isTerminus());
		assertFalse(maTile38.isTree());
		assertFalse(maTile38.isPathTo(Direction.NORTH));
		assertFalse(maTile38.isPathTo(Direction.EAST));
		assertFalse(maTile38.isPathTo(Direction.SOUTH));
		assertFalse(maTile38.isPathTo(Direction.WEST));
		assertTrue(maTile38.getTileDirection().equals(Direction.WEST));

		Tile maTile39 = Tile.parseTile("Tile_FTFFHZ01");
		assertFalse(maTile39.isEmpty());
		assertTrue(maTile39.isBuilding());
		assertFalse(maTile39.isDeckTile());
		assertFalse(maTile39.isStop());
		assertFalse(maTile39.isTerminus());
		assertFalse(maTile39.isTree());
		assertFalse(maTile39.isPathTo(Direction.NORTH));
		assertFalse(maTile39.isPathTo(Direction.EAST));
		assertFalse(maTile39.isPathTo(Direction.SOUTH));
		assertFalse(maTile39.isPathTo(Direction.WEST));
		assertTrue(maTile39.getTileDirection().equals(Direction.WEST));

		Tile maTile40 = Tile.parseTile("Tile_FTFFIZ01");
		assertFalse(maTile40.isEmpty());
		assertTrue(maTile40.isBuilding());
		assertFalse(maTile40.isDeckTile());
		assertFalse(maTile40.isStop());
		assertFalse(maTile40.isTerminus());
		assertFalse(maTile40.isTree());
		assertFalse(maTile40.isPathTo(Direction.NORTH));
		assertFalse(maTile40.isPathTo(Direction.EAST));
		assertFalse(maTile40.isPathTo(Direction.SOUTH));
		assertFalse(maTile40.isPathTo(Direction.WEST));
		assertTrue(maTile40.getTileDirection().equals(Direction.WEST));

		Tile maTile41 = Tile.parseTile("Tile_FTFFKZ01");
		assertFalse(maTile41.isEmpty());
		assertTrue(maTile41.isBuilding());
		assertFalse(maTile41.isDeckTile());
		assertFalse(maTile41.isStop());
		assertFalse(maTile41.isTerminus());
		assertFalse(maTile41.isTree());
		assertFalse(maTile41.isPathTo(Direction.NORTH));
		assertFalse(maTile41.isPathTo(Direction.EAST));
		assertFalse(maTile41.isPathTo(Direction.SOUTH));
		assertFalse(maTile41.isPathTo(Direction.WEST));
		assertTrue(maTile41.getTileDirection().equals(Direction.WEST));

		Tile maTile42 = Tile.parseTile("Tile_FTFFLZ01");
		assertFalse(maTile42.isEmpty());
		assertTrue(maTile42.isBuilding());
		assertFalse(maTile42.isDeckTile());
		assertFalse(maTile42.isStop());
		assertFalse(maTile42.isTerminus());
		assertFalse(maTile42.isTree());
		assertFalse(maTile42.isPathTo(Direction.NORTH));
		assertFalse(maTile42.isPathTo(Direction.EAST));
		assertFalse(maTile42.isPathTo(Direction.SOUTH));
		assertFalse(maTile42.isPathTo(Direction.WEST));
		assertTrue(maTile42.getTileDirection().equals(Direction.WEST));

		Tile maTile43 = Tile.parseTile("Tile_FTFFMZ01");
		assertFalse(maTile43.isEmpty());
		assertTrue(maTile43.isBuilding());
		assertFalse(maTile43.isDeckTile());
		assertFalse(maTile43.isStop());
		assertFalse(maTile43.isTerminus());
		assertFalse(maTile43.isTree());
		assertFalse(maTile43.isPathTo(Direction.NORTH));
		assertFalse(maTile43.isPathTo(Direction.EAST));
		assertFalse(maTile43.isPathTo(Direction.SOUTH));
		assertFalse(maTile43.isPathTo(Direction.WEST));
		assertTrue(maTile43.getTileDirection().equals(Direction.WEST));

		Tile maTile44 = Tile.parseTile("Tile_TFFFZZ040213");
		assertFalse(maTile44.isEmpty());
		assertFalse(maTile44.isBuilding());
		assertTrue(maTile44.isDeckTile());
		assertFalse(maTile44.isStop());
		assertFalse(maTile44.isTerminus());
		assertTrue(maTile44.isTree());
		assertTrue(maTile44.isPathTo(Direction.NORTH));
		assertTrue(maTile44.isPathTo(Direction.EAST));
		assertTrue(maTile44.isPathTo(Direction.SOUTH));
		assertTrue(maTile44.isPathTo(Direction.WEST));
		assertTrue(maTile44.getTileDirection().equals(Direction.WEST));

		Tile maTile45 = Tile.parseTile("Tile_TFFFZZ02010223");
		assertFalse(maTile45.isEmpty());
		assertFalse(maTile45.isBuilding());
		assertTrue(maTile45.isDeckTile());
		assertFalse(maTile45.isStop());
		assertFalse(maTile45.isTerminus());
		assertTrue(maTile45.isTree());
		assertTrue(maTile45.isPathTo(Direction.NORTH));
		assertTrue(maTile45.isPathTo(Direction.EAST));
		assertTrue(maTile45.isPathTo(Direction.SOUTH));
		assertTrue(maTile45.isPathTo(Direction.WEST));
		assertTrue(maTile45.getTileDirection().equals(Direction.WEST));

		Tile maTile46 = Tile.parseTile("Tile_TFFFZZ02021203");
		assertFalse(maTile46.isEmpty());
		assertFalse(maTile46.isBuilding());
		assertTrue(maTile46.isDeckTile());
		assertFalse(maTile46.isStop());
		assertFalse(maTile46.isTerminus());
		assertTrue(maTile46.isTree());
		assertTrue(maTile46.isPathTo(Direction.NORTH));
		assertTrue(maTile46.isPathTo(Direction.EAST));
		assertTrue(maTile46.isPathTo(Direction.SOUTH));
		assertTrue(maTile46.isPathTo(Direction.WEST));
		assertTrue(maTile46.getTileDirection().equals(Direction.WEST));

		Tile maTile47 = Tile.parseTile("Tile_TFFFZZ06031323");
		assertFalse(maTile47.isEmpty());
		assertFalse(maTile47.isBuilding());
		assertTrue(maTile47.isDeckTile());
		assertFalse(maTile47.isStop());
		assertFalse(maTile47.isTerminus());
		assertTrue(maTile47.isTree());
		assertTrue(maTile47.isPathTo(Direction.NORTH));
		assertTrue(maTile47.isPathTo(Direction.EAST));
		assertTrue(maTile47.isPathTo(Direction.SOUTH));
		assertTrue(maTile47.isPathTo(Direction.WEST));
		assertTrue(maTile47.getTileDirection().equals(Direction.WEST));

		Tile maTile48 = Tile.parseTile("Tile_TFFFZZ06121323");
		assertFalse(maTile48.isEmpty());
		assertFalse(maTile48.isBuilding());
		assertTrue(maTile48.isDeckTile());
		assertFalse(maTile48.isStop());
		assertFalse(maTile48.isTerminus());
		assertTrue(maTile48.isTree());
		assertTrue(maTile48.isPathTo(Direction.NORTH));
		assertTrue(maTile48.isPathTo(Direction.EAST));
		assertTrue(maTile48.isPathTo(Direction.SOUTH));
		assertFalse(maTile48.isPathTo(Direction.WEST));
		assertTrue(maTile48.getTileDirection().equals(Direction.WEST));		

		Tile maTile49 = Tile.parseTile("Tile_TFFFZZ0401122303");
		assertFalse(maTile49.isEmpty());
		assertFalse(maTile49.isBuilding());
		assertTrue(maTile49.isDeckTile());
		assertFalse(maTile49.isStop());
		assertFalse(maTile49.isTerminus());
		assertTrue(maTile49.isTree());
		assertTrue(maTile49.isPathTo(Direction.NORTH));
		assertTrue(maTile49.isPathTo(Direction.EAST));
		assertTrue(maTile49.isPathTo(Direction.SOUTH));
		assertTrue(maTile49.isPathTo(Direction.WEST));
		assertTrue(maTile49.getTileDirection().equals(Direction.WEST));		

	}

	@Test
	public void TestUniqueRotations() {
		Tile maTuileSymetrique = Tile.parseTile("Tile_FFFFZZ2113"), // tuile ligne droite
			maTuileNonSymetrique = Tile.parseTile("Tile_FFFFZZ2003"); // tuile courbe
		Tile[] resTabSym = new Tile[4],
			resTabNonSym = new Tile[4];
		for(int i = 0; i < 4; i++) {
			resTabNonSym[i] = Tile.parseTile("Tile_FFFFZZ99");
			resTabSym[i] = Tile.parseTile("Tile_FFFFZZ99");
		}
		int nbRotationsSym = maTuileSymetrique.getUniqueRotationList(resTabSym),
			nbRotationsNonSym = maTuileNonSymetrique.getUniqueRotationList(resTabNonSym);
		assertTrue("la tuile symétrique a " + nbRotationsSym + " rotations uniques", nbRotationsSym==2);
		assertTrue("la tuile non symétrique a " + nbRotationsNonSym + " rotations uniques", nbRotationsNonSym==4);
	}

}
