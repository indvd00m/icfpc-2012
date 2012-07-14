package ru.bosony.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 0:37:14
 * 
 */
public class TestMineParsing {

	protected static String alignMap(String map) {
		String alignedMap = "";
		String[] rows = map.split("\n");
		int y = rows.length;
		int x = 0;
		for (String row : rows) {
			if (x < row.length())
				x = row.length();
		}
		for (int curY = 0; curY < rows.length; curY++) {
			String row = rows[curY];
			for (int curX = 0; curX < row.length() || curX < x; curX++) {
				if (curX < row.length())
					alignedMap += row.charAt(curX);
				else
					alignedMap += " ";
			}
			alignedMap += "\n";
		}
		return alignedMap.replaceAll("\n$", "");
	}

	@Test
	public void testMap01() {
		String map = ExampleMaps.MAP_01.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap02() {
		String map = ExampleMaps.MAP_02.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap03() {
		String map = ExampleMaps.MAP_03.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap04() {
		String map = ExampleMaps.MAP_04.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap05() {
		String map = ExampleMaps.MAP_05.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap06() {
		String map = ExampleMaps.MAP_06.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap07() {
		String map = ExampleMaps.MAP_07.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap08() {
		String map = ExampleMaps.MAP_08.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap09() {
		String map = ExampleMaps.MAP_09.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

	@Test
	public void testMap10() {
		String map = ExampleMaps.MAP_10.getMap();
		Mine mine = new Mine(map);
		String parsed = mine.toText();
		assertEquals(alignMap(map), parsed);
	}

}
