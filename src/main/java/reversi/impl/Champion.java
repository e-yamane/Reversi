package reversi.impl;

import java.awt.Point;
import java.util.List;

import reversi.Board;
import reversi.BoardState;
import reversi.Player;


public class Champion extends Player {

	public Champion(String name) {
		super(name);
	}

	private static int[][] boardPoints = {
		{10, -4, 5, 0, 0, 5, -4, 10,},
		{-4, -5, 5, 5, 5, 5, -5, -4,},
		{5, 5, 9, 0, 0, 9, 5, 5,},
		{0, 5, 0, 0, 0, 0, 5, 0,},
		{0, 5, 0, 0, 0, 0, 5, 0,},
		{5, 5, 9, 5, 5, 9, 5, 5,},
		{-4, -5, 0, 0, 0, 0, -5, -4,},
		{10, -4, 5, 0, 0, 5, -4, 10,},
	};

	private int step = 1;
	@Override
	public Point nextPoint(Board board, BoardState state) {
		System.out.println(step + "目");
		List<Point> nextPoints = board.getAvailablePoints(state);
	
		int beforeCount = 100;
		Point bestPoint = nextPoints.get(0);
		for (Point p : nextPoints) {
			Board stubBoard = board.clone();
			stubBoard.put(p, state);
			if (step > 6) {
				int count = stubBoard.getAvailablePoints(state.reverse()).size();
				if (beforeCount > count) {
					beforeCount = count;
					if (getAxisPoint(p.x, p.y) < 0) {
						continue;
					}
					bestPoint = p;
				}
			
				if (beforeCount == count) {
					bestPoint = selectPoint(bestPoint, p);
				}
			} else {
				bestPoint = selectPoint(bestPoint, p);
			}
		}
	
		step++;
		return bestPoint;
	}

	private Point selectPoint(Point bestPoint, Point p) {
		int p1 = getAxisPoint(bestPoint.x, bestPoint.y);
		int p2 = getAxisPoint(p.x, p.y);
	
		return (p1 > p2) ? bestPoint : p;
	}

	public static int getAxisPoint(int x, int y) {
		return boardPoints[y][x];
	}
}
