package ru.bosony.interpreters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ru.bosony.model.game.Game;
import ru.bosony.model.game.GameState;
import ru.bosony.model.maps.ExampleMaps;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         15.07.2012 0:20:00
 * 
 */
public class TextInterpreter {

	protected Mine				mine;
	protected Game				game;
	protected BufferedWriter	writer;
	protected BufferedReader	reader;

	public TextInterpreter(Game game) {
		this.game = game;
		this.mine = game.getMine();
		writer = new BufferedWriter(new OutputStreamWriter(System.out));
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public void start() throws IOException {
		display();
		while (true) {
			String route = reader.readLine();
			game.move(route);
			display();
			if (game.getState() != GameState.Game)
				break;
		}
	}

	protected void display() throws IOException {
		writer.newLine();
		writer.write("********************************************************************************");
		writer.newLine();
		writer.write("State:         " + game.getState());
		writer.newLine();
		writer.write("Score:         " + game.getScore() + " ("
				+ (game.getLastScoreChange() >= 0 ? "+" + game.getLastScoreChange() : game.getLastScoreChange()) + ")");
		writer.newLine();
		writer.write("Steps:         " + game.getRoute().size());
		writer.newLine();
		writer.write("Route:         " + game.getStringRoute());
		writer.newLine();
		writer.write("Underwater:    " + game.getUnderwater() + " of " + mine.getRobotWaterproof());
		writer.newLine();
		writer.write("Razors:        " + mine.getRazorsCount());
		writer.newLine();
		writer.write("Labmdas found: " + game.getLambdaCollectedCount() + " of "
				+ mine.getLambdasAndHighOrderRocksStartCount());
		writer.newLine();
		writer.newLine();
		writer.write(mine.toText());
		writer.newLine();
		writer.newLine();
		writer.write("Available actions: ");
		String movs = "";
		for (Movement mov : Movement.values()) {
			movs += mov.toText() + " (" + mov.toString().toLowerCase() + "), ";
		}
		movs = movs.replaceAll(",\\s*$", "");
		writer.write(movs);
		writer.newLine();
		writer.write("Enter action(s): ");
		writer.flush();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String map = "";
		for (String arg : args) {
			map += arg + "\n";
		}
		map = map.replaceAll("\n$", "");
		// default map
		if (map.length() == 0) {
			map = ExampleMaps.MAP_01.getMap();
		}

		Mine mine = new Mine(map);
		Game game = new Game(mine);
		TextInterpreter interpreter = new TextInterpreter(game);
		interpreter.start();
	}

}
