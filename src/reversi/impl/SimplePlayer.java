package reversi.impl;

import java.awt.Point;

import reversi.Board;
import reversi.BoardState;
import reversi.Player;
import reversi.Reversi;

public class SimplePlayer extends Player {
	public SimplePlayer(String name) {
		super(name);
	}

	@Override
	public Point nextPoint(Board board, BoardState state) {
		return board.getAvailablePoints(state).get(0);
	}
	
	public static void main(String[] args) {
		Reversi r = new Reversi(new SimplePlayer("çï"), new SimplePlayer("çï"));
		r.fight();
	}
}
