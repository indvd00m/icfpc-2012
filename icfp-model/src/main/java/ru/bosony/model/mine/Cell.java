package ru.bosony.model.mine;

import ru.bosony.model.cellscontents.CellContent;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         13.07.2012 22:10:18
 * 
 */
public class Cell {

	protected Coordinate	coordinate;
	protected CellContent	content;

	public Cell(Coordinate coordinate, CellContent content) {
		this.coordinate = coordinate;
		this.content = content;
	}

	public CellContent getContent() {
		return content;
	}

	public void setContent(CellContent content) {
		this.content = content;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

}
