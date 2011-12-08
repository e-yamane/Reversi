package reversi.impl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import reversi.Board;
import reversi.Player;
import reversi.Reporter;
import reversi.Reversi;
import reversi.Reversi.Result;

public class SwingReporter implements Reporter {
	List<Object[]> eventStack = new ArrayList<Object[]>();
	int currentIndex = 0;
	
	@Override
	public void finish(Result result) {
	    Object[] params = new Object[]{EventType.FINISH, result};
	    eventStack.add(params);
	}

	@Override
	public void put(Board board, int number, Player player, Point p) {
	    Object[] params = new Object[]{EventType.PUT, board.clone(), number, player, p};
	    eventStack.add(params);
	}

	@Override
	public void skip(Board board, Player skipper) {
	    Object[] params = new Object[]{EventType.SKIP, board.clone(), skipper};
	    eventStack.add(params);
	}

	ReversiFrame frame;
	@Override
	public void start(Board board, Player black, Player white) {
		frame = new ReversiFrame();
	    frame.setVisible(true);
	    Object[] params = new Object[]{EventType.START, board.clone(), black, white};
	    eventStack.add(params);
	    display(0);
	}
	
	void prev() {
		if(currentIndex > 0) {
			display(--currentIndex);
		}
	}
	
	void next() {
		if(currentIndex + 1 != eventStack.size()) {
			display(++currentIndex);
		}
	}
	
	void auto() {
		while(currentIndex + 1 != eventStack.size()) {
			display(++currentIndex);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	void display(int i) {
		Object[] params = eventStack.get(i);
		EventType type = (EventType)params[0];
		switch(type) {
		case START:
			displayStart((Board)params[1], (Player)params[2], (Player)params[3]);
			break;
		case PUT:
			displayPut((Board)params[1], (Integer)params[2], (Player)params[3], (Point)params[4]);
			break;
		case SKIP:
			displaySkip((Board)params[1], (Player)params[2]);
			break;
		case FINISH:
			displayFinish((Result)params[1]);
			break;
		}
	}

	void displayStart(Board board, Player black, Player white) {
		frame.info.setText(String.format("開始ます。黒：%s 白：%s", black.getName(), white.getName()));
		frame.board.setText(dumpBoard(board));
	}

	void displayPut(Board board, Integer number, Player player, Point p) {
		frame.info.setText(String.format("第%d手目(%s)[%s] ", number, player.getName(), p));
		frame.board.setText(dumpBoard(board));
	}

	void displaySkip(Board board, Player skipper) {
		frame.info.setText(String.format("%sは置く場所がありません。パスします。 ", skipper.getName()));
		frame.board.setText(dumpBoard(board));
	}
	
	void displayFinish(Result result) {
		frame.info.setText(result.judge().toString());
	}
	
	static String dumpBoard(Board board) {
		StringBuilder sb = new StringBuilder();
		for(int y = 0 ; y < Board.MAX ; y++) {
			for(int x = 0 ; x < Board.MAX ; x++) {
				switch (board.getState(new Point(x, y))) {
				case EMPTY:
					sb.append("＋");
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

	static enum EventType {
		START,
		PUT,
		SKIP,
		FINISH
	}
	
	class ReversiFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		JTextField info;
		JTextArea board;
		ReversiFrame(){
			setTitle("リバーシ");
			setBounds(100, 100, 300, 250);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			Box p = Box.createVerticalBox();
			
			info = new JTextField(20);
			board = new JTextArea();
			board.setRows(8);
			board.setColumns(8);
			
			p.add(info);
			p.add(board);
			
			Box buttons = Box.createHorizontalBox();
			JButton prev = new JButton("前へ");
			prev.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					prev();
				}
			});
			JButton next = new JButton("次へ");
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					next();
				}
			});
			
			JButton auto = new JButton("自動");
			auto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Runnable run = new Runnable() {
						@Override
						public void run() {
							auto();
						}
					};
					Thread t = new Thread(run);
					t.start();
				}
			});

			buttons.add(prev);
			buttons.add(next);
			buttons.add(auto);

			p.add(buttons);
			
			Container contentPane = getContentPane();
			contentPane.add(p, BorderLayout.CENTER);
		}
	}

	public static void main(String[] args) throws Exception {
		Reversi r = new Reversi(new BeginerPlayer("黒"), new BeginerPlayer("白"), new SwingReporter());
		r.fight();
	}
}
