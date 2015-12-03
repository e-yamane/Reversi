package reversi;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class BoardTest extends TestCase {
	public void test������() throws Exception {
//		Map<BoardState, Integer> map = new HashMap<BoardState, Integer>();
//		map.put(BoardState.BLANK, 0);
//		map.put(BoardState.BLACK, 1);
//		map.put(BoardState.WHITE, 2);
		Board b = new Board();
		assertEquals("���̌�������Ă��܂��B", 2, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 2, b.getBlacks());
		assertEquals("�����Ⴀ��܂���B", BoardState.WHITE, b.getState(new Point(3, 3)));
		assertEquals("�����Ⴀ��܂���B", BoardState.WHITE, b.getState(new Point(4, 4)));
		assertEquals("�����Ⴀ��܂���B", BoardState.BLACK, b.getState(new Point(3, 4)));
		assertEquals("�����Ⴀ��܂���B", BoardState.BLACK, b.getState(new Point(4, 3)));
	}
	
	public void test�Ղ̃T�C�Y���w�肵�ď������ł��鎖() throws Exception {
		Board b = new Board(4, 4);
		assertEquals("���̌�������Ă��܂��B", 2, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 2, b.getBlacks());
		assertEquals("�󔒂̌�������Ă��܂��B", 12, b.getCount(BoardState.EMPTY));
		assertEquals("�����Ⴀ��܂���B", BoardState.WHITE, b.getState(new Point(1, 1)));
		assertEquals("�����Ⴀ��܂���B", BoardState.WHITE, b.getState(new Point(2, 2)));
		assertEquals("�����Ⴀ��܂���B", BoardState.BLACK, b.getState(new Point(1, 2)));
		assertEquals("�����Ⴀ��܂���B", BoardState.BLACK, b.getState(new Point(2, 1)));
	}
	
	public void test�ՃT�C�Y���w�肵��clone�����ۂɔՂ̃T�C�Y�������p������() throws Exception {
		Board b = new Board(4, 4);
		b = b.clone();
		assertEquals("���̌�������Ă��܂��B", 2, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 2, b.getBlacks());
		assertEquals("�󔒂̌�������Ă��܂��B", 12, b.getCount(BoardState.EMPTY));
	}
	
	public void test���̌����v�Z() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[0][0] = BoardState.WHITE;
		assertEquals("���̌�������Ă��܂��B", 3, b.getWhites());
	}

	public void test���̌����v�Z() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[0][0] = BoardState.BLACK;
		assertEquals("���̌�������Ă��܂��B", 3, b.getBlacks());
	}

	public void test������ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		b.put(new Point(3, 5), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 1, b.getBlacks());
	}

	public void test�������ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		b.put(new Point(4, 2), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 1, b.getBlacks());
	}

	public void test�E�����ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		b.put(new Point(2, 4), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 1, b.getBlacks());
	}

	public void test�������ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		b.put(new Point(5, 3), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 1, b.getBlacks());
	}

	public void test���΂ߏ�����ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[5][5] = BoardState.BLACK;
		b.put(new Point(6, 6), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 2, b.getBlacks());
	}

	public void test�E�΂ߏ�����ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[2][5] = BoardState.WHITE;
		b.put(new Point(2, 5), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 6, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 0, b.getBlacks());
	}

	public void test�E�΂߉������ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[2][2] = BoardState.BLACK;
		b.put(new Point(1, 1), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 4, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 2, b.getBlacks());
	}

	public void test���΂߉������ɔ��]�ł��鎖() throws Exception {
		Board b = new Board();
		//�C���`�L
		b.board[5][2] = BoardState.WHITE;
		b.put(new Point(5, 2), BoardState.WHITE);
		assertEquals("���̌�������Ă��܂��B", 6, b.getWhites());
		assertEquals("���̌�������Ă��܂��B", 0, b.getBlacks());
	}

	public void testBLANK�ȊO�ɂ͒u���Ȃ���() throws Exception {
		Point[] ps = new Point[4];
		ps[0] = new Point(3, 3);
		ps[1] = new Point(3, 4);
		ps[2] = new Point(4, 3);
		ps[3] = new Point(4, 4);
		Board b = new Board();
		for(Point p : ps) {
			try {
				b.put(p, BoardState.WHITE);
				fail("��O���������Ă��܂���");
			} catch(IllegalArgumentException e) {
				
			}
		}
	}

	public void test�ꖇ���Ђ�����Ԃ��Ȃ��ӏ��ɂ͒u���Ȃ���() throws Exception {
		Board b = new Board();
		try {
			b.put(new Point(0, 0), BoardState.WHITE);
			fail("��O���������Ă��܂���");
		} catch(IllegalArgumentException e) {
			
		}
	}

	public void test�u����Ƃ���̈ꗗ���ԋp����鎖() throws Exception {
		Board b = new Board();
		List<Point> points = b.getAvailablePoints(BoardState.BLACK);
		assertEquals("�ԋp��������Ă��܂��B", 4, points.size());
		Set<Point> set = new HashSet<Point>(points);
		assertTrue("�|�C���g������Ă��܂��B", set.contains(new Point(3, 2)));
		assertTrue("�|�C���g������Ă��܂��B", set.contains(new Point(2, 3)));
		assertTrue("�|�C���g������Ă��܂��B", set.contains(new Point(4, 5)));
		assertTrue("�|�C���g������Ă��܂��B", set.contains(new Point(5, 4)));
	}
	
	public void testClone() throws Exception {
		Board src = new Board();
		//�C���`�L
		src.board[5][2] = BoardState.WHITE;
		Board dest = src.clone();
		assertEquals("�N���[���o���Ă܂���B", BoardState.WHITE, dest.board[5][2]);

		src.board[5][2] = BoardState.BLACK;
		assertEquals("DeepClone�o���Ă��܂���B", BoardState.WHITE, dest.board[5][2]);
		assertEquals("DeepClone�o���Ă��܂���B", BoardState.BLACK, src.board[5][2]);
		
	}
}
