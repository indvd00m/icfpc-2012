package ru.bosony.solvers;

import ru.bosony.model.mine.Mine;

/**
 * @author 	indvdum (gotoindvdum[at]gmail[dot]com) <br>
 * 			16.07.2012 4:30:47
 *
 */
public abstract class AbstractSolver {

	protected Mine	mine		= null;
	SolverListener	listener	= null;

	public AbstractSolver(Mine mine, SolverListener listener) {
		this.mine = mine;
		this.listener = listener;
		solve();
	}

	protected abstract void solve();
}
