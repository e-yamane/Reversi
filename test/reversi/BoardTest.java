package reversi;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class BoardTest extends TestCase {
	public void test初期化() throws Exception {
//		Map<BoardState, Integer> map = new HashMap<BoardState, Integer>();
//		map.put(BoardState.BLANK, 0);
//		map.put(BoardState.BLACK, 1);
//		map.put(BoardState.WHITE, 2);
		Board b = new Board();
		assertEquals("黒の個数が誤っています。", 2, b.getWhites());
		assertEquals("白の個数が誤っています。", 2, b.getBlacks());
		assertEquals("黒じゃありません。", BoardState.WHITE, b.getState(new Point(3, 3)));
		assertEquals("黒じゃありません。", BoardState.WHITE, b.getState(new Point(4, 4)));
		assertEquals("白じゃありません。", BoardState.BLACK, b.getState(new Point(3, 4)));
		assertEquals("白じゃありません。", BoardState.BLACK, b.getState(new Point(4, 3)));
	}
	
	public void test盤のサイズを指定して初期化できる事() throws Exception {
		Board b = new Board(4, 4);
		assertEquals("黒の個数が誤っています。", 2, b.getWhites());
		assertEquals("白の個数が誤っています。", 2, b.getBlacks());
		assertEquals("空白の個数が誤っています。", 12, b.getCount(BoardState.EMPTY));
		assertEquals("黒じゃありません。", BoardState.WHITE, b.getState(new Point(1, 1)));
		assertEquals("黒じゃありません。", BoardState.WHITE, b.getState(new Point(2, 2)));
		assertEquals("白じゃありません。", BoardState.BLACK, b.getState(new Point(1, 2)));
		assertEquals("白じゃありません。", BoardState.BLACK, b.getState(new Point(2, 1)));
	}
	
	public void test盤サイズを指定してcloneした際に盤のサイズも引き継ぐこと() throws Exception {
		Board b = new Board(4, 4);
		b = b.clone();
		assertEquals("黒の個数が誤っています。", 2, b.getWhites());
		assertEquals("白の個数が誤っています。", 2, b.getBlacks());
		assertEquals("空白の個数が誤っています。", 12, b.getCount(BoardState.EMPTY));
	}
	
	public void test白の個数を計算() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[0][0] = BoardState.WHITE;
		assertEquals("白の個数が誤っています。", 3, b.getWhites());
	}

	public void test黒の個数を計算() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[0][0] = BoardState.BLACK;
		assertEquals("黒の個数が誤っています。", 3, b.getBlacks());
	}

	public void test上向きに反転できる事() throws Exception {
		Board b = new Board();
		b.put(new Point(3, 5), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 1, b.getBlacks());
	}

	public void test下向きに反転できる事() throws Exception {
		Board b = new Board();
		b.put(new Point(4, 2), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 1, b.getBlacks());
	}

	public void test右向きに反転できる事() throws Exception {
		Board b = new Board();
		b.put(new Point(2, 4), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 1, b.getBlacks());
	}

	public void test左向きに反転できる事() throws Exception {
		Board b = new Board();
		b.put(new Point(5, 3), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 1, b.getBlacks());
	}

	public void test左斜め上向きに反転できる事() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[5][5] = BoardState.BLACK;
		b.put(new Point(6, 6), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 2, b.getBlacks());
	}

	public void test右斜め上向きに反転できる事() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[2][5] = BoardState.WHITE;
		b.put(new Point(2, 5), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 6, b.getWhites());
		assertEquals("黒の個数が誤っています。", 0, b.getBlacks());
	}

	public void test右斜め下向きに反転できる事() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[2][2] = BoardState.BLACK;
		b.put(new Point(1, 1), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 4, b.getWhites());
		assertEquals("黒の個数が誤っています。", 2, b.getBlacks());
	}

	public void test左斜め下向きに反転できる事() throws Exception {
		Board b = new Board();
		//インチキ
		b.board[5][2] = BoardState.WHITE;
		b.put(new Point(5, 2), BoardState.WHITE);
		assertEquals("白の個数が誤っています。", 6, b.getWhites());
		assertEquals("黒の個数が誤っています。", 0, b.getBlacks());
	}

	public void testBLANK以外には置けない事() throws Exception {
		Point[] ps = new Point[4];
		ps[0] = new Point(3, 3);
		ps[1] = new Point(3, 4);
		ps[2] = new Point(4, 3);
		ps[3] = new Point(4, 4);
		Board b = new Board();
		for(Point p : ps) {
			try {
				b.put(p, BoardState.WHITE);
				fail("例外が発生していません");
			} catch(IllegalArgumentException e) {
				
			}
		}
	}

	public void test一枚もひっくり返せない箇所には置けない事() throws Exception {
		Board b = new Board();
		try {
			b.put(new Point(0, 0), BoardState.WHITE);
			fail("例外が発生していません");
		} catch(IllegalArgumentException e) {
			
		}
	}

	public void test置けるところの一覧が返却される事() throws Exception {
		Board b = new Board();
		List<Point> points = b.getAvailablePoints(BoardState.BLACK);
		assertEquals("返却数が誤っています。", 4, points.size());
		Set<Point> set = new HashSet<Point>(points);
		assertTrue("ポイントが誤っています。", set.contains(new Point(3, 2)));
		assertTrue("ポイントが誤っています。", set.contains(new Point(2, 3)));
		assertTrue("ポイントが誤っています。", set.contains(new Point(4, 5)));
		assertTrue("ポイントが誤っています。", set.contains(new Point(5, 4)));
	}
	
	public void testClone() throws Exception {
		Board src = new Board();
		//インチキ
		src.board[5][2] = BoardState.WHITE;
		Board dest = src.clone();
		assertEquals("クローン出来てません。", BoardState.WHITE, dest.board[5][2]);

		src.board[5][2] = BoardState.BLACK;
		assertEquals("DeepClone出来ていません。", BoardState.WHITE, dest.board[5][2]);
		assertEquals("DeepClone出来ていません。", BoardState.BLACK, src.board[5][2]);
		
	}
}
