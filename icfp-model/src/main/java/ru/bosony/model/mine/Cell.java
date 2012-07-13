package ru.bosony.model.mine;

import ru.bosony.model.cellscontents.CellContent;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         13.07.2012 22:10:18
 * 
 */
public class Cell {

	protected int			x;
	protected int			y;
	protected CellContent	content;

	public Cell(int x, int y, CellContent content) {
		this.x = x;
		this.y = y;
		this.content = content;
	}

	public CellContent getContent() {
		return content;
	}

	public void setContent(CellContent content) {
		this.content = content;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
