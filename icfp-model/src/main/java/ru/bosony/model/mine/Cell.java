package ru.bosony.model.mine;

import ru.bosony.model.cellscontents.CellContent;
import ru.bosony.model.moving.Coordinate;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         13.07.2012 22:10:18
 * 
 */
public class Cell implements Cloneable {

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

	@Override
	protected Cell clone() throws CloneNotSupportedException {
		Cell clone = new Cell(coordinate, content);
		return clone;
	}

	@Override
	public String toString() {
		return "Cell [coordinate=" + coordinate + ", content=" + content + "]";
	}

}
