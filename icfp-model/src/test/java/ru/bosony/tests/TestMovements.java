package ru.bosony.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 2:29:05
 * 
 */
public class TestMovements {

	@Test
	public void test() {
		String map = ExampleMaps.MAP_01.getMap();
		Mine mine = new Mine(map);
		assertEquals(mine.getSizeX(), 6);
		assertEquals(mine.getSizeY(), 6);
		Set<Movement> movs = mine.getAvailableRobotMovements();
		assertFalse(movs.contains(Movement.UP));
		assertFalse(movs.contains(Movement.RIGHT));
		assertTrue(movs.contains(Movement.DOWN));
		assertTrue(movs.contains(Movement.LEFT));
	}

}
