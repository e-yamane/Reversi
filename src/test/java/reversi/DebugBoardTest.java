package reversi;

import java.awt.Point;

import junit.framework.TestCase;

public class DebugBoardTest extends TestCase {
	public void test黒を適当な位置に置ける事() throws Exception {
		DebugBoard db = new DebugBoard();
		DebugBoard db2 = db.black(1, 0);
		assertTrue(db == db2);
		assertEquals(BoardState.BLACK, db2.getState(new Point(1, 0)));
		assertEquals(BoardState.EMPTY, db2.getState(new Point(0, 1)));
	}

	public void test白を適当な位置に置ける事() throws Exception {
		DebugBoard db = new DebugBoard();
		DebugBoard db2 = db.white(1, 0);
		assertTrue(db == db2);
		assertEquals(BoardState.WHITE, db2.getState(new Point(1, 0)));
		assertEquals(BoardState.EMPTY, db2.getState(new Point(0, 1)));
	}

	public void test適当な位置を空白にできる事() throws Exception {
		DebugBoard db = new DebugBoard();
		DebugBoard db2 = db.empty(3, 4);
		assertTrue(db == db2);
		assertEquals(BoardState.EMPTY, db2.getState(new Point(3, 4)));
		assertEquals(BoardState.BLACK, db2.getState(new Point(4, 3)));
	}
	
	public void test連続で手を進めた状況を作り出せる事() throws Exception {
		DebugBoard db = new DebugBoard();
		DebugBoard db2 = db.steps(BoardState.BLACK, 
				new Point(3, 2),
				new Point(4, 2),
				new Point(5, 2));
		assertTrue(db == db2);
		assertEquals(6, db2.getBlacks());
		assertEquals(1, db2.getWhites());
	}
	
	public void test全面セットできる事() throws Exception {
		BoardState 黒 = BoardState.BLACK;
		BoardState 白 = BoardState.WHITE;
		BoardState __ = BoardState.EMPTY;
		BoardState[][] board = new BoardState[][]{
				{__,__,__,白,__,__,__,__},	
				{__,__,__,__,__,__,__,__},	
				{__,__,__,__,__,__,__,__},	
				{__,__,__,__,__,__,__,__},	
				{__,__,__,__,__,__,__,__},	
				{__,黒,__,__,__,__,__,__},	
				{__,黒,__,__,__,__,__,__},	
				{__,__,__,__,__,__,__,__},	
		};
		DebugBoard db = new DebugBoard();
		db.sets(board);
		assertEquals(2, db.getBlacks());
		assertEquals(1, db.getWhites());
		assertEquals(BoardState.BLACK, db.getState(new Point(1, 5)));
		assertEquals(BoardState.BLACK, db.getState(new Point(1, 6)));
		assertEquals(BoardState.WHITE, db.getState(new Point(3, 0)));
	}
}
