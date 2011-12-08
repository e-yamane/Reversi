package reversi.impl;

import java.awt.Point;

import reversi.Board;
import reversi.BoardState;
import reversi.Player;
import reversi.Reversi;

public class BeginerPlayer extends Player {

	public BeginerPlayer(String name) {
		super(name);
	}

	@Override
	public Point nextPoint(Board board, BoardState state) {
		int max = -1;
		Point ret = null;
		for(Point p : board.getAvailablePoints(state)) {
			Board tmp = board.clone();
			tmp.put(p, state);
			int count = getCounts(tmp, state);
			if(max < count) {
				max = count;
				ret = p;
			}
		}
		return ret;
	}

	private int getCounts(Board tmp, BoardState state) {
		if(state == BoardState.BLACK) {
			return tmp.getBlacks();
		} else {
			return tmp.getWhites();
		}
	}

	public static void main(String[] args) {
		Reversi r = new Reversi(new BeginerPlayer("•"), new BeginerPlayer("”’"));
		r.fight();
	}
}
