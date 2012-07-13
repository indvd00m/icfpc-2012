package ru.bosony.model.mine;

import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.io.TextRepresentable;

public class Mine implements TextRepresentable {

	public Cell[][]	cells;
	protected int	sizeX;
	protected int	sizeY;

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
				Cell cell = new Cell(curX + 1, rows.length - curY, content);
				cells[curX][curY] = cell;
			}
		}
	}

	@Override
	public String toString() {
		return toText();
	}

}
