package ru.bosony.model.moving;

import java.util.HashSet;
import java.util.Set;

/**
 * @author indvdum (gotoindvdum[at]gmail[dot]com) <br>
 *         14.07.2012 1:07:51
 * 
 */
public class Coordinate {

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int	x;
	public int	y;

	public Coordinate left() {
		return new Coordinate(x - 1, y);
	}

	public Coordinate up() {
		return new Coordinate(x, y + 1);
	}

	public Coordinate right() {
		return new Coordinate(x + 1, y);
	}

	public Coordinate down() {
		return new Coordinate(x, y - 1);
	}

	public Coordinate leftAndUp() {
		return new Coordinate(x - 1, y + 1);
	}

	public Coordinate rightAndUp() {
		return new Coordinate(x + 1, y + 1);
	}

	public Coordinate rightAndDown() {
		return new Coordinate(x + 1, y - 1);
	}

	public Coordinate leftAndDown() {
		return new Coordinate(x - 1, y - 1);
	}

	public Set<Coordinate> getNeighboringCoordinates() {
		Set<Coordinate> coords = new HashSet<Coordinate>();
		coords.add(left());
		coords.add(up());
		coords.add(right());
		coords.add(down());
		coords.add(leftAndUp());
		coords.add(rightAndUp());
		coords.add(rightAndDown());
		coords.add(leftAndDown());
		return coords;
	}

	public Set<Coordinate> getAdjacentCoordinates() {
		Set<Coordinate> coords = new HashSet<Coordinate>();
		coords.add(left());
		coords.add(up());
		coords.add(right());
		coords.add(down());
		return coords;
	}

	public Movement getNecessaryMovement(Coordinate nCoordinate) {
		if (left().equals(nCoordinate))
			return Movement.LEFT;
		if (up().equals(nCoordinate))
			return Movement.UP;
		if (right().equals(nCoordinate))
			return Movement.RIGHT;
		if (down().equals(nCoordinate))
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
			return x == ((Coordinate) obj).x && y == ((Coordinate) obj).y;
		return super.equals(obj);
	}

}
