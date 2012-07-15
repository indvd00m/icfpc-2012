package ru.bosony.model.mine;

import static ru.bosony.model.cellscontents.CellContent.ClosedLambdaLift;
import static ru.bosony.model.cellscontents.CellContent.Earth;
import static ru.bosony.model.cellscontents.CellContent.Empty;
import static ru.bosony.model.cellscontents.CellContent.Lambda;
import static ru.bosony.model.cellscontents.CellContent.MiningRobot;
import static ru.bosony.model.cellscontents.CellContent.OpenLambdaLift;
import static ru.bosony.model.cellscontents.CellContent.Rock;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.io.TextRepresentable;
import ru.bosony.model.moving.Coordinate;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 2:02:18
 * 
 */
public class Mine implements TextRepresentable {

	protected Cell[][]			cells;
	protected int				sizeX;
	protected int				sizeY;
	protected int				waterLevel		= 0;
	protected int				flooding		= 0;
	protected int				robotWaterproof	= 10;
	protected static Pattern	pattern			= Pattern.compile("(?s)(.*?)(\n\n)?(Water )?(\\d*)\n?(Flooding )?(\\d*)\n?(Waterproof )?(\\d*)");

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
		text = text.replaceAll("\n*$", "");
		if (waterLevel != 0 || flooding != 0 || robotWaterproof != 10) {
			text += "\n\n";
			text += "Water " + waterLevel + "\n";
			text += "Flooding " + flooding + "\n";
			text += "Waterproof " + robotWaterproof;
		}
		return text;
	}

	protected void fromText(String str) {
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches())
			throw new RuntimeException("Can't parse mine from string");
		String body = matcher.group(1);
		String[] rows = body.split("\n+");
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
				if (content == null)
					content = Empty;
				Cell cell = new Cell(new Coordinate(curX + 1, rows.length - curY), content);
				cells[curX][curY] = cell;
			}
		}
		
		String sWaterLevel = matcher.group(4);
		String sFlooding = matcher.group(6);
		String sRobotWaterproof = matcher.group(8);
		if (sWaterLevel.length() > 0)
			waterLevel = Integer.parseInt(sWaterLevel);
		if (sFlooding.length() > 0)
			flooding = Integer.parseInt(sFlooding);
		if (sRobotWaterproof.length() > 0)
			robotWaterproof = Integer.parseInt(sRobotWaterproof);
	}

	public Cell getCell(Coordinate coordinate) {
		if (coordinate == null)
			return null;
		int x = coordinate.x;
		int y = coordinate.y;
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
		Cell robotCell = getRobotCell();
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

	public Set<Cell> findCells(CellContent content) {
		Set<Cell> finded = new HashSet<Cell>();
		for (int y = sizeY - 1; y >= 0; y--) {
			for (int x = 0; x < sizeX; x++) {
				if (cells[x][y].getContent() == content)
					finded.add(cells[x][y]);
			}
		}
		return finded;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public Cell getRobotCell() {
		for (Cell cell : findCells(MiningRobot)) {
			return cell;
		}
		throw new RuntimeException("Where is robot!?");
	}

	public Cell[][] cloneCells() {
		Cell[][] clone = new Cell[sizeX][sizeY];
		for (int y = sizeY - 1; y >= 0; y--) {
			for (int x = 0; x < sizeX; x++) {
				try {
					clone[x][y] = cells[x][y].clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return clone;
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	public int getFlooding() {
		return flooding;
	}

	public int getRobotWaterproof() {
		return robotWaterproof;
	}

}
