package ru.bosony.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.bosony.model.game.Game;
import ru.bosony.model.game.GameState;
import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 1:43:29
 * 
 */
public class TestHighOrderRocks {

	@Test
	public void test() {
		String map = ExampleMaps.HOROCK_2.getMap();
		Mine mine = new Mine(map);
		Game game = new Game(mine);
		assertEquals(game.move("RRRRRRRRRRRRLLLLLLLLLLUUURRRRRRRRLLLLLLDULLLLLUUULRLLLLRRLLRLULLLLLLLLLLDDDDDD"),
				GameState.Win);
	}

}
