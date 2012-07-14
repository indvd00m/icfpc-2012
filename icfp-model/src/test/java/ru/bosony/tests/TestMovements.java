package ru.bosony.tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;

/**
 * @author 	indvdum (gotoindvdum[at]gmail[dot]com) <br>
 * 			14.07.2012 2:29:05
 *
 */
public class TestMovements {

	@Test
	public void test() {
		/* Map 01 (6x6)
		 * 
			######
			#. *R#
			#  \.#
			#\ * #
			L  .\#
			######
		 */
		String map = 
				"######\n" + 
				"#. *R#\n" + 
				"#  \\.#\n" + 
				"#\\ * #\n" + 
				"L  .\\#\n" + 
				"######";
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
