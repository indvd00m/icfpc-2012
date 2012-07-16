package ru.bosony.solvers;

import java.io.IOException;

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
	}

	public static void main(String[] args) throws IOException, InterruptedException {
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
