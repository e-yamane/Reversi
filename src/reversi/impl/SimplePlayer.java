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
		for(Point p : board.points()) {
			try {
				board.put(p, state);
				return p;
			} catch(Exception e) {
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
//		Reversi r = new Reversi(new NakamuraPlayer("’†‘º"), new AzuchiPlayer("ˆÀ“y"), new SwingReporter());
//		r.fight();
	}
}
