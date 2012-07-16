package ru.bosony.solvers;

import java.util.HashMap;
import java.util.Map;

import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 4:30:47
 * 
 */
public abstract class AbstractSolver {

	protected Mine					mine			= null;
	protected SolverListener		listener		= null;
	protected Map<String, Integer>	routesWithScore	= new HashMap<String, Integer>();
	protected long					attemptsCount	= 0;

	public AbstractSolver(Mine mine, SolverListener listener) {
		this.mine = mine;
		this.listener = listener;
	}

	protected void addNewRoute(String route, int score) {
		if (!routesWithScore.containsKey(route) && score > getMaxFoundScore()) {
			routesWithScore.put(route, score);
			listener.foundNextRoute(route);
		}
	}

	protected int getMaxFoundScore() {
		int maxScore = 0;
		for (int foundScore : routesWithScore.values()) {
			if (foundScore > maxScore)
				maxScore = foundScore;
		}
		return maxScore;
	}

	public abstract void solve();
}
