package reversi;

import java.awt.Point;

abstract public class Player {
	private final String name;
	protected Player(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	abstract public Point nextPoint(Board clone, BoardState state);
}
