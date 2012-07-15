package ru.bosony.solvers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 2:16:03
 * 
 */
public class Solver {

	protected Mine				mine;
	protected BufferedWriter	writer;

	public Solver(Mine mine) {
		this.mine = mine;
		writer = new BufferedWriter(new OutputStreamWriter(System.out));
	}

	public void start() throws IOException {
		// TODO solver
		writer.write("A");
		writer.flush();
	}

	public static void main(String[] args) throws IOException {
		String map = "";
		for (String arg : args) {
			map += arg + "\n";
		}
		map = map.replaceAll("\n$", "");
		Mine mine = new Mine(map);
		Solver solver = new Solver(mine);
		solver.start();
	}

}
