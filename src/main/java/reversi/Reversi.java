package reversi;

import java.awt.Point;

public class Reversi {
	final Player black;
	final Player white;
	final Board board;
	final Reporter reporter;
	final long blackLimitTime;
	final long whiteLimitTime;
	int   count = 0;
	BoardState current;
	
	static final long DEFAULT_LIMIT_TIME = 2000;
	
	public Reversi(Player black, Player white) {
		this(black, white, new DefaultReporter());
	}
	
	public Reversi(Player black, Player white, Reporter reporter) {
		this(black, DEFAULT_LIMIT_TIME, white, DEFAULT_LIMIT_TIME, reporter);
	}
	
	public Reversi(Player black, long blackLimitTime, Player white, long whiteLimitTime, Reporter reporter) {
		this.black = black;
		this.white = white;
		this.board = new Board();
		this.reporter = reporter;
		this.blackLimitTime = blackLimitTime;
		this.whiteLimitTime = whiteLimitTime;
	}
	
	public Result fight() {
		Result result = null;
		try {
			reporter.start(getBoard(), black, white);
			trying(BoardState.BLACK);
			result = new Result(this);
		} catch(Exception ex) {
			ex.printStackTrace();
			result = new Result(this, ex);
		}
		reporter.finish(result);
		return result;
	}

	Board getBoard() {
		return board.clone();
	}

	private void trying(BoardState state) {
		while(true) {
			if(tryingOnce(state)) {
				state = state.reverse();
				continue;
			} else {
				if(!tryingOnce(state.reverse())) {
					break;
				}
			}
		}
	}

	final Object monitor = new Object();
	boolean tryingOnce(BoardState state) {
		if(isPuttable(state)) {
			this.current = state;
			RunnableImpl run = new RunnableImpl(getTargetPlayer(state));
			try {
				new Thread(run).start();
				Thread.sleep(getLimitTime());
				System.out.println("タイムアウト！！");
				throw new RuntimeException("タイムアウト");
			} catch (InterruptedException e) {
			}
			Point p = run.p;
			this.board.put(p, state);
			count++;
			reporter.put(getBoard(), count, getTargetPlayer(state), p);
			return true;
		} else {
			skip(state);
			return false;
		}
	}

	long getLimitTime() {
		if(current == BoardState.BLACK) {
			return blackLimitTime;
		} else {
			return whiteLimitTime;
		}
	}

	class RunnableImpl implements Runnable {
		Point p;
		Thread target;
		final Player player;

		RunnableImpl(Player player) {
			this.player = player;
			this.target = Thread.currentThread();
		}

		@Override
		public void run() {
			p = player.nextPoint(getBoard(), current);
			target.interrupt();
		}
	}
	
	public boolean isPuttable(BoardState state) {
		for(Point p : getBoard().points()) {
			try {
				getBoard().put(p, state);
				return true;
			} catch(Exception e) {
			}
		}
		return false;
	}

	void skip(BoardState state) {
		if(!isFinish()) {
			reporter.skip(getBoard(), getTargetPlayer(state));
		}
	}

	boolean isFinish() {
		return getBoard().getCount(BoardState.EMPTY) == 0; 
	}

	Player getTargetPlayer(BoardState state) {
		if(state == BoardState.BLACK) {
			return black;
		} else {
			return white;
		}
	}
	
	String dumpBoard() {
		return DefaultReporter.dumpBoard(getBoard());
	}

	static class DefaultReporter implements Reporter {
		
		@Override
		public void finish(Result result) {
			System.out.println(result.judge());
		}

		@Override
		public void put(Board board, int number, Player player, Point p) {
			System.out.println(String.format("第%d手目(%s)[%s] ", number, player.getName(), p));
			System.out.println(dumpBoard(board));
		}

		@Override
		public void skip(Board board, Player skipper) {
			System.out.println(String.format("%sは置く場所がありません。パスします。 ", skipper.getName()));
		}

		@Override
		public void start(Board board, Player black, Player white) {
			System.out.println(String.format("開始ます。黒：%s 白：%s", black.getName(), white.getName()));
			System.out.println(dumpBoard(board));
		}

		static String dumpBoard(Board board) {
			StringBuilder sb = new StringBuilder();
			for(int y = 0 ; y < board.getYMax() ; y++) {
				for(int x = 0 ; x < board.getXMax() ; x++) {
					switch (board.getState(new Point(x, y))) {
					case EMPTY:
						sb.append("□");
						break;
					case BLACK:
						sb.append("●");
						break;
					case WHITE:
						sb.append("○");
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	public static class Result {
		final Reversi reversi;
		final Exception ex;
		
		Result(Reversi reversi) {
			this(reversi, null);
		}
		
		Result(Reversi reversi, Exception ex) {
			this.reversi = reversi;
			this.ex = ex;
		}
		
		public Judge judge() {
			if(ex != null) {
				return (reversi.getTargetPlayer(reversi.current) == reversi.black) ? Judge.WHITE : Judge.BLACK;
			}
			int whites = reversi.getBoard().getWhites();
			int blacks = reversi.getBoard().getBlacks();
			if(whites > blacks) {
				return Judge.WHITE;
			} else if(whites < blacks) {
				return Judge.BLACK;
			} else {
				return Judge.DRAW;
			}
		}
	}

	public static enum Judge {
		BLACK,
		WHITE,
		DRAW;
	}
}
