package ru.bosony.solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.bosony.graph.CellsLink;
import ru.bosony.graph.CellsLinkTransformer;
import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.game.Game;
import ru.bosony.model.game.GameState;
import ru.bosony.model.mine.Cell;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Movement;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 11:05:26
 * 
 */
public class DijkstraSolver extends AbstractSolver {

	protected Map<String, Set<Movement>>	loseMovs	= new HashMap<String, Set<Movement>>();
	protected Set<CellsLink>				loseLinks	= new HashSet<CellsLink>();

	public DijkstraSolver(Mine mine, SolverListener listener) {
		super(mine, listener);
	}

	@Override
	public void solve() {
		while (true) {
			Mine testMine = null;
			try {
				testMine = mine.clone();
			} catch (CloneNotSupportedException e1) {
				throw new RuntimeException(e1);
			}
			Game game = new Game(testMine);

			while (game.getState() == GameState.Game) {
				String map = testMine.toText();
				if (!loseMovs.containsKey(map)) {
					loseMovs.put(map, new HashSet<Movement>());
				}
				Set<Cell> targets = testMine.findCells(CellContent.Lambda);
				targets.addAll(testMine.findCells(CellContent.HighOrderRock));
				if (targets.size() == 0)
					targets = testMine.findCells(CellContent.OpenLambdaLift);
				Graph<Cell, CellsLink> graph = getDirectedGraph(testMine);
				DijkstraShortestPath<Cell, CellsLink> alg = new DijkstraShortestPath<Cell, CellsLink>(graph,
						new CellsLinkTransformer<CellsLink, Double>());
				Cell nextTarget = null;
				List<CellsLink> routeToNextTarget = null;
				double shortestDist = Double.MAX_VALUE;
				for (Cell target : targets) {
					if (target == null)
						continue;
					List<CellsLink> route = null;
					Number dist = null;
					try {
						route = alg.getPath(testMine.getRobotCell(), target);
						dist = alg.getDistance(testMine.getRobotCell(), target);
					} catch (IllegalArgumentException e) {
						// hmmm...
						continue;
					}
					if (dist != null && route != null && route.size() > 0 && dist.doubleValue() < shortestDist) {
						CellsLink firstLink = route.get(0);
						Movement mov = firstLink.getSource().getCoordinate()
								.getNecessaryMovement(firstLink.getTarget().getCoordinate());
						if (!loseMovs.get(map).contains(mov)) {
							shortestDist = dist.doubleValue();
							nextTarget = target;
							routeToNextTarget = route;
							break;
						}
					}
				}
				if (nextTarget != null) {
					for (CellsLink link : routeToNextTarget) {
						Cell source = link.getSource();
						Cell target = link.getTarget();
						if (isSafeMovement(testMine, source, target)) {
							Movement mov = source.getCoordinate().getNecessaryMovement(target.getCoordinate());
							GameState state = game.move(mov);
							if (state == GameState.Lose || state == GameState.Abort || game.hasViciousCircle()) {
								loseMovs.get(map).add(mov);
								loseLinks.add(link);
							}
						}
						break;
					}
				} else {
					for (Movement mov : Movement.values()) {
						if (!loseMovs.get(map).contains(mov)) {
							GameState state = game.move(mov);
							if (state == GameState.Lose || state == GameState.Abort || map.equals(testMine.toText())
									|| game.hasViciousCircle())
								loseMovs.get(map).add(mov);
							break;
						}
					}
				}
			}
			if (game.getState() == GameState.Win) {
				addNewRoute(game.getStringRoute(), game.getScore());
			}
			attemptsCount++;
		}
	}

	protected boolean isSafeMovement(Mine mine, Cell source, Cell target) {
		if (source == null || target == null)
			return false;
		if (source.getContent().getTrampolineTarget() == target.getContent())
			return true;
		Cell upCell = mine.getCell(target.getCoordinate().up());
		if (upCell == null)
			return true;
		Cell upUpCell = mine.getCell(target.getCoordinate().up().up());
		Cell upUpLeftCell = mine.getCell(target.getCoordinate().up().up().left());
		Cell upUpRightCell = mine.getCell(target.getCoordinate().up().up().right());
		if (upCell.getContent() == CellContent.Empty
				&& (upUpCell != null
						&& (upUpCell.getContent() == CellContent.Rock || upUpCell.getContent() == CellContent.HighOrderRock)
						|| upUpLeftCell != null
						&& (upUpLeftCell.getContent() == CellContent.Rock || upUpLeftCell.getContent() == CellContent.HighOrderRock) || upUpRightCell != null
						&& (upUpRightCell.getContent() == CellContent.Rock || upUpRightCell.getContent() == CellContent.HighOrderRock))) {
			return false;
		}
		return true;
	}

	protected Graph<Cell, CellsLink> getDirectedGraph(Mine mine) {
		Graph<Cell, CellsLink> graph = new DirectedSparseGraph<Cell, CellsLink>();
		for (CellsLink link : getCellLinks(mine)) {
			graph.addEdge(link, link.getSource(), link.getTarget());
		}
		return graph;
	}

	protected List<CellsLink> getCellLinks(Mine mine) {
		List<CellsLink> links = new ArrayList<CellsLink>();
		Cell robotCell = mine.getRobotCell();
		findAllCellLinks(mine, robotCell, links, new HashSet<Cell>());
		return links;
	}

	protected void findAllCellLinks(Mine mine, Cell cell, List<CellsLink> links, Set<Cell> processedCells) {
		if (processedCells.contains(cell))
			return;
		else
			processedCells.add(cell);
		for (Cell neighboringCell : mine.getAdjacentCells(cell)) {
			if (neighboringCell == null)
				continue;
			CellContent neighboringContent = neighboringCell.getContent();
			if (neighboringContent == CellContent.Earth || neighboringContent == CellContent.Empty
					|| neighboringContent == CellContent.HuttonsRazor || neighboringContent == CellContent.Lambda
					|| neighboringContent == CellContent.OpenLambdaLift) {
				if (isSafeMovement(mine, cell, neighboringCell)) {
					CellsLink link = new CellsLink(mine, cell, neighboringCell);
					if (!loseLinks.contains(link)) {
						links.add(link);
						findAllCellLinks(mine, neighboringCell, links, processedCells);
					}
				}
			} else if (!CellContent.getTargets().contains(cell.getContent())
					&& CellContent.getTargets().contains(neighboringContent)) {
				findAllCellLinks(mine, neighboringCell, links, processedCells);
			}
		}
		if (CellContent.getTrampolines().contains(cell.getContent())) {
			for (Cell target : mine.findCells(cell.getContent().getTrampolineTarget())) {
				if (target == null)
					continue;
				if (isSafeMovement(mine, cell, target)) {
					CellsLink link = new CellsLink(mine, cell, target);
					if (!loseLinks.contains(link)) {
						links.add(link);
						findAllCellLinks(mine, target, links, processedCells);
					}
				}
			}
		}
	}
}
