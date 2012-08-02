package ru.bosony.solvers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 2:16:03
 * 
 */
public class Solver {

	protected Mine		mine;
	protected String	route	= "A";

	public Solver(Mine mine) {
		this.mine = mine;
	}

	public void start() throws IOException, InterruptedException {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println(route);
			}
		});
		AbstractSolver solver = new DijkstraSolver(mine, new SolverListener() {

			@Override
			public void foundNextRoute(String newRoute) {
				route = newRoute;
			}
		});
		solver.solve();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		String map = "";
		for (String arg : args) {
			map += arg + "\n";
		}
		if (map == null || map.length() == 0) {
			map = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			while (line != null) {
				line = reader.readLine();
				if (line != null)
					map += line + "\n";
			}
			reader.close();
		}
		map = map.replaceAll("\n$", "");
		Mine mine = new Mine(map);
		Solver solver = new Solver(mine);
		solver.start();
	}

}
