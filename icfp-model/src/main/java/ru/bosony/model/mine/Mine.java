package ru.bosony.model.mine;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;

import ru.bosony.model.cellscontents.CellContent;
import static ru.bosony.model.cellscontents.CellContent.*;
import ru.bosony.model.io.TextRepresentable;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 2:02:18
 * 
 */
public class Mine implements TextRepresentable {

	private Cell[][]	cells;
	private Cell[][]	prevStepCells;
	protected int		sizeX;
	protected int		sizeY;
	protected Cell		robotCell;
	protected int		score					= 0;
	protected int		lastScoreChange			= 0;
	protected GameState	state					= GameState.Game;
	protected int		lambdaCollectedCount	= 0;

	public Mine(String text) {
		fromText(text);
		if (findCells(OpenLambdaLift).size() != 0)
			throw new RuntimeException("Detected OpenLambdaLift in new map!");
		if (findCells(MiningRobot).size() != 1)
			throw new RuntimeException("New map does not contain a robot!");
		if (findCells(ClosedLambdaLift).size() != 1)
			throw new RuntimeException("New map does not contain a ClosedLambdaLift!");
	}

	@Override
	public String toText() {
		String text = "";
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				text += cells[x][y].getContent().toText();
			}
			text += "\n";
		}
		return text.replaceAll("\n*$", "");
	}

	private void fromText(String text) {
		String[] rows = text.split("\n+");
		int y = rows.length;
		int x = 0;
		for (String row : rows) {
			if (x < row.length())
				x = row.length();
		}
		sizeX = x;
		sizeY = y;
		cells = new Cell[x][y];
		for (int curY = 0; curY < rows.length; curY++) {
			String row = rows[curY];
			for (int curX = 0; curX < row.length() || curX < x; curX++) {
				CellContent content = null;
				if (curX < row.length())
					content = CellContent.fromText("" + row.charAt(curX));
				else
					content = Empty;
				Cell cell = new Cell(new Coordinate(curX + 1, rows.length - curY), content);
				cells[curX][curY] = cell;
				if (content == MiningRobot)
					robotCell = cell;
			}
		}
	}

	public Cell getCell(Coordinate coordinate) {
		return getCell(coordinate, cells);
	}

	protected Cell getCell(Coordinate coordinate, Cell[][] cells) {
		if (coordinate == null)
			return null;
		int x = coordinate.x;
		int y = coordinate.y;
		if (cells == null)
			return null;
		if (x < 1 || x > sizeX || y < 1 || y > sizeY)
			return null;
		return cells[x - 1][sizeY - y];
	}

	@Override
	public String toString() {
		return toText();
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public Set<Movement> getAvailableRobotMovements() {
		Set<Movement> movs = new HashSet<Movement>();
		Set<Cell> nCells = getNeighboringCells(robotCell);
		for (Cell nCell : nCells) {
			CellContent nContent = nCell.getContent();
			Coordinate nCoordinate = nCell.getCoordinate();
			Cell rightNCell = getCell(nCoordinate.right());
			Cell leftNCell = getCell(nCoordinate.left());
			if (nContent == Empty
					|| nContent == Earth
					|| nContent == Lambda
					|| nContent == OpenLambdaLift
					|| (nContent == Rock && robotCell.getCoordinate().right().equals(nCoordinate) && rightNCell != null && rightNCell
							.getContent() == Empty)
					|| (nContent == Rock && robotCell.getCoordinate().left().equals(nCoordinate) && leftNCell != null && leftNCell
							.getContent() == Empty)) {
				Movement mov = robotCell.getCoordinate().getNecessaryMovement(nCoordinate);
				movs.add(mov);
			}
		}
		movs.add(Movement.ABORT);
		movs.add(Movement.WAIT);
		return movs;
	}

	public Set<Cell> getNeighboringCells(Cell cell) {
		Set<Cell> cells = new HashSet<Cell>();
		for (Coordinate coordinate : cell.getCoordinate().getNeighboringCoordinates())
			cells.add(getCell(coordinate));
		return cells;
	}

	protected void moveRobot(Cell nextRobotCell) {
		robotCell.setContent(Empty);
		nextRobotCell.setContent(MiningRobot);
		robotCell = nextRobotCell;
	}

	public GameState move(Movement mov) {
		if (state != GameState.Game)
			throw new RuntimeException("Game over!");

		// Score
		int prevScore = score;
		score--;

		// Moving and scoring
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
			nextRobotCoord = robotCell.coordinate.left();
			break;
		case UP:
			nextRobotCoord = robotCell.coordinate.up();
			break;
		case RIGHT:
			nextRobotCoord = robotCell.coordinate.right();
			break;
		case DOWN:
			nextRobotCoord = robotCell.coordinate.down();
			break;
		}
		Cell nextRobotCell = getCell(nextRobotCoord);
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
		} else if (robotCell.coordinate.right() == nextRobotCoord && nextRobotCellContent == Rock && getContent(getCell(nextRobotCoord.right())) == Empty) {
			moveRobot(nextRobotCell);
			getCell(nextRobotCoord.right()).setContent(Rock);
		} else if (robotCell.coordinate.left() == nextRobotCoord && nextRobotCellContent == Rock && getContent(getCell(nextRobotCoord.left())) == Empty) {
			moveRobot(nextRobotCell);
			getCell(nextRobotCoord.left()).setContent(Rock);
		} else {
			mov = Movement.WAIT;
		}

		// Other ending conditions
		if (getContent(getCell(robotCell.coordinate.up(), prevStepCells)) != Rock
				&& getContent(getCell(robotCell.coordinate.up())) == Rock) {
			lastScoreChange = score - prevScore;
			state = GameState.Lose;
			return state;
		}

		// World changes
		Cell[][] newCells = Arrays.copyOf(cells, cells.length);
		for (int y = 1; y <= sizeY; y++) {
			for (int x = 1; x <= sizeX; x++) {
				Coordinate curCoord = new Coordinate(x, y);
				Coordinate leftCoord = curCoord.left();
				Coordinate upCoord = curCoord.up();
				Coordinate rightCoord = curCoord.right();
				Coordinate downCoord = curCoord.down();
				Coordinate leftAndUpCoord = curCoord.leftAndUp();
				Coordinate rightAndUpCoord = curCoord.rightAndUp();
				Coordinate rightAndDownCoord = curCoord.rightAndDown();
				Coordinate leftAndDownCoord = curCoord.leftAndDown();
				Cell curCell = getCell(curCoord);
				Cell leftCell = getCell(leftCoord);
				Cell upCell = getCell(upCoord);
				Cell rightCell = getCell(rightCoord);
				Cell downCell = getCell(downCoord);
				Cell leftAndUpCell = getCell(leftAndUpCoord);
				Cell rightAndUpCell = getCell(rightAndUpCoord);
				Cell rightAndDownCell = getCell(rightAndDownCoord);
				Cell leftAndDownCell = getCell(leftAndDownCoord);

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
				} else if (getContent(curCell) == ClosedLambdaLift && findCells(Lambda).size() == 0) {
					getCell(curCoord, newCells).setContent(OpenLambdaLift);
				}
			}
		}
		prevStepCells = cells;
		cells = newCells;

		lastScoreChange = score - prevScore;
		state = GameState.Game;
		return state;
	}

	protected CellContent getContent(Cell cell) {
		if (cell == null)
			return null;
		return cell.getContent();
	}

	public Collection<Cell> findCells(CellContent content) {
		Collection<Cell> finded = new HashSet<Cell>();
		for (int y = sizeY - 1; y >= 0; y--) {
			for (int x = 0; x < sizeX - 1; x++) {
				if (cells[x][y].getContent() == content)
					finded.add(cells[x][y]);
			}
		}
		return finded;
	}

	public int getLastScoreChange() {
		return lastScoreChange;
	}

}
