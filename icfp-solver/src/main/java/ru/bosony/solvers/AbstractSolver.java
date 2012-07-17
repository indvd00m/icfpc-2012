package ru.bosony.solvers;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ru.bosony.model.game.Game;
import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 4:30:47
 * 
 */
public abstract class AbstractSolver {

	protected Mine				mine			= null;
	protected SolverListener	listener		= null;
	protected Set<Game>			games			= new HashSet<Game>();
	protected long				attemptsCount	= 0;
	protected long				startTime		= System.currentTimeMillis();

	public AbstractSolver(Mine mine, SolverListener listener) {
		this.mine = mine;
		this.listener = listener;
	}

	protected void addNewRoute(Game game) {
		Game maxScoreGame = getMaxFoundScoreGame();
		if (maxScoreGame == null || game.getScore() > maxScoreGame.getScore()
				|| game.getScore() == maxScoreGame.getScore()
				&& game.getRoute().size() < maxScoreGame.getRoute().size()) {
			listener.foundNextRoute(game.getStringRoute());
			// TODO delete
			System.out.println(new DecimalFormat("0.00").format((((System.currentTimeMillis() - startTime) / 1000d)))
					+ " seconds, State = " + game.getState() + ", Score = " + game.getScore() + ", Route = "
					+ game.getStringRoute());
		}
		games.add(game);
	}

	protected Game getMaxFoundScoreGame() {
		Game game = null;
		for (Game foundGame : games) {
			if (game == null || foundGame.getScore() > game.getScore())
				game = foundGame;
		}
		return game;
	}

	public abstract void solve();
}
