package ru.bosony.model.mine;

import java.util.HashSet;
import java.util.Set;

import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.io.TextRepresentable;
import ru.bosony.model.moving.Movement;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 2:02:18
 * 
 */
public class Mine implements TextRepresentable {

	private Cell[][]	cells;
	protected int		sizeX;
	protected int		sizeY;
	protected Cell		robotCell;

	public Mine(String text) {
		fromText(text);
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
					content = CellContent.Empty;
				Cell cell = new Cell(new Coordinate(curX + 1, rows.length - curY), content);
				cells[curX][curY] = cell;
				if (content == CellContent.MiningRobot)
					robotCell = cell;
			}
		}
	}

	public Cell getCell(Coordinate coordinate) {
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
		Set<Cell> nCells = getNeighboringCells(robotCell);
		for (Cell nCell : nCells) {
			CellContent nContent = nCell.getContent();
			Coordinate nCoordinate = nCell.getCoordinate();
			Cell rightNCell = getCell(nCoordinate.right());
			Cell leftNCell = getCell(nCoordinate.left());
			if (nContent == CellContent.Empty
					|| nContent == CellContent.Earth
					|| nContent == CellContent.Lambda
					|| nContent == CellContent.OpenLambdaLift
					|| (nContent == CellContent.Rock && robotCell.getCoordinate().right().equals(nCoordinate)
							&& rightNCell != null && rightNCell.getContent() == CellContent.Empty)
					|| (nContent == CellContent.Rock && robotCell.getCoordinate().left().equals(nCoordinate)
							&& leftNCell != null && leftNCell.getContent() == CellContent.Empty)) {
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

}
