package ru.bosony.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.bosony.model.game.Game;
import ru.bosony.model.game.GameState;
import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         15.07.2012 23:46:26
 * 
 */
public class TestBeardsAndRazors {

	@Test
	public void test() {
		String map = ExampleMaps.MAP_FROM_BEARDS_AND_RAZORS_TASK.getMap();
		Mine mine = new Mine(map);
		Game game = new Game(mine);
		assertEquals(game.move("DDDLLLLLLLURRRRRRRRRRRRRDDDDDLLLLLLLDDDLLLLLDDRRRRRRRUUURRRRDLDSDRRD"), GameState.Win);
	}

}
