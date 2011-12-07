package reversi.impl;

import java.awt.Point;

import reversi.Board;
import reversi.BoardState;
import reversi.Player;

public class Champion extends Player {

	public Champion(String name) {
		super(name);
	}

	@Override
	public Point nextPoint(Board board, BoardState state) {
		int max = Integer.MIN_VALUE;
		Point ret = null;
		for(Point p : board.points()) {
			try {
				Board tmp = board.clone();
				tmp.put(p, state);
				int point = getPoint(board, tmp, state);
				if(max < point) {
					max = point;
					ret = p;
				}
			} catch(Exception e) {
			}
		}
		return ret;
	}

	private int getPoint(Board org, Board tmp, BoardState state) {
		int ret = 0;
		for(Point p : tmp.points()) {
			if(tmp.getState(p) == state) {
				ret += BIAS[p.y][p.x];
			}
		}
		return ret;
	}

	final static int[][] BIAS =
		{
			{20,1,10,10,10,10,1,20},
			{ 1,2, 3, 3, 3, 3,2, 1},
			{10,3, 3, 5, 5, 3,3,10},
			{10,3, 5, 5, 5, 5,3,10},
			{10,3, 5, 5, 5, 5,3,10},
			{10,3, 3, 5, 5, 3,3,10},
			{ 1,2, 3, 3, 3, 3,2, 1},
			{20,1,10,10,10,10,1,20}
		};
}
