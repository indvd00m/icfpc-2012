package ru.bosony.solvers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	public DijkstraSolver(Mine mine, SolverListener listener) {
		super(mine, listener);
	}

	@Override
	protected void solve() {
		while (true) {
			Game game = new Game(mine);

			while (game.getState() == GameState.Game) {
				try {
					Set<Cell> targets = mine.findCells(CellContent.Lambda);
					targets.addAll(mine.findCells(CellContent.HighOrderRock));
					if (targets.size() == 0)
						targets = mine.findCells(CellContent.OpenLambdaLift);
					if (targets.size() == 0)
						break;
					Graph<Cell, CellsLink> graph = getDirectedGraph();
					DijkstraShortestPath<Cell, CellsLink> alg = new DijkstraShortestPath<Cell, CellsLink>(graph,
							new CellsLinkTransformer<CellsLink, Double>());
					Cell nextTarget = null;
					List<CellsLink> routeToNextTarget = null;
					double shortestDist = Double.MAX_VALUE;
					for (Cell target : targets) {
						List<CellsLink> route = alg.getPath(mine.getRobotCell(), target);
						Number dist = alg.getDistance(mine.getRobotCell(), target);
						if (dist != null && dist.doubleValue() < shortestDist) {
							shortestDist = dist.doubleValue();
							nextTarget = target;
							routeToNextTarget = route;
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
				} catch (IllegalArgumentException e) {
					// hmmm...
				}
			}
			if (game.getState() == GameState.Win) {
				addNewRoute(game.getStringRoute(), game.getScore());
			}
			attemptsCount++;
		}
	}

	protected Graph<Cell, CellsLink> getDirectedGraph() {
		Graph<Cell, CellsLink> graph = new DirectedSparseGraph<Cell, CellsLink>();
		for (CellsLink link : getCellLinks()) {
			graph.addEdge(link, link.getSource(), link.getTarget());
		}
		return graph;
	}

	protected List<CellsLink> getCellLinks() {
		List<CellsLink> links = new ArrayList<CellsLink>();
		Cell robotCell = mine.getRobotCell();
		findAllCellLinks(robotCell, links, new HashSet<Cell>());
		return links;
	}

	protected void findAllCellLinks(Cell cell, List<CellsLink> links, Set<Cell> processedCells) {
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
				findAllCellLinks(neighboringCell, links, processedCells);
			} else if (!CellContent.getTargets().contains(cell.getContent())
					&& CellContent.getTargets().contains(neighboringContent)) {
				findAllCellLinks(neighboringCell, links, processedCells);
			}
		}
		if (CellContent.getTrampolines().contains(cell.getContent())) {
			for (Cell target : mine.findCells(cell.getContent().getTrampolineTarget())) {
				if (target == null)
					continue;
				CellsLink link = new CellsLink(mine, cell, target);
				links.add(link);
				findAllCellLinks(target, links, processedCells);
			}
		}
	}
}
