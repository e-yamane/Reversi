package reversi;

import java.awt.Point;

import reversi.impl.SimplePlayer;

import junit.framework.TestCase;

public class ReversiTest extends TestCase {
	public void test生成できる事() {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		assertTrue("黒番プレイヤーが誤っています。", black == r.black);
		assertTrue("白番プレイヤーが誤っています。", white == r.white);
	}

	public void test白が置けない事が判断可能な事() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		for(int y = 3 ; y <= 5 ; y++) {
			for(int x = 3 ; x <= 5 ; x++) {
				r.board.board[y][x] = BoardState.WHITE;
			}
		}
		r.board.board[4][4] = BoardState.BLACK;
		assertFalse("白は置けません。", r.isPuttable(BoardState.WHITE));
		assertTrue("黒は置けます。", r.isPuttable(BoardState.BLACK));
	}
	
	public void test黒が置けない事が判断可能な事() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		for(int y = 3 ; y <= 5 ; y++) {
			for(int x = 3 ; x <= 5 ; x++) {
				r.board.board[y][x] = BoardState.BLACK;
			}
		}
		r.board.board[4][4] = BoardState.WHITE;
		assertFalse("黒は置けません。", r.isPuttable(BoardState.BLACK));
		assertTrue("白は置けます。", r.isPuttable(BoardState.WHITE));
	}


	public void test両者打つところが無ければその時点の結果を返却する事() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		r.getBoard().board[4][4] = BoardState.EMPTY;
		r.getBoard().board[3][4] = BoardState.EMPTY;
		r.getBoard().board[4][3] = BoardState.EMPTY;
		Reversi.Result result = r.fight();
		assertEquals("結果が誤っています。", Reversi.Judge.WHITE, result.judge());
	}
	
	public void test一手進められる事() throws Exception {
		Player black = new SimplePlayer("黒");
		Player white = new SimplePlayer("白");
		Reversi r = new Reversi(black, white);
		r.tryingOnce(BoardState.BLACK);
		final String result = 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□●□□□□\n" + 
			"□□□●●□□□\n" + 
			"□□□●○□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n";
		assertEquals("返却値が誤っています。", result, r.dumpBoard());
	}
	
	public void test例外を発生させた方が負ける事() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		Reversi.Result result = r.fight();
		assertEquals("結果が誤っています。", Reversi.Judge.WHITE, result.judge());
	}

	public void test２秒応答がなければ問答無用で負けになること() throws Exception {
		Player black = new LaterPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		Reversi.Result result = r.fight();
		assertEquals("結果が誤っています。", Reversi.Judge.WHITE, result.judge());
	}
	
	public void testDumpBoard() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		final String result = 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□○●□□□\n" + 
			"□□□●○□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n" + 
			"□□□□□□□□\n";
		assertEquals("返却値が誤っています。", result, r.dumpBoard());
	}

	
	static class EmptyPlayer extends Player {
		public EmptyPlayer() {
			super("ほげ");
		}

		@Override
		public Point nextPoint(Board clone, BoardState state) {
			return new Point(0, 0);
		}
		
	}

	static class LaterPlayer extends SimplePlayer {
		public LaterPlayer() {
			super("頭悪い子");
		}

		@Override
		public Point nextPoint(Board board, BoardState state) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return super.nextPoint(board, state);
		}
		
	}
}
