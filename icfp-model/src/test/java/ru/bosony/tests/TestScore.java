package ru.bosony.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ru.bosony.model.game.Game;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;

public class TestScore {

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
		Game game = new Game(mine);
		game.move(Movement.ABORT);
		assertEquals(game.getScore(), -1);
	}

}
