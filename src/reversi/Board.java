package reversi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Cloneable {
	BoardState[][] board;
	public final static int MAX = 8;
	
	public Board() {
		board = new BoardState[8][];
		for(int i = 0 ; i < MAX ; i++) {
			board[i] = new BoardState[MAX];
			Arrays.fill(board[i], BoardState.EMPTY);
		}
		board[3][3] = BoardState.WHITE;
		board[4][4] = BoardState.WHITE;
		board[3][4] = BoardState.BLACK;
		board[4][3] = BoardState.BLACK;
	}

	public int getWhites() {
		return getCount(BoardState.WHITE);
	}

	public int getBlacks() {
		return getCount(BoardState.BLACK);
	}

	public BoardState getState(Point point) {
		return board[(int)point.y][(int)point.x];
	}

	public void put(Point point, BoardState state) {
		if(getState(point) != BoardState.EMPTY) {
			throw new IllegalArgumentException("Šù‚É’u‚©‚ê‚Ä‚¢‚Ü‚·B");
		}
		List<Point> list = getReversePoints(point, state);
		if(list.size() == 0) {
			throw new IllegalArgumentException("ˆê–‡‚à•Ô‚¹‚Ü‚¹‚ñ");
		}
		for(Point p : list) {
			reverse(p);
		}
		setState(point, state);
	}

	@Override
	public Board clone() {
		Board ret = new Board();
		for(Point p : points()) {
			ret.setState(p, getState(p));
		}
		return ret;
	}

	public List<Point> getAvailablePoints(BoardState state) {
		List<Point> ret = new ArrayList<Point>();
		for(Point p : points()) {
			if(getReversePoints(p, state).size() != 0) {
				ret.add(p);
			}
		}
		return ret;
	}
	
	int getCount(BoardState boardState) {
		int ret = 0;
		for(BoardState cell : cells()) {
			if(cell == boardState) {
				ret++;
			}
		}
		return ret;
	}

	private List<Point> getReversePoints(Point point, BoardState state) {
		if(getState(point) != BoardState.EMPTY) {
			return new ArrayList<Point>();
		}
		List<Reverser> reversers = new ArrayList<Reverser>();
		reversers.add(new Upper());
		reversers.add(new Lower());
		reversers.add(new Righter());
		reversers.add(new Lefter());
		reversers.add(new LeftUpper());
		reversers.add(new RightUpper());
		reversers.add(new RightLower());
		reversers.add(new LeftLower());
		List<Point> list = new ArrayList<Point>();
		for(Reverser r : reversers) {
			list.addAll(r.reverser(point, state));
		}
		return list;
	}

	public Iterable<Point> points() {
		List<Point> ret = new ArrayList<Point>();
		for(int y = 0 ; y < MAX ; y++) {
			for(int x = 0 ; x < MAX ; x++) {
				ret.add(new Point(x, y));
			}
		}
		return ret;
	}

	public Iterable<BoardState> cells() {
		List<BoardState> ret = new ArrayList<BoardState>();
		for(Point p : points()) {
			ret.add(getState(p));
		}
		return ret;
	}
	
	private void reverse(Point p) {
		setState(p, getState(p).reverse());
	}

	private void setState(Point p, BoardState state) {
		board[p.y][p.x] = state;
	}

	abstract class Reverser {
		boolean isStop;
		final List<Point> reverser(Point point, BoardState state) {
			isStop = false;
			List<Point> ret = loop(point, state, new ArrayList<Point>());
			return (ret == null || isStop == false) ? new ArrayList<Point>() : ret;
		}

		abstract List<Point> loop(Point point, BoardState state, List<Point> ret);
		
		final protected List<Point> logic(Point point, BoardState state, List<Point> ret) {
			if(ret == null || isStop) {
				return ret;
			}
			if(getState(point) == BoardState.EMPTY) {
				return null;
			} else if(getState(point) == state) {
				isStop = true;
			} else {
				ret.add(point);
			}
			return ret;
		}
	}
	
	class Upper extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = (int)point.y - 1 ; i >= 0 ; i--) {
				Point p = new Point(point.x, i);
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class Lower extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = (int)point.y + 1 ; i < MAX ; i++) {
				Point p = new Point(point.x, i);
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class Righter extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = (int)point.x + 1 ; i < MAX ; i++) {
				Point p = new Point(i, point.y);
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class Lefter extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = (int)point.x - 1 ; i >= 0 ; i--) {
				Point p = new Point(i, point.y);
				list = logic(p, state, list);
			}
			return list;
		}
	}
	
	class LeftUpper extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = 1 ; i < MAX ; i++) {
				Point p = new Point(point.x - i, point.y - i);
				if(p.x < 0 || p.y < 0) {
					return list;
				}
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class RightUpper extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = 1 ; i < MAX ; i++) {
				Point p = new Point(point.x + i, point.y - i);
				if(p.x >= MAX || p.y < 0) {
					return list;
				}
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class RightLower extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = 1 ; i < MAX ; i++) {
				Point p = new Point(point.x + i, point.y + i);
				if(p.x >= MAX || p.y >= MAX) {
					return list;
				}
				list = logic(p, state, list);
			}
			return list;
		}
	}

	class LeftLower extends Reverser {
		@Override
		public List<Point> loop(Point point, BoardState state, List<Point> list) {
			for(int i = 1 ; i < MAX ; i++) {
				Point p = new Point(point.x - i, point.y + i);
				if(p.x < 0 || p.y >= MAX) {
					return list;
				}
				list = logic(p, state, list);
			}
			return list;
		}
	}
}
