package ru.bosony.model.game;

import static ru.bosony.model.cellscontents.CellContent.ClosedLambdaLift;
import static ru.bosony.model.cellscontents.CellContent.Earth;
import static ru.bosony.model.cellscontents.CellContent.Empty;
import static ru.bosony.model.cellscontents.CellContent.Lambda;
import static ru.bosony.model.cellscontents.CellContent.MiningRobot;
import static ru.bosony.model.cellscontents.CellContent.OpenLambdaLift;
import static ru.bosony.model.cellscontents.CellContent.Rock;

import java.util.ArrayList;
import java.util.List;

import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.mine.Cell;
import ru.bosony.model.mine.Mine;
import ru.bosony.model.moving.Coordinate;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 22:51:20
 * 
 */
public class Game {

	protected Mine				mine					= null;
	protected int				score					= 0;
	protected int				lastScoreChange			= 0;
	protected GameState			state					= GameState.Game;
	protected int				lambdaCollectedCount	= 0;
	protected List<Movement>	route					= new ArrayList<Movement>();

	public Game(Mine mine) {
		this.mine = mine;
	}

	public Mine getMine() {
		return mine;
	}

	protected Cell getCell(Coordinate coordinate, Cell[][] cells) {
		if (coordinate == null)
			return null;
		int x = coordinate.x;
		int y = coordinate.y;
		if (cells == null)
			return null;
		if (x < 1 || x > mine.getSizeX() || y < 1 || y > mine.getSizeY())
			return null;
		return cells[x - 1][mine.getSizeY() - y];
	}

	protected void moveRobot(Cell nextRobotCell) {
		mine.getRobotCell().setContent(Empty);
		nextRobotCell.setContent(MiningRobot);
	}

	public GameState move(Movement mov) {
		if (state != GameState.Game)
			throw new RuntimeException("Game over!");

		// Score
		int prevScore = score;

		// Moving and scoring
		route.add(mov);
		Coordinate robotCoord = mine.getRobotCell().getCoordinate();
		Coordinate nextRobotCoord = null;
		switch (mov) {
		case ABORT:
			score += 25 * lambdaCollectedCount;

			lastScoreChange = score - prevScore;
			state = GameState.Abort;
			return state;
		case WAIT:
			break;
		case LEFT:
			nextRobotCoord = robotCoord.left();
			break;
		case UP:
			nextRobotCoord = robotCoord.up();
			break;
		case RIGHT:
			nextRobotCoord = robotCoord.right();
			break;
		case DOWN:
			nextRobotCoord = robotCoord.down();
			break;
		}
		score--;
		Cell nextRobotCell = mine.getCell(nextRobotCoord);
		CellContent nextRobotCellContent = getContent(nextRobotCell);
		if (nextRobotCellContent == Empty || nextRobotCellContent == Earth) {
			moveRobot(nextRobotCell);
		} else if (nextRobotCellContent == Lambda) {
			moveRobot(nextRobotCell);
			lambdaCollectedCount++;
			score += 25;
		} else if (nextRobotCellContent == OpenLambdaLift) {
			moveRobot(nextRobotCell);
			score += 50 * lambdaCollectedCount;

			lastScoreChange = score - prevScore;
			state = GameState.Win;
			return state;
		} else if (robotCoord.right().equals(nextRobotCoord) && nextRobotCellContent == Rock
				&& getContent(mine.getCell(nextRobotCoord.right())) == Empty) {
			moveRobot(nextRobotCell);
			mine.getCell(nextRobotCoord.right()).setContent(Rock);
		} else if (robotCoord.left().equals(nextRobotCoord) && nextRobotCellContent == Rock
				&& getContent(mine.getCell(nextRobotCoord.left())) == Empty) {
			moveRobot(nextRobotCell);
			mine.getCell(nextRobotCoord.left()).setContent(Rock);
		} else {
			mov = Movement.WAIT;
		}

		// World changes
		Cell[][] newCells = mine.cloneCells();
		for (int y = 1; y <= mine.getSizeY(); y++) {
			for (int x = 1; x <= mine.getSizeX(); x++) {
				Coordinate curCoord = new Coordinate(x, y);
				Coordinate leftCoord = curCoord.left();
				Coordinate upCoord = curCoord.up();
				Coordinate rightCoord = curCoord.right();
				Coordinate downCoord = curCoord.down();
				Coordinate leftAndUpCoord = curCoord.leftAndUp();
				Coordinate rightAndUpCoord = curCoord.rightAndUp();
				Coordinate rightAndDownCoord = curCoord.rightAndDown();
				Coordinate leftAndDownCoord = curCoord.leftAndDown();
				Cell curCell = mine.getCell(curCoord);
				Cell leftCell = mine.getCell(leftCoord);
				Cell upCell = mine.getCell(upCoord);
				Cell rightCell = mine.getCell(rightCoord);
				Cell downCell = mine.getCell(downCoord);
				Cell leftAndUpCell = mine.getCell(leftAndUpCoord);
				Cell rightAndUpCell = mine.getCell(rightAndUpCoord);
				Cell rightAndDownCell = mine.getCell(rightAndDownCoord);
				Cell leftAndDownCell = mine.getCell(leftAndDownCoord);

				if (getContent(curCell) == Rock && getContent(downCell) == Empty) {
					getCell(curCoord, newCells).setContent(Empty);
					getCell(downCoord, newCells).setContent(Rock);
				} else if (getContent(curCell) == Rock && getContent(downCell) == Empty
						&& getContent(rightCell) == Empty && getContent(rightAndDownCell) == Empty) {
					getCell(curCoord, newCells).setContent(Empty);
					getCell(rightAndDownCoord, newCells).setContent(Rock);
				} else if (getContent(curCell) == Rock && getContent(downCell) == Rock
						&& (getContent(rightCell) != Empty || getContent(rightAndDownCell) != Empty)
						&& getContent(leftCell) == Empty && getContent(leftAndDownCell) == Empty) {
					getCell(curCoord, newCells).setContent(Empty);
					getCell(leftAndDownCoord, newCells).setContent(Rock);
				} else if (getContent(curCell) == Rock && getContent(downCell) == Lambda
						&& getContent(rightCell) == Empty && getContent(rightAndDownCell) == Empty) {
					getCell(curCoord, newCells).setContent(Empty);
					getCell(rightAndDownCoord, newCells).setContent(Rock);
				} else if (getContent(curCell) == ClosedLambdaLift && mine.findCells(Lambda).size() == 0) {
					getCell(curCoord, newCells).setContent(OpenLambdaLift);
				}
			}
		}

		// Other ending conditions
		robotCoord = mine.getRobotCell().getCoordinate();
		if (getContent(mine.getCell(robotCoord.up())) != Rock && getContent(getCell(robotCoord.up(), newCells)) == Rock) {
			mine.setCells(newCells);
			lastScoreChange = score - prevScore;
			state = GameState.Lose;
			return state;
		}

		mine.setCells(newCells);

		lastScoreChange = score - prevScore;
		state = GameState.Game;
		return state;
	}

	public GameState move(List<Movement> movs) {
		for (Movement mov : movs) {
			if (move(mov) != GameState.Game)
				return state;
		}
		return state;
	}

	public GameState move(String route) {
		List<Movement> movs = new ArrayList<Movement>();
		for (int i = 0; i < route.length(); i++) {
			String s = "" + route.charAt(i);
			for (Movement mov : Movement.values()) {
				if (mov.toText().equals(s)) {
					movs.add(mov);
					break;
				}
			}
		}
		return move(movs);
	}

	protected CellContent getContent(Cell cell) {
		if (cell == null)
			return null;
		return cell.getContent();
	}

	public int getLastScoreChange() {
		return lastScoreChange;
	}

	public int getScore() {
		return score;
	}

	public GameState getState() {
		return state;
	}

	public int getLambdaCollectedCount() {
		return lambdaCollectedCount;
	}

	public List<Movement> getRoute() {
		return route;
	}

	public String getStringRoute() {
		String movs = "";
		for (Movement mov : route) {
			movs += mov.toText();
		}
		return movs;
	}
}
