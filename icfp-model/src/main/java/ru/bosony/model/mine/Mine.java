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
public class Mine implements TextRepresentable, Cloneable {

	protected Cell[][]			cells;
	protected int				sizeX;
	protected int				sizeY;
	protected int				waterLevel							= 0;
	protected int				flooding							= 0;
	protected int				robotWaterproof						= 10;
	protected int				growth								= 25;
	protected int				razorsCount							= 0;
	protected int				lambdasAndHighOrderRocksStartCount	= 0;
	protected String			metadata							= "";
	protected static Pattern	minePattern							= Pattern
																			.compile("(?s)(.*?)(\n\n(Water.*|Flooding.*|Waterproof.*|Trampoline.*|Growth.*|Razors.*)|$)");
	protected static Pattern	waterPattern						= Pattern.compile("(?s).*(?<=.*Water )(\\d+).*");
	protected static Pattern	floodingPattern						= Pattern.compile("(?s).*(?<=.*Flooding )(\\d+).*");
	protected static Pattern	waterproofPattern					= Pattern
																			.compile("(?s).*(?<=.*Waterproof )(\\d+).*");
	protected static Pattern	growthPattern						= Pattern.compile("(?s).*(?<=.*Growth )(\\d+).*");
	protected static Pattern	razorsPattern						= Pattern.compile("(?s).*(?<=.*Razors )(\\d+).*");
	protected static Pattern	trampolinePattern					= Pattern
																			.compile("(?s).*?Trampoline ([A-I]) targets ([1-9]).*");
	protected static String		firstTrampolineRegexp				= "(?s).*?Trampoline ([A-I]) targets ([1-9])";

	public Mine(String text) {
		this(text, null);
		if (findCells(OpenLambdaLift).size() != 0)
			throw new RuntimeException("Detected OpenLambdaLift in new map!");
		if (findCells(MiningRobot).size() != 1)
			throw new RuntimeException("New map does not contain a robot!");
		if (findCells(ClosedLambdaLift).size() != 1)
			throw new RuntimeException("New map does not contain a ClosedLambdaLift!");
	}

	protected Mine(String text, Object obj) {
		fromText(text);
		lambdasAndHighOrderRocksStartCount = findCells(Lambda).size() + findCells(CellContent.HighOrderRock).size();
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
		text += "\n\n";
		text += metadata;
		text = text.replaceAll("\n*$", "");
		return text;
	}

	protected void fromText(String str) {
		Matcher matcher = minePattern.matcher(str);
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

		metadata = matcher.group(3);
		if (metadata != null) {
			// Water flooding
			String sWaterLevel = "";
			String sFlooding = "";
			String sRobotWaterproof = "";
			Matcher waterMatcher = waterPattern.matcher(metadata);
			if (waterMatcher.matches()) {
				sWaterLevel = waterMatcher.group(1);
			}
			Matcher floodingMatcher = floodingPattern.matcher(metadata);
			if (floodingMatcher.matches()) {
				sFlooding = floodingMatcher.group(1);
			}
			Matcher waterproofMatcher = waterproofPattern.matcher(metadata);
			if (waterproofMatcher.matches()) {
				sRobotWaterproof = waterproofMatcher.group(1);
			}
			if (sWaterLevel.length() > 0)
				waterLevel = Integer.parseInt(sWaterLevel);
			if (sFlooding.length() > 0)
				flooding = Integer.parseInt(sFlooding);
			if (sRobotWaterproof.length() > 0)
				robotWaterproof = Integer.parseInt(sRobotWaterproof);

			// Trampolines and targets
			String trampolines = metadata;
			while (true) {
				Matcher trampolineMatcher = trampolinePattern.matcher(trampolines);
				if (trampolineMatcher.matches()) {
					String sTrampoline = trampolineMatcher.group(1);
					String sTarget = trampolineMatcher.group(2);
					CellContent trampoline = CellContent.fromText(sTrampoline);
					CellContent target = CellContent.fromText(sTarget);
					if (trampoline != null && target != null)
						trampoline.setTrampolineTarget(target);
				} else
					break;
				trampolines = trampolines.replaceFirst(firstTrampolineRegexp, "");
			}

			// Growth and razorsCount
			String sGrowth = "";
			String sRazors = "";
			Matcher growthMatcher = growthPattern.matcher(metadata);
			if (growthMatcher.matches()) {
				sGrowth = growthMatcher.group(1);
			}
			Matcher razorsMatcher = razorsPattern.matcher(metadata);
			if (razorsMatcher.matches()) {
				sRazors = razorsMatcher.group(1);
			}
			if (sGrowth.length() > 0)
				growth = Integer.parseInt(sGrowth);
			if (sRazors.length() > 0)
				razorsCount = Integer.parseInt(sRazors);
		} else
			metadata = "";
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

	public Set<Cell> getAdjacentCells(Cell cell) {
		Set<Cell> cells = new HashSet<Cell>();
		for (Coordinate coordinate : cell.getCoordinate().getAdjacentCoordinates())
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

	public int getGrowth() {
		return growth;
	}

	public int getRazorsCount() {
		return razorsCount;
	}

	public void setRazorsCount(int razorsCount) {
		this.razorsCount = razorsCount;
	}

	public int getLambdasAndHighOrderRocksStartCount() {
		return lambdasAndHighOrderRocksStartCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mine) {
			Mine otherMine = (Mine) obj;
			return toText().equals(otherMine.toText());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return toText().hashCode();
	}

	@Override
	public Mine clone() {
		return new Mine(toText(), null);
	}

}
