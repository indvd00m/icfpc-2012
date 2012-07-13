package ru.bosony.model.mine;

import java.util.HashSet;
import java.util.Set;

import ru.bosony.model.moving.Movement;

/**
 * @author 	indvdum (gotoindvdum[at]gmail[dot]com) <br>
 * 			14.07.2012 1:07:51
 *
 */
public class Coordinate {
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x;
	public int y;
	
	public Coordinate left() {
		return new Coordinate(x - 1, y);
	}
	
	public Coordinate top() {
		return new Coordinate(x, y + 1);
	}
	
	public Coordinate right() {
		return new Coordinate(x + 1, y);
	}
	
	public Coordinate bottom() {
		return new Coordinate(x, y - 1);
	}
	
	public Coordinate leftAndTop() {
		return new Coordinate(x - 1, y + 1);
	}
	
	public Coordinate rightAndTop() {
		return new Coordinate(x + 1, y + 1);
	}
	
	public Coordinate rightAndBottom() {
		return new Coordinate(x + 1, y - 1);
	}
	
	public Coordinate leftAndBottom() {
		return new Coordinate(x - 1, y - 1);
	}
	
	public Set<Coordinate> getNeighboringCoordinates() {
		Set<Coordinate> coords = new HashSet<Coordinate>();
		coords.add(left());
		coords.add(top());
		coords.add(right());
		coords.add(bottom());
		coords.add(leftAndTop());
		coords.add(rightAndTop());
		coords.add(rightAndBottom());
		coords.add(leftAndBottom());
		return coords;
	}
	
	public Movement getNecessaryMovement(Coordinate nCoordinate) {
		if (left().equals(nCoordinate))
			return Movement.LEFT;
		if (top().equals(nCoordinate))
			return Movement.UP;
		if (right().equals(nCoordinate))
			return Movement.RIGHT;
		if (bottom().equals(nCoordinate))
			return Movement.DOWN;
		return null;
	}

	@Override
	public int hashCode() {
		return x ^ y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Coordinate)
			return x == ((Coordinate)obj).x && y == ((Coordinate)obj).y;
		return super.equals(obj);
	}
	
}
