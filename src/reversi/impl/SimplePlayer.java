package reversi.impl;

import java.awt.Point;

import reversi.Board;
import reversi.BoardState;
import reversi.CompositeReporter;
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
//		MinaPlayerServer player = new WebUIPlayer(8088);
		MinaPlayerServer black = new MinaPlayerServer(8088);
		MinaPlayerServer white = new MinaPlayerServer(8089);
		Reversi r = new Reversi(black, white, 
				new CompositeReporter(black.getReporter(), white.getReporter(), new SwingReporter()));
		r.fight();
	}
}
