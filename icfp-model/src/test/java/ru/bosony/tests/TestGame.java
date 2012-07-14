package ru.bosony.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ru.bosony.model.game.Game;
import ru.bosony.model.game.GameState;
import ru.bosony.model.mine.Mine;

/**
 * @author 	indvdum (gotoindvdum[at]gmail[dot]com) <br>
 * 			14.07.2012 23:55:07
 *
 */
public class TestGame {

	@Test
	public void testMapFromTask() {
		/* Map from task (15x15)
		 * 
			###############
			#***...R......#
			#***... ...*..#
			#\\\... ..\\\.#
			#...... ...*..#
			#..     .. ...#
			#.... .... ...#
			#.... .... ...#
			#.. .       ..#
			#..*. .. .....#
			#.... .. .....#
			#.\.. .......*#
			#.............#
			#.........   .#
			#############L#

		 */
		String map = 
				"###############\n" + 
				"#***...R......#\n" + 
				"#***... ...*..#\n" + 
				"#\\\\\\... ..\\\\\\.#\n" + 
				"#...... ...*..#\n" + 
				"#..     .. ...#\n" + 
				"#.... .... ...#\n" + 
				"#.... .... ...#\n" + 
				"#.. .       ..#\n" + 
				"#..*. .. .....#\n" + 
				"#.... .. .....#\n" + 
				"#.\\.. .......*#\n" + 
				"#.............#\n" + 
				"#.........   .#\n" + 
				"#############L#";
		Mine mine = new Mine(map);
		Game game = new Game(mine);
		String route = "DDDLLLLLLURRRRRRRRRRRRDDDDDDDLLLLLLLLLLLDDDRRRRRRRRRRRD"; 
		assertEquals(game.move(route), GameState.Win);
	}

}
