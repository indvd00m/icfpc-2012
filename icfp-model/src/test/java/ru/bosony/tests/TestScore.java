package ru.bosony.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.bosony.model.game.Game;
import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;

public class TestScore {

	@Test
	public void test() {
		String map = ExampleMaps.MAP_01.getMap();
		Mine mine = new Mine(map);
		Game game = new Game(mine);
		game.move(Movement.ABORT);
		assertEquals(game.getScore(), -1);
	}

}
