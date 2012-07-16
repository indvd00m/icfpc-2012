package ru.bosony.solvers;

import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 4:31:43
 * 
 */
public class SimpleSolver extends AbstractSolver {

	public SimpleSolver(Mine mine, SolverListener listener) {
		super(mine, listener);
	}

	@Override
	protected void solve() {
		String route = "";
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			route += "A";
			listener.foundNextRoute(route);
		}
	}

}
