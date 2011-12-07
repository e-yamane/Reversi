package reversi;

import java.awt.Point;

abstract public class Player {
	public final String name;
	protected Player(String name) {
		this.name = name;
	}
	abstract public Point nextPoint(Board clone, BoardState state);
}
