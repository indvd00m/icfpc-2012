package ru.bosony.model.maps;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         15.07.2012 0:28:52
 * 
 */
public enum ExampleMaps {
	/**
	 * <pre>
		######
		#. *R#
		#  \.#
		#\ * #
		L  .\#
		######
	 * </pre>
	 */
	MAP_01(
			"######\n" + 
			"#. *R#\n" + 
			"#  \\.#\n" + 
			"#\\ * #\n" + 
			"L  .\\#\n" + 
			"######"
			),
	/**
	 * <pre>
		#######
		#..***#
		#..\\\#
		#...**#
		#.*.*\#
		LR....#
		#######
	 * </pre>
	 */
	MAP_02(
			"#######\n" + 
			"#..***#\n" + 
			"#..\\\\\\#\n" + 
			"#...**#\n" + 
			"#.*.*\\#\n" + 
			"LR....#\n" + 
			"#######"
			),
	/**
	 * <pre>
		########
		#..R...#
		#..*...#
		#..#...#
		#.\.\..L
		####**.#
		#\.....#
		#\..* .#
		########
	 * </pre>
	 */
	MAP_03(
			"########\n" + 
			"#..R...#\n" + 
			"#..*...#\n" + 
			"#..#...#\n" + 
			"#.\\.\\..L\n" + 
			"####**.#\n" + 
			"#\\.....#\n" + 
			"#\\..* .#\n" + 
			"########"
			),
	/**
	 * <pre>
		#########
		#.*..#\.#
		#.\..#\.L
		#.R .##.#
		#.\  ...#
		#..\  ..#
		#...\  ##
		#....\ \#
		#########
	 * </pre>
	 */
	MAP_04(
			"#########\n" + 
			"#.*..#\\.#\n" + 
			"#.\\..#\\.L\n" + 
			"#.R .##.#\n" + 
			"#.\\  ...#\n" + 
			"#..\\  ..#\n" + 
			"#...\\  ##\n" + 
			"#....\\ \\#\n" + 
			"#########"
			),
	/**
	 * <pre>
		############
		#..........#
		#.....*....#
		#..\\\\\\..#
		#.     ....#
		#..\\\\\\\.#
		#..\..    .#
		#..\.. ....#
		#..... ..* #
		#..### ### #
		#...R#\#\\.#
		######L#####
	 * </pre>
	 */
	MAP_05(
			"############\n" + 
			"#..........#\n" + 
			"#.....*....#\n" + 
			"#..\\\\\\\\\\\\..#\n" + 
			"#.     ....#\n" + 
			"#..\\\\\\\\\\\\\\.#\n" + 
			"#..\\..    .#\n" + 
			"#..\\.. ....#\n" + 
			"#..... ..* #\n" + 
			"#..### ### #\n" + 
			"#...R#\\#\\\\.#\n" + 
			"######L#####"
			),
	/**
	 * <pre>
		###############
		#\\\.......** #
		#\\#.#####...##
		#\\#.....*##. #
		#\#####\...## #
		#\......####* #
		#\.######* #.\#
		#\.#. *...##.##
		#\##. ..  *...#
		#\...... L#.#.#
		###########.#.#
		#\..........#.#
		##.##########.#
		#R.#\.........#
		###############
	 * </pre>
	 */
	MAP_06(
			"###############\n" + 
			"#\\\\\\.......** #\n" + 
			"#\\\\#.#####...##\n" + 
			"#\\\\#.....*##. #\n" + 
			"#\\#####\\...## #\n" + 
			"#\\......####* #\n" + 
			"#\\.######* #.\\#\n" + 
			"#\\.#. *...##.##\n" + 
			"#\\##. ..  *...#\n" + 
			"#\\...... L#.#.#\n" + 
			"###########.#.#\n" + 
			"#\\..........#.#\n" + 
			"##.##########.#\n" + 
			"#R.#\\.........#\n" + 
			"###############"
			),
	/**
	 * <pre>
	    #######
	    ##    *#
	     ##R  *##
	      ##\\\\##
	       ##....##
	      ##..\ . ##
	     ## . L .  ##
	    ##\\\# #\\\\##
	   ######   #######
	 * </pre>
	 */
	MAP_07(
			"    #######\n" + 
			"    ##    *#\n" + 
			"     ##R  *##\n" + 
			"      ##\\\\\\\\##\n" + 
			"       ##....##\n" + 
			"      ##..\\ . ##\n" + 
			"     ## . L .  ##\n" + 
			"    ##\\\\\\# #\\\\\\\\##\n" + 
			"   ######   #######"
			),
	/**
	 * <pre>
		##############
		#\\... ......#
		###.#. ...*..#
		  #.#. ... ..#
		### #.   \ ..#
		#. .#..... **#######
		#.#\#..... ..\\\*. #
		#*\\#.###. ####\\\ #
		#\\.#.     ...## \ #
		#\#.#..... ....# \ #  
		###.#..... ....#   ##
		#\\.#..... ....#\   # 
		########.. ..###*####
		#......... .........#
		#......... ....***..#
		#..\\\\\ # ####.....#
		#........*R..\\\   .#
		##########L##########
	 * </pre>
	 */
	MAP_08(
			"##############\n" + 
			"#\\\\... ......#\n" + 
			"###.#. ...*..#\n" + 
			"  #.#. ... ..#\n" + 
			"### #.   \\ ..#\n" + 
			"#. .#..... **#######\n" + 
			"#.#\\#..... ..\\\\\\*. #\n" + 
			"#*\\\\#.###. ####\\\\\\ #\n" + 
			"#\\\\.#.     ...## \\ #\n" + 
			"#\\#.#..... ....# \\ #  \n" + 
			"###.#..... ....#   ##\n" + 
			"#\\\\.#..... ....#\\   # \n" + 
			"########.. ..###*####\n" + 
			"#......... .........#\n" + 
			"#......... ....***..#\n" + 
			"#..\\\\\\\\\\ # ####.....#\n" + 
			"#........*R..\\\\\\   .#\n" + 
			"##########L##########"
			),
	/**
	 * <pre>
		        #L#######
		        #*** \\ #
		        #\\\ .. #
		#########.##    ##########
		#.......\ ..........*   .#
		#*******\......#....#\\ .#
		###\.\\\...**..#....... *#
		#*****\\  .\\..##     #\.#
		######### ....  ##########
		        #       #
		        ####*####      
		        #.......#
		#########  \\\\*##########
		#*\\  **#     *..*\ \\\\\#
		#.\**\*** .....**.# \\##\#
		#\R......     .\\.. \\\\\#
		##########################
	 * </pre>
	 */
	MAP_09(
			"        #L#######\n" + 
			"        #*** \\\\ #\n" + 
			"        #\\\\\\ .. #\n" + 
			"#########.##    ##########\n" + 
			"#.......\\ ..........*   .#\n" + 
			"#*******\\......#....#\\\\ .#\n" + 
			"###\\.\\\\\\...**..#....... *#\n" + 
			"#*****\\\\  .\\\\..##     #\\.#\n" + 
			"######### ....  ##########\n" + 
			"        #       #\n" + 
			"        ####*####      \n" + 
			"        #.......#\n" + 
			"#########  \\\\\\\\*##########\n" + 
			"#*\\\\  **#     *..*\\ \\\\\\\\\\#\n" + 
			"#.\\**\\*** .....**.# \\\\##\\#\n" + 
			"#\\R......     .\\\\.. \\\\\\\\\\#\n" + 
			"##########################"
			),
	/**
	 * <pre>
		#############################
		#..........................\#
		#..\\###...#....        ###.#
		#..\*\\\.. #.... ..##\\..\#.#
		#..\*\.... #.... ..#\#....#.#
		#...\###.. #.... ....#....#.#
		#... ..... ..... .####......#
		#\\. #....           .......#
		#... #..#. .....*\ ##.......#
		#.#....... ...#..  ....######
		#. ...#... ...#.\  ....#..* #
		##........ ...#.. #....#.#\\#
		#.....*... .....*\#\\.....*.#
		#.***.* .......*\****.....#.#
		#.\\\.. ................   .#
		#.#####    .######    ##### #
		#....\\.................... #
		#....****...#.##.....\\\\..\#
		#....\\\\...#.........*....\#
		#....\\\\...#.\\.    #\###.\#
		#....     ..#.... ...#\\\\. #
		#........ ..#.... ...#..... #
		#........         ........#R#
		###########################L#
	 * </pre>
	 */
	MAP_10(
			"#############################\n" + 
			"#..........................\\#\n" + 
			"#..\\\\###...#....        ###.#\n" + 
			"#..\\*\\\\\\.. #.... ..##\\\\..\\#.#\n" + 
			"#..\\*\\.... #.... ..#\\#....#.#\n" + 
			"#...\\###.. #.... ....#....#.#\n" + 
			"#... ..... ..... .####......#\n" + 
			"#\\\\. #....           .......#\n" + 
			"#... #..#. .....*\\ ##.......#\n" + 
			"#.#....... ...#..  ....######\n" + 
			"#. ...#... ...#.\\  ....#..* #\n" + 
			"##........ ...#.. #....#.#\\\\#\n" + 
			"#.....*... .....*\\#\\\\.....*.#\n" + 
			"#.***.* .......*\\****.....#.#\n" + 
			"#.\\\\\\.. ................   .#\n" + 
			"#.#####    .######    ##### #\n" + 
			"#....\\\\.................... #\n" + 
			"#....****...#.##.....\\\\\\\\..\\#\n" + 
			"#....\\\\\\\\...#.........*....\\#\n" + 
			"#....\\\\\\\\...#.\\\\.    #\\###.\\#\n" + 
			"#....     ..#.... ...#\\\\\\\\. #\n" + 
			"#........ ..#.... ...#..... #\n" + 
			"#........         ........#R#\n" + 
			"###########################L#"
			),
	/**
	 * <pre>
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
	 * </pre>
	 */
	MAP_FROM_TASK(
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
			"#############L#"
			);

	private String	map;

	private ExampleMaps(String map) {
		this.map = map;
	}

	public String getMap() {
		return map;
	}
}
