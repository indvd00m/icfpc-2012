package ru.bosony.graph;

import ru.bosony.model.mine.Cell;
import ru.bosony.model.mine.Mine;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         16.07.2012 11:09:35
 * 
 */
public class CellsLink {
	Mine	mine;
	Cell	source;
	Cell	target;
	double	weight	= 1d;

	public CellsLink(Mine mine, Cell source, Cell target) {
		this.mine = mine;
		this.source = source;
		this.target = target;
		calculateWeight();
	}

	protected void calculateWeight() {

	}

	public Mine getMine() {
		return mine;
	}

	public Cell getSource() {
		return source;
	}

	public Cell getTarget() {
		return target;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellsLink) {
			CellsLink otherLink = (CellsLink) obj;
			return source.equals(otherLink.source) && target.equals(otherLink.target);
		}
		return super.equals(obj);
	}

}
