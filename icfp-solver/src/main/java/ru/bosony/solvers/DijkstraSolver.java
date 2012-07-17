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

	protected Map<String, Set<Movement>>	loseMovs							= new HashMap<String, Set<Movement>>();
	protected Map<String, Set<CellsLink>>	loseLinks							= new HashMap<String, Set<CellsLink>>();
	protected Map<String, Set<Coordinate>>	attendedWalkingAroundCoordinates	= new HashMap<String, Set<Coordinate>>();
	protected Map<String, Set<Coordinate>>	noWay								= new HashMap<String, Set<Coordinate>>();

	public DijkstraSolver(Mine mine, SolverListener listener) {
		super(mine, listener);
	}

	@Override
	public void solve() {
		while (true) {
			Mine mine = null;
			mine = initialMine.clone();
			Game game = new Game(mine);

			while (game.getState() == GameState.Game) {
				Cell robotCell = mine.getRobotCell();
				String map = mine.toText();
				String mapWithoutRobot = mine.toTextWithoutRobot();
				if (!loseMovs.containsKey(map)) {
					loseMovs.put(map, new HashSet<Movement>());
				}
				if (!loseLinks.containsKey(map)) {
					loseLinks.put(map, new HashSet<CellsLink>());
				}
				if (!noWay.containsKey(map)) {
					noWay.put(map, new HashSet<Coordinate>());
				}
				if (!attendedWalkingAroundCoordinates.containsKey(mapWithoutRobot)) {
					attendedWalkingAroundCoordinates.put(mapWithoutRobot, new HashSet<Coordinate>());
				}
				attendedWalkingAroundCoordinates.get(mapWithoutRobot).add(robotCell.getCoordinate());
				Set<Cell> targets = new HashSet<Cell>();
				boolean isFreeWay = false;
				for (Cell cell : mine.getAdjacentCells(robotCell)) {
					Movement mov = robotCell.getCoordinate().getNecessaryMovement(cell.getCoordinate());
					isFreeWay |= !attendedWalkingAroundCoordinates.get(mapWithoutRobot).contains(cell.getCoordinate())
							&& !loseMovs.get(map).contains(mov);
				}
				if (isFreeWay) {
					targets.addAll(mine.findCells(CellContent.Lambda));
					targets.addAll(mine.findCells(CellContent.HighOrderRock));
					if (targets.size() == 0)
						targets.addAll(mine.findCells(CellContent.OpenLambdaLift));
					targets.removeAll(mine.getCells(noWay.get(map)));
				} else {
					targets.addAll(mine.findCells(CellContent.Empty));
					targets.addAll(mine.findCells(CellContent.Earth));
					targets.addAll(mine.findCells(CellContent.HuttonsRazor));
					for (CellContent content : CellContent.getTrampolines()) {
						targets.addAll(mine.findCells(content));
					}
					targets.removeAll(mine.getCells(attendedWalkingAroundCoordinates.get(mapWithoutRobot)));
					targets.removeAll(mine.getCells(noWay.get(map)));
					if (targets.size() == 0) {
						game.move(Movement.ABORT);
						continue;
					}
				}
				Graph<Cell, CellsLink> graph = getDirectedGraph(mine);
				DijkstraShortestPath<Cell, CellsLink> alg = new DijkstraShortestPath<Cell, CellsLink>(graph,
						new CellsLinkTransformer<CellsLink, Double>());
				Cell nextTarget = null;
				List<CellsLink> routeToNextTarget = null;
				double shortestDist = Double.MAX_VALUE;
				for (Cell target : targets) {
					List<CellsLink> route = null;
					Number dist = null;
					try {
						route = alg.getPath(mine.getRobotCell(), target);
						dist = alg.getDistance(mine.getRobotCell(), target);
					} catch (IllegalArgumentException e) {
						noWay.get(map).add(target.getCoordinate());
						continue;
					}
					if (dist != null && route != null && route.size() > 0 && dist.doubleValue() < shortestDist) {
						CellsLink firstLink = route.get(0);
						Movement mov = firstLink.getSource().getCoordinate()
								.getNecessaryMovement(firstLink.getTarget().getCoordinate());
						if (!loseMovs.get(map).contains(mov)
								&& !attendedWalkingAroundCoordinates.get(mapWithoutRobot).contains(
										target.getCoordinate())) {
							shortestDist = dist.doubleValue();
							nextTarget = target;
							routeToNextTarget = route;
						}
					}
				}
				if (nextTarget != null) {
					for (CellsLink link : routeToNextTarget) {
						Cell source = link.getSource();
						Cell target = link.getTarget();
						if (isSafeMovement(mine, source, target)) {
							Movement mov = source.getCoordinate().getNecessaryMovement(target.getCoordinate());
							GameState state = game.move(mov);
							if (state == GameState.Lose || state == GameState.Abort) {
								loseMovs.get(map).add(mov);
								loseLinks.get(map).add(link);
							}
						}
						break;
					}
				} else {
					// Walking around
					for (Movement mov : Movement.values()) {
						if (!loseMovs.get(map).contains(mov)) {
							if (mov == Movement.RAZOR && mine.getRazorsCount() > 0) {
								boolean isRazorApplied = false;
								for (Cell cell : mine.getNeighboringCells(robotCell)) {
									isRazorApplied |= cell.getContent() == CellContent.WadlersBeard;
								}
								if (!isRazorApplied)
									continue;
							}
							Cell movCell = null;
							for (Cell cell : mine.getAdjacentCells(robotCell)) {
								if (robotCell.getCoordinate().getNecessaryMovement(cell.getCoordinate()) == mov) {
									if (!attendedWalkingAroundCoordinates.get(mapWithoutRobot).contains(
											cell.getCoordinate())) {
										movCell = cell;
										break;
									}
								}

							}
							if (mov == Movement.WAIT
									|| mov == Movement.RAZOR
									|| movCell != null
									&& !attendedWalkingAroundCoordinates.get(mapWithoutRobot).contains(
											movCell.getCoordinate())) {
								GameState state = game.move(mov);
								if (movCell != null && mapWithoutRobot.equals(mine.toTextWithoutRobot()))
									attendedWalkingAroundCoordinates.get(mapWithoutRobot).add(movCell.getCoordinate());
								if (state == GameState.Game && map.equals(mine.toText())) {
									loseMovs.get(map).add(mov);
								}
								break;
							}
						}
					}
				}
			}
			addNewRoute(game);
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
		String map = mine.toText();
		for (Cell neighboringCell : mine.getAdjacentCells(cell)) {
			if (neighboringCell == null)
				continue;
			CellContent neighboringContent = neighboringCell.getContent();
			if (neighboringContent == CellContent.Earth || neighboringContent == CellContent.Empty
					|| neighboringContent == CellContent.HuttonsRazor || neighboringContent == CellContent.Lambda
					|| neighboringContent == CellContent.OpenLambdaLift) {
				if (isSafeMovement(mine, cell, neighboringCell)) {
					CellsLink link = new CellsLink(mine, cell, neighboringCell);
					if (!loseLinks.get(map).contains(link)) {
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
					if (!loseLinks.get(map).contains(link)) {
						links.add(link);
						findAllCellLinks(mine, target, links, processedCells);
					}
				}
			}
		}
	}
}
