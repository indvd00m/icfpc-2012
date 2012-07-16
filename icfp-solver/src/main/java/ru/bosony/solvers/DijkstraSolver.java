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
import ru.bosony.model.moving.Coordinate;
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

	protected Map<Mine, Set<Cell>>	testedTargetCells	= new HashMap<Mine, Set<Cell>>();

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
				if (!testedTargetCells.containsKey(testMine)) {
					testedTargetCells.put(testMine, new HashSet<Cell>());
				}
				Set<Cell> targets = testMine.findCells(CellContent.Lambda);
				targets.addAll(testMine.findCells(CellContent.HighOrderRock));
				if (targets.size() == 0)
					targets = testMine.findCells(CellContent.OpenLambdaLift);
				if (targets.size() == 0 || testedTargetCells.get(testMine).containsAll(targets)) {
					// No targets. Walking around
					Cell robotCell = testMine.getRobotCell();
					for (Coordinate nextCoordinate : robotCell.getCoordinate().getAdjacentCoordinates()) {
						Cell nextCell = testMine.getCell(nextCoordinate);
						targets.add(nextCell);
					}
					if (testedTargetCells.get(testMine).containsAll(targets)) {
						// Fail. All cases are tried.
						game.move(Movement.ABORT);
						break;
					}
				}
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
						testedTargetCells.get(testMine).add(target);
						continue;
					}
					if (dist != null && route != null && dist.doubleValue() < shortestDist
							&& !testedTargetCells.get(testMine).contains(target)) {
						shortestDist = dist.doubleValue();
						nextTarget = target;
						routeToNextTarget = route;
						testedTargetCells.get(testMine).add(target);
					}
				}
				if (nextTarget != null) {
					for (CellsLink link : routeToNextTarget) {
						Movement mov = link.getSource().getCoordinate()
								.getNecessaryMovement(link.getTarget().getCoordinate());
						game.move(mov);
						break;
					}
				}
			}
			if (game.getState() == GameState.Win) {
				addNewRoute(game.getStringRoute(), game.getScore());
			}
			attemptsCount++;
		}
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
				CellsLink link = new CellsLink(mine, cell, neighboringCell);
				links.add(link);
				findAllCellLinks(mine, neighboringCell, links, processedCells);
			} else if (!CellContent.getTargets().contains(cell.getContent())
					&& CellContent.getTargets().contains(neighboringContent)) {
				findAllCellLinks(mine, neighboringCell, links, processedCells);
			}
		}
		if (CellContent.getTrampolines().contains(cell.getContent())) {
			for (Cell target : mine.findCells(cell.getContent().getTrampolineTarget())) {
				if (target == null)
					continue;
				CellsLink link = new CellsLink(mine, cell, target);
				links.add(link);
				findAllCellLinks(mine, target, links, processedCells);
			}
		}
	}
}
