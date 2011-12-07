package reversi;

import java.awt.Point;

public class DebugBoard extends Board {
	public DebugBoard black(int x, int y) {
		setState(new Point(x, y), BoardState.BLACK);
		return this;
	}

	public DebugBoard white(int x, int y) {
		setState(new Point(x, y), BoardState.WHITE);
		return this;
	}

	public DebugBoard empty(int x, int y) {
		setState(new Point(x, y), BoardState.EMPTY);
		return this;
	}

	public DebugBoard steps(BoardState state, Point... points) {
		for(Point p  : points) {
			put(p, state);
			state = state.reverse();
		}
		return this;
	}
	
	public void sets(BoardState[][] board) {
		for(int y = 0 ; y < MAX ; y++) {
			for(int x = 0 ; x < MAX ; x++) {
				setState(new Point(x, y), board[y][x]);
			}
		}
	}
}
