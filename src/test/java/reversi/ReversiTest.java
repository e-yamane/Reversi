package reversi;

import java.awt.Point;

import reversi.impl.SimplePlayer;

import junit.framework.TestCase;

public class ReversiTest extends TestCase {
	public void test�����ł��鎖() {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		assertTrue("���ԃv���C���[������Ă��܂��B", black == r.black);
		assertTrue("���ԃv���C���[������Ă��܂��B", white == r.white);
	}

	public void test�����u���Ȃ��������f�\�Ȏ�() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		for(int y = 3 ; y <= 5 ; y++) {
			for(int x = 3 ; x <= 5 ; x++) {
				r.board.board[y][x] = BoardState.WHITE;
			}
		}
		r.board.board[4][4] = BoardState.BLACK;
		assertFalse("���͒u���܂���B", r.isPuttable(BoardState.WHITE));
		assertTrue("���͒u���܂��B", r.isPuttable(BoardState.BLACK));
	}
	
	public void test�����u���Ȃ��������f�\�Ȏ�() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		for(int y = 3 ; y <= 5 ; y++) {
			for(int x = 3 ; x <= 5 ; x++) {
				r.board.board[y][x] = BoardState.BLACK;
			}
		}
		r.board.board[4][4] = BoardState.WHITE;
		assertFalse("���͒u���܂���B", r.isPuttable(BoardState.BLACK));
		assertTrue("���͒u���܂��B", r.isPuttable(BoardState.WHITE));
	}


	public void test���ґłƂ��낪������΂��̎��_�̌��ʂ�ԋp���鎖() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		r.getBoard().board[4][4] = BoardState.EMPTY;
		r.getBoard().board[3][4] = BoardState.EMPTY;
		r.getBoard().board[4][3] = BoardState.EMPTY;
		Reversi.Result result = r.fight();
		assertEquals("���ʂ�����Ă��܂��B", Reversi.Judge.WHITE, result.judge());
	}
	
	public void test���i�߂��鎖() throws Exception {
		Player black = new SimplePlayer("��");
		Player white = new SimplePlayer("��");
		Reversi r = new Reversi(black, white);
		r.tryingOnce(BoardState.BLACK);
		final String result = 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n";
		assertEquals("�ԋp�l������Ă��܂��B", result, r.dumpBoard());
	}
	
	public void test��O�𔭐����������������鎖() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		Reversi.Result result = r.fight();
		assertEquals("���ʂ�����Ă��܂��B", Reversi.Judge.WHITE, result.judge());
	}

	public void test�Q�b�������Ȃ���Ζⓚ���p�ŕ����ɂȂ邱��() throws Exception {
		Player black = new LaterPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		Reversi.Result result = r.fight();
		assertEquals("���ʂ�����Ă��܂��B", Reversi.Judge.WHITE, result.judge());
	}
	
	public void testDumpBoard() throws Exception {
		Player black = new EmptyPlayer();
		Player white = new EmptyPlayer();
		Reversi r = new Reversi(black, white);
		final String result = 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n" + 
			"����������������\n";
		assertEquals("�ԋp�l������Ă��܂��B", result, r.dumpBoard());
	}

	
	static class EmptyPlayer extends Player {
		public EmptyPlayer() {
			super("�ق�");
		}

		@Override
		public Point nextPoint(Board clone, BoardState state) {
			return new Point(0, 0);
		}
		
	}

	static class LaterPlayer extends SimplePlayer {
		public LaterPlayer() {
			super("�������q");
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
